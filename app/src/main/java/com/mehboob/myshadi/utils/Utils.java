package com.mehboob.myshadi.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.navigation.NavController;

import com.google.android.material.snackbar.Snackbar;

public class Utils {


    public static void safelyNavigate(NavController navController, @IdRes int resId, Bundle args) {
        try {
            navController.navigate(resId, args);
        } catch (Exception e) {
            Log.e("Tag",e.toString());
        }
    }

    public static void showSnackBar(Activity context,String message){
        Snackbar snackbar = Snackbar.make(
                context.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
