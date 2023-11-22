package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityProfileForBinding;

public class ProfileForActivity extends AppCompatActivity {
private ActivityProfileForBinding binding;
private boolean enableButton=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileForBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        checkBoxesValidation();

        binding.btnContinue.setActivated(false);
        binding.btnContinue.setFocusable(false);
        binding.btnContinue.setActivated(false);

        if (enableButton) {
         binding.btnContinue.setClickable(true);
         binding.btnContinue.setFocusable(true);
            binding.btnContinue.setActivated(true);
        }
    }

    private void checkBoxesValidation() {

        checkProfileBoxes();


    }

    private void checkProfileBoxes() {
        CompoundButton.OnCheckedChangeListener checkChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If any checkbox is checked, uncheck all other checkboxes
                if (isChecked) {
                    enableButton=true;
                    uncheckAllCheckboxes(buttonView.getId());

                }
            }
        };



       binding. chkMyself.setOnCheckedChangeListener(checkChangeListener);
        binding.chkMySon.setOnCheckedChangeListener(checkChangeListener);
        binding. chkMyBrother.setOnCheckedChangeListener(checkChangeListener);
        binding. chkMySister.setOnCheckedChangeListener(checkChangeListener);
        binding. chkMyDaughter.setOnCheckedChangeListener(checkChangeListener);
        binding. chkMyRelative.setOnCheckedChangeListener(checkChangeListener);
        binding. chkMyFriend.setOnCheckedChangeListener(checkChangeListener);
    }

    private void uncheckAllCheckboxes(int checkedCheckboxId) {

        // List of all CheckBoxes
        CheckBox[] checkboxes = {binding.chkMyself, binding.chkMySon, binding.chkMyBrother, binding.chkMySister, binding.chkMyDaughter, binding.chkMyRelative, binding.chkMyFriend};

        // Uncheck all checkboxes except the one that triggered the change
        for (CheckBox checkbox : checkboxes) {
            if (checkbox.getId() != checkedCheckboxId) {
                checkbox.setChecked(false);

            }
        }
    }
}