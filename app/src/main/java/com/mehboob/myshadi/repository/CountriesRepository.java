package com.mehboob.myshadi.repository;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.myshadi.json.Countries;

import java.util.ArrayList;
import java.util.List;

public class CountriesRepository {


    private DatabaseReference databaseReference;




    public CountriesRepository() {
        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("countries").child("Data");
    }



    public void getCountries(final CountryCallback callback) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Countries> countries = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Countries country = childSnapshot.getValue(Countries.class);
                    if (country != null) {
                        countries.add(country);
                    }
                }
                callback.onSuccess(countries);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public interface CountryCallback {
        void onSuccess(List<Countries> countries);
        void onError(String errorMessage);
    }
}
