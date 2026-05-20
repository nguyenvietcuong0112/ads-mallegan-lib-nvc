package com.mallegan.ads.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.mallegan.ads.BuildConfig;
import com.mallegan.ads.R;
import com.mallegan.ads.banner.BannerPlugin;
import com.mallegan.ads.billing.AppPurchase;
import com.mallegan.ads.callback.AdCallback;
import com.mallegan.ads.callback.InterCallback;
import com.mallegan.ads.callback.NativeCallback;
import com.mallegan.ads.callback.RewardCallback;
import com.mallegan.ads.dialog.LoadingAdsDialog;
import com.mallegan.ads.dialog.LoadingAdsDialogNoti;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Admob {
    public static final String BANNER_INLINE_SMALL_STYLE = "BANNER_INLINE_SMALL_STYLE";
    public static final String BANNER_INLINE_LARGE_STYLE = "BANNER_INLINE_LARGE_STYLE";
    public static long timeLimitAds = 0;
    public static boolean isShowAllAds = true;

    private static Admob INSTANCE;
    final AdmobCore core;

    private Admob() {
        core = new AdmobCore();
    }

    public static Admob getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Admob();
        }
        return INSTANCE;
    }

    public  void initAdmob(Context context, List<String> testDeviceList) {
        core.initAdmob(context, testDeviceList);
    }

    public  void initAdmob(Context context) {
        core.initAdmob(context);
    }

    public  void setFan(boolean fan) {
        core.setFan(fan);
    }

    public  void setDisableAdResumeWhenClickAds(boolean disableAdResumeWhenClickAds) {
        core.setDisableAdResumeWhenClickAds(disableAdResumeWhenClickAds);
    }

    public  void setOpenShowAllAds(boolean isShowAllAds) {
        core.setOpenShowAllAds(isShowAllAds);
    }

    public  void setOpenEventLoadTimeLoadAdsSplash(boolean logTimeLoadAdsSplash) {
        core.setOpenEventLoadTimeLoadAdsSplash(logTimeLoadAdsSplash);
    }

    public  void setOpenEventLoadTimeShowAdsInter(boolean logLogTimeShowAds) {
        core.setOpenEventLoadTimeShowAdsInter(logLogTimeShowAds);
    }

    public  final boolean isLoadFullAds() {
        return core.isLoadFullAds();
    }

    public  void loadBanner(final Activity mActivity, String id) {
        core.loadBanner(mActivity, id);
    }

    public  void loadBannerPlugin(Activity activity, ViewGroup layout, ViewGroup shimmer, BannerPlugin.Config config) {
        core.loadBannerPlugin(activity, layout, shimmer, config);
    }

    public  void loadBanner(final Activity mActivity, String id, AdCallback callback) {
        core.loadBanner(mActivity, id, callback);
    }

    public  void loadBanner(final Activity mActivity, String id, Boolean useInlineAdaptive) {
        core.loadBanner(mActivity, id, useInlineAdaptive);
    }

    public  void loadInlineBanner(final Activity activity, String id, String inlineStyle) {
        core.loadInlineBanner(activity, id, inlineStyle);
    }

    public  void loadBanner(final Activity mActivity, String id, final AdCallback callback, Boolean useInlineAdaptive) {
        core.loadBanner(mActivity, id, callback, useInlineAdaptive);
    }

    public  void loadInlineBanner(final Activity activity, String id, String inlineStyle, final AdCallback callback) {
        core.loadInlineBanner(activity, id, inlineStyle, callback);
    }

    public  void loadCollapsibleBanner(final Activity mActivity, String id, String gravity) {
        core.loadCollapsibleBanner(mActivity, id, gravity);
    }

    public  void loadBannerFragment(final Activity mActivity, String id, final View rootView) {
        core.loadBannerFragment(mActivity, id, rootView);
    }

    public  void loadBannerFragment(final Activity mActivity, String id, final View rootView, final AdCallback callback) {
        core.loadBannerFragment(mActivity, id, rootView, callback);
    }

    public  void loadBannerFragment(final Activity mActivity, String id, final View rootView, Boolean useInlineAdaptive) {
        core.loadBannerFragment(mActivity, id, rootView, useInlineAdaptive);
    }

    public  void loadInlineBannerFragment(final Activity activity, String id, final View rootView, String inlineStyle) {
        core.loadInlineBannerFragment(activity, id, rootView, inlineStyle);
    }

    public  void loadBannerFragment(final Activity mActivity, String id, final View rootView, final AdCallback callback, Boolean useInlineAdaptive) {
        core.loadBannerFragment(mActivity, id, rootView, callback, useInlineAdaptive);
    }

    public  void loadInlineBannerFragment(final Activity activity, String id, final View rootView, String inlineStyle, final AdCallback callback) {
        core.loadInlineBannerFragment(activity, id, rootView, inlineStyle, callback);
    }

    public  void loadCollapsibleBannerFragment(final Activity mActivity, String id, final View rootView, String gravity) {
        core.loadCollapsibleBannerFragment(mActivity, id, rootView, gravity);
    }

    public  boolean interstitialSplashLoaded() {
        return core.interstitialSplashLoaded();
    }

    public  InterstitialAd getmInterstitialSplash() {
        return core.getmInterstitialSplash();
    }

    public  RewardedAd getRewardedAd() {
        return core.getRewardedAd();
    }

    public  void loadSplashInterAds(final Context context, String id, long timeOut, long timeDelay, final InterCallback adListener) {
        core.loadSplashInterAds(context, id, timeOut, timeDelay, adListener);
    }

    public  void loadSplashInterAds2(final Context context, String id, long timeDelay, final InterCallback adListener) {
        core.loadSplashInterAds2(context, id, timeDelay, adListener);
    }

    public  void loadSplashInterAdsFloor(final Context context, List<String> listID, long timeDelay, final InterCallback adListener) {
        core.loadSplashInterAdsFloor(context, listID, timeDelay, adListener);
    }

    public  void dismissLoadingDialog() {
        core.dismissLoadingDialog();
    }

    public  void loadInterAds(Context context, String id, InterCallback adCallback) {
        core.loadInterAds(context, id, adCallback);
    }

    public  void showInterAds(Context context, InterstitialAd mInterstitialAd, final InterCallback callback) {
        core.showInterAds(context, mInterstitialAd, callback);
    }

    public  void showInterAdsNoti(Context context, InterstitialAd mInterstitialAd, final InterCallback callback) {
        core.showInterAdsNoti(context, mInterstitialAd, callback);
    }

    public  void loadAndShowInter(AppCompatActivity activity, String idInter, int timeDelay, int timeOut, InterCallback callback) {
        core.loadAndShowInter(activity, idInter, timeDelay, timeOut, callback);
    }

    public  void loadAndShowInterNoti(AppCompatActivity activity, String idInter, int timeDelay, int timeOut, InterCallback callback) {
        core.loadAndShowInterNoti(activity, idInter, timeDelay, timeOut, callback);
    }

    public  void showRewardAds(final Activity context, final RewardCallback adCallback) {
        core.showRewardAds(context, adCallback);
    }

    public  void initRewardAds(Context context, String id) {
        core.initRewardAds(context, id);
    }

    public  void loadNativeAd(Context context, String id, final NativeCallback callback) {
        core.loadNativeAd(context, id, callback);
    }

    public  void loadNativeAds(Context context, String id, int numsOfAds, final NativeCallback callback) {
        core.loadNativeAds(context, id, numsOfAds, callback);
    }

    public  void loadNativeAdFloor(Context context, List<String> listID, final NativeCallback callback) {
        core.loadNativeAdFloor(context, listID, callback);
    }

    public  void pushAdsToViewCustom(NativeAd nativeAd, NativeAdView adView) {
        core.pushAdsToViewCustom(nativeAd, adView);
    }

    public  void loadNativeFragment(final Activity mActivity, String id, View parent) {
        core.loadNativeFragment(mActivity, id, parent);
    }

    public  AdRequest getAdRequest() {
        return core.getAdRequest();
    }

    public  void setOpenActivityAfterShowInterAds(boolean openActivityAfterShowInterAds) {
        core.setOpenActivityAfterShowInterAds(openActivityAfterShowInterAds);
    }

    public  String getDeviceId(Activity activity) {
        return core.getDeviceId(activity);
    }

    public  void onCheckShowSplashWhenFail(final AppCompatActivity activity, final InterCallback callback, int timeDelay) {
        core.onCheckShowSplashWhenFail(activity, callback, timeDelay);
    }

    public  void onCheckShowSplashWhenFailClickButton(final AppCompatActivity activity, InterstitialAd interstitialAd, final InterCallback callback, int timeDelay) {
        core.onCheckShowSplashWhenFailClickButton(activity, interstitialAd, callback, timeDelay);
    }

    public  int round1000(long time) {
        return core.round1000(time);
    }

    public  void loadSplashInterAds3(final Context context, String id, long timeDelay, final InterCallback adListener) {
        core.loadSplashInterAds3(context, id, timeDelay, adListener);
    }

    public  void loadAndShowInter2(AppCompatActivity activity, String idInter, int timeDelay, int timeOut, InterCallback callback) {
        core.loadAndShowInter2(activity, idInter, timeDelay, timeOut, callback);
    }
}