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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MatchMakingRepository {


    private Application application;

    private MatchPref matchPref;

    private List<UserProfile> bestMatchRecentProfiles;

    private MutableLiveData<List<UserProfile>> bestMatchRecentUsers;

    public MatchMakingRepository(Application application) {

        matchPref = new MatchPref(application.getApplicationContext());

        this.application = application;

        bestMatchRecentUsers= new MutableLiveData<>();
        bestMatchRecentProfiles= new ArrayList<>();


    }

    public MutableLiveData<List<UserProfile>> getBestMatchRecentUsers() {
        return bestMatchRecentUsers;
    }

    public void checkRecentBestMatchesProfiles(UserProfile currentUserProfile) {

        DatabaseReference profilesRef = FirebaseDatabase.getInstance().getReference("userProfiles");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date startDate = calendar.getTime();

        // Format dates for display
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedStartDate = sdf.format(startDate);
        String formattedCurrentDate = sdf.format(new Date());
        profilesRef.orderByChild("time").startAt(startDate.getTime()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot profileSnapshot : dataSnapshot.getChildren()) {
                    UserProfile userProfile = profileSnapshot.getValue(UserProfile.class);

                    // Check if the profile matches preferences
                    if (arePreferencesMatching(currentUserProfile, userProfile)) {
                        // This profile is a potential match

                        bestMatchRecentProfiles.add(userProfile);

                        bestMatchRecentUsers.setValue(bestMatchRecentProfiles);
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

        int height1 = Integer.parseInt(currentUserProfile.getHeight());
        int height2 = Integer.parseInt(otherUserProfile.getHeight());


        int minHeight = Math.min(Integer.parseInt(currentUserProfile.getPreferences().getMinHeight()), Integer.parseInt(otherUserProfile.getPreferences().getMinHeight()));
        int maxHeight = Math.max(Integer.parseInt(currentUserProfile.getPreferences().getMaxHeight()), Integer.parseInt(otherUserProfile.getPreferences().getMaxHeight()));
        //TODO
        // Profile matchmaking
        int minAge = Math.min(Integer.parseInt(currentUserProfile.getPreferences().getMinAge()), Integer.parseInt(otherUserProfile.getPreferences().getMinAge()));
        int maxAge = Math.max(Integer.parseInt(currentUserProfile.getPreferences().getMaxAge()), Integer.parseInt(otherUserProfile.getPreferences().getMaxAge()));

        // Check if ages and other preferences are within the specified range
        return age1 >= minAge && age1 <= maxAge &&
                age2 >= minAge && age2 <= maxAge &&
                height1>=minHeight && height1<=maxHeight &&
                height2>=minHeight && height2 <=maxHeight &&
                currentUserProfile.getPreferences().getCity().equals(otherUserProfile.getPreferences().getCity()) &&
                currentUserProfile.getPreferences().getCommunity().equals(otherUserProfile.getCommunity()) &&
                currentUserProfile.getPreferences().getSubCommunity().equals(otherUserProfile.getSubCommunity()) &&
                currentUserProfile.getPreferences().getMaritalStatus().equals(otherUserProfile.getPreferences().getMaritalStatus())&&
                currentUserProfile.getPreferences().getReligion().equals(otherUserProfile.getPreferences().getReligion());

        // Implement logic to compare preferences (e.g., age range, religion, etc.)
        // Return true if preferences match, otherwise false
    }
}
