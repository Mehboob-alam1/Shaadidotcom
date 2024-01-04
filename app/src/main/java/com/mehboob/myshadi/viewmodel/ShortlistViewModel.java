package com.mehboob.myshadi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.repository.ShortListedRepository;

import java.util.List;

public class ShortlistViewModel extends AndroidViewModel {

    private ShortListedRepository shortListedRepository;
    private MutableLiveData<List<UserProfile>> listUserProfiles;
    public ShortlistViewModel(@NonNull Application application) {
        super(application);

        shortListedRepository= new ShortListedRepository(application);
        listUserProfiles= shortListedRepository.getUserProfilesMutableData();

    }


    public MutableLiveData<List<UserProfile>> getListUserProfiles() {
        return listUserProfiles;
    }

    public void fetchShortlistedProfiles(List<Connection> connections){
        shortListedRepository.fetchShortlistedProfiles(connections);
    }


}
