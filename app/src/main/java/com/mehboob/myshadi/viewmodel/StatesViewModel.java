package com.mehboob.myshadi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mehboob.myshadi.json.States;
import com.mehboob.myshadi.repository.StateRepository;

import java.util.List;

public class StatesViewModel extends AndroidViewModel {

    private StateRepository repository;

    private MutableLiveData<List<States>> mutableLiveData= new MutableLiveData<>();
    public StatesViewModel(@NonNull Application application) {
        super(application);

        repository= new StateRepository();



    }

    public MutableLiveData<List<States>> getMutableLiveData() {
        return mutableLiveData;
    }

    public void loadStates(String countryCode){


        repository.getStates(countryCode,new StateRepository.StatesCallback() {
            @Override
            public void onSuccess(List<States> states) {
                mutableLiveData.setValue(states);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}
