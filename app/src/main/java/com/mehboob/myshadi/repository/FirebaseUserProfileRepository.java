package com.mehboob.myshadi.repository;

import android.app.Activity;
import android.app.Application;
import android.health.connect.datatypes.BoneMassRecord;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mehboob.myshadi.model.ProfileCheck;
import com.mehboob.myshadi.model.profilemodel.Preferences;
import com.mehboob.myshadi.model.profilemodel.ProfileResponse;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.models.User;
import com.mehboob.myshadi.utils.MatchPref;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.TinyDB;
import com.mehboob.myshadi.viewmodel.AuthViewModel;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUserProfileRepository {


    private MutableLiveData<UserProfile> userProfileLiveData;


    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    int uploadedCount = 0;
    private Application application;
    private MutableLiveData<Boolean> isProfileCompleted;
    private MutableLiveData<Boolean> isPreferencesAdded;
    private MutableLiveData<Boolean> isBioUpdated;
    private SessionManager sessionManager;


    public FirebaseUserProfileRepository(Application application) {
        this.storageReference = FirebaseStorage.getInstance().getReference("userProfiles").child("images");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userProfileLiveData = new MutableLiveData<>();
        this.application = application;
        isProfileCompleted = new MutableLiveData<>();
        isPreferencesAdded = new MutableLiveData<>();
        sessionManager = new SessionManager(application.getApplicationContext());
        isBioUpdated = new MutableLiveData<>();
    }


    public MutableLiveData<Boolean> getIsProfileCompleted() {
        return isProfileCompleted;
    }

    public MutableLiveData<Boolean> getIsPreferencesAdded() {
        return isPreferencesAdded;
    }

    public MutableLiveData<Boolean> getIsBioUpdated() {
        return isBioUpdated;
    }

    public void uploadProfile(UserProfile userProfile, ArrayList<String> imageUrls) {

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
                , imageUrls,
                true,
                userProfile.getAccountType(),
                userProfile.getIsVerified()
                , userProfile.getTime(),
                userProfile.getPreferences(),
                userProfile.getLatitude(),
                userProfile.getLongitude(),
                userProfile.getAboutMe(),
                userProfile.getDate_of_birth());

        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("userProfiles");

        userProfileRef.child(userProfile.getGender())

                .child(profile.getUserId())
                .setValue(profile)

                .addOnCompleteListener(task -> {


                    if (task.isComplete() && task.isSuccessful()) {


                        userProfileLiveData.setValue(userProfile);

                        isProfileCompleted.setValue(true);

                        //        uploadChecks(userProfile);


                    } else {
                        userProfileLiveData.setValue(null);
                        isProfileCompleted.setValue(false);
                    }
                }).addOnFailureListener(e -> {
                    userProfileLiveData.setValue(null);
                    isProfileCompleted.setValue(false);

                });


    }

    public void uploadChecks(ProfileCheck profileCheck) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ProfileChecks");

        reference.child(profileCheck.getUserId())
                .setValue(profileCheck);
    }


    // Method to fetch user profile data from Firebase
    public void getProfileData(String userId) {


        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("userProfiles")
                .child(sessionManager.fetchGender())
                .child(userId);

        userProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                    userProfileLiveData.setValue(userProfile);
                    Log.d("userProfileCheck", userProfile.toString());
                    Log.d("userProfileCheck", sessionManager.fetchGender());

                } else {
                    // Handle the case where the profile data doesn't exist
                    Log.d("userProfileCheck", "No profile");
                    userProfileLiveData.setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                Log.d("userProfileCheck", databaseError.getMessage());
                userProfileLiveData.setValue(null);
            }
        });


    }

    // Getter method for the MutableLiveData
    public MutableLiveData<UserProfile> getUserProfileLiveData() {
        return userProfileLiveData;
    }


    public void updateSharedPreferences(String userID) {
        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("userProfiles");
        userProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                if (userSnapshot.exists()) {

                    // Retrieve user profile information

                    for (DataSnapshot snapshot: userSnapshot.getChildren()){
                        if (snapshot.child(userID).exists()) {
                            String gender = snapshot.child(userID).child("gender").getValue(String.class);

                            sessionManager.saveGender(gender);
                            sessionManager.saveUserID(userID);

                            Log.d("sharedPreferencesUpdate", "Updated preferences");
                            Log.d("sharedPreferencesUpdate", sessionManager.fetchGender());
                            Log.d("sharedPreferencesUpdate", sessionManager.fetchUserId());
                        }
                    }




                } else {
                    Toast.makeText(application, "SharedPreferences not updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void updateMatchPreferences(String userId) {

        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("userProfiles");

        userProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                if (userSnapshot.exists()) {

                    String city = userSnapshot.child(userId).child("city").getValue(String.class);
                    String community = userSnapshot.child(userId).child("community").getValue(String.class);
                    String maritalStatus = userSnapshot.child(userId).child("maritalStatus").getValue(String.class);
                    String maxAge = userSnapshot.child(userId).child("maxAge").getValue(String.class);
                    String maxHeight = userSnapshot.child(userId).child("maxHeight").getValue(String.class);
                    String minAge = userSnapshot.child(userId).child("minAge").getValue(String.class);
                    String minHeight = userSnapshot.child(userId).child("minHeight").getValue(String.class);
                    String religion = userSnapshot.child(userId).child("religion").getValue(String.class);
                    String subCommunity = userSnapshot.child(userId).child("subCommunity").getValue(String.class);

                    MatchPref pref = new MatchPref(application);
                    pref.savePref(new Preferences(minAge, maxAge, minHeight, maxHeight, city, religion, community, subCommunity, maritalStatus));
                } else {
                    Toast.makeText(application, "MatchPreferences not updated", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void updatePreferences(Preferences preferences, String userId) {

        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("userProfiles");

        userProfileRef.child(sessionManager.fetchGender()).child(userId)
                .child("preferences")
                .setValue(preferences)
                .addOnCompleteListener(task -> {

                    if (task.isComplete() && task.isSuccessful()) {

                        isPreferencesAdded.setValue(true);

                    } else {
                        isPreferencesAdded.setValue(false);
                    }
                }).addOnFailureListener(e -> {
                    isPreferencesAdded.setValue(false);
                });
    }


    public void updateAboutMe(String aboutMe, String userId) {

        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("userProfiles");

        userProfileRef.child(sessionManager.fetchGender()).child(userId)
                .child("aboutMe")
                .setValue(aboutMe).addOnCompleteListener(task -> {

                    if (task.isComplete() && task.isSuccessful()) {
                        isBioUpdated.setValue(true);
                    } else {
                        isBioUpdated.setValue(false);
                    }
                }).addOnFailureListener(e -> {
                    isBioUpdated.setValue(false);
                });
    }

    public void updateLocation(String lat, String lon, String userId) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userProfiles").child(sessionManager.fetchGender()).child(userId);
        ref.child("latitude").setValue(lat);
        ref.child("longitude").setValue(lon);
    }

    public void uploadImagesToFirebase(List<Uri> images, UserProfile userProfile, StorageUploadCallback callback) {
        int totalImages = images.size();

        ArrayList<String> imageUrls = new ArrayList<>();

        for (Uri imageUri : images) {
            // Create a unique filename for the image (you may want to improve this logic)
            String filename = System.currentTimeMillis() + ".jpg";

            // Get a reference to the storage location
            StorageReference imageRef = storageReference.child(userProfile.getUserId()).child(filename);

            // Upload the image to Firebase Storage
            imageRef.putFile(imageUri)
                    .continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        // Continue with the task to get the download URL
                        return imageRef.getDownloadUrl();
                    })
                    .addOnSuccessListener(uri -> {
                        // Image uploaded successfully, and download URL obtained
                        String imageUrl = uri.toString();
                        imageUrls.add(imageUrl);

                        uploadedCount++;

                        // Check if all images are uploaded
                        if (uploadedCount == totalImages) {
                            TinyDB tinyDB = new TinyDB(application);
                            tinyDB.putListString("images", imageUrls);

                            // All images uploaded, now save image URLs to Firebase Realtime Database
                            uploadProfile(userProfile, imageUrls);
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


    public MutableLiveData<Boolean> isProfileComplete(String userId) {
        MutableLiveData<Boolean> isProfileCompleteLiveData = new MutableLiveData<>();

        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("ProfileChecks")

                .child(userId)
                .child("profileCompleted");

        userProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean isProfileComplete = dataSnapshot.getValue(Boolean.class);
                    isProfileCompleteLiveData.setValue(isProfileComplete);
                } else {
                    // Handle the case where the node doesn't exist
                    isProfileCompleteLiveData.setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                isProfileCompleteLiveData.setValue(false);
            }
        });

        return isProfileCompleteLiveData;
    }


}
