package com.mehboob.myshadi.repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.myshadi.json.Countries;
import com.mehboob.myshadi.json.States;

import java.util.ArrayList;
import java.util.List;

public class StateRepository {

    private DatabaseReference databaseReference;



    public StateRepository(){

        databaseReference= FirebaseDatabase.getInstance().getReference("states").child("Data");
    }

    public void getStates(final StatesCallback callback){



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<States> states = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    States state = childSnapshot.getValue(States.class);
                    if (state != null) {
                        states.add(state);
                    }
                }
                callback.onSuccess(states);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public interface StatesCallback {
        void onSuccess(List<States> states);
        void onError(String errorMessage);
    }
}
