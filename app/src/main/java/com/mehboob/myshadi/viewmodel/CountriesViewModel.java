package com.mehboob.myshadi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mehboob.myshadi.json.Countries;
import com.mehboob.myshadi.repository.CountriesRepository;

import java.util.List;

public class CountriesViewModel extends AndroidViewModel {

    private CountriesRepository countriesRepository;

    private MutableLiveData<List<Countries>> mutableLiveData= new MutableLiveData<>();
    public CountriesViewModel(@NonNull Application application) {
        super(application);

        countriesRepository = new CountriesRepository();
        loadCountries();
    }

    public MutableLiveData<List<Countries>> getMutableLiveData() {
        return mutableLiveData;
    }

    public void loadCountries(){


        countriesRepository.getCountries(new CountriesRepository.CountryCallback() {
            @Override
            public void onSuccess(List<Countries> countries) {

                mutableLiveData.setValue(countries);

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}
