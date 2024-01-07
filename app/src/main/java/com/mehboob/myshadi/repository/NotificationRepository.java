package com.mehboob.myshadi.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.myshadi.model.profilemodel.NotificationData;

import java.util.ArrayList;
import java.util.List;

public class NotificationRepository {


    private Application application;

    private MutableLiveData<List<NotificationData>> notificationDataList;

    private List<NotificationData> list;

    private MutableLiveData<Boolean> notificationDeleted;

    public NotificationRepository(Application application) {
        this.application = application;
        notificationDataList = new MutableLiveData<>();
        list = new ArrayList<>();
        notificationDeleted= new MutableLiveData<>();

    }


    public void fetchNotificationsIRecieved(String userId) {
        if (userId != null) {


            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifications");

            databaseReference.child(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                for (DataSnapshot snap : snapshot.getChildren()) {

                                    NotificationData data = snap.getValue(NotificationData.class);
                                    list.add(data);
                                }
                                notificationDataList.setValue(list);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

    }


    public void deleteNotification(String userId,String otherUserId){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifications");


        databaseReference.child(userId).child(otherUserId)
                .setValue(null)
                .addOnCompleteListener(task -> {
                    if (task.isComplete() && task.isSuccessful()){
                        notificationDeleted.setValue(true);
                    }else{
                        notificationDeleted.setValue(false);
                    }
                }).addOnFailureListener(e -> {
                    notificationDeleted.setValue(false);
                });

    }

    public MutableLiveData<Boolean> getNotificationDeleted() {
        return notificationDeleted;
    }

    public MutableLiveData<List<NotificationData>> getNotificationDataList() {
        return notificationDataList;
    }
}
