package com.mehboob.myshadi.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.os.PerformanceHintManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.util.Util;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.model.profilemodel.NotificationData;
import com.mehboob.myshadi.model.profilemodel.Preferences;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.Dao.RecentMatchesDao;
import com.mehboob.myshadi.room.Dao.SentConnectionDao;
import com.mehboob.myshadi.room.database.DataDatabase;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.room.entities.UserProfileData;
import com.mehboob.myshadi.utils.MatchPref;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MatchMakingRepository {


    private Application application;


    private SessionManager sessionManager;

    // new objects
    private RecentMatchesDao recentMatchesDao;

    DataDatabase dataDatabase;
    private LiveData<List<UserMatches>> allUserProfiles;
    private List<UserMatches> allUserProfilesData;
    private MutableLiveData<Boolean> connectionSent = new MutableLiveData<>();

    private SentConnectionDao sentConnectionDao;

    private LiveData<List<Connection>> connectedUserIds;


    public MatchMakingRepository(Application application) {
        FirebaseApp.initializeApp(application);

        dataDatabase = DataDatabase.getInstance(application);
        recentMatchesDao = dataDatabase.recentMatchesDao();
        allUserProfiles = recentMatchesDao.getAllUserProfiles();
        this.application = application;
        sessionManager = new SessionManager(application);
        allUserProfilesData = new ArrayList<>();

        sentConnectionDao = dataDatabase.sentConnectionDao();
        connectedUserIds = sentConnectionDao.getSentConnections();

    }

    public void insertUserProfile(List<UserMatches> userProfileEntity) {
        // You may want to perform this operation in a background thread.
        new InsertAsyncTask(dataDatabase).execute(userProfileEntity);
    }

    public MutableLiveData<Boolean> getConnectionSent() {
        return connectionSent;
    }
    public LiveData<List<Connection>> getConnectedUserIds() {
        return connectedUserIds;
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

    public LiveData<List<UserMatches>> getBestMatchesPref(int minAge, int maxAge) {

        long currentDate = Calendar.getInstance().getTimeInMillis();
        long minDobMillis = calculateMillisForAge(minAge);
        long maxDobMillis = calculateMillisForAge(maxAge);


        return recentMatchesDao.getBestMatchesPref(sessionManager.fetchCityName(), sessionManager.fetchCommunity(),
                sessionManager.fetchSubCommunity(), sessionManager.fetchMaritalStatus(), minDobMillis, maxDobMillis);
    }

    public LiveData<List<UserMatches>> getNearestProfiles(double userLatitude, double userLongitude, double radius, int limit) {
        double radiusSquared = Math.pow(radius, 2);
        return recentMatchesDao.getNearestProfiles(userLatitude, userLongitude, radiusSquared, limit);
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


    public void makeConnections(Connection connection, UserMatches otherUserMatches, UserProfileData currentUser) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("ConnectionISent").child(currentUser.getUserId())
                .child(otherUserMatches.getUserId())
                .setValue(connection)
                .addOnCompleteListener(task -> {
                    if (task.isComplete()) {

                          connectionToUser(connection,otherUserMatches,currentUser);

                    }
                }).addOnFailureListener(e -> {
                });

    }

    private void connectionToUser(Connection connection, UserMatches otherUserMatches, UserProfileData currentUser) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("ConnectionIRecieved").child(otherUserMatches.getUserId())
                .child(currentUser.getUserId())
                .setValue(connection)
                .addOnCompleteListener(task -> {
                    if (task.isComplete() && task.isSuccessful()){
                        connectionSent.postValue(true);
                       //  insertSentConnection(connection);

                        NotificationData notificationData= new NotificationData(currentUser.getImageUrl(), currentUser.getFullName(),
                                currentUser.getUserId(),currentUser.getFullName() + " is interested in your profile",otherUserMatches.getUserId(),String.valueOf(System.currentTimeMillis()));
                        setNotificationToServerToOtherUser(notificationData);
                    }
                }).addOnFailureListener(e -> {
                    connectionSent.postValue(false);
                });

    }

    private void setNotificationToServerToOtherUser(NotificationData notificationData) {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Notifications");
         databaseReference.child(notificationData.getUserId())
                 .child(notificationData.getFromUserId())
                 .setValue(notificationData);
    }

    public void sendNotification(Connection connection, UserMatches otherUserMatches, UserProfileData currentUser) {

        // current user name, message,currentUSerID,otherUSerToken


        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject notificationObject = new JSONObject();

            notificationObject.put("title", currentUser.getFullName());
            notificationObject.put("body", currentUser.getFullName() + " sent you a connect");


            JSONObject dataObject = new JSONObject();
            dataObject.put("userId", currentUser.getUserId());
            dataObject.put("gender", currentUser.getGender());

            jsonObject.put("notification", notificationObject);
            jsonObject.put("data", dataObject);
            jsonObject.put("to", otherUserMatches.getToken());

            callApi(jsonObject, connection, otherUserMatches, currentUser);
        } catch (Exception e) {

        }
    }


    public void callApi(JSONObject jsonObject, Connection connection, UserMatches otherUserMatches, UserProfileData userProfileData) {
        MediaType JSON = MediaType.get("application/json");

        OkHttpClient client = new OkHttpClient();

        String url = "https://fcm.googleapis.com/fcm/send";

        RequestBody requestBody = RequestBody.create(jsonObject.toString(), JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Authorization", "Bearer AAAAtT1kHVE:APA91bHLrXzauLb6Iw0bxzoFS6mkLhDvJRxECeM96INdpD3DeFjNAlx-YXTWgnMFTqz_eNyt8lGnBYpH7ks6ot2LO1J6H6IH7tbfEwuHXz9152YOOiXzy5tx7mfvop6kOZ9A_uXwLrk0")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                connectionSent.postValue(false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {


                    makeConnections(connection, otherUserMatches, userProfileData);
                }
            }
        });
    }

    public void insertSentConnection(Connection sentConnection) {
        new InsertSentConnectionAsyncTask(sentConnectionDao).execute(sentConnection);
    }

    public void deleteUserMatches(UserMatches userMatches) {
        new DeleteUserMatchesAsyncTask(recentMatchesDao).execute(userMatches);
    }



    private static class InsertSentConnectionAsyncTask extends AsyncTask<Connection, Void, Void> {
        private SentConnectionDao sentConnectionDao;

        private InsertSentConnectionAsyncTask(SentConnectionDao sentConnectionDao) {
            this.sentConnectionDao = sentConnectionDao;
        }

        @Override
        protected Void doInBackground(Connection... sentConnections) {
            sentConnectionDao.insertSentConnection(sentConnections[0]);
            return null;
        }
    }

    private static class DeleteUserMatchesAsyncTask extends AsyncTask<UserMatches, Void, Void> {
       private RecentMatchesDao recentMatchesDao;

        private DeleteUserMatchesAsyncTask(RecentMatchesDao recentMatchesDao) {
            this.recentMatchesDao = recentMatchesDao;
        }

        @Override
        protected Void doInBackground(UserMatches... userMatches) {
            recentMatchesDao.deleteUserMatches(userMatches[0]);
            return null;
        }
    }
}
