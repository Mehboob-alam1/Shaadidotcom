package com.mehboob.myshadi.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.myshadi.model.profilemodel.Preferences;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.utils.MatchPref;

public class MatchMakingRepository {


    private Application application;

    private MatchPref matchPref;


    public MatchMakingRepository(Application application){

        matchPref= new MatchPref(application.getApplicationContext());

        this.application= application;



    }



    public void checkRecentMatches(UserProfile currentUserProfile){

        DatabaseReference profilesRef = FirebaseDatabase.getInstance().getReference("userProfiles");

        profilesRef.orderByChild("time").limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot profileSnapshot : dataSnapshot.getChildren()) {
                    UserProfile userProfile = profileSnapshot.getValue(UserProfile.class);

                    // Check if the profile matches preferences
                    if (arePreferencesMatching(currentUserProfile, userProfile)) {
                        // This profile is a potential match
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private boolean arePreferencesMatching(UserProfile currentUserProfile, UserProfile otherUserProfile) {

        int age1 = Integer.parseInt(currentUserProfile.getDob());
        int age2 = Integer.parseInt(otherUserProfile.getDob());


        //TODO
        // Profile matchmaking
        int minAge = Math.min(Integer.parseInt(currentUserProfile.getPreferences().getMinAge()), Integer.parseInt(otherUserProfile.getPreferences().getMinAge()));
        int maxAge = Math.max(Integer.parseInt(currentUserProfile.getPreferences().getMaxAge()), Integer.parseInt(otherUserProfile.getPreferences().getMaxAge()));

        // Check if ages and other preferences are within the specified range
        return age1 >= minAge && age1 <= maxAge &&
                age2 >= minAge && age2 <= maxAge &&
                currentUserProfile.getPreferences().getReligion().equals(otherUserProfile.getPreferences().getReligion());

        // Implement logic to compare preferences (e.g., age range, religion, etc.)
        // Return true if preferences match, otherwise false
    }
}
