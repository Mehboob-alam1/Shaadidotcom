package com.mehboob.myshadi.views.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityDashBoardBinding;

public class DashBoardActivity extends AppCompatActivity {
private ActivityDashBoardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}