package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.ImageViewPager;
import com.mehboob.myshadi.databinding.ActivityImagesFullBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ImagesFullActivity extends AppCompatActivity {
    private ActivityImagesFullBinding binding;

    private ImageViewPager imageViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImagesFullBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ArrayList<String> listPrivate = new ArrayList<>();

        Type type = new TypeToken<List<String>>() {
        }.getType();
        listPrivate = new Gson().fromJson(getIntent().getStringExtra("private_list"), type);


      binding.btnFinish.setOnClickListener(view -> {
          finish();
      });
        imageViewPager = new ImageViewPager(this, listPrivate);

        binding.viewPagerMain.setAdapter(imageViewPager);
    }
}