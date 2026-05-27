package com.mallegan.ads.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustAdRevenue
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.AdjustEvent
import com.adjust.sdk.LogLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * AdjustHelper - Quản lý tập trung tích hợp Adjust SDK v5.x cho Thư viện Ads (Library).
 * Thiết kế Singleton (Kotlin object), tách biệt hoàn toàn Logic của App và Library.
 * Nhận Adjust ID (App Token) và In-App Purchase Event Token động từ App khi khởi tạo.
 */
object AdjustHelper : Application.ActivityLifecycleCallbacks {
    private const val TAG = "AdjustHelper"

    private var adjustToken: String? = null
    private var iapEventToken: String? = null
    private var isTrackingEnabled = false
    private var isDebugMode = false

    /**
     * Khởi tạo Adjust SDK từ Application của App.
     * @param application Lớp Application của app tích hợp
     * @param appToken Token chính của Adjust (Adjust ID)
     * @param iapEventToken Token sự kiện mua hàng trong ứng dụng chung (Nếu có)
     * @param isDebug Cờ cấu hình môi trường (true -> SANDBOX, false -> PRODUCTION)
     */
    fun init(application: Application, appToken: String, iapEventToken: String? = null, isDebug: Boolean = false) {
        this.adjustToken = appToken
        this.iapEventToken = iapEventToken
        this.isDebugMode = isDebug
        this.isTrackingEnabled = true

        val environment = if (isDebug) {
            AdjustConfig.ENVIRONMENT_SANDBOX
        } else {
            AdjustConfig.ENVIRONMENT_PRODUCTION
        }

        val config = AdjustConfig(application, appToken, environment)

        if (isDebug) {
            config.setLogLevel(LogLevel.VERBOSE)
        }

        // Các callback hỗ trợ ghi nhận kết quả và debug
        config.setOnAttributionChangedListener { attribution ->
            Log.d(TAG, "Attribution changed: TrackerToken = ${attribution.trackerToken}, Network = ${attribution.network}, Campaign = ${attribution.campaign}")
        }

        config.setOnSessionTrackingSucceededListener { sessionSuccess ->
            Log.d(TAG, "Session tracking succeeded: ${sessionSuccess.message}")
        }

        config.setOnSessionTrackingFailedListener { sessionFailed ->
            Log.e(TAG, "Session tracking failed: ${sessionFailed.message}")
        }

        // Khởi tạo SDK
        Adjust.initSdk(config)

        // Đăng ký lifecycle tự động gọi Adjust.onResume() và Adjust.onPause()
        application.registerActivityLifecycleCallbacks(this)
        Log.d(TAG, "Adjust SDK initialized in ${if (isDebug) "SANDBOX" else "PRODUCTION"} mode.")
    }

    /**
     * 1. Gửi sự kiện In-App Purchase chung được cấu hình khi init
     * @param price Giá sản phẩm
     * @param currency Mã tiền tệ
     * @param orderId ID giao dịch từ Google Play (deduplicationId) giúp tránh trùng lặp dữ liệu
     */
    fun trackEventInAppPurchase(price: Double, currency: String, orderId: String) {
        if (isDebugMode) {
            Log.d(TAG, "trackEventInAppPurchase: Bỏ qua ở môi trường DEBUG (Chỉ chạy trên Production).")
            return
        }

        val token = iapEventToken
        if (token.isNullOrEmpty()) {
            Log.e(TAG, "iapEventToken chưa được cấu hình khi khởi tạo AdjustHelper!")
            return
        }

        val adjustEvent = AdjustEvent(token).apply {
            setRevenue(price, currency)
            setOrderId(orderId) // Deduplication ID
        }
        Adjust.trackEvent(adjustEvent)
        Log.d(TAG, "trackEventInAppPurchase: Đã gửi event IAP. Price = $price, Currency = $currency, OrderId = $orderId")
    }

    /**
     * 2. Gửi một sự kiện đơn giản từ App bằng Event Token (ví dụ: Retention Day, Ad Milestone)
     */
    fun trackSimpleEvent(eventToken: String) {
        if (eventToken.isEmpty()) return
        Adjust.trackEvent(AdjustEvent(eventToken))
        Log.d(TAG, "trackSimpleEvent: Đã gửi event. Token = $eventToken")
    }

    /**
     * 3. Gửi sự kiện mua hàng cụ thể (ví dụ: Weekly, Monthly) với doanh thu và trì hoãn Coroutine (giảm tải CPU)
     * @param eventToken Token sự kiện của gói mua hàng cụ thể
     * @param price Giá gói
     * @param currency Mã tiền tệ
     * @param orderId ID giao dịch (deduplicationId)
     * @param delayMs Thời gian trì hoãn (mặc định 2000ms để tránh nghẽn CPU khi thanh toán)
     */
    fun trackRevenueEvent(
        eventToken: String,
        price: Double,
        currency: String,
        orderId: String,
        delayMs: Long = 2000
    ) {
        if (eventToken.isEmpty()) return

        CoroutineScope(Dispatchers.Default).launch {
            if (delayMs > 0) {
                Log.d(TAG, "trackRevenueEvent: Trì hoãn ${delayMs}ms...")
                delay(delayMs)
            }
            val adjustEvent = AdjustEvent(eventToken).apply {
                setRevenue(price, currency)
                setOrderId(orderId)
            }
            Adjust.trackEvent(adjustEvent)
            Log.d(TAG, "trackRevenueEvent: Đã gửi event doanh thu thành công. Token = $eventToken")
        }
    }

    /**
     * 4. Ghi nhận doanh thu quảng cáo hiển thị thời gian thực (ILRD) từ AdMob / AppLovin MAX
     * @param adSource Nguồn ad (Admob hoặc MAX)
     * @param revenue Giá trị doanh thu thực tế (đã chuyển về dạng Double gốc)
     * @param currencyCode Mã tiền tệ
     * @param adUnitId ID đơn vị quảng cáo
     * @param networkName Tên mạng quảng cáo trung gian (mediation network)
     */
    fun adjustTrackAdRevenue(
        adSource: String,
        revenue: Double,
        currencyCode: String,
        adUnitId: String,
        networkName: String
    ) {
        val adjustSource = when (adSource.lowercase()) {
            "admob" -> "admob_sdk"
            "max", "applovin" -> "applovin_max_sdk"
            else -> adSource
        }

        val adjustAdRevenue = AdjustAdRevenue(adjustSource).apply {
            setRevenue(revenue, currencyCode)
            setAdRevenueNetwork(networkName)
            setAdRevenueUnit(adUnitId)
        }

        Adjust.trackAdRevenue(adjustAdRevenue)
        Log.d(TAG, "adjustTrackAdRevenue: Đã track ad revenue. Source = $adjustSource, Revenue = $revenue, Network = $networkName")
    }

    // --- ACTIVITY LIFECYCLE CALLBACKS ---
    override fun onActivityResumed(activity: Activity) {
        if (isTrackingEnabled) {
            Adjust.onResume()
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (isTrackingEnabled) {
            Adjust.onPause()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
