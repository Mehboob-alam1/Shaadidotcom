package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.room.entities.UserMatches;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfileDetailedActivity extends AppCompatActivity {
private UserMatches userMatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detailed);


        Type type = new TypeToken<UserMatches>() {
        }.getType();
        userMatches = new Gson().fromJson(getIntent().getStringExtra("currentPerson"), type);
        Toast.makeText(this, ""+userMatches.toString(), Toast.LENGTH_SHORT).show();
    }
}