package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityRelgionBinding;
import com.mehboob.myshadi.utils.SessionManager;

public class ReligionCastActivity extends AppCompatActivity {
    private ActivityRelgionBinding binding;
    private SessionManager sessionManager;

    private String[] religion = {"Hindu", "Muslim", "Christian", "Jain", "Sikh", "Buddhist", "Parsi", "Jewish", "Other", "No religion", "Spiritual - not religious"};
    private String[] community = {"Hindi", "Marathi", "Punjabi", "Bengali", "Urdu", "Gujrati", "Telugu", "Kannada", "English", "Tamil", "Odia", "Marwari", "Aka", "Arabic", "Arunachali", "Assamese", "Awadhi"
            , "Baluchi", "Bhojpuri", "Bhutia", "Brahui", "Brij", "Burmese", "Chattisgarhi", "Chinese", "Coorgi", "Dogri", "French", "Gharwali", "Garo", "Haryanavi",
            "Himachli/Pahari", "Hindko", "Kakbarak", "Kanauji", "Kashmiri", "Khandesi", "Khasi", "Konkani", "Koshali", "Pashto", "Sindhi", "Balochi", "Shina", "Brushuski", "Khawar", "Wakhi", "Seraiki",
            "Other"};

    String[] livingIn = {
            "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda", "Argentina", "Armenia", "Australia", "Austria",
            "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan",
            "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia",
            "Cameroon", "Canada", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo", "Costa Rica",
            "Cote d'Ivoire", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador",
            "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Fiji", "Finland", "France",
            "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau",
            "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy",
            "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, North", "Korea, South", "Kosovo", "Kuwait",
            "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg",
            "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mexico",
            "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru",
            "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Macedonia", "Norway", "Oman", "Pakistan",
            "Palau", "Palestine", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar",
            "Romania", "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino",
            "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia",
            "Solomon Islands", "Somalia", "South Africa", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Sweden", "Switzerland",
            "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia",
            "Turkey", "Turkmenistan", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay",
            "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRelgionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager= new SessionManager(this);

        binding.imgBack.setOnClickListener(view -> {
            finish();
        });

        setReligionSpinner();
        setCommunitySpinner();
        setCountrySpinner();

        binding.spinnerReligion.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        binding.spinnerComunity.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        binding.spinnerLivingIn.setOnItemSelectedListener(new CustomOnItemSelectedListener());


        binding.btnContinue.setOnClickListener(view -> {

            String religion=binding.spinnerReligion.getSelectedItem().toString();
            String community=binding.spinnerComunity.getSelectedItem().toString();
            String livingIn=binding.spinnerLivingIn.getSelectedItem().toString();
            sessionManager.saveReligion(religion);
            sessionManager.saveCommunity(community);
            sessionManager.saveLivingIn(livingIn);

            startActivity(new Intent(ReligionCastActivity.this, EmailPhoneNumberActivity.class));





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
                binding.spinnerLivingIn.getSelectedItem() != null) {
//            binding.btnContinue.setEnabled(true);

            updateButtonState(true);
        } else {
       //     btnContinue.setEnabled(false);

            updateButtonState(false);
        }
    }

    private void setCountrySpinner() {


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, livingIn);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerLivingIn.setAdapter(adapter);
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


    private void updateButtonState(boolean isChecked) {
        binding.btnContinue.setEnabled(isChecked);

        // You can also customize the button appearance here
        if (isChecked) {
            binding.btnContinue.setBackground(getResources().getDrawable(R.drawable.btn_back_clicked));
        } else {
            binding.btnContinue.setBackground(getResources().getDrawable(R.drawable.btn_back_unclicked));
        }
    }
}