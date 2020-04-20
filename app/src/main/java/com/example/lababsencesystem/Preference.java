package com.example.lababsencesystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static android.content.Context.MODE_PRIVATE;

public class Preference {

    public static boolean saveAcc(String email, String pass,Context context,String type){
        SharedPreferences  Prefs=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor=Prefs.edit();
        prefsEditor.putString("Email",email);
        prefsEditor.putString("pass",pass);
        prefsEditor.putString("type",type);
        prefsEditor.apply();
        return true;
    }

    public  static String getEmail(Context context){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("Email",null);
    }
    public  static String getPass(Context context){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("pass",null);
    }
    public  static String getType(Context context){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("type",null);
    }

}
