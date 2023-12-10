package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityIncomeDetailsBinding;
import com.mehboob.myshadi.model.profilemodel.ProfileResponse;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.models.User;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.UserViewModel;

public class IncomeDetailsActivity extends AppCompatActivity {
    private ActivityIncomeDetailsBinding binding;
    private String[] workWith = {"Select", "Private Company", "Government / Public Sector", "Defense / Civil Services", "Business / Self Employed", "Not Working"};

    private FUPViewModel fupViewModel;
    private UserViewModel userViewModel;

    private SessionManager sessionManager;

    private User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIncomeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fupViewModel = new ViewModelProvider(this).get(FUPViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        sessionManager = new SessionManager(this);


        setWorkWithSpinner();


        userViewModel.getLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userData = user;
            }
        });
        binding.btnContinue.setOnClickListener(view -> {

            if (binding.spinnerWorkWith.getSelectedItem().equals("Select")) {
                Utils.showSnackBar(this, "Select a valid job");
            } else if (binding.etIncome.getText().toString().isEmpty() || binding.etWorksAs.getText().toString().isEmpty()) {
                Utils.showSnackBar(this, "Must fill all fields");
            } else {
                sessionManager.saveIncome(binding.etIncome.getText().toString());
                sessionManager.saveWorkAs(binding.etWorksAs.getText().toString());

                uploadData();
            }


        });

    }

    private void uploadData() {
        UserProfile profile = new UserProfile(sessionManager.fetchProfileFor(),
                sessionManager.fetchGender(),
                sessionManager.fetchFullName(),
                sessionManager.fetchDob(),
                sessionManager.fetchReligion(),
                sessionManager.fetchCommunity(),
                sessionManager.fetchLivingIn(),
                sessionManager.fetchEmail(),
                sessionManager.fetchPhoneNumber(),
                sessionManager.fetchCountryCode(),
                sessionManager.fetchStateName(),
                sessionManager.fetchStateCode(),
                sessionManager.fetchCityName(),
                sessionManager.fetchSubCommunity(),
                sessionManager.fetchMaritalStatus(),
                sessionManager.fetchChildren(),
                sessionManager.fetchHeight(),
                sessionManager.fetchDiet(),
                sessionManager.fetchQualifications(),
                sessionManager.fetchCollege(),
                sessionManager.fetchIncome(),
                sessionManager.fetchWorkWith(),
                sessionManager.fetchWorkAs(),
                "",
                userData.getUserId());
        fupViewModel.uploadUserProfile("",profile);

        fupViewModel.getResponse().observe(this, profileResponse -> {

            Utils.showSnackBar(this, profileResponse.getMessage() + profileResponse.isUpload());

            if (profileResponse.isUpload()) {
                startActivity(new Intent(IncomeDetailsActivity.this, SetPreferencesActivity.class));
            }

        });
    }

    private void setWorkWithSpinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, workWith);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerWorkWith.setAdapter(adapter);

        binding.spinnerWorkWith.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getSelectedItem().equals("Not Working")) {
                    binding.lineWorkAs.setVisibility(View.VISIBLE);
                } else {
                    binding.lineWorkAs.setVisibility(View.GONE);
                }

                sessionManager.saveWorkWith(adapterView.getSelectedItem().toString());


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        fupViewModel.getUserProfileMutableLiveData().observe(this, userProfile -> {
            Utils.showSnackBar(this, userProfile.toString());
        });


    }
}