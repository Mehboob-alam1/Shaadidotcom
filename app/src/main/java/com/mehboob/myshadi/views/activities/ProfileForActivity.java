package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityProfileForBinding;
import com.mehboob.myshadi.utils.SessionManager;

public class ProfileForActivity extends AppCompatActivity {
    private ActivityProfileForBinding binding;
    private boolean enableButton = false;

    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileForBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
sessionManager= new SessionManager(this);
        checkBoxesValidation();
        setButtonClickListeners();
    }

    private void checkBoxesValidation() {
        checkProfileBoxes();
        checkGenderBoxes();
    }

    private void checkProfileBoxes() {
        CompoundButton.OnCheckedChangeListener checkChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    uncheckAllCheckboxes(buttonView.getId());
                }
                if (isGenderKnowChecked()) {
                    enableButton = true;
                    uncheckGenderBoxes(R.id.chkFemale);
                    uncheckGenderBoxes(R.id.chkMale);

                } else {
                    enableButton = isAnyCheckboxChecked() && isAnyGenderCheckboxChecked();

                }
                updateButtonState(enableButton);

                // Show/hide gender checkboxes based on the selected profile checkboxes
                updateGenderCheckBoxVisibility();
            }
        };

        binding.chkMyself.setOnCheckedChangeListener(checkChangeListener);
        binding.chkMySon.setOnCheckedChangeListener(checkChangeListener);
        binding.chkMyBrother.setOnCheckedChangeListener(checkChangeListener);
        binding.chkMySister.setOnCheckedChangeListener(checkChangeListener);
        binding.chkMyDaughter.setOnCheckedChangeListener(checkChangeListener);
        binding.chkMyRelative.setOnCheckedChangeListener(checkChangeListener);
        binding.chkMyFriend.setOnCheckedChangeListener(checkChangeListener);
    }

    private void checkGenderBoxes() {
        CompoundButton.OnCheckedChangeListener changeListener = (compoundButton, b) -> {
            if (b) {
                uncheckGenderBoxes(compoundButton.getId());
            }
            enableButton = isAnyCheckboxChecked() && isAnyGenderCheckboxChecked();
            updateButtonState(enableButton);
        };

        binding.chkMale.setOnCheckedChangeListener(changeListener);
        binding.chkFemale.setOnCheckedChangeListener(changeListener);
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

    private boolean isGenderKnowChecked() {

        return binding.chkMySon.isChecked() ||
                binding.chkMyBrother.isChecked() ||
                binding.chkMySister.isChecked() ||
                binding.chkMyDaughter.isChecked();


    }


    private int getGenderKnownId() {

        CheckBox[] getId = {binding.chkMySon, binding.chkMyDaughter, binding.chkMyBrother, binding.chkMySister};

        for (CheckBox checkBox : getId) {
            if (checkBox.isChecked()) {
                return checkBox.getId();
            }

        }
        return binding.chkMyself.getId();
    }

    private boolean isAnyCheckboxChecked() {


        return binding.chkMyself.isChecked() ||

                binding.chkMyRelative.isChecked() ||
                binding.chkMyFriend.isChecked();
    }

    private boolean isAnyGenderCheckboxChecked() {
        return binding.chkMale.isChecked() || binding.chkFemale.isChecked();
    }

    private void uncheckAllCheckboxes(int checkedCheckboxId) {
        CheckBox[] checkboxes = {binding.chkMyself, binding.chkMySon, binding.chkMyBrother, binding.chkMySister, binding.chkMyDaughter, binding.chkMyRelative, binding.chkMyFriend};
        for (CheckBox checkbox : checkboxes) {
            if (checkbox.getId() != checkedCheckboxId) {
                checkbox.setChecked(false);
            }
        }
    }

    private void uncheckGenderBoxes(int checkedCheckboxId) {
        CheckBox[] genderCheckboxes = {binding.chkMale, binding.chkFemale};
        for (CheckBox checkbox : genderCheckboxes) {
            if (checkbox.getId() != checkedCheckboxId) {
                checkbox.setChecked(false);
            }
        }
    }

    private void updateGenderCheckBoxVisibility() {
        // If one of the checkboxes from the first group is checked, hide gender checkboxes in the second group
        if (binding.chkMySon.isChecked() || binding.chkMyBrother.isChecked() || binding.chkMySister.isChecked() || binding.chkMyDaughter.isChecked()) {
            binding.linearLayout4.setVisibility(View.GONE);
        } else {
            binding.linearLayout4.setVisibility(View.VISIBLE);
        }
    }

    private void setButtonClickListeners() {
        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View view) {
                // Get the text from the checked checkboxes in each group
                String profileText = getCheckedProfileText();
                String genderText = getCheckedGenderText();
                String genderKnown = "";

                if (genderText == "") {
                    switch (profileText) {

                        case "My Son":
                            genderKnown = "Male";
                            break;
                        case "My Brother":
                            genderKnown = "Male";
                            break;
                        case "My Sister":
                            genderKnown = "Female";
                            break;
                        case "My Daughter":
                            genderKnown = "Female";
                            break;
                    }
                    String resultText = "Profile: " + profileText + "\nGender: " + genderKnown;

                    Log.d("ProfileActivity", "onClick: "+resultText);
                }else{

                    genderKnown=getCheckedGenderText();
                    String resultText = "Profile: " + profileText + "\nGender: " + genderKnown;
                    Log.d("ProfileActivity", "onClick: "+resultText);
                }
          sessionManager.saveProfileFor(profileText);
          sessionManager.saveGender(genderKnown);
                String resultText = "Shared Pref Profile: " + profileText + "\nGender: " + genderKnown;
                Log.d("ProfileActivity", "onClick: "+resultText);



            }
        });
    }

    private String getCheckedProfileText() {
        // Iterate over profile checkboxes and get text from the checked one
        CheckBox[] profileCheckboxes = {binding.chkMyself, binding.chkMySon, binding.chkMyBrother, binding.chkMySister, binding.chkMyDaughter, binding.chkMyRelative, binding.chkMyFriend};
        for (CheckBox checkbox : profileCheckboxes) {
            if (checkbox.isChecked()) {
                return checkbox.getText().toString();
            }
        }
        return ""; // Return an empty string if none are checked
    }

    private String getCheckedGenderText() {
        // Iterate over gender checkboxes and get text from the checked one
        CheckBox[] genderCheckboxes = {binding.chkMale, binding.chkFemale};
        for (CheckBox checkbox : genderCheckboxes) {
            if (checkbox.isChecked()) {
                return checkbox.getText().toString();
            }
        }
        return ""; // Return an empty string if none are checked
    }
}
