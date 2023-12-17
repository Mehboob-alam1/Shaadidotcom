package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SearchView;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.CitiesAdapter;
import com.mehboob.myshadi.adapters.CountriesAdapter;
import com.mehboob.myshadi.databinding.ActivityCityNameBinding;
import com.mehboob.myshadi.databinding.ActivityCountryNamesBinding;
import com.mehboob.myshadi.json.Countries;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.viewmodel.CitiesViewModel;
import com.mehboob.myshadi.viewmodel.CountriesViewModel;

import java.util.ArrayList;
import java.util.List;

public class CityNameActivity extends AppCompatActivity {
    private ActivityCityNameBinding binding;

    private CitiesViewModel citiesViewModel;

    private CitiesAdapter adapter;
    private List<CitiesAdapter> citiesList;

    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityCityNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        citiesViewModel = new ViewModelProvider(this).get(CitiesViewModel.class);
        sessionManager= new SessionManager(this);

        citiesList= new ArrayList<>();
        adapter = new CitiesAdapter();


        binding.imgCancel.setOnClickListener(view -> {
            finish();
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.progressBar.setVisibility(View.GONE);
            }
        },1500);

        citiesViewModel.getCities(sessionManager.fetchStateCode());
        citiesViewModel.getMutableLiveData().observe(this,cities -> {
            adapter.setCities(cities);

            adapter.setOnItemClickListener((city, position) -> {


                sessionManager.saveCityName(city.getName());

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