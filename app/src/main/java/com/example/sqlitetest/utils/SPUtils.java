package com.example.sqlitetest.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by liuchen on 2016/4/20.
 */
public class SPUtils {

    public static final String SP_FILE_DEFAULT = "ketr_tjzc";


    public static void setInt(Context context, String key, int degree) {
        SharedPreferences preferences = context.getSharedPreferences(SP_FILE_DEFAULT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, degree);
        editor.commit();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(SP_FILE_DEFAULT, Context.MODE_PRIVATE);
        return preferences.getInt(key, -1);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SP_FILE_DEFAULT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(SP_FILE_DEFAULT, Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(SP_FILE_DEFAULT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(SP_FILE_DEFAULT, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defaultValue);
    }



}
