package com.mehboob.myshadi.utils;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.navigation.NavController;

public class Utils {


    public static void safelyNavigate(NavController navController, @IdRes int resId, Bundle args) {
        try {
            navController.navigate(resId, args);
        } catch (Exception e) {
            Log.e("Tag",e.toString());
        }
    }
}
