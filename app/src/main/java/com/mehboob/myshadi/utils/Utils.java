package com.mehboob.myshadi.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.Period;

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

    public static String extractFirst8Characters(String userId) {
        if (userId != null && userId.length() >= 8) {
            return userId.substring(0, 8);
        } else {
            // Handle the case where the user ID is null or shorter than 8 characters
            return ""; // or throw an exception, return a default value, etc.
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int calculateAge(String dateOfBirth) {
        // Parse the date of birth string to LocalDate
        LocalDate dob = LocalDate.parse(dateOfBirth);

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Calculate the difference between the current date and date of birth
        Period period = Period.between(dob, currentDate);

        // Return the age
        return period.getYears();
    }
}
