package com.mehboob.myshadi.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.Dao.SentConnectionDao;
import com.mehboob.myshadi.room.database.DataDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShortListedRepository {

    private Application application;

    private LiveData<List<Connection>> connectedUserIds;
    private DataDatabase dataDatabase;
    private SentConnectionDao sentConnectionDao;

    private MutableLiveData<List<UserProfile>> userProfilesMutableData;

    public ShortListedRepository(Application application) {

        this.application = application;
        dataDatabase = DataDatabase.getInstance(application);
        sentConnectionDao = dataDatabase.sentConnectionDao();
        connectedUserIds = sentConnectionDao.getSentConnections();
        userProfilesMutableData = new MutableLiveData<>();
    }

    public MutableLiveData<List<UserProfile>> getUserProfilesMutableData() {
        return userProfilesMutableData;
    }

    public void fetchShortlistedProfiles(List<Connection> connections) {
        List<UserProfile> userProfiles = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userProfiles");

        for (Connection connection : connections) {

            ref.child(connection.getConnectionToGender())
                    .child(connection.getConnectionToId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                UserProfile profile = snapshot.getValue(UserProfile.class);
                                userProfiles.add(profile);
                                userProfilesMutableData.setValue(userProfiles);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }

    }

}
