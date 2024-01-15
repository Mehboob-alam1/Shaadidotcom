package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityEditBasicInfoBinding;

public class EditBasicInfoActivity extends AppCompatActivity {
private ActivityEditBasicInfoBinding binding;
    private String[] maritalStatus = {"Never Married", "Divorced", "Widowed", "Awaiting Divorce", "Annulled"};
    private String[] religion = { "Muslim", "Hindu", "Christian", "Jain", "Sikh", "Buddhist", "Parsi", "Jewish", "Other", "No religion", "Spiritual - not religious"};
    private String[] community = { "Urdu","Hindi", "Marathi", "Punjabi", "Bengali",  "Gujrati", "Telugu", "Kannada", "English", "Tamil", "Odia", "Marwari", "Aka", "Arabic", "Arunachali", "Assamese", "Awadhi"
            , "Baluchi", "Bhojpuri", "Bhutia", "Brahui", "Brij", "Burmese", "Chattisgarhi", "Chinese", "Coorgi", "Dogri", "French", "Gharwali", "Garo", "Haryanavi",
            "Himachli/Pahari", "Hindko", "Kakbarak", "Kanauji", "Kashmiri", "Khandesi", "Khasi", "Konkani", "Koshali", "Pashto", "Sindhi", "Balochi", "Shina", "Brushuski", "Khawar", "Wakhi", "Seraiki",
            "Other"};

    private String[] workWith = {"Private Company", "Government / Public Sector", "Defense / Civil Services", "Business / Self Employed", "Not Working"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditBasicInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
        setMaritalStatus();
        setHeightSpinner();
        setReligionSpinner();
        setCommunitySpinner();
        setSubCommunity();
setWorkWithSpinner();

        binding.btnUpdateBasicInfo.setOnClickListener(v -> {

        });
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