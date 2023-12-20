package com.mehboob.myshadi.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.os.PerformanceHintManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.myshadi.model.profilemodel.Preferences;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.Dao.RecentMatchesDao;
import com.mehboob.myshadi.room.database.DataDatabase;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.utils.MatchPref;
import com.mehboob.myshadi.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MatchMakingRepository {


    private Application application;


    private SessionManager sessionManager;

    // new objects
    private RecentMatchesDao userProfileDao;

    DataDatabase dataDatabase;
    private LiveData<List<UserMatches>> allUserProfiles;
    private List<UserMatches> allUserProfilesData;

    public MatchMakingRepository(Application application) {

        dataDatabase = DataDatabase.getInstance(application);
        userProfileDao = dataDatabase.recentMatchesDao();
        allUserProfiles = userProfileDao.getAllUserProfiles();
        this.application = application;
        sessionManager= new SessionManager(application);
        allUserProfilesData = new ArrayList<>();

    }

    public void insertUserProfile(List<UserMatches> userProfileEntity) {
        // You may want to perform this operation in a background thread.
        new InsertAsyncTask(dataDatabase).execute(userProfileEntity);
    }

    public LiveData<List<UserMatches>> getAllUserProfiles() {
        return allUserProfiles;
    }

    private static class InsertAsyncTask extends AsyncTask<List<UserMatches>, Void, Void> {
        private RecentMatchesDao recentMatchesDao;

        InsertAsyncTask(DataDatabase dataDatabase) {
            recentMatchesDao = dataDatabase.recentMatchesDao();
        }

        @Override
        protected Void doInBackground(List<UserMatches>... lists) {
            recentMatchesDao.insertRecentMatches(lists[0]);

            return null;
        }
    }

    public void checkMyProfileMatches() {
        DatabaseReference profilesRef = FirebaseDatabase.getInstance().getReference("userProfiles");

        String gender = sessionManager.fetchGender();
        String starts = "";

        if (gender.equals("Male"))
            starts = "Female";
        else
            starts = "Male";

        profilesRef.child(starts)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot profileSnapshot : snapshot.getChildren()) {
                            UserMatches userProfile = profileSnapshot.getValue(UserMatches.class);


                            allUserProfilesData.add(userProfile);


                            // Check if the profile matches preferences

                        }
                        insertUserProfile(allUserProfilesData);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Matches", error.getMessage());
                    }
                });

    }

    private boolean arMyPrefMatching(UserProfile currentUserProfile, UserProfile otherUserProfile) {

        String gender1 = currentUserProfile.getGender();
        String gender2 = otherUserProfile.getGender();
        return !gender1.equals(gender2) &&
                currentUserProfile.getPreferences().getCity().equals(otherUserProfile.getPreferences().getCity()) &&
                currentUserProfile.getPreferences().getCommunity().equals(otherUserProfile.getCommunity()) &&
                currentUserProfile.getPreferences().getSubCommunity().equals(otherUserProfile.getSubCommunity()) &&
                currentUserProfile.getPreferences().getMaritalStatus().equals(otherUserProfile.getPreferences().getMaritalStatus()) &&
                currentUserProfile.getPreferences().getReligion().equals(otherUserProfile.getPreferences().getReligion());
    }


    private boolean arePreferencesMatching(UserProfile currentUserProfile, UserProfile otherUserProfile) {

        int age1 = Integer.parseInt(currentUserProfile.getDob());
        int age2 = Integer.parseInt(otherUserProfile.getDob());


        String gender1 = currentUserProfile.getGender();
        String gender2 = otherUserProfile.getGender();


        //TODO
        // Profile matchmaking
        int minAge = Math.min(Integer.parseInt(currentUserProfile.getPreferences().getMinAge()), Integer.parseInt(otherUserProfile.getPreferences().getMinAge()));
        int maxAge = Math.max(Integer.parseInt(currentUserProfile.getPreferences().getMaxAge()), Integer.parseInt(otherUserProfile.getPreferences().getMaxAge()));

        return
                age1 >= minAge && age1 <= maxAge &&
                        age2 >= minAge && age2 <= maxAge &&

                        !gender1.equals(gender2) &&
                        currentUserProfile.getPreferences().getCity().equals(otherUserProfile.getPreferences().getCity()) &&
                        currentUserProfile.getPreferences().getCommunity().equals(otherUserProfile.getCommunity()) &&
                        currentUserProfile.getPreferences().getSubCommunity().equals(otherUserProfile.getSubCommunity()) &&
                        currentUserProfile.getPreferences().getMaritalStatus().equals(otherUserProfile.getPreferences().getMaritalStatus()) &&
                        currentUserProfile.getPreferences().getReligion().equals(otherUserProfile.getPreferences().getReligion());


    }
}
