package com.mehboob.myshadi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ShareActionProvider;

public class SessionManager {


    private SharedPreferences sharedPreferences;
    private final String PROFILE_FOR = "profile_for";
    private final String GENDER = "gender";
    private final String FULL_NAME = "full_name";

    private final String DATE_OF_BIRTH = "dob";

    private final String RELIGION = "religion";
    private final String COMMUNITY = "community";
    private final String LIVING_IN = "living_in";
    private final String COUNTRY_CODE = "country_code";

    private final String EMAIL = "email";

    private final String PHONE_NUMBER = "phone";


    public SessionManager(Context context) {

        sharedPreferences = context.getSharedPreferences(PROFILE_FOR, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(GENDER, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(FULL_NAME, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(DATE_OF_BIRTH, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(RELIGION, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(COMMUNITY, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(LIVING_IN, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(EMAIL, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(PHONE_NUMBER, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(COUNTRY_CODE, Context.MODE_PRIVATE);


    }

    public void saveCountryCode(String code){

        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(COUNTRY_CODE,code);
        editor.apply();
        editor.commit();
    }

    public String fetchCountryCode(){

        return sharedPreferences.getString(COUNTRY_CODE,null);
    }

    public void saveReligion(String religion) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(RELIGION, religion);
        editor.apply();
        editor.commit();
    }


    public void saveEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, email);
        editor.apply();
        editor.commit();

    }


    public String fetchEmail() {
        return sharedPreferences.getString(EMAIL, null);
    }

    public void savePhoneNumber(String phoneNumber) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PHONE_NUMBER, phoneNumber);
        editor.apply();
        editor.commit();
    }

    public String fetchPhoneNumber() {
        return sharedPreferences.getString(PHONE_NUMBER, null);
    }

    public String fetchReligion() {

        return sharedPreferences.getString(RELIGION, null);
    }

    public void saveCommunity(String community) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COMMUNITY, community);
        editor.apply();
        editor.commit();
    }

    public String fetchCommunity() {

        return sharedPreferences.getString(COMMUNITY, null);
    }

    public void saveLivingIn(String livingIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LIVING_IN, livingIn);
        editor.apply();
        editor.commit();
    }

    public String fetchLivingIn() {

        return sharedPreferences.getString(LIVING_IN, null);
    }

    public void saveProfileFor(String profileFor) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROFILE_FOR, profileFor);
        editor.apply();
        editor.commit();
    }

    public String fetchProfileFor() {

        return sharedPreferences.getString(PROFILE_FOR, null);
    }

    public void saveFullName(String fullName) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FULL_NAME, fullName);
        editor.apply();
        editor.commit();
    }

    public String fetchFullName() {

        return sharedPreferences.getString(FULL_NAME, null);
    }


    public void saveGender(String gender) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GENDER, gender);
        editor.apply();
        editor.commit();
    }

    public String fetchGender() {

        return sharedPreferences.getString(GENDER, null);
    }

    public void saveDOb(String dob) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DATE_OF_BIRTH, dob);
        editor.apply();
        editor.commit();
    }

    public String fetchDob() {

        return sharedPreferences.getString(DATE_OF_BIRTH, null);
    }
}
