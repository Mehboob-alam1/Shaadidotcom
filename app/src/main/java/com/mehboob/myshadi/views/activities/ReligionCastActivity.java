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

        setReligionSpinner();
        setCommunitySpinner();


        binding.txtCountry.setOnClickListener(view -> {
            Intent intent = new Intent(ReligionCastActivity.this, CountryNamesActivity.class);
            startActivity(intent);
        });






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
        binding.imgBack.setOnClickListener(view -> finish());



    }





    private void setCommunitySpinner() {


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, community);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerComunity.setAdapter(adapter);
    }

    private void setReligionSpinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, religion);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerReligion.setAdapter(adapter);
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