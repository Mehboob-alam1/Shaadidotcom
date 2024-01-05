package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityProfileDetailedBinding;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.viewmodel.FUPViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfileDetailedActivity extends AppCompatActivity {
    private UserMatches userMatches;

    private ActivityProfileDetailedBinding binding;


    private FUPViewModel fupViewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fupViewModel = new ViewModelProvider(this).get(FUPViewModel.class);
        sessionManager = new SessionManager(this);
        binding.btnBackProfile.setOnClickListener(v -> {
            finish();
        });

        Type type = new TypeToken<UserMatches>() {
        }.getType();
        userMatches = new Gson().fromJson(getIntent().getStringExtra("currentPerson"), type);

        fupViewModel.getProfile(sessionManager.fetchUserId());

        bindData(userMatches);
    }

    private void bindData(UserMatches userMatches) {


        try {
            Glide.with(this).load(userMatches.getImageUrl())
                    .placeholder(R.drawable.img_no_internet)
                    .into(binding.imgProfileImageM)
            ;

        } catch (Exception e) {
            Log.d("Exception", e.getLocalizedMessage());
        }

        if (userMatches.isVerified()) {
            binding.imgVerifiedM.setVisibility(View.VISIBLE);

        }
        fupViewModel.getUserProfileLiveData().observe(this, userProfileData -> {
            if (userProfileData != null) {


                try {


                    Glide.with(this)
                            .load(userProfileData.getImageUrl())
                            .placeholder(R.drawable.profile)
                            .into(binding.imgMyProfile);
                } catch (Exception e) {
                    Log.d("Exception", e.getLocalizedMessage());
                }
                if (userProfileData.isVerified()) {
                    binding.txtBirthDate.setText(userMatches.getDate_of_birth());
                    binding.txtBirthDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    binding.txtContact.setText(userMatches.getPhoneNumber());
                    binding.txtContact.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    binding.txtEmail.setText(userMatches.getEmail());
                    binding.txtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    binding.txtWorkAsWorkandWith.setText(userMatches.getWorkAs() + ", " + userMatches.getWorksWith());
                    binding.txtWorkAsWorkandWith.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    binding.txtCollegeName.setText(userMatches.getCollege());
                    binding.txtCollegeName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                } else {
                    Toast.makeText(this, "You are not verified ", Toast.LENGTH_SHORT).show();
                }

            }

        });
        // if this current user is verified

        binding.txtContact.setText(extractPhoneNumber(userMatches.getPhoneNumber()));
        binding.txtIncome.setText(userMatches.getIncome());
        binding.txtProfileNameM.setText(userMatches.getFullName());
        binding.txtProfileAgeHeightM.setText(userMatches.getDob() + " yrs," + userMatches.getHeight());
        binding.txtProfileProfessionM.setText(userMatches.getWorkAs());
        binding.txtCommunitySubCommunityM.setText(userMatches.getCommunity() + " ," + userMatches.getSubCommunity());
        binding.txtCityCountryM.setText(userMatches.getCityName() + "," + userMatches.getLivingIn());
        binding.txtAboutPerson.setText("About " + userMatches.getFullName());
        binding.txtAboutDescPerson.setText(userMatches.getAboutMe());
        binding.txtCreatedBySelf.setText("Created by Self");
        binding.txtId.setText("ID: " + userMatches.getUserId());
        binding.txtAge.setText(userMatches.getDob() + " yrs old");
        binding.txtHeight.setText("Height- " + userMatches.getHeight());
        binding.txtMaritalStatus.setText(userMatches.getMaritalStatus());
        binding.txtLivesCityStateCountry.setText(userMatches.getCityName() + ", " + userMatches.getStateName() + ", " + userMatches.getLivingIn());
        binding.txtReligionCommunity.setText(userMatches.getReligion() + ", " + userMatches.getCommunity());
        binding.txtSubCommunity.setText(userMatches.getSubCommunity());
        binding.txtDiet.setText(userMatches.getDiet());

        if (userMatches.getGender().equals("Male")) {
            binding.txtYouAndPersonM.setText("You and him");
            binding.txtYouAndPerson.setText("You and him");
        } else {
            binding.txtYouAndPersonM.setText("You and her");
            binding.txtYouAndPerson.setText("You and her");
        }

        Glide.with(this)
                .load(userMatches.getImageUrl())
                .placeholder(R.drawable.profile)
                .into(binding.imgPersonProfile);


    }

    private String extractPhoneNumber(String phoneNumber) {


        // Extract the relevant parts of the phone number
        String countryCode = phoneNumber.substring(0, 3);
        String operatorCode = phoneNumber.substring(3, 6);
        String remainingDigits = phoneNumber.substring(6);

        // Create the formatted phone number
        String formattedPhoneNumber = countryCode + operatorCode + "******";

        return formattedPhoneNumber;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}