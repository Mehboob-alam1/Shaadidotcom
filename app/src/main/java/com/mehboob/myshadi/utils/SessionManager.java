package com.mehboob.myshadi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ShareActionProvider;

public class SessionManager {


    private SharedPreferences sharedPreferences;
    private final String PROFILE_FOR="profile_for";
    private final String GENDER="gender";


    public SessionManager(Context context) {

        sharedPreferences = context.getSharedPreferences(PROFILE_FOR, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(GENDER, Context.MODE_PRIVATE);

    }

    public void saveProfileFor(String profileFor){

        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(PROFILE_FOR,profileFor);
        editor.apply();
        editor.commit();
    }

    public String fetchProfileFor(){

        return sharedPreferences.getString(PROFILE_FOR,null);
    }


    public void saveGender(String gender){

        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(GENDER,gender);
        editor.apply();
        editor.commit();
    }

    public String fetchGender(){

        return sharedPreferences.getString(GENDER,null);
    }
}
