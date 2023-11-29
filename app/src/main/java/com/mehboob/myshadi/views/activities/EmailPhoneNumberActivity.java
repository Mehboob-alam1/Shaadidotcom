package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityEmailPhoneNumberBinding;
import com.mehboob.myshadi.utils.SessionManager;

public class EmailPhoneNumberActivity extends AppCompatActivity {
    private ActivityEmailPhoneNumberBinding binding;
    private SessionManager sessionManager;
    String[] phoneCountryCodes = {"+92",
            "+93", "+355", "+213", "+376", "+244", "+1-268", "+54", "+374", "+61", "+43",
            "+994", "+1-242", "+973", "+880", "+1-246", "+375", "+32", "+501", "+229", "+975",
            "+591", "+387", "+267", "+55", "+673", "+359", "+226", "+257", "+238", "+855",
            "+237", "+1", "+236", "+235", "+56", "+86", "+57", "+269", "+242", "+506",
            "+225", "+385", "+53", "+357", "+420", "+45", "+253", "+1-767", "+1-809", "+1-829",
            "+593", "+20", "+503", "+240", "+291", "+372", "+251", "+679", "+358", "+33",
            "+241", "+220", "+995", "+49", "+233", "+30", "+1-473", "+502", "+224", "+245",
            "+592", "+509", "+504", "+36", "+354", "+91", "+62", "+98", "+964", "+353",
            "+972", "+39", "+1-876", "+81", "+962", "+7", "+254", "+686", "+850", "+82",
            "+965", "+996", "+856", "+371", "+961", "+266", "+231", "+218", "+423", "+370",
            "+352", "+261", "+265", "+60", "+960", "+223", "+356", "+692", "+222", "+230",
            "+52", "+691", "+373", "+377", "+976", "+382", "+212", "+258", "+95", "+264",
            "+674", "+977", "+31", "+64", "+505", "+227", "+234", "+389", "+47", "+968",
            "+680", "+970", "+507", "+675", "+595", "+51", "+63", "+48", "+351", "+974",
            "+40", "+7", "+250", "+869", "+1-758", "+1-784", "+685", "+378", "+239", "+966",
            "+221", "+381", "+248", "+232", "+65", "+421", "+386", "+677", "+252", "+27",
            "+211", "+34", "+94", "+249", "+597", "+268", "+46", "+41", "+963", "+886", "+992",
            "+255", "+66", "+670", "+228", "+676", "+1-868", "+216", "+90", "+993", "+688",
            "+256", "+380", "+971", "+44", "+1", "+598", "+998", "+678", "+379", "+58", "+84",
            "+967", "+260", "+263"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(this);
        setPhoneCodes();


        binding.etEmail.addTextChangedListener(textWatcher);
        binding.etPhoneNumber.addTextChangedListener(textWatcher);


        binding.btnContinue.setOnClickListener(view -> {

            if (binding.etPhoneNumber.getText().toString().length() < 9) {
                Toast.makeText(this, "Phone number invalid", Toast.LENGTH_SHORT).show();
            } else {

                String email = binding.etEmail.getText().toString();
                String phoneNumber = binding.spinnerCountryCode.getSelectedItem().toString() + binding.etPhoneNumber.getText().toString();
                sessionManager.saveEmail(email);
                sessionManager.savePhoneNumber(phoneNumber);

            }
        });


    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Not needed for this example
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Not needed for this example
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Check if both EditText fields are filled
            boolean isEditText1Filled = !binding.etPhoneNumber.getText().toString().trim().isEmpty();
            boolean isEditText2Filled = !binding.etEmail.getText().toString().trim().isEmpty();
            boolean isValid = isValidEmail(binding.etEmail.getText()
                    .toString());

            if (!isValid) {
                binding.etEmail.setError("Invalid email");
            }

            // Enable the button only if both EditText fields are filled
            updateButtonState(isEditText1Filled && isEditText2Filled && isValid);
        }
    };

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void setPhoneCodes() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, phoneCountryCodes);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerCountryCode.setAdapter(adapter);
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