package com.thelawhouse.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    public static SharedPreferences TheLawHouseSharedPreference;

    public static void putString(String key, String value) {
        TheLawHouseSharedPreference = MyApplication.getAppContext()
                .getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = TheLawHouseSharedPreference.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key, String defaultValue) {
        TheLawHouseSharedPreference = MyApplication.getAppContext()
                .getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return TheLawHouseSharedPreference.getString(key, defaultValue);
    }

    public static void putInt(String key, int value) {
        TheLawHouseSharedPreference = MyApplication.getAppContext()
                .getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = TheLawHouseSharedPreference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key, int defaultValue) {
        TheLawHouseSharedPreference = MyApplication.getAppContext()
                .getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        int string = TheLawHouseSharedPreference.getInt(key, defaultValue);
        return string;
    }

    public static void putBoolean(String key, boolean value) {
        TheLawHouseSharedPreference = MyApplication.getAppContext()
                .getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = TheLawHouseSharedPreference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        TheLawHouseSharedPreference = MyApplication.getAppContext()
                .getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        boolean string = TheLawHouseSharedPreference.getBoolean(key, defaultValue);
        return string;
    }

    public static boolean contains(String key) {
        TheLawHouseSharedPreference = MyApplication.getAppContext()
                .getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (TheLawHouseSharedPreference.contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    public static void remove(String key) {
        TheLawHouseSharedPreference = MyApplication.getAppContext()
                .getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = TheLawHouseSharedPreference.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void clearPreference() {
        TheLawHouseSharedPreference = MyApplication.getAppContext()
                .getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        TheLawHouseSharedPreference.edit().clear().commit();
    }
}
