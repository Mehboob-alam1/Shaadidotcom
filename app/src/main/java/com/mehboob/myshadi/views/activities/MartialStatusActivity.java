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
    String[] heights = {"Select",
            "4ft 5in - 134cm",
            "4ft 6in - 137cm",
            "4ft 7in - 140cm",
            "4ft 8in - 142cm",
            "4ft 9in - 145cm",
            "4ft 10in - 147cm",
            "4ft 11in - 150cm",
            "5ft - 152cm",
            "5ft 1in - 155cm",
            "5ft 2in - 157cm",
            "5ft 3in - 160cm",
            "5ft 4in - 163cm",
            "5ft 5in - 165cm",
            "5ft 6in - 168cm",
            "5ft 7in - 170cm",
            "5ft 8in - 173cm",
            "5ft 9in - 175cm",
            "5ft 10in - 178cm",
            "5ft 11in - 180cm",
            "6ft - 183cm",
            "6ft 1in - 185cm",
            "6ft 2in - 188cm",
            "6ft 3in - 191cm",
            "6ft 4in - 193cm",
            "6ft 5in - 196cm",
            "6ft 6in - 198cm",
            "6ft 7in - 201cm",
            "6ft 8in - 203cm",
            "6ft 9in - 206cm",
            "6ft 10in - 208cm",
            "6ft 11in - 211cm",
            "7ft - 213cm"
    };

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

        binding.btnContinue.setOnClickListener(view -> {
            if (binding.spinnerMaritalStatus.getSelectedItem().equals("Select")) {
                Utils.showSnackBar(this, "Select a valid marital status");
            } else if (binding.spinnerChildren.getSelectedItem().equals("Select")) {
                Utils.showSnackBar(this, "Please select a valid one");
            } else if (binding.spinnerHeight.getSelectedItem().equals("Select")) {
                Utils.showSnackBar(this, "Select a valid height");
            } else if (binding.spinnerHeight.getSelectedItem().equals("Select")) {
                Utils.showSnackBar(this, "Add a valid diet");
            } else {
                startActivity(new Intent(MartialStatusActivity.this, QualificationActivity.class));
            }
        });

    }

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


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, heights);

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