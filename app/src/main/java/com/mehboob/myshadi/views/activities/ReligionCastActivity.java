package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityRelgionBinding;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;

public class ReligionCastActivity extends AppCompatActivity {
    private ActivityRelgionBinding binding;
    private SessionManager sessionManager;

    private String[] religion = {"Select Religion", "Hindu", "Muslim", "Christian", "Jain", "Sikh", "Buddhist", "Parsi", "Jewish", "Other", "No religion", "Spiritual - not religious"};
    private String[] community = {"Select Community", "Hindi", "Marathi", "Punjabi", "Bengali", "Urdu", "Gujrati", "Telugu", "Kannada", "English", "Tamil", "Odia", "Marwari", "Aka", "Arabic", "Arunachali", "Assamese", "Awadhi"
            , "Baluchi", "Bhojpuri", "Bhutia", "Brahui", "Brij", "Burmese", "Chattisgarhi", "Chinese", "Coorgi", "Dogri", "French", "Gharwali", "Garo", "Haryanavi",
            "Himachli/Pahari", "Hindko", "Kakbarak", "Kanauji", "Kashmiri", "Khandesi", "Khasi", "Konkani", "Koshali", "Pashto", "Sindhi", "Balochi", "Shina", "Brushuski", "Khawar", "Wakhi", "Seraiki",
            "Other"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRelgionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(this);

        binding.imgBack.setOnClickListener(view -> {
            finish();
        });
//        countryList = loadCountries();
        setReligionSpinner();
        setCommunitySpinner();
//        setSpinnerLivingIn();


        binding.txtCountry.setOnClickListener(view -> {
            Intent intent = new Intent(ReligionCastActivity.this, CountryNamesActivity.class);
            startActivity(intent);
        });


        binding.spinnerReligion.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        binding.spinnerComunity.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        binding.spinnerComunity.setOnItemSelectedListener(new CustomOnItemSelectedListener());

//        binding.spinnerLivingIn.setOnClickListener(view -> {
//            showBottomDialog(countryList, binding.spinnerLivingIn, "Select a Country");
//        });
//        binding.spinnerLivingIn.setOnItemSelectedListener(new CustomOnItemSelectedListener());


        binding.btnContinue.setOnClickListener(view -> {

            if (binding.spinnerComunity.getSelectedItem().equals("Select Community")) {
                Utils.showSnackBar(this, "Select a community");
            } else if (binding.spinnerReligion.getSelectedItem().equals("Select Religion")) {
                Utils.showSnackBar(this, "Select a religion");
            } else if (binding.txtCountry.getText().toString().equals("Select a country")) {
                Utils.showSnackBar(this, "Select a country");
            } else {

                String religion = binding.spinnerReligion.getSelectedItem().toString();
                String community = binding.spinnerComunity.getSelectedItem().toString();

                sessionManager.saveReligion(religion);
                sessionManager.saveCommunity(community);


                startActivity(new Intent(ReligionCastActivity.this, EmailPhoneNumberActivity.class));
            }


        });

    }


    private class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            checkButtonStatus(); // Check button status whenever a spinner item is selected
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

            // Do nothing
        }
    }

    private void checkButtonStatus() {

        if (binding.spinnerReligion.getSelectedItem() != null &&
                binding.spinnerComunity.getSelectedItem() != null &&
                binding.txtCountry.getText() != "Select a country") {
            binding.btnContinue.setEnabled(true);

            updateButtonState(true);
        } else {
            //     btnContinue.setEnabled(false);

            updateButtonState(false);
        }
    }


    private void setCommunitySpinner() {


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, community);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerComunity.setAdapter(adapter);
    }

    //    private void setSpinnerLivingIn() {
//
//
//        ListAdapter adapter = new ListAdapter(this,countryList);
//
//        binding.spinnerLivingIn.setAdapter(adapter);
//
//
//        binding.spinnerLivingIn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                Log.d("ItemCode",countryList.get(i).getCode());
//                sessionManager.saveLivingIn(countryList.get(i).getName());
//                sessionManager.saveCountryCode(countryList.get(i).getCode());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }
    private void setReligionSpinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, religion);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerReligion.setAdapter(adapter);
    }


    private void updateButtonState(boolean isChecked) {
        binding.btnContinue.setEnabled(isChecked);

        // You can also customize the button appearance here
        if (isChecked) {
            binding.btnContinue.setBackground(getResources().getDrawable(R.drawable.btn_back_clicked));
        } else {
            binding.btnContinue.setBackground(getResources().getDrawable(R.drawable.btn_back_unclicked));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sessionManager.fetchLivingIn().equals("null")) {
            binding.txtCountry.setText("Select a country");
        } else {

            binding.txtCountry.setText(sessionManager.fetchLivingIn());
        }
    }
}