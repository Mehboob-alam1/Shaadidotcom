package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.StateAdapter;
import com.mehboob.myshadi.databinding.ActivityStateCityBinding;
import com.mehboob.myshadi.databinding.BottomSheetStateBinding;
import com.mehboob.myshadi.json.State;
import com.mehboob.myshadi.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StateCityActivity extends AppCompatActivity {
    private ActivityStateCityBinding binding;

    private String[] states = {"Gilgit-Baltistan", "Punjab", "Sindh", "Khyber Pakhtunkhwa", "Balochistan", "Azad Kashmir"};
    private SessionManager sessionManager;

    private String TAG = "StateActivity";
  private   List<String> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStateCityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(this);


        loadStates(sessionManager.fetchCountryCode());


    }




    private void loadStates(String selectedCountryCode) {
        List<State> stateList = new ArrayList<>();


        try {
            // Load JSON file from assets
            InputStream is = getAssets().open("states.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            // Parse JSON array
            JSONObject jsonObject = new JSONObject(json);
            JSONArray dataArray = jsonObject.getJSONArray("Data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject state = dataArray.getJSONObject(i);
                String countryCode = state.getString("country");
                if (selectedCountryCode.equals(countryCode)) {
                    String stateName = state.getString("name");
                    String stateCode = state.getString("code");
                    Log.d(TAG, "loadStates: " + stateName);
                    stateList.add(new State(stateName, stateCode));
                }
            }

            // Display states in ListView
            StateAdapter stateAdapter = new StateAdapter(this, stateList);


            binding.txtState.setAdapter(stateAdapter);

            binding.txtState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    sessionManager.saveStateName(stateList.get(i).getName());
                    sessionManager.saveStateCode(stateList.get(i).getCode());



                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });




        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

//    cities = loadCities(sessionManager.fetchStateCode());
//
//    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
//
//    // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//    // Apply the adapter to the spinner
//            binding.txtCity.setAdapter(adapter);
//
//            binding.txtCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//            sessionManager.saveCityName(binding.txtCity.getSelectedItem().toString());
//
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> adapterView) {
//
//        }
//    });
//    private List<String> loadCities(String selectedStateCode) {
//        List<String> stateList = new ArrayList<>();
//
//        try {
//            // Load JSON file from assets
//            InputStream is = getAssets().open("cities.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            String json = new String(buffer, StandardCharsets.UTF_8);
//
//            // Parse JSON array
//            JSONObject jsonObject = new JSONObject(json);
//            JSONArray dataArray = jsonObject.getJSONArray("Data");
//
//            for (int i = 0; i < dataArray.length(); i++) {
//                JSONObject state = dataArray.getJSONObject(i);
//                String stateCode = state.getString("code");
//                if (selectedStateCode.equals(stateCode)) {
//                    String cityname = state.getString("name");
//                    stateList.add(cityname);
//                }
//            }
//
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
//
//        return stateList;
//    }
}