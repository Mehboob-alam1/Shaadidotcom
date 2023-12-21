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
    private RecentMatchesDao recentMatchesDao;

    DataDatabase dataDatabase;
    private LiveData<List<UserMatches>> allUserProfiles;
    private List<UserMatches> allUserProfilesData;

    public MatchMakingRepository(Application application) {

        dataDatabase = DataDatabase.getInstance(application);
        recentMatchesDao = dataDatabase.recentMatchesDao();
        allUserProfiles = recentMatchesDao.getAllUserProfiles();
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

    public LiveData<List<UserMatches>> getUserProfilesCreatedLastWeek() {
        long weekAgoTimestamp = getWeekAgoTimestamp();
        return recentMatchesDao.getUserProfilesCreatedLastWeek(weekAgoTimestamp);
    }

    public LiveData<List<UserMatches>> getBestMatchesPref(int minAge,int maxAge){

        long currentDate = Calendar.getInstance().getTimeInMillis();
        long minDobMillis = calculateMillisForAge(minAge);
        long maxDobMillis = calculateMillisForAge(maxAge);


        return recentMatchesDao.getBestMatchesPref(sessionManager.fetchGender(), sessionManager.fetchCityName(), sessionManager.fetchCommunity(),
               sessionManager.fetchSubCommunity(),sessionManager.fetchMaritalStatus(),minDobMillis,maxDobMillis );
    }

    private long getWeekAgoTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7); // 7 days ago
        return calendar.getTimeInMillis();
    }
    private long calculateMillisForAge(int age) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -age);
        return calendar.getTimeInMillis();
    }
}
