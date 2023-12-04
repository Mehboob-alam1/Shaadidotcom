package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.mehboob.myshadi.utils.Utils;

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

    String[] religiousDenominations = {
            // Christianity
            "Catholicism",
            "Anglicanism",
            "Lutheranism",
            "Presbyterianism",
            "Baptist",
            "Methodist",
            "Pentecostalism",
            "Eastern Orthodoxy",
            "Oriental Orthodoxy",
            "Mormonism",
            "Jehovah's Witnesses",
            // Islam
            "Sunni Islam",
            "Hanafi",
            "Maliki",
            "Shafi'i",
            "Hanbali",
            "Brelvi",
            "Deobandi",
            "Shia Islam",
            "Twelver (Ithna Ashari)",
            "Ismaili",
            "Zaidi",

            "Sufism",
            // Judaism
            "Orthodox Judaism",
            "Hasidic Judaism",
            "Modern Orthodox",
            "Conservative Judaism",
            "Reform Judaism",
            "Reconstructionist Judaism",
            // Hinduism
            "Vaishnavism",
            "Shaivism",
            "Shaktism",
            "Smartism",
            "Advaita Vedanta",
            "Dvaita Vedanta",
            "Vishishtadvaita",
            // Buddhism
            "Theravada Buddhism",
            "Mahayana Buddhism",
            "Zen Buddhism",
            "Pure Land Buddhism",
            "Tibetan Buddhism",
            "Vajrayana Buddhism",
            // Sikhism
            "Sikhism",
            // Jainism
            "Digambara",
            "Svetambara"
            // Add more denominations as needed
    };

    private SessionManager sessionManager;

    private String TAG = "StateActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStateCityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(this);


        loadStates(sessionManager.fetchCountryCode());

        setSubCommunities();

        binding.btnContinue.setOnClickListener(view -> {
            if (binding.txtState.getSelectedItem() == null) {

                Utils.showSnackBar(this, "Add your state");

            } else if (binding.txtCommunity.getSelectedItem() == null) {
                Utils.showSnackBar(this, "Add your community");
            } else if (binding.etCity.getText().toString().isEmpty()) {
                Utils.showSnackBar(this, "Add your city");
            } else {
                // start activity

                sessionManager.saveCityName(binding.etCity.getText().toString());

                startActivity(new Intent(StateCityActivity.this, MartialStatusActivity.class));
            }
        });


    }

    private void setSubCommunities() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, religiousDenominations);
//

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.txtCommunity.setAdapter(adapter);
        binding.txtCommunity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sessionManager.saveSubCommunity(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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


}