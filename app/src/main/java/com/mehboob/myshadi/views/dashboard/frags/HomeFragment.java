package com.mehboob.myshadi.views.dashboard.frags;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.mehboob.myshadi.R;

import com.mehboob.myshadi.adapters.homeAdapters.NewMatchesAdapter;
import com.mehboob.myshadi.databinding.FragmentHomeBinding;

import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.room.entities.UserProfileData;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.MatchMakingViewModel;
import com.mehboob.myshadi.views.activities.AddBioActivity;
import com.mehboob.myshadi.views.activities.ProfileDetailedActivity;
import com.mehboob.myshadi.views.activities.SetPreferencesActivity;
import com.mehboob.myshadi.views.dashboard.EditProfileActivity;
import com.mehboob.myshadi.views.dashboard.premium.UpgradePremiumActivity;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;

    private FUPViewModel fupViewModel;

    private MatchMakingViewModel matchMakingViewModel;

    private NewMatchesAdapter newMatchesAdapter;
    private LinearLayoutManager layoutManager;

    private UserProfileData userProfileData;

    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(requireActivity());


        //


        setProfileData();

        setRecyclerView();

        binding.lineAboutYourSelf.setOnClickListener(view -> {

            startActivity(new Intent(requireActivity(), AddBioActivity.class));
        });


        binding.linePartnerPref.setOnClickListener(view -> {

            Intent i = new Intent(requireActivity(), SetPreferencesActivity.class);
            i.putExtra("skip", true);
            startActivity(i);

        });


        return binding.getRoot();
    }

    private void setRecyclerView() {

        newMatchesAdapter = new NewMatchesAdapter(new ArrayList<>(), requireActivity());
        binding.recyclerNewMatches.setAdapter(newMatchesAdapter);
        binding.recyclerNewMatches.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));

        newMatchesAdapter.setOnItemClickListener((userProfile, position) -> {
            Intent i = new Intent(requireContext(), ProfileDetailedActivity.class);
            i.putExtra("currentPerson", new Gson().toJson(userProfile));
            startActivity(i);
        });

        newMatchesAdapter.setOnConnectClickListener((userMatches, position) -> {
            fupViewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), userProfileData -> {
                Connection connection = new Connection(userProfileData.getUserId(),
                        userMatches.getUserId(), String.valueOf(System.currentTimeMillis()),
                        userMatches.getUserId() + "_" + userProfileData.getUserId(),"Pending",false,userProfileData.getGender(),userMatches.getGender());
                matchMakingViewModel.sendNotification(connection, userMatches, userProfileData);

                matchMakingViewModel.getConnectionSent().observe(getViewLifecycleOwner(), aBoolean -> {
                    if (aBoolean) {
                        Toast.makeText(requireActivity(), "Connection sent", Toast.LENGTH_SHORT).show();
                        matchMakingViewModel.getUserProfilesCreatedLastWeek().observe(getViewLifecycleOwner(), new Observer<List<UserMatches>>() {
                            @Override
                            public void onChanged(List<UserMatches> userMatches) {
                                userMatches.get(position).setConnectionSent(true);
                                userMatches.remove(position);
                                newMatchesAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        Toast.makeText(requireActivity(), "Connection not sent ", Toast.LENGTH_SHORT).show();
                    }
                });
            });

        });
    }


    private void setProfileData() {


        fupViewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), new Observer<UserProfileData>() {
            @Override
            public void onChanged(UserProfileData userProfile) {


                try {


                    Glide.with(requireActivity()).load(userProfile.getImageUrl())
                            .placeholder(R.drawable.profile_1)
                            .into(binding.profileImage);
                    binding.txtName.setText(userProfile.getFullName());
                    binding.txtRegNo.setText(Utils.extractFirst8Characters(userProfile.getUserId()));
                    if (userProfile.isVerified()) {
                        binding.imgVerified.setImageResource(R.drawable.baseline_verified_user_24);
                    }

                    binding.txtAccountType.setText(userProfile.getAccountType());

                } catch (NullPointerException e) {
                    Log.d("ProfileReadingException", e.getLocalizedMessage());
                }

                binding.btnEditProfile.setOnClickListener(view -> {
                    startActivity(new Intent(requireActivity(), EditProfileActivity.class));
                });
                binding.btnUpgradeNow.setOnClickListener(view -> {
                    startActivity(new Intent(requireActivity(), UpgradePremiumActivity.class));
                });

                try {


                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(task -> {
                                if (!task.isSuccessful()) {
                                    Log.d("token", "Fetching FCM registration token failed");
                                    return;
                                }

                                // Get new FCM registration token
                                String token = task.getResult();

                                fupViewModel.updateToken(token, sessionManager.fetchUserId());
                                sessionManager.saveToken(token);
                                // Log and toast

                                Log.d("token", "The token is " + token);
                            });
                } catch (Exception e) {
                    Log.d("Exception", e.getLocalizedMessage());
                }
            }
        });


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fupViewModel = new ViewModelProvider(requireActivity()).get(FUPViewModel.class);

        matchMakingViewModel = new ViewModelProvider(requireActivity()).get(MatchMakingViewModel.class);


    }


    @Override
    public void onResume() {
        super.onResume();

        matchMakingViewModel.getUserProfilesCreatedLastWeek().observe(this, userMatches -> {

            newMatchesAdapter.setNewMatches(userMatches);

            binding.txtNewMatchesCount.setText("(" + userMatches.size() + ")");


        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}