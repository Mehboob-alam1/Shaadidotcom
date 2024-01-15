package com.mehboob.myshadi.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.room.database.DataDatabase;

import java.util.ArrayList;
import java.util.List;

public class MakeConnectionRepository {

    private Application application;
    private DataDatabase database;
    private MutableLiveData<List<Connection>> connectedUserData;
    private MutableLiveData<Boolean> connected;


    public MakeConnectionRepository(Application application) {

        this.application = application;
        connectedUserData = new MutableLiveData<>();
        connected = new MutableLiveData<>();


    }


    public void connectBothUsers(Connection connection) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child("ConnectionIRecieved").child(connection.getConnectionFromId()).child(connection.getConnectionToId())
                .child("connected")
                .setValue(true)
                .addOnCompleteListener(task -> {
                    if (task.isComplete() && task.isSuccessful()) {
                        ref.child("ConnectionIRecieved").child(connection.getConnectionFromId()).child(connection.getConnectionToId())
                                .child("status")
                                .setValue("connected").addOnCompleteListener(task1 -> {
                                    if (task1.isComplete() && task1.isSuccessful()) {
                                        ref.child("ConnectionISent").child(connection.getConnectionToId()).child(connection.getConnectionFromId())
                                                .child("connected")
                                                .setValue(true);

                                        ref.child("ConnectionISent").child(connection.getConnectionToId()).child(connection.getConnectionFromId())
                                                .child("status")
                                                .setValue("connected");
                                    }
                                });
                    }
                });


        connected.setValue(true);


    }


    public MutableLiveData<List<Connection>> getConnectedProfiles(String userId) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ConnectionISent");
        List<Connection> connections = new ArrayList<>();
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Connection connectedUsers = snap.getValue(Connection.class);
                        if (connectedUsers.isConnected() && connectedUsers.getStatus().equals("connected")) {
                            connections.add(connectedUsers);
                        }

                    }

                    connectedUserData.setValue(connections);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                connectedUserData.setValue(null);
            }
        });

        return connectedUserData;
    }

    public MutableLiveData<Boolean> getConnected() {
        return connected;
    }
}
