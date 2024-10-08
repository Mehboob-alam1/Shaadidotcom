package com.mehboob.myshadi.repository;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.health.connect.datatypes.BoneMassRecord;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.mehboob.myshadi.model.ProfileCheck;
import com.mehboob.myshadi.model.profilemodel.Preferences;
import com.mehboob.myshadi.model.profilemodel.ProfileResponse;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.Dao.RecentMatchesDao;
import com.mehboob.myshadi.room.Dao.UserProfileDataDao;
import com.mehboob.myshadi.room.database.DataDatabase;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.room.entities.UserProfileData;
import com.mehboob.myshadi.room.models.User;
import com.mehboob.myshadi.utils.MatchPref;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.TinyDB;
import com.mehboob.myshadi.viewmodel.AuthViewModel;
import com.mehboob.myshadi.views.activities.ProfileDetailedActivity;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUserProfileRepository {


//    private MutableLiveData<UserProfile> userProfileLiveData;

    private LiveData<UserProfileData> userProfileData;
    private MutableLiveData<UserProfileData> userProfileDataMutable;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    int uploadedCount = 0;
    private Application application;
    private MutableLiveData<Boolean> isProfileCompleted;
    private MutableLiveData<Boolean> isPreferencesAdded;
    private MutableLiveData<Boolean> isBioUpdated;
    private SessionManager sessionManager;

    private DataDatabase dataDatabase;

    private UserProfileDataDao userProfileDataDao;
    private String token;


    private MutableLiveData<UserMatches> anyUserProfileData;


    public FirebaseUserProfileRepository(Application application) {
        this.storageReference = FirebaseStorage.getInstance().getReference("userProfiles").child("images");
        databaseReference = FirebaseDatabase.getInstance().getReference();
//        userProfileLiveData = new MutableLiveData<>();
        this.application = application;
        isProfileCompleted = new MutableLiveData<>();
        dataDatabase = DataDatabase.getInstance(application);
        isPreferencesAdded = new MutableLiveData<>();
        userProfileDataDao = dataDatabase.userProfileDataDao();
        sessionManager = new SessionManager(application.getApplicationContext());
        isBioUpdated = new MutableLiveData<>();

        userProfileData = userProfileDataDao.getUserProfileLiveData();
        userProfileDataMutable = new MutableLiveData<>();
        setUserProfileDataMutable(userProfileData);
        anyUserProfileData=new MutableLiveData<>();

    }

    public void setUserProfileDataMutable(LiveData<UserProfileData> data) {
        userProfileDataMutable.postValue(data.getValue());
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

    public MutableLiveData<UserProfileData> getUserProfileData() {
        return userProfileDataMutable;
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
                userProfile.getDate_of_birth(),
                userProfile.getToken());

        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("userProfiles");

        userProfileRef.child(userProfile.getGender())

                .child(profile.getUserId())
                .setValue(profile)

                .addOnCompleteListener(task -> {


                    if (task.isComplete() && task.isSuccessful()) {


//                        userProfileLiveData.setValue(userProfile);

                        isProfileCompleted.setValue(true);


                        insertUserProfile(new UserProfileData(userProfile));


                        //        uploadChecks(userProfile);


                    } else {
//                        userProfileLiveData.setValue(null);
                        isProfileCompleted.setValue(false);
                    }
                }).addOnFailureListener(e -> {
//                    userProfileLiveData.setValue(null);
                    isProfileCompleted.setValue(false);

                });


    }

    private void insertUserProfile(UserProfileData userProfile) {

        new InsertProfileTask(dataDatabase).execute(userProfile);
    }


    private static class InsertProfileTask extends AsyncTask<UserProfileData, Void, Void> {
        private UserProfileDataDao userProfileDataDao;

        InsertProfileTask(DataDatabase dataDatabase) {
            userProfileDataDao = dataDatabase.userProfileDataDao();
        }

        @Override
        protected Void doInBackground(UserProfileData... userProfileData) {
try {
    userProfileDataDao.insertUserProfile(userProfileData[0]);
}catch (Exception e){
    Log.d("Exception",e.getLocalizedMessage());
}
            return null;
        }
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

        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserProfileData userProfile = dataSnapshot.getValue(UserProfileData.class);

                    userProfileDataMutable.postValue(userProfile);

                    Log.d("userProfileCheck", userProfile.toString());
                    Log.d("userProfileCheck", sessionManager.fetchGender());

                } else {
                    // Handle the case where the profile data doesn't exist
                    Log.d("userProfileCheck", "No profile");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                Log.d("userProfileCheck", databaseError.getMessage());
            }
        });


    }

    // Getter method for the MutableLiveData
    //public MutableLiveData<UserProfile> getUserProfileLiveData() {
//        return userProfileLiveData;
//    }


    public void updateSharedPreferences(String userID) {
        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("ProfileChecks");
        userProfileRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                if (userSnapshot.exists()) {

                    // Retrieve user profile information

                    ProfileCheck profileCheck = userSnapshot.getValue(ProfileCheck.class);


                    sessionManager.saveGender(profileCheck.getGender());
                    sessionManager.saveUserID(userID);

                    Log.d("sharedPreferencesUpdate", "Updated preferences");
                    Log.d("sharedPreferencesUpdate", sessionManager.fetchGender());
                    Log.d("sharedPreferencesUpdate", sessionManager.fetchUserId());


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

    public void updateToken(String token, String userId) {
        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("userProfiles");

        userProfileRef.child(sessionManager.fetchGender()).child(userId)
                .child("token")
                .setValue(token);
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


    public MutableLiveData<UserMatches> getAnyUserProfile(String userId,String gender){



        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("userProfiles")
                .child(gender)
                .child(userId);
        userProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserMatches userProfile = snapshot.getValue(UserMatches.class);

                    anyUserProfileData.setValue(userProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return anyUserProfileData;
    }


}
