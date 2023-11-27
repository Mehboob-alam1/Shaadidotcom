package com.mehboob.myshadi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ShareActionProvider;

public class SessionManager {


    private SharedPreferences sharedPreferences;
    private final String PROFILE_FOR="profile_for";
    private final String GENDER="gender";
    private final String FULL_NAME="full_name";

    private final String DATE_OF_BIRTH="dob";



    public SessionManager(Context context) {

        sharedPreferences = context.getSharedPreferences(PROFILE_FOR, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(GENDER, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(FULL_NAME, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(DATE_OF_BIRTH, Context.MODE_PRIVATE);

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
    public void saveFullName(String fullName){

        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(FULL_NAME,fullName);
        editor.apply();
        editor.commit();
    }

    public String fetchFullName(){

        return sharedPreferences.getString(FULL_NAME,null);
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

    public void saveDOb(String dob){

        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(DATE_OF_BIRTH,dob);
        editor.apply();
        editor.commit();
    }

    public String fetchDob(){

        return sharedPreferences.getString(DATE_OF_BIRTH,null);
    }
}
