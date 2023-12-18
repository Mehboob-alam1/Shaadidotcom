package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityAddBioBinding;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;

public class AddBioActivity extends AppCompatActivity {

    private ActivityAddBioBinding binding;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddBioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);

        binding.btnBackAboutMe.setOnClickListener(view -> finish());


        binding.btnSaveAboutMe.setOnClickListener(view -> {

            if (binding.etTellmeAbout.getText().toString().isEmpty()) {
                Utils.showSnackBar(this, "Add about your self");
            } else {
                sessionManager.saveAboutMe(binding.etTellmeAbout.getText().toString());


            }
        });
    }
}