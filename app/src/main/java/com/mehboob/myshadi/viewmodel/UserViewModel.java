package com.mehboob.myshadi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mehboob.myshadi.repository.DataRepository;
import com.mehboob.myshadi.room.models.User;

public class UserViewModel extends AndroidViewModel {
    private DataRepository dataRepository;
    private LiveData<User> liveData;
    private Application application;

    private long ifInserted;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.application=application;

        dataRepository=new DataRepository(application);
        liveData=dataRepository.getUserMutableLiveData();
        ifInserted= dataRepository.checkIfInserted();
    }
   public void insertUser(User userMutableLiveData){
        dataRepository.insertData(userMutableLiveData);
    }

    public LiveData<User> getLiveData() {
        return liveData;
    }

    public long getIfInserted() {
        return ifInserted;
    }
}
