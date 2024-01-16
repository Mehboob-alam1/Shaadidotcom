package com.mehboob.myshadi.views.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityEditProfileBinding;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.views.activities.AddBioActivity;
import com.mehboob.myshadi.views.activities.EditBasicInfoActivity;
import com.mehboob.myshadi.views.dashboard.premium.UpgradePremiumActivity;

public class EditProfileActivity extends AppCompatActivity {
    private ActivityEditProfileBinding binding;
    private FUPViewModel fupViewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager= new SessionManager(this);
        fupViewModel = new ViewModelProvider(this).get(FUPViewModel.class);

        fupViewModel.getProfile(sessionManager.fetchUserId());

        setProfileData();
        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setProfileData() {


        fupViewModel.getUserProfileLiveData().observe(this, userProfile -> {


            try {


                Glide.with(this).load(userProfile.getImageUrl())
                        .placeholder(R.drawable.profile_1)
                        .into(binding.circleImageView);
                binding.txtName.setText(userProfile.getFullName());
                binding.txtRegNo.setText(Utils.extractFirst8Characters(userProfile.getUserId()));
                if (userProfile.isVerified()) {
                    binding.btnUpgradeNow.setVisibility(View.GONE);
                    binding.imgVerified.setImageResource(R.drawable.baseline_verified_user_24);
                }

                binding.txtAccountType.setText(userProfile.getAccountType());

                binding.txtAboutMeDescription.setText(userProfile.getAboutMe());
                binding.txtBasicInfo.setText("Profile for\t\t\t\t" + userProfile.getProfileFor() + "\n"
                        + "Age\t\t\t\t" + userProfile.getDob() + "\n" +
                        "Marital status\t\t\t\t" + userProfile.getMaritalStatus() + "\n" +
                        "Height\t\t\t\t" + userProfile.getHeight() + "\n" +
                        "Relgion\t\t\t\t" + userProfile.getReligion() + "\n" +
                        "Community\t\t\t\t" + userProfile.getCommunity() + "\n" +
                        "Sub Community\t\t\t\t" + userProfile.getSubCommunity() + "\n" +
                        "Country\t\t\t\t" + userProfile.getLivingIn() + "\n" +
                        "State\t\t\t\t" + userProfile.getStateName() + "\n" +
                        "City\t\t\t\t" + userProfile.getCityName() + "\n" +
                        "Qualification\t\t\t\t" + userProfile.getQualifications() + "\n" +
                        "Works with\t\t\t\t" + userProfile.getWorksWith() + "\n" +
                        "Works as\t\t\t\t" + userProfile.getWorkAs() + "\n" +
                        "Annual Income\t\t\t\t" + userProfile.getIncome() + "\n" +
                        "Diet\t\t\t\t" + userProfile.getDiet());

                binding.btnEditBasicInfo.setOnClickListener(v -> {

                    Intent i = new Intent(this, EditBasicInfoActivity.class);
                    i.putExtra("user",new Gson().toJson(userProfile));
                    startActivity(i);

                });
            } catch (NullPointerException e) {
                Log.d("ProfileReadingException", e.getLocalizedMessage());
            }


            binding.btnEditBio.setOnClickListener(view -> {
                startActivity(new Intent(this, AddBioActivity.class));
            });
            binding.btnUpgradeNow.setOnClickListener(view -> {
                startActivity(new Intent(this, UpgradePremiumActivity.class));
            });



        });


    }

}