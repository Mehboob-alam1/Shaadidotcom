package com.mehboob.myshadi.room.entities;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayListConverter {

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append(item).append(",");
        }
        return sb.toString();
    }

    @TypeConverter
    public static ArrayList<String> toArrayList(String value) {
        String[] items = value.split(",");
        return new ArrayList<>(Arrays.asList(items));
    }

}
