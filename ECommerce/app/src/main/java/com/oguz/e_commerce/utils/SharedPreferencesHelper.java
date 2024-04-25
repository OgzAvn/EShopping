package com.oguz.e_commerce.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String SHARED_PREF_NAME = "MySharedPref";

    private static SharedPreferencesHelper instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPreferencesHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesHelper(context);
        }
        return instance;
    }

    public void clear(){
        editor.clear();
        editor.apply();
    }

    // Örnek bir String değeri kaydetmek için metot
    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    // Örnek bir String değeri almak için metot
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    // Örnek bir int değeri kaydetmek için metot
    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    // Örnek bir int değeri almak için metot
    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void saveBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Örnek bir int değeri almak için metot
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }


}