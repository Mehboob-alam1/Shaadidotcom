package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityAllConnectedUsersBinding;

public class AllConnectedUsersActivity extends AppCompatActivity {
private ActivityAllConnectedUsersBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityAllConnectedUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());






    }
}

//getConnectedUsers