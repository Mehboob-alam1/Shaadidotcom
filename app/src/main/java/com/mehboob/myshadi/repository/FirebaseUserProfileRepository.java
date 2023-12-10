package com.mehboob.myshadi.repository;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mehboob.myshadi.model.profilemodel.ProfileResponse;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
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
    public FirebaseUserProfileRepository() {
        this.storageReference = FirebaseStorage.getInstance().getReference("userProfiles").child("images");
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    public MutableLiveData<UserProfile> getUserProfileMutableLiveData() {
        return userProfileMutableLiveData;
    }

    public void uploadProfile(UserProfile userProfile,List<String> imageUrls) {

        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("userProfiles");


        userProfileRef.child(userProfile.getGender())
                .child(userProfile.getLivingIn())
                .child(userProfile.getReligion())
                .child(userProfile.getCommunity())
                .child(userProfile.getSubCommunity())
                .child(userProfile.getMaritalStatus())

                .child(userProfile.getUserId())
                .setValue(userProfile)

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

        List<String> imageUrls = new ArrayList<>();

        for (Uri imageUri : images) {
            // Create a unique filename for the image (you may want to improve this logic)
            String filename = System.currentTimeMillis() + ".jpg";

            // Get a reference to the storage location
            StorageReference imageRef = storageReference.child(FirebaseAuth.getInstance().getUid()).child(filename);

            // Upload the image to Firebase Storage
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully
                        String imageUrl = imageRef.getDownloadUrl().toString();
                        imageUrls.add(imageUrl);

                        uploadedCount++;

                        // Check if all images are uploaded
                        if (uploadedCount == totalImages) {
                            TinyDB tinyDB= new TinyDB(application)
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
