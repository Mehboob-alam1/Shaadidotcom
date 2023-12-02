package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityQualificationBinding;

public class QualificationActivity extends AppCompatActivity {
private ActivityQualificationBinding binding;

private String[] qualification={};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityQualificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}