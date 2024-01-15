package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ArrayAdapter;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivitySetPreferencesBinding;

import com.mehboob.myshadi.model.profilemodel.Preferences;
import com.mehboob.myshadi.room.entities.UserProfileData;
import com.mehboob.myshadi.utils.MatchPref;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.views.dashboard.DashBoardActivity;


import java.util.List;

public class SetPreferencesActivity extends AppCompatActivity {
    private ActivitySetPreferencesBinding binding;
    private MatchPref matchPref;
    private FUPViewModel fupViewModel;
    private String userId;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fupViewModel = new ViewModelProvider(this).get(FUPViewModel.class);
        binding = ActivitySetPreferencesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(this);

        matchPref = new MatchPref(this);


        fupViewModel.getUserProfileLiveData().observe(this, new Observer<UserProfileData>() {
            @Override
            public void onChanged(UserProfileData userProfileData) {
                if (userProfileData != null) {
                    userId = userProfileData.getUserId();
                }
            }
        });





        setReligionsAdapter();

        setCommunityAdapter();

        setSubCommunity();

        setMaritalStatus();


//        binding.ageRangeSlider.addOnChangeListener((slider, value, fromUser) -> {
//            List<Float> vals = slider.getValues();
//
//            float firstThumb = vals.get(0);
//            float secondThumb = vals.get(1);
//
//
//            Utils.showSnackBar(this, "values are " + firstThumb + " " + secondThumb);
//        });


        binding.btnContinue.setOnClickListener(view -> {

            if (
                    binding.spinnerReligion.getSelectedItemPosition() == 0 ||
                            binding.spinnerComunity.getSelectedItemPosition() == 0 ||
                            binding.spinnerSubCommunity.getSelectedItemPosition() == 0 ||
                            binding.spinnerMaritalStatus.getSelectedItemPosition() == 0) {
                Utils.showSnackBar(this, "Select valid option");
            } else {


                Preferences preferences = new Preferences(String.valueOf(21),
                        String.valueOf(34),
                        "Any",
                        "Any",
                        binding.spinnerCity.getText().toString(),
                        binding.spinnerReligion.getSelectedItem().toString(),
                        binding.spinnerComunity.getSelectedItem().toString(),
                        binding.spinnerSubCommunity.getSelectedItem().toString(),
                        binding.spinnerMaritalStatus.getSelectedItem().toString());

                fupViewModel.updatePreferences(preferences, sessionManager.fetchUserId());
                matchPref.savePref(preferences);
                fupViewModel.getIsPreferencesAdded().observe(this, aBoolean -> {
                    if (aBoolean) {
                        Utils.showSnackBar(this, "Preferences update successfully");
                        updateUi();
                    } else {
                        Utils.showSnackBar(this, "Something went wrong");
                    }
                });


                //  Utils.showSnackBar(this, matchPref.fetchPref().toString());


            }


        });
    }

    public void updateUi() {
        startActivity(new Intent(SetPreferencesActivity.this, DashBoardActivity.class));
    }

    private void setMaritalStatus() {


        ArrayAdapter<CharSequence> maritalStatusAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.marital_statuses,
                android.R.layout.simple_spinner_item
        );
        maritalStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter to the marital status spinner
        binding.spinnerMaritalStatus.setAdapter(maritalStatusAdapter);
    }

    private void setSubCommunity() {

        ArrayAdapter<CharSequence> denominationsAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.religious_denominations,
                android.R.layout.simple_spinner_item
        );
        denominationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter to the denominations spinner
        binding.spinnerSubCommunity.setAdapter(denominationsAdapter);
    }

    private void setCommunityAdapter() {

        ArrayAdapter<CharSequence> communityAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.communities,
                android.R.layout.simple_spinner_item
        );
        communityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter to the community spinner
        binding.spinnerComunity.setAdapter(communityAdapter);
    }

    private void setReligionsAdapter() {

        ArrayAdapter<CharSequence> religionAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.religions,
                android.R.layout.simple_spinner_item
        );
        religionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter to the religion spinner
        binding.spinnerReligion.setAdapter(religionAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();

        fupViewModel.getUserProfileLiveData().observe(this, userProfile -> {

            if (userProfile != null) {
                binding.spinnerCity.setText(userProfile.getCityName());
            }


        });


    }
}