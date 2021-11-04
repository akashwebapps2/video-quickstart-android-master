package com.twilio.video.test.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;


public class UserPref {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    public UserPref(Context context) {
        UserPref.context = context;
        sharedPreferences = context.getSharedPreferences("hey", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
       // editor.apply();

    }


    public static void setLogged(boolean is) {
        editor.putBoolean("USER_LOGGED", is);
        editor.apply();
    }


    public static boolean isLogged() {
        return sharedPreferences.getBoolean("USER_LOGGED", false);
    }


    public static void clear() {
        editor.clear();
    }

    public static void setUserName(String is) {
        editor.putString("setUserName", is);
        editor.apply();
    }


    public static String getUserName() {
        return sharedPreferences.getString("setUserName", "");
    }




}
