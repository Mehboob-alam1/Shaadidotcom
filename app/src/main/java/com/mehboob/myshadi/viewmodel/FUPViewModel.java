package com.mehboob.myshadi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mehboob.myshadi.model.profilemodel.ProfileResponse;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.repository.FirebaseUserProfileRepository;

public class FUPViewModel extends AndroidViewModel {



    private MutableLiveData<ProfileResponse> response;

    private  MutableLiveData<UserProfile> userProfileMutableLiveData;


    private FirebaseUserProfileRepository repository;

    public FUPViewModel(@NonNull Application application) {
        super(application);
      repository= new FirebaseUserProfileRepository();
response=repository.getProfileResponse();
      userProfileMutableLiveData=repository.getUserProfileMutableLiveData();
    }


    public MutableLiveData<ProfileResponse> getResponse() {
        return response;
    }

    public MutableLiveData<UserProfile> getUserProfileMutableLiveData() {
        return userProfileMutableLiveData;
    }

    public void uploadUserProfile(UserProfile userProfile){

        repository.uploadProfile(userProfile);
    }
}
