package com.mehboob.myshadi.views.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityNameDateBirthBinding;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NameDateBirthActivity extends AppCompatActivity {
    private ActivityNameDateBirthBinding binding;
    private SessionManager sessionManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNameDateBirthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager= new SessionManager(this);

        binding.imgBack.setOnClickListener(view -> {
            finish();
        });


        binding.btnContinue.setOnClickListener(view -> {
            validateDate();
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void validateDate() {
        String dayStr = binding.etDay.getText().toString();
        String monthStr = binding.etMonth.getText().toString();
        String yearStr = binding.etYear.getText().toString();
        String firstName = binding.etFirstName.getText().toString();
        String lastName = binding.etLastName.getText().toString();

        if (dayStr.isEmpty() || monthStr.isEmpty() || yearStr.isEmpty()) {
            showToast("Please enter all date components.");
            return;
        }

        int day = Integer.parseInt(dayStr);
        int month = Integer.parseInt(monthStr);
        int year = Integer.parseInt(yearStr);

        if (day < 1 || day > 31 || month < 1 || month > 12 || year < 1970) {
            showToast("Invalid day or month. Day should be between 1 and 31, and month between 1 and 12.");
            return;
        }

       if (firstName.isEmpty() || lastName.isEmpty()){
           showToast("Kindly provide name to proceed further");
           return;
       }
        // Check for valid date format
        if (!isValidDate(day, month, year)) {
            showToast("Invalid date format. Please enter a valid date.");
            return;
        }

        // Check if age is at least 21 years
        if (!isAgeValid(day, month, year, 21)) {
            showToast("Age must be at least 21 years.");
            return;
        }

        // Continue with your logic if the date is valid and age is at least 21
        // ...
        String fullName=firstName + " "+lastName;

       // String dateOfBirth = "2000-12-31";
        String dob=yearStr +"-"+monthStr+ "-"+dayStr;
       String date= String.valueOf(Utils.calculateAge(dob));
        sessionManager.saveFullName(fullName);
        sessionManager.saveDOb(date);
        startActivity(new Intent(NameDateBirthActivity.this, ReligionCastActivity.class));
        showToast("Date is valid and age is at least 21.");
    }

    private boolean isValidDate(int day, int month, int year) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            dateFormat.setLenient(false);
            dateFormat.parse(day + "/" + month + "/" + year);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isAgeValid(int day, int month, int year, int minimumAge) {
        Calendar birthDate = Calendar.getInstance();
        birthDate.set(year, month - 1, day); // Note: month is zero-based
        Calendar currentDate = Calendar.getInstance();

        int age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        if (birthDate.after(currentDate)) {
            return false; // Future date
        } else if (birthDate.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)
                && birthDate.get(Calendar.DAY_OF_MONTH) > currentDate.get(Calendar.DAY_OF_MONTH)) {
            age--; // Birthday not yet occurred this year
        }

        return age >= minimumAge;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}