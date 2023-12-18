package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityMartialStatusBinding;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MartialStatusActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private List<String> cities;
    private ActivityMartialStatusBinding binding;
    private String[] maritalStatus = {"Select", "Never Married", "Divorced", "Widowed", "Awaiting Divorce", "Annulled"};
    private String[] children = {"Select", "No", "Yes,Living together", "Yes,Not Living together"};


    private String[] diet = {"Select", "Veg", "Non-Veg", "Occasionally Non-Veg", "Eggetarian", "Jain", "Vegan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMartialStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(this);


        setMaritalStatus();
        setChildrenSpinner();
        setHeightSpinner();
        setDietSpinner();
        binding.imgBack.setOnClickListener(view -> finish());

        binding.btnContinue.setOnClickListener(view -> {
            if (binding.spinnerMaritalStatus.getSelectedItem().equals("Select")) {
                Utils.showSnackBar(this, "Select a valid marital status");
            } else if (binding.spinnerHeight.getSelectedItem().equals("Select height")) {
                Utils.showSnackBar(this, "Select a valid height");
            } else if (binding.spinnerDiet.getSelectedItem().equals("Select")) {
                Utils.showSnackBar(this, "Add a valid diet");
            } else if (!binding.spinnerMaritalStatus.getSelectedItem().equals("Never Married") && binding.spinnerChildren.getSelectedItem().equals("Select")) {
                Utils.showSnackBar(this, "Do you have children?");
            } else {
                startActivity(new Intent(MartialStatusActivity.this, QualificationActivity.class));
            }
        });

    }

    //else if (binding.spinnerChildren.getSelectedItem().equals("Select")) {
//        Utils.showSnackBar(this, "Please select a valid one");
//    }
    private void setDietSpinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, diet);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerDiet.setAdapter(adapter);

        binding.spinnerDiet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sessionManager.saveDiet(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setHeightSpinner() {


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.heights,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerHeight.setAdapter(adapter);

        binding.spinnerHeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sessionManager.saveHeight(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setChildrenSpinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, children);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerChildren.setAdapter(adapter);

        binding.spinnerChildren.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sessionManager.saveChildren(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setMaritalStatus() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, maritalStatus);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerMaritalStatus.setAdapter(adapter);

        binding.spinnerMaritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!adapterView.getSelectedItem().equals("Never Married")) {
                    binding.lineChildren.setVisibility(View.VISIBLE);
                    binding.spinnerChildren.setSelection(0);

                } else {
                    binding.lineChildren.setVisibility(View.GONE);

                }
                sessionManager.saveMaritalStatus(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


}