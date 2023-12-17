package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SearchView;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.CountriesAdapter;
import com.mehboob.myshadi.databinding.ActivityCountryNamesBinding;
import com.mehboob.myshadi.json.Countries;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.viewmodel.CountriesViewModel;

import java.util.ArrayList;
import java.util.List;

public class CountryNamesActivity extends AppCompatActivity {
private ActivityCountryNamesBinding binding;

    private CountriesViewModel countryViewModel;

    private CountriesAdapter adapter;
    private List<Countries> countriesList;

    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityCountryNamesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        countryViewModel = new ViewModelProvider(this).get(CountriesViewModel.class);
        sessionManager= new SessionManager(this);

        countriesList= new ArrayList<>();
        adapter = new CountriesAdapter();


        binding.imgCancel.setOnClickListener(view -> {
            finish();
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
          binding.progressBar.setVisibility(View.GONE);
            }
        },1500);

        countryViewModel.getMutableLiveData().observe(this, countries -> {
            // Update the adapter with the new list of countries

            adapter.setCountries(countries);

            adapter.setOnItemClickListener((country, position) -> {


                sessionManager.saveLivingIn(country.getName());
                sessionManager.saveCountryCode(country.getCode());
                finish();
            });
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);




        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
         adapter.getFilter().filter(newText);
                return false;
            }
        });

    }
}