package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityQualificationBinding;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;

public class QualificationActivity extends AppCompatActivity {
    private ActivityQualificationBinding binding;

    String[] qualifications = {
            "Diploma",
            "Associate Degree",
            "B.E/B.Tech",
            "B.Sc",
            "B.A",
            "B.Com",
            "BBA",
            "B.Arch",
            "LLB",
            "B.Ed",
            "MBBS",
            "B.Pharm",
            "BDS",
            "B.Voc",
            "BFA",
            "BHM",
            "BCA",
            "BSW",
            "B.Des",
            "B.Plan",
            "BAMS",
            "BHMS",
            "BPT",
            "BOT",
            "BMLT",
            "BCJ",
            "BBA LLB",
            "B.Com LLB",
            "B.Tech + M.Tech",
            "B.Tech + MBA",
            "Integrated M.Sc",
            "Integrated MBA",
            "Integrated LLB",
            "Integrated B.Tech",
            "M.E/M.Tech",
            "M.Sc",
            "M.A",
            "M.Com",
            "MBA",
            "MCA",
            "LLM",
            "MDS",
            "MD",
            "MS",
            "M.Pharm",
            "M.Voc",
            "MFA",
            "MHM",
            "M.Des",
            "M.Plan",
            "M.Arch",
            "MPT",
            "MOT",
            "MMLT",
            "M.Com LLB",
            "MBA + PhD",
            "Ph.D"
    };

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQualificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);


        setSpinnerQualifications();

        binding.btnContinue.setOnClickListener(view -> {

            if (binding.etCollege.getText().toString().isEmpty()) {
                Utils.showSnackBar(this, "Add college you attended");
            } else if (binding.spinnerHighestQualification.getSelectedItem() == null) {
                Utils.showSnackBar(this, "What is highest qualification");
            } else {
                sessionManager.saveCollege(binding.etCollege.getText().toString());
                startActivity(new Intent(QualificationActivity.this, IncomeDetailsActivity.class));
            }
        });
    }

    private void setSpinnerQualifications() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, qualifications);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerHighestQualification.setAdapter(adapter);

        binding.spinnerHighestQualification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                sessionManager.saveQualifications(adapterView.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}