package com.mehboob.myshadi.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.repository.MatchMakingRepository;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.room.entities.UserProfileData;

import java.util.List;

public class MatchMakingViewModel extends AndroidViewModel {


    private MatchMakingRepository repository;

    private LiveData<List<UserMatches>> bestRecentMatchedProfiles;

    private MutableLiveData<Boolean>   connectionSent;


    public MatchMakingViewModel(Application application) {
        super(application);


        repository = new MatchMakingRepository(application);

        bestRecentMatchedProfiles = repository.getAllUserProfiles();

        connectionSent=repository.getConnectionSent();


    }

    public MutableLiveData<Boolean> getConnectionSent() {
        return connectionSent;
    }

    public LiveData<List<UserMatches>> getUserProfilesCreatedLastWeek() {

        return repository.getUserProfilesCreatedLastWeek();
    }

    public void getUserProfiles() {

        repository.checkMyProfileMatches();
    }


    public void sendNotification(Connection connection, UserMatches otherUserMatches, UserProfileData currentUser){

        repository.sendNotification(connection,otherUserMatches,currentUser);
    }
    public LiveData<List<UserMatches>> getBestMatchesPref(int minAge,int maxAge){

        return repository.getBestMatchesPref(minAge,maxAge);
    }

    public LiveData<List<UserMatches>> getNearestProfiles(double userLatitude, double userLongitude, double radius, int limit) {
        return repository.getNearestProfiles(userLatitude, userLongitude, radius, limit);
    }
    public LiveData<List<UserMatches>> getBestRecentMatchedProfiles() {
        return bestRecentMatchedProfiles;
    }
    public void deleteUserMatches(UserMatches userMatches) {
        repository.deleteUserMatches(userMatches);
    }


    public void insertConnection(Connection connection){
        repository.insertSentConnection(connection);
    }
    public LiveData<List<Connection>> getConnectedUserIds() {
        return repository.getConnectedUserIds();
    }
}
