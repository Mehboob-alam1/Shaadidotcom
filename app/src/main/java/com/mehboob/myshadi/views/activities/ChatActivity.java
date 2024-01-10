package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityChatBinding;
import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.viewmodel.FUPViewModel;

import java.lang.reflect.Type;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private Connection connection;
    private FUPViewModel fupViewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fupViewModel = new ViewModelProvider(this).get(FUPViewModel.class);
        sessionManager = new SessionManager(this);
        Type type = new TypeToken<Connection>() {
        }.getType();
        connection = new Gson().fromJson(getIntent().getStringExtra("user"), type);

        try {
            binding.txtName.setText(connection.getConnectionToName());
            Glide.with(this).load(connection.getConnectionToImage())
                    .into(binding.circleImageView);
        } catch (Exception e) {
            Log.d("Exception", e.getLocalizedMessage());

        }

        fupViewModel.getProfile(sessionManager.fetchUserId());





    }

    @Override
    protected void onResume() {
        super.onResume();

        fupViewModel.getUserProfileLiveData().observe(this, userProfileData -> {
            if (userProfileData!=null) {
                if (userProfileData.isVerified()) {
                    binding.notVerifiedLayout.setVisibility(View.GONE);
                    binding.relativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}