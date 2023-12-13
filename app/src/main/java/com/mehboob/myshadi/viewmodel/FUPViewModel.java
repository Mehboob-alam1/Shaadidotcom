package com.mehboob.myshadi.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mehboob.myshadi.model.profilemodel.ProfileResponse;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.repository.FirebaseUserProfileRepository;

import java.util.ArrayList;
import java.util.List;

public class FUPViewModel extends AndroidViewModel {




    private final MutableLiveData<List<Uri>> selectedImages = new MutableLiveData<>();

    private MutableLiveData<UserProfile> userProfileLiveData ;

    private MutableLiveData<Boolean> checkIfUpload;



    private FirebaseUserProfileRepository repository;

    public FUPViewModel(@NonNull Application application) {
        super(application);
        repository = new FirebaseUserProfileRepository(application);
checkIfUpload=repository.getIsProfileCompleted();
        userProfileLiveData=repository.getUserProfileLiveData();

    }


    public MutableLiveData<Boolean> getCheckIfUpload() {
        return checkIfUpload;
    }

    public MutableLiveData<UserProfile> getUserProfileLiveData() {
        return userProfileLiveData;
    }



    public void getProfile(UserProfile userProfile){


        repository.getProfileData(userProfile);
    }


    public void uploadUserProfile(List<Uri> images, UserProfile userProfile) {

        repository.uploadImagesToFirebase(images, userProfile, new FirebaseUserProfileRepository.StorageUploadCallback() {
            @Override
            public void onSuccess(List<String> imageUrls) {

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    public LiveData<List<Uri>> getSelectedImages() {
        return selectedImages;
    }

    public void setSelectedImages(List<Uri> images) {
        selectedImages.setValue(images);
    }

    public interface ProfileCompletionCallback {
        void onProfileCompletion(boolean isProfileComplete);
    }
    public void checkProfileCompletion(String userId, ProfileCompletionCallback callback) {
        LiveData<Boolean> profileCompleteLiveData = repository.isProfileComplete(userId);

        profileCompleteLiveData.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isProfileComplete) {
                callback.onProfileCompletion(isProfileComplete);
                profileCompleteLiveData.removeObserver(this);
            }
        });
    }
}
