package com.mehboob.myshadi.room.entities;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.mehboob.myshadi.model.profilemodel.Preferences;

public class PreferencesConverter {


    private static Gson gson = new Gson();

    // TypeConverter for CommentCount to String
    @TypeConverter
    public static String fromPreferences(Preferences preferences) {
        return gson.toJson(preferences);
    }

    // TypeConverter for String to CommentCount
    @TypeConverter
    public static Preferences toPreferences(String json) {
        return gson.fromJson(json, Preferences.class);
    }
}
