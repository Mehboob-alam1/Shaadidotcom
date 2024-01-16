package com.mehboob.myshadi.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityEditBasicInfoBinding;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.room.entities.UserProfileData;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Objects;

public class EditBasicInfoActivity extends AppCompatActivity {
    private ActivityEditBasicInfoBinding binding;
    private String[] maritalStatus = {"Never Married", "Divorced", "Widowed", "Awaiting Divorce", "Annulled"};
    private String[] religion = {"Muslim", "Hindu", "Christian", "Jain", "Sikh", "Buddhist", "Parsi", "Jewish", "Other", "No religion", "Spiritual - not religious"};
    private String[] community = {"Urdu", "Hindi", "Marathi", "Punjabi", "Bengali", "Gujrati", "Telugu", "Kannada", "English", "Tamil", "Odia", "Marwari", "Aka", "Arabic", "Arunachali", "Assamese", "Awadhi"
            , "Baluchi", "Bhojpuri", "Bhutia", "Brahui", "Brij", "Burmese", "Chattisgarhi", "Chinese", "Coorgi", "Dogri", "French", "Gharwali", "Garo", "Haryanavi",
            "Himachli/Pahari", "Hindko", "Kakbarak", "Kanauji", "Kashmiri", "Khandesi", "Khasi", "Konkani", "Koshali", "Pashto", "Sindhi", "Balochi", "Shina", "Brushuski", "Khawar", "Wakhi", "Seraiki",
            "Other"};

    private String[] workWith = {"Private Company", "Government / Public Sector", "Defense / Civil Services", "Business / Self Employed", "Not Working"};
    private String[] diet = {"Select", "Veg", "Non-Veg", "Occasionally Non-Veg", "Eggetarian", "Jain", "Vegan"};
    private UserProfileData userProfile;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBasicInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(this);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        Type type = new TypeToken<UserProfileData>() {
        }.getType();
        userProfile = new Gson().fromJson(getIntent().getStringExtra("user"), type);


        setSomeData(userProfile);
        setMaritalStatus();
        setHeightSpinner();
        setReligionSpinner();
        setCommunitySpinner();
        setSubCommunity();
        setWorkWithSpinner();
        setDietSpinner();

        binding.btnUpdateBasicInfo.setOnClickListener(v -> {

            if (binding.etAge.getText().toString().isEmpty()) {
                Utils.showSnackBar(this, "Add age of yours");
                binding.etAge.requestFocus();
                return;
            }
            if (binding.spinnerMaritalStatus.getSelectedItem() == null) {
                Utils.showSnackBar(this, "Add your marital status");
                return;
            }
            if (binding.spinnerHeight.getSelectedItem() == null) {
                Utils.showSnackBar(this, "Add your height");
                return;
            }
            if (binding.spinnerHeight.getSelectedItem().equals("Select height")) {
                Utils.showSnackBar(this, "Please select your height");
                return;
            }
            if (binding.spinnerReligion.getSelectedItem().equals("Select Religion")) {
                Utils.showSnackBar(this, "Please select your religion");
                return;
            }

            if (binding.spinnerCommunity.getSelectedItem() == null) {
                Utils.showSnackBar(this, "Add your community");
                return;
            }
            if (binding.spinnerSubCommunity.getSelectedItem().equals("Select sub community")) {
                Utils.showSnackBar(this, "Add your sub community");
                return;
            }
            if (binding.spinnerworksWith.getSelectedItem() == null) {
                Utils.showSnackBar(this, "Where do you work?");
                return;
            }

            if (binding.etWorkAs.getText().toString().isEmpty()) {
                Utils.showSnackBar(this, "Kindly add what do you work");
                return;

            }

            if (binding.spinnerDiet.getSelectedItem().equals("Select")) {
                Utils.showSnackBar(this, "Add your diet");

                return;

            }

            if (binding.etIncome.getText().toString().isEmpty()) {
                Utils.showSnackBar(this, "Add your income");
                return;
            }

            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait.....");
            dialog.setCancelable(false);
            dialog.show();

            String age = binding.etAge.getText().toString();
            String maritalStatus = binding.spinnerMaritalStatus.getSelectedItem().toString();
            String height = binding.spinnerHeight.getSelectedItem().toString();
            String religion = binding.spinnerReligion.getSelectedItem().toString();
            String community = binding.spinnerCommunity.getSelectedItem().toString();
            String subCommunity = binding.spinnerSubCommunity.getSelectedItem().toString();
            String workWith = binding.spinnerworksWith.getSelectedItem().toString();
            String workAs = binding.etWorkAs.getText().toString();
            String diet = binding.spinnerDiet.getSelectedItem().toString();
            String annualIncome = binding.etIncome.getText().toString();


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("userProfiles").child(sessionManager.fetchGender()).child(sessionManager.fetchUserId());

            HashMap<String, Object> map = new HashMap<>();
            map.put("dob", age);
            map.put("maritalStatus", maritalStatus);
            map.put("height", height);
            map.put("religion", religion);
            map.put("community", community);
            map.put("subCommunity", subCommunity);
            map.put("worksWith", workWith);
            map.put("workAs", workAs);
            map.put("diet", diet);
            map.put("income", annualIncome);


            reference.updateChildren(map)
                    .addOnSuccessListener(aVoid -> {
                        // Handle successful update
                        Utils.showSnackBar(this, "Profile update successfully");
                        dialog.dismiss();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Utils.showSnackBar(this, e.getLocalizedMessage());
                    });


        });
    }

    private void setSomeData(UserProfileData userProfile) {


        try {
            binding.txtProfileCreatedBy.setText(userProfile.getProfileFor());
            binding.txtCountry.setText(userProfile.getLivingIn());
            binding.txtState.setText(userProfile.getStateName());
            binding.txtCity.setText(userProfile.getCityName());
        } catch (Exception e) {
            Log.d("Exception", e.getLocalizedMessage());
        }
    }

    private void setDietSpinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, diet);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerDiet.setAdapter(adapter);
    }

    private void setHeightSpinner() {


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.heights,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerHeight.setAdapter(adapter);

    }


    private void setMaritalStatus() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, maritalStatus);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerMaritalStatus.setAdapter(adapter);


    }

    private void setReligionSpinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, religion);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerReligion.setAdapter(adapter);
    }

    private void setCommunitySpinner() {


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, community);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerCommunity.setAdapter(adapter);
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

    private void setWorkWithSpinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, workWith);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerworksWith.setAdapter(adapter);
    }
}