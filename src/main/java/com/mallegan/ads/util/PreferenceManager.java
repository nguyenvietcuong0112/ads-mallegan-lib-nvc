package com.mallegan.ads.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String PREF_NAME = "pref_ads_libs";
    public static final String PREF_ADMOB_NETWORK = "admob_network";
    public static final String PREF_ADMOB_NETWORK_APPSFLYER = "admob_network_appsflyer";
    public static final String PREF_IS_SHOW_FULL_ADS = "is_admob_network_full_ads";
    public static final String ORGANIC = "organic";
    public static final String UNTRUSTED_DEVICES = "untrusted";
    public static String ADS_SPACE_15_SECOND;
    private static SharedPreferences.Editor editor;
    private static PreferenceManager instance;
    private static SharedPreferences mShare;

    public PreferenceManager() {
    }

    public static void init(Context context) {
        instance = new PreferenceManager();
        editor = (mShare = context.getApplicationContext().getSharedPreferences("pref_ads_libs", 0)).edit();
    }

    public static PreferenceManager getInstance() {
        return instance;
    }

    public void remove(String var1) {
        if (mShare.contains(var1)) {
            editor.remove(var1);
            editor.commit();
        }

    }

    public void putBoolean(String var1, boolean var2) {
        mShare.edit().putBoolean(var1, var2).apply();
    }

    public void putInt(String var1, int var2) {
        mShare.edit().putInt(var1, var2).apply();
    }

    public void putFloat(String var1, float var2) {
        mShare.edit().putFloat(var1, var2).apply();
    }

    public void putLong(String var1, long var2) {
        mShare.edit().putLong(var1, var2).apply();
    }

    public boolean getBoolean(String var1) {
        return mShare.getBoolean(var1, false);
    }

    public int getInt(String var1) {
        return mShare.getInt(var1, 0);
    }

    public long getLong(String var1) {
        return mShare.getLong(var1, 0L);
    }

    public float getFloat(String var1) {
        return mShare.getFloat(var1, 0.0F);
    }

    public void putString(String var1, String var2) {
        mShare.edit().putString(var1, var2).apply();
    }

    public String getString(String var1, String var2) {
        return mShare.getString(var1, var2);
    }
}
