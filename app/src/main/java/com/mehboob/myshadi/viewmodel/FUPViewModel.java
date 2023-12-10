package com.mehboob.myshadi.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mehboob.myshadi.model.profilemodel.ProfileResponse;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.repository.FirebaseUserProfileRepository;

import java.util.ArrayList;
import java.util.List;

public class FUPViewModel extends AndroidViewModel {



    private MutableLiveData<ProfileResponse> response;

    private  MutableLiveData<UserProfile> userProfileMutableLiveData;

    private final MutableLiveData<List<Uri>> selectedImages = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isProfile = new MutableLiveData<>();
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

    public void uploadUserProfile(List<Uri> images,UserProfile userProfile){

        repository.uploadImagesToFirebase(images, userProfile, new FirebaseUserProfileRepository.StorageUploadCallback() {
            @Override
            public void onSuccess(List<String> imageUrls) {
                isProfile.setValue(true);
            }

            @Override
            public void onError(String errorMessage) {
isProfile.setValue(false);
            }
        });
    }
    public LiveData<List<Uri>> getSelectedImages() {
        return selectedImages;
    }

    public void setSelectedImages(List<Uri> images) {
        selectedImages.setValue(images);
    }


}
