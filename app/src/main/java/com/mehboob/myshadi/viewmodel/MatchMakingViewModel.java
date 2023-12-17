package com.mehboob.myshadi.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.repository.MatchMakingRepository;

import java.util.List;

public class MatchMakingViewModel extends AndroidViewModel {


    private MatchMakingRepository repository;

    private MutableLiveData<List<UserProfile>> bestRecentMatchedProfiles;

    public MatchMakingViewModel(Application application){
        super(application);


        repository= new MatchMakingRepository(application);

        bestRecentMatchedProfiles=repository.getBestMatchRecentUsers();
    }



    public void checkRecentBestMatchesProfiles(UserProfile currentUserProfile){

        repository.checkRecentBestMatchesProfiles(currentUserProfile);

    }

    public MutableLiveData<List<UserProfile>> getBestRecentMatchedProfiles() {
        return bestRecentMatchedProfiles;
    }
}