package com.mehboob.myshadi.repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.myshadi.json.Cities;
import com.mehboob.myshadi.json.States;

import java.util.ArrayList;
import java.util.List;

public class CitiesRepository {


    private DatabaseReference databaseReference;

    public CitiesRepository(){


        databaseReference= FirebaseDatabase.getInstance().getReference("cities").child("Data");

    }



    public void getCities(String stateCode,CitiesCallBack citiesCallBack){

//        Query query = databaseReference.orderByChild("state_code").equalTo(stateCode);
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<Cities> cities = new ArrayList<>();
//                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                    Cities city = childSnapshot.getValue(Cities.class);
//                    if (city != null) {
//                        cities.add(city);
//                    }
//                }
//                citiesCallBack.onSuccess(cities);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                citiesCallBack.onFailure(error.getMessage());
//            }
//        });


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Cities> cities = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Cities city = childSnapshot.getValue(Cities.class);
                    if (city != null) {
                        cities.add(city);
                    }
                }
                citiesCallBack.onSuccess(cities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                citiesCallBack.onFailure(error.getMessage());
            }
        });

    }


    public interface CitiesCallBack{
        void onSuccess(List<Cities> cities);

        void onFailure(String errorMessage);
    }
}
