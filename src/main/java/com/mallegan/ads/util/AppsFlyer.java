package com.mallegan.ads.util;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.appsflyer.AFAdRevenueData;
import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AdRevenueScheme;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.MediationNetwork;
import com.google.android.gms.ads.AdValue;

import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AppsFlyer {
    private static AppsFlyer INSTANCE;
    private boolean enableTrackingAppFlyerRevenue = false;
    private boolean enableLoggingAdRevenue = false;
    private static final String TAG = "AppFlyer";

    public static AppsFlyer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppsFlyer();
        }
        return INSTANCE;
    }

    public void initAppFlyer(Application context, String devKey, boolean enableTrackingAppFlyerRevenue) {
        this.enableTrackingAppFlyerRevenue = enableTrackingAppFlyerRevenue;
        initAppFlyerDebug(context, devKey, false);
    }

    public void initAppFlyer(Application context, String devKey, boolean enableTrackingAppFlyerRevenue, boolean enableDebug) {
        this.enableTrackingAppFlyerRevenue = enableTrackingAppFlyerRevenue;
        initAppFlyerDebug(context, devKey, enableDebug);
    }

    public void initAppFlyer(Application context, String devKey, boolean enableTrackingAppFlyerRevenue, AppsFlyerConversionListener appsFlyerConversionListener) {
        this.enableTrackingAppFlyerRevenue = enableTrackingAppFlyerRevenue;
        initAppFlyerDebug(context, devKey, false, appsFlyerConversionListener);
    }

    public void initAppFlyerDebug(Application context, String devKey, boolean enableDebugLog) {
        AppsFlyerLib.getInstance().init(devKey, null, context);
        AppsFlyerLib.getInstance().start(context);

//        AppsFlyerAdRevenue.Builder afRevenueBuilder = new AppsFlyerAdRevenue.Builder(context);
//        AppsFlyerAdRevenue.initialize(afRevenueBuilder.build());
        AppsFlyerLib.getInstance().setDebugLog(enableDebugLog);
        PreferenceManager.getInstance().putBoolean("is_admob_network_full_ads", true);
    }

    public void initAppFlyerDebug(Application context, String devKey, boolean enableDebugLog, AppsFlyerConversionListener appsFlyerConversionListener) {
        AppsFlyerLib.getInstance().init(devKey, appsFlyerConversionListener, context);
        AppsFlyerLib.getInstance().start(context);

//        AppsFlyerAdRevenue.Builder afRevenueBuilder = new AppsFlyerAdRevenue.Builder(context);
//        AppsFlyerAdRevenue.initialize(afRevenueBuilder.build());
        AppsFlyerLib.getInstance().setDebugLog(enableDebugLog);
    }

    public void initAppFlyerWithLogAdRevenue(Application context, String devKey, boolean enableLoggingAdRevenue) {
        this.enableLoggingAdRevenue = enableLoggingAdRevenue;
        initAppFlyerDebug(context, devKey, true);
    }

    public void pushTrackEventAdmob(Context context, AdValue adValue, String adId, String adType) {
        Log.e(TAG, "Log tracking event AppFlyer: enableAppFlyer:" + this.enableTrackingAppFlyerRevenue + " --- AdType: " + adType + " --- value: " + adValue.getValueMicros() / 1000000);
        if (enableTrackingAppFlyerRevenue) {
//            Map<String, String> customParams = new HashMap<>();
//            customParams.put(AdRevenueScheme.AD_UNIT, adId);
//            customParams.put(AdRevenueScheme.AD_TYPE, adType);
//
//
//            AppsFlyerAdRevenue.logAdRevenue(
//                "Admob",
//                MediationNetwork.googleadmob,
//                Currency.getInstance(Locale.US),
//                (double) adValue.getValueMicros() / 1000000.0,
//                customParams
//            );


            AppsFlyerLib appsflyer = AppsFlyerLib.getInstance();
            AFAdRevenueData adRevenueData = new AFAdRevenueData(
                    "Admob",       // monetizationNetwork
                    MediationNetwork.GOOGLE_ADMOB, // mediationNetwork
                    "USD",           // currencyIso4217Code
                    (double) adValue.getValueMicros() / 1000000.0         // revenue
            );

            Map<String, Object> additionalParameters = new HashMap<>();
            additionalParameters.put(AdRevenueScheme.COUNTRY, Currency.getInstance(Locale.US));
            additionalParameters.put(AdRevenueScheme.AD_UNIT, adId);
            additionalParameters.put(AdRevenueScheme.AD_TYPE, adType);

//
//            Map<String, Object> valueCustomEvent = new HashMap<>();
//            valueCustomEvent.put(AdRevenueScheme.COUNTRY, Currency.getInstance(Locale.US));
//            valueCustomEvent.put(AdRevenueScheme.AD_UNIT, adId);
//            valueCustomEvent.put(AdRevenueScheme.AD_TYPE, adType);
//            valueCustomEvent.put("af_revenue", (double) adValue.getValueMicros() / 1000000.0);

            appsflyer.logAdRevenue(adRevenueData, additionalParameters);
//            appsflyer.logEvent(context, "ad_revenue_100_percent", valueCustomEvent);

        }
    }

    public void logRevenueEvent(Context context, AdValue adValue) {
        if (enableLoggingAdRevenue) {
            Log.e("LOGGING AD REVENUE", String.valueOf(adValue.getValueMicros()));
            Map<String, Object> customParams = new HashMap<>();
            customParams.put(AFInAppEventParameterName.REVENUE, (double) adValue.getValueMicros() / 1000000.0);
            AppsFlyerLib.getInstance().logEvent(context, "ad_impression", customParams);
        }
    }
}
