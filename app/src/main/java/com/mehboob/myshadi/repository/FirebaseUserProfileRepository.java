package com.mehboob.myshadi.repository;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mehboob.myshadi.model.profilemodel.ProfileResponse;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.models.User;
import com.mehboob.myshadi.utils.TinyDB;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUserProfileRepository {


    private MutableLiveData<ProfileResponse> profileResponse = new MutableLiveData<>();

    private MutableLiveData<UserProfile> userProfileMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<ProfileResponse> getProfileResponse() {
        return profileResponse;
    }

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    int uploadedCount = 0;
    private Application application;
    public FirebaseUserProfileRepository(Application application) {
        this.storageReference = FirebaseStorage.getInstance().getReference("userProfiles").child("images");
        databaseReference= FirebaseDatabase.getInstance().getReference();

        this.application=application;
    }

    public MutableLiveData<UserProfile> getUserProfileMutableLiveData() {
        return userProfileMutableLiveData;
    }

    public void uploadProfile(UserProfile userProfile,ArrayList<String> imageUrls) {

        UserProfile profile = new UserProfile(userProfile.getProfileFor(),
                userProfile.getGender(),
                userProfile.getFullName(),
                userProfile.getDob(),
                userProfile.getReligion(),
                userProfile.getCommunity(),
                userProfile.getLivingIn(),
                userProfile.getEmail(),
                userProfile.getPhoneNumber(),
                userProfile.getCountryCode(),
                userProfile.getStateName(),
                userProfile.getStateCode(),
                userProfile.getCityName(),
                userProfile.getSubCommunity(),
                userProfile.getMaritalStatus(),
                userProfile.getChildren(),
                userProfile.getHeight(),
                userProfile.getDiet(),
                userProfile.getQualifications(),
                userProfile.getCollege(),
                userProfile.getIncome(),
                userProfile.getWorksWith(),
                userProfile.getWorkAs(),
                imageUrls.get(0),
                userProfile.getUserId()
                ,imageUrls);

        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("userProfiles");


        userProfileRef.child(profile.getGender())
                .child(profile.getLivingIn())
                .child(profile.getReligion())
                .child(profile.getCommunity())
                .child(profile.getSubCommunity())
                .child(profile.getMaritalStatus())

                .child(profile.getUserId())
                .setValue(profile)

                .addOnCompleteListener(task -> {


                    if (task.isComplete() && task.isSuccessful()) {

                        profileResponse.setValue(new ProfileResponse(true, "Profile uploaded"));

                        userProfileMutableLiveData.setValue(userProfile);

                    } else {
                        profileResponse.setValue(new ProfileResponse(false, "Something went wrong"));

                    }
                }).addOnFailureListener(e -> {
                    profileResponse.setValue(new ProfileResponse(false, e.getLocalizedMessage().toString()));

                });


    }

    public void uploadImagesToFirebase(List<Uri> images,UserProfile userProfile, StorageUploadCallback callback) {
        int totalImages = images.size();

        ArrayList<String> imageUrls = new ArrayList<>();

        for (Uri imageUri : images) {
            // Create a unique filename for the image (you may want to improve this logic)
            String filename = System.currentTimeMillis() + ".jpg";

            // Get a reference to the storage location
            StorageReference imageRef = storageReference.child(FirebaseAuth.getInstance().getUid()).child(filename);

            // Upload the image to Firebase Storage
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully
                        String imageUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
                        imageUrls.add(imageUrl);

                        uploadedCount++;

                        // Check if all images are uploaded
                        if (uploadedCount == totalImages) {
                            TinyDB tinyDB= new TinyDB(application);
                            tinyDB.putListString("images", imageUrls);

                            // All images uploaded, now save image URLs to Firebase Realtime Database
                            uploadProfile(userProfile,imageUrls);
                            callback.onSuccess(imageUrls);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error
                        callback.onError(e.getMessage());
                    });
        }
    }

    public interface StorageUploadCallback {
        void onSuccess(List<String> imageUrls);

        void onError(String errorMessage);
    }


}
