package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityHelpSupportBinding;
import com.mehboob.myshadi.model.HelpRequest;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;

import java.util.UUID;

public class HelpSupportActivity extends AppCompatActivity {
private ActivityHelpSupportBinding binding;
private SessionManager sessionManager;
String pushId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        binding=ActivityHelpSupportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager= new SessionManager(this);

        binding.imgBack.setOnClickListener(v -> {
            finish();
        });
pushId= UUID.randomUUID().toString();

        binding.btnSubmit.setOnClickListener(v -> {
            if (binding.etTypeOfIssue.getText().toString().isEmpty()){
                Utils.showSnackBar(this,"Specify your issue");
                binding.etTypeOfIssue.requestFocus();
            }else if (binding.etSubjet.getText().toString().isEmpty()){
                Utils.showSnackBar(this,"Specify the subject");
                binding.etSubjet.requestFocus();

            }else if(binding.etDescription.getText().toString().isEmpty()){
                Utils.showSnackBar(this,"Add your description");
                binding.etDescription.requestFocus();
            }else{
                HelpRequest helpRequest= new HelpRequest(sessionManager.fetchUserId(),
                        sessionManager.fetchFullName(),sessionManager.fetchGender(),
                        pushId,binding.etTypeOfIssue.getText().toString(),
                        binding.etSubjet.getText().toString(),
                        binding.etDescription.getText().toString());
                sendRequestToAdmin(helpRequest);
            }
        });
    }

    private void sendRequestToAdmin(HelpRequest helpRequest) {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("HelpRequest");
        reference.child(helpRequest.getUserId())
                .child(helpRequest.getPushId())
                .setValue(helpRequest)
                .addOnCompleteListener(task -> {
                    if (task.isComplete() && task.isSuccessful()){

                     Utils.showSnackBar(this,"Your request has been added");
                    }else{
                        Utils.showSnackBar(this,"Something went wrong");
                    }
                }).addOnFailureListener(e -> {
                    Utils.showSnackBar(this,e.getLocalizedMessage());
                });
    }
}