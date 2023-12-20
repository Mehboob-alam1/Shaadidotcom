package com.mehboob.myshadi.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.repository.MatchMakingRepository;
import com.mehboob.myshadi.room.entities.UserMatches;

import java.util.List;

public class MatchMakingViewModel extends AndroidViewModel {


    private MatchMakingRepository repository;

    private LiveData<List<UserMatches>> bestRecentMatchedProfiles;


    public MatchMakingViewModel(Application application){
        super(application);


        repository= new MatchMakingRepository(application);

        bestRecentMatchedProfiles=repository.getAllUserProfiles();


    }



   public void getUserProfiles(){

        repository.checkMyProfileMatches();
   }

    public LiveData<List<UserMatches>> getBestRecentMatchedProfiles() {
        return bestRecentMatchedProfiles;
    }
}
