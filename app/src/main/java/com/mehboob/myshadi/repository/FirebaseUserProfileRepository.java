package com.mehboob.myshadi.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mehboob.myshadi.model.profilemodel.ProfileResponse;
import com.mehboob.myshadi.model.profilemodel.UserProfile;

public class FirebaseUserProfileRepository {


    private MutableLiveData<ProfileResponse> profileResponse = new MutableLiveData<>();

    private MutableLiveData<UserProfile> userProfileMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<ProfileResponse> getProfileResponse() {
        return profileResponse;
    }

    public MutableLiveData<UserProfile> getUserProfileMutableLiveData() {
        return userProfileMutableLiveData;
    }

    public void uploadProfile(UserProfile userProfile) {

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
//                        uploadStatus.setValue(true);
//                        taskMessage.setValue(task.getResult().toString());

                        userProfileMutableLiveData.setValue(userProfile);

                    } else {
                        profileResponse.setValue(new ProfileResponse(false, "Something went wrong"));

                    }
                }).addOnFailureListener(e -> {
                    profileResponse.setValue(new ProfileResponse(false, e.getLocalizedMessage().toString()));

                });


    }


}
