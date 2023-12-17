package com.mehboob.myshadi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mehboob.myshadi.json.Cities;
import com.mehboob.myshadi.repository.CitiesRepository;

import java.util.List;

public class CitiesViewModel extends AndroidViewModel {

    private CitiesRepository citiesRepository;

    private MutableLiveData<List<Cities>> mutableLiveData = new MutableLiveData<>();
    public CitiesViewModel(@NonNull Application application) {
        super(application);

        citiesRepository= new CitiesRepository();
    }

    public MutableLiveData<List<Cities>> getMutableLiveData() {
        return mutableLiveData;
    }

    public void getCities(String stateCode){


        citiesRepository.getCities(stateCode, new CitiesRepository.CitiesCallBack() {
            @Override
            public void onSuccess(List<Cities> cities) {
                mutableLiveData.setValue(cities);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}
