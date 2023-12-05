package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivitySetPreferencesBinding;

import java.util.ArrayList;
import java.util.List;

public class SetPreferencesActivity extends AppCompatActivity {
    private ActivitySetPreferencesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySetPreferencesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSpinnerAges();

        setSpinnerHeights();
        
        setReligionsAdapter();
        
        setCommunityAdapter();

        setSubCommunity();
        
        setMaritalStatus();
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

    private void setSpinnerHeights() {

        ArrayAdapter<CharSequence> heightsAdapter = ArrayAdapter.createFromResource(this,
                R.array.heights,
                android.R.layout.simple_spinner_item
        );

        heightsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerHeightMin.setAdapter(heightsAdapter);
        binding.spinnerHeightMax.setAdapter(heightsAdapter);

        binding.spinnerHeightMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateSpinnerHeightMax(i, binding.spinnerHeightMax);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void setSpinnerAges() {
        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.ages,
                android.R.layout.simple_spinner_item
        );
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter to both spinners
        binding.spinnerAgeMin.setAdapter(ageAdapter);
        binding.spinnerAgeMax.setAdapter(ageAdapter);


        binding.spinnerAgeMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateSpinnerAgeMax(i, binding.spinnerAgeMax);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
}