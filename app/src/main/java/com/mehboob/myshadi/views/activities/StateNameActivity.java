package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SearchView;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.CountriesAdapter;
import com.mehboob.myshadi.adapters.StateAdapter;
import com.mehboob.myshadi.databinding.ActivityStateNameBinding;
import com.mehboob.myshadi.json.Countries;
import com.mehboob.myshadi.json.States;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.viewmodel.CountriesViewModel;
import com.mehboob.myshadi.viewmodel.StatesViewModel;

import java.util.ArrayList;
import java.util.List;

public class StateNameActivity extends AppCompatActivity {
private ActivityStateNameBinding binding;
private StatesViewModel statesViewModel;
private SessionManager sessionManager;
    private StateAdapter adapter;
    private List<States> statesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityStateNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        statesViewModel = new ViewModelProvider(this).get(StatesViewModel.class);
        sessionManager= new SessionManager(this);

        statesList= new ArrayList<>();
        adapter = new StateAdapter();


        binding.imgCancel.setOnClickListener(view -> {
            finish();
        });

        new Handler().postDelayed(() -> binding.progressBar.setVisibility(View.GONE),1500);

        statesViewModel.loadStates(sessionManager.fetchCountryCode());
        statesViewModel.getMutableLiveData().observe(this, states -> {
            adapter.setStates(states);

            adapter.setOnItemClickListener((state, position) -> {


                sessionManager.saveStateName(state.getName());
                sessionManager.saveStateCode(state.getCode());
                finish();
            });
        });

        binding.recyclerViewState.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewState.setAdapter(adapter);




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