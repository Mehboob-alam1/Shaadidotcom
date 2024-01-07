package com.mehboob.myshadi.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityConnectionRecivedDetailedBinding;
import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.viewmodel.ConnectionViewModel;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.NotificationViewModel;
import com.mehboob.myshadi.views.dashboard.frags.InboxFragment;

import java.lang.reflect.Type;

public class ConnectionRecivedDetailedActivity extends AppCompatActivity {
    private ActivityConnectionRecivedDetailedBinding binding;
    private UserMatches userMatches;
    private FUPViewModel fupViewModel;
    private SessionManager sessionManager;
    private ConnectionViewModel connectionViewModel;
    private NotificationViewModel notificationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnectionRecivedDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        fupViewModel = new ViewModelProvider(this).get(FUPViewModel.class);
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        connectionViewModel = new ViewModelProvider(this).get(ConnectionViewModel.class);
        sessionManager = new SessionManager(this);
        binding.btnBackProfile.setOnClickListener(v -> {
            finish();
        });

        Type type = new TypeToken<UserMatches>() {
        }.getType();
        userMatches = new Gson().fromJson(getIntent().getStringExtra("currentPerson"), type);

        fupViewModel.getProfile(sessionManager.fetchUserId());

        bindData(userMatches);


        binding.btnBackProfile.setOnClickListener(v -> {
            finish();
        });


    }

    private void bindData(UserMatches userMatches) {


        try {
            Glide.with(this).load(userMatches.getImageUrl())
                    .placeholder(R.drawable.img_no_internet)
                    .into(binding.imgProfileImageM)
            ;

        } catch (Exception e) {
            Log.d("Exception", e.getLocalizedMessage());
        }

        if (userMatches.isVerified()) {
            binding.imgVerifiedM.setVisibility(View.VISIBLE);

        }
        fupViewModel.getUserProfileLiveData().observe(this, userProfileData -> {
            if (userProfileData != null) {
                checkIfIAlsoSentConnection(userProfileData.getUserId(), userMatches.getUserId());


                binding.btnAcceptConnection.setOnClickListener(v -> {

                    // setBothUserConnected
                    binding.progressBar.setVisibility(View.VISIBLE);

                    Connection connection = new Connection(userProfileData.getUserId(),
                            userMatches.getUserId(), String.valueOf(System.currentTimeMillis()),
                            userMatches.getUserId() + "_" + userProfileData.getUserId(), "Connected", false, userProfileData.getGender(), userMatches.getGender(), userProfileData.getImageUrl(), userMatches.getImageUrl(), userMatches.getFullName(), userProfileData.getFullName());
                    connectionViewModel.connectBothUsers(connection);


                    notificationViewModel.getNotificationDeleted().observe(this, aBoolean1 -> {

                        if (aBoolean1) {
                            binding.progressBar.setVisibility(View.GONE);
                            Utils.showSnackBar(this, "Accepted the invitation");
                            finish();
                        } else {
                            Utils.showSnackBar(this, "Try again");
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    });

                });
                try {


                    Glide.with(this)
                            .load(userProfileData.getImageUrl())
                            .placeholder(R.drawable.profile)
                            .into(binding.imgMyProfile);
                } catch (Exception e) {
                    Log.d("Exception", e.getLocalizedMessage());
                }
                if (userProfileData.isVerified()) {
                    binding.txtBirthDate.setText(userMatches.getDate_of_birth());
                    binding.txtBirthDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    binding.txtContact.setText(userMatches.getPhoneNumber());
                    binding.txtContact.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    binding.txtEmail.setText(userMatches.getEmail());
                    binding.txtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    binding.txtWorkAsWorkandWith.setText(userMatches.getWorkAs() + ", " + userMatches.getWorksWith());
                    binding.txtWorkAsWorkandWith.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    binding.txtCollegeName.setText(userMatches.getCollege());
                    binding.txtCollegeName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                } else {
                }

            }

        });
        // if this current user is verified

        binding.txtContact.setText(extractPhoneNumber(userMatches.getPhoneNumber()));
        binding.txtIncome.setText(userMatches.getIncome());
        binding.txtProfileNameM.setText(userMatches.getFullName());
        binding.txtProfileAgeHeightM.setText(userMatches.getDob() + " yrs," + userMatches.getHeight());
        binding.txtProfileProfessionM.setText(userMatches.getWorkAs());
        binding.txtCommunitySubCommunityM.setText(userMatches.getCommunity() + " ," + userMatches.getSubCommunity());
        binding.txtCityCountryM.setText(userMatches.getCityName() + "," + userMatches.getLivingIn());
        binding.txtAboutPerson.setText("About " + userMatches.getFullName());
        binding.txtAboutDescPerson.setText(userMatches.getAboutMe());
        binding.txtCreatedBySelf.setText("Created by Self");
        binding.txtId.setText("ID: " + userMatches.getUserId());
        binding.txtAge.setText(userMatches.getDob() + " yrs old");
        binding.txtHeight.setText("Height- " + userMatches.getHeight());
        binding.txtMaritalStatus.setText(userMatches.getMaritalStatus());
        binding.txtLivesCityStateCountry.setText(userMatches.getCityName() + ", " + userMatches.getStateName() + ", " + userMatches.getLivingIn());
        binding.txtReligionCommunity.setText(userMatches.getReligion() + ", " + userMatches.getCommunity());
        binding.txtSubCommunity.setText(userMatches.getSubCommunity());
        binding.txtDiet.setText(userMatches.getDiet());

        if (userMatches.getGender().equals("Male")) {
            binding.txtYouAndPersonM.setText("You and him");
            binding.txtYouAndPerson.setText("You and him");
        } else {
            binding.txtYouAndPersonM.setText("You and her");
            binding.txtYouAndPerson.setText("You and her");
        }

        Glide.with(this)
                .load(userMatches.getImageUrl())
                .placeholder(R.drawable.profile)
                .into(binding.imgPersonProfile);


        connectionViewModel.getConnected().observe(this, aBoolean -> {
            if (aBoolean) {
                //User have connected

                notificationViewModel.deleteNotification(sessionManager.fetchUserId(), userMatches.getUserId());


                // List of all connected users
            } else {
                Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private String extractPhoneNumber(String phoneNumber) {


        // Extract the relevant parts of the phone number
        String countryCode = phoneNumber.substring(0, 3);
        String operatorCode = phoneNumber.substring(3, 6);
        String remainingDigits = phoneNumber.substring(6);

        // Create the formatted phone number
        String formattedPhoneNumber = countryCode + operatorCode + "******";

        return formattedPhoneNumber;
    }


    public void checkIfIAlsoSentConnection(String myUserId, String thisUserId) {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ConnectionIRecieved");

        ref.child(myUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(thisUserId).exists()) {
                    binding.txtDidYou.setText("You also send connect to this profile");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}