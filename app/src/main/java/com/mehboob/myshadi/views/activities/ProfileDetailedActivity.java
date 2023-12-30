package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityProfileDetailedBinding;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.viewmodel.FUPViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfileDetailedActivity extends AppCompatActivity {
    private UserMatches userMatches;

    private ActivityProfileDetailedBinding binding;


    private FUPViewModel fupViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBackProfile.setOnClickListener(v -> {
            finish();
        });

        Type type = new TypeToken<UserMatches>() {
        }.getType();
        userMatches = new Gson().fromJson(getIntent().getStringExtra("currentPerson"), type);
        Toast.makeText(this, "" + userMatches.toString(), Toast.LENGTH_SHORT).show();


        bindData(userMatches);
    }

    private void bindData(UserMatches userMatches) {


        try {
            Glide.with(this).load(userMatches.getImageUrl())
                    .placeholder(R.drawable.img_no_internet)
                    .into(binding.imgProfileImageM)
                    ;

        }catch (Exception e){
            Log.d("Exception",e.getLocalizedMessage());
        }

        if (userMatches.isVerified()){
            binding.imgVerifiedM.setVisibility(View.VISIBLE);
        }

        if (userMatches.getGender().equals("Male")){
            binding.txtYouAndPersonM.setText("You and him");
        }else{
            binding.txtYouAndPersonM.setText("You and her");
        }


    }
}