package com.mehboob.myshadi.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mehboob.myshadi.model.ProfileCheck;
import com.mehboob.myshadi.model.profilemodel.Preferences;
import com.mehboob.myshadi.model.profilemodel.ProfileResponse;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.repository.FirebaseUserProfileRepository;
import com.mehboob.myshadi.room.entities.UserProfileData;

import java.util.ArrayList;
import java.util.List;

public class FUPViewModel extends AndroidViewModel {




    private final MutableLiveData<List<Uri>> selectedImages = new MutableLiveData<>();

    private LiveData<UserProfileData> userProfileLiveData ;

    private MutableLiveData<Boolean> checkIfUpload;


private MutableLiveData<Boolean> isPreferencesAdded;
private MutableLiveData<Boolean> isBioUpdated;
    private FirebaseUserProfileRepository repository;

    public FUPViewModel(@NonNull Application application) {
        super(application);
        repository = new FirebaseUserProfileRepository(application);
checkIfUpload=repository.getIsProfileCompleted();
userProfileLiveData=repository.getUserProfileData();
//        userProfileLiveData=repository.getUserProfileLiveData();
        isPreferencesAdded=repository.getIsPreferencesAdded();
        isBioUpdated=repository.getIsBioUpdated();

    }


    public MutableLiveData<Boolean> getCheckIfUpload() {
        return checkIfUpload;
    }

    public MutableLiveData<Boolean> getIsPreferencesAdded() {
        return isPreferencesAdded;
    }

    public LiveData<UserProfileData> getUserProfileLiveData() {
        return userProfileLiveData;
    }

    public MutableLiveData<Boolean> getIsBioUpdated() {
        return isBioUpdated;
    }

//    public void getProfile(String userID){
//
//
//        repository.getProfileData(userID);
//    }


    public void updateMatchesPreferences(String userId){
        repository.updateMatchPreferences(userId);
    }

    public void updateSharedPreferences(String userId){
        repository.updateSharedPreferences(userId);
    }

    public void updatePreferences(Preferences preferences,String userId){
        repository.updatePreferences(preferences,userId);
    }

    public void updateLocation(String lat,String lon,String userId){

        repository.updateLocation(lat,lon,userId);

    }

    public void updateAboutMe(String aboutMe,String userId){
        repository.updateAboutMe(aboutMe,userId);
    }

    public void uploadChecks(ProfileCheck profileCheck){
        repository.uploadChecks(profileCheck);
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
