package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityAccountSettingBinding;

public class AccountSettingActivity extends AppCompatActivity {

    private ActivityAccountSettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnLogout.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(AccountSettingActivity.this,MainActivity.class));
        });




    }
}