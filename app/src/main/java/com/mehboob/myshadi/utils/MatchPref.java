package com.mehboob.myshadi.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mehboob.myshadi.model.match.Match;

import java.lang.reflect.Type;

public class MatchPref {


    private SharedPreferences sharedPreferences;
    private String PREFERENCE = "pref";

    public MatchPref(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }


    public void savePref(Match match) {

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//
//        editor.putString(PREFERENCE, gson.toJson(match));
//        editor.apply();
//        editor.commit();

        Gson gson = new Gson();
        String jsonStr = gson.toJson(match, Match.class);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFERENCE, jsonStr);
        editor.apply();

    }

    public Match fetchPref() {

//
//        String jsonObj = sharedPreferences.getString(PREFERENCE, null);
//        Type type = new TypeToken<Match>() {
//        }.getType();
//        Gson gson = new Gson();
//        return gson.fromJson(jsonObj, type);

        String json = sharedPreferences.getString(PREFERENCE, null);
        if (json != null) {
            Gson gson = new Gson();
            return gson.fromJson(json, Match.class);
        } else {
            return null; // Handle the case where the key is not found
        }
    }
}
