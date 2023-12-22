package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivitySetPreferencesBinding;

import com.mehboob.myshadi.model.profilemodel.Preferences;
import com.mehboob.myshadi.utils.MatchPref;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.views.dashboard.DashBoardActivity;

import java.util.ArrayList;
import java.util.List;

public class SetPreferencesActivity extends AppCompatActivity {
    private ActivitySetPreferencesBinding binding;
    private MatchPref matchPref;
    private FUPViewModel fupViewModel;
    private String userId;
    private boolean isSkipHide;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fupViewModel = new ViewModelProvider(this).get(FUPViewModel.class);
        binding = ActivitySetPreferencesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager=new SessionManager(this);

        matchPref = new MatchPref(this);

        isSkipHide = getIntent().getBooleanExtra("skip", false);
        if (isSkipHide) {
            binding.btnSkip.setVisibility(View.GONE);
        }
        fupViewModel.getUserProfileLiveData().observe(this, userProfile -> userId = userProfile.getUserId());

        binding.btnSkip.setOnClickListener(view -> {
            updateUi();
        });





        setReligionsAdapter();

        setCommunityAdapter();

        setSubCommunity();

        setMaritalStatus();


        binding.ageRangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> vals = slider.getValues();

         float firstThumb=vals.get(0);
         float secondThumb=vals.get(1);


            Utils.showSnackBar(this,"values are " + firstThumb + " " +secondThumb);
        });



        binding.btnContinue.setOnClickListener(view -> {

            if (
                    binding.spinnerReligion.getSelectedItemPosition() == 0 ||
                    binding.spinnerComunity.getSelectedItemPosition() == 0 ||
                    binding.spinnerSubCommunity.getSelectedItemPosition() == 0 ||
                    binding.spinnerMaritalStatus.getSelectedItemPosition() == 0) {
                Utils.showSnackBar(this, "Select valid option");
            } else {


                Preferences preferences = new Preferences(String.valueOf(binding.ageRangeSlider.getValues().get(0)),
                        String.valueOf(binding.ageRangeSlider.getValues().get(1)),
                  "Any",
                        "Any",
                        binding.spinnerCity.getText().toString(),
                        binding.spinnerReligion.getSelectedItem().toString(),
                        binding.spinnerComunity.getSelectedItem().toString(),
                        binding.spinnerSubCommunity.getSelectedItem().toString(),
                        binding.spinnerMaritalStatus.getSelectedItem().toString());

                fupViewModel.updatePreferences(preferences,sessionManager.fetchUserId() );
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






    private void updateSpinnerAgeMax(int minAgePosition, Spinner maxAgeSpinner) {

        List<CharSequence> maxAgeList = new ArrayList<>();
        ArrayAdapter<CharSequence> ageAdapter = (ArrayAdapter<CharSequence>) maxAgeSpinner.getAdapter();

        for (int i = minAgePosition + 1; i < ageAdapter.getCount(); i++) {
            maxAgeList.add(ageAdapter.getItem(i));
        }

        // Clear and update the max age spinner adapter


        ageAdapter.notifyDataSetChanged();
    }

    private void updateSpinnerHeightMax(int minHeightPosition, Spinner spinnerHeightMax) {
        List<CharSequence> maxHeightList = new ArrayList<>();
        ArrayAdapter<CharSequence> heightAdapter = (ArrayAdapter<CharSequence>) spinnerHeightMax.getAdapter();

        for (int i = minHeightPosition + 1; i < heightAdapter.getCount(); i++) {
            maxHeightList.add(heightAdapter.getItem(i));
        }

        // Clear and update the max height spinner adapter


        heightAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();

        fupViewModel.getUserProfileLiveData().observe(this, userProfile -> {


            binding.spinnerCity.setText(userProfile.getCityName());

        });


    }
}