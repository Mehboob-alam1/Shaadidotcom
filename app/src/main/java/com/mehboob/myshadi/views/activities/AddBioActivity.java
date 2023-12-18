package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityAddBioBinding;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.viewmodel.FUPViewModel;

public class AddBioActivity extends AppCompatActivity {

    private ActivityAddBioBinding binding;

    private SessionManager sessionManager;
    private FUPViewModel fupViewModel;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddBioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        fupViewModel = new ViewModelProvider(this).get(FUPViewModel.class);

        sessionManager = new SessionManager(this);

        binding.btnBackAboutMe.setOnClickListener(view -> finish());


        binding.btnSaveAboutMe.setOnClickListener(view -> {
            progressDialog.show();
            if (binding.etTellmeAbout.getText().toString().isEmpty()) {
                Utils.showSnackBar(this, "Add about your self");
            } else {

                fupViewModel.updateAboutMe(binding.etTellmeAbout.getText().toString(), sessionManager.fetchUserId());
                sessionManager.saveAboutMe(binding.etTellmeAbout.getText().toString());

                fupViewModel.getIsBioUpdated().observe(this, aBoolean -> {


                    if (aBoolean) {
                        progressDialog.dismiss();
                        Utils.showSnackBar(this, "Bio updated");
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Utils.showSnackBar(this, "Something went wrong !");
                    }
                });


            }
        });
    }
}