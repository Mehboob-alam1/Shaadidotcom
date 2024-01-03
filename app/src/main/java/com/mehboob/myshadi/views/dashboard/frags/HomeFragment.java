package com.mehboob.myshadi.views.dashboard.frags;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.mehboob.myshadi.viewmodel.NotificationViewModel;
import com.mehboob.myshadi.views.activities.AddBioActivity;
import com.mehboob.myshadi.views.activities.NOtificationsActivity;
import com.mehboob.myshadi.views.activities.ProfileDetailedActivity;
import com.mehboob.myshadi.views.activities.SetPreferencesActivity;
import com.mehboob.myshadi.views.dashboard.EditProfileActivity;
import com.mehboob.myshadi.views.dashboard.premium.UpgradePremiumActivity;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements LocationListener {


    private FragmentHomeBinding binding;

    private FUPViewModel fupViewModel;

    private MatchMakingViewModel matchMakingViewModel;

    private NotificationViewModel viewModel;

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

        binding.navNotifications.setOnClickListener(v -> {
            if (!sessionManager.fetchUserId().equals("null")) {
                startActivity(new Intent(requireActivity(), NOtificationsActivity.class));
            } else {
                Toast.makeText(requireActivity(), "Fetching your data", Toast.LENGTH_SHORT).show();
            }
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
                        userMatches.getUserId() + "_" + userProfileData.getUserId(), "Pending", false, userProfileData.getGender(), userMatches.getGender());
                matchMakingViewModel.sendNotification(connection, userMatches, userProfileData);

                matchMakingViewModel.getConnectionSent().observe(getViewLifecycleOwner(), aBoolean -> {
                    if (aBoolean) {
                        Toast.makeText(requireActivity(), "Connection sent", Toast.LENGTH_SHORT).show();


                        matchMakingViewModel.deleteUserMatches(userMatches);

                        matchMakingViewModel.insertConnection(connection);
                        userMatches.setConnectionSent(true);
                        newMatchesAdapter.notifyDataSetChanged();

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
        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);


    }


    @Override
    public void onResume() {
        super.onResume();

        matchMakingViewModel.getConnectedUserIds().observe(getViewLifecycleOwner(), connectedUserIds -> {
            Log.d("Connections", connectedUserIds.toString());


            matchMakingViewModel.getUserProfilesCreatedLastWeek().observe(getViewLifecycleOwner(), userMatches -> {


                List<UserMatches> filteredRecentMatches = new ArrayList<>();


                if (connectedUserIds != null) {
                    Toast.makeText(requireActivity(), "Running from here", Toast.LENGTH_SHORT).show();
                    for (UserMatches userProfile : userMatches) {
                        boolean isUserConnected = false;

                        // Check if the user ID is in the connectedUserIds list
                        for (Connection connection : connectedUserIds) {
                            if (connection.getConnectionToId().equals(userProfile.getUserId())) {
                                isUserConnected = true;
                                break; // No need to check further, user is connected
                            }
                        }

                        // If the user is not connected, add to the filtered list
                        if (!isUserConnected) {
                            filteredRecentMatches.add(userProfile);
                        }
                    }

                    // Use filteredRecentMatches for your UI or any further processing
                    newMatchesAdapter.setNewMatches(filteredRecentMatches);
                    newMatchesAdapter.notifyDataSetChanged();

                    binding.txtNewMatchesCount.setText("(" + filteredRecentMatches.size() + ")");
                } else {
                    Toast.makeText(requireActivity(), "Running from there", Toast.LENGTH_SHORT).show();


                    newMatchesAdapter.setNewMatches(userMatches);
                    newMatchesAdapter.notifyDataSetChanged();
                    binding.txtNewMatchesCount.setText("(" + userMatches.size() + ")");


                }
            });


        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());

        fupViewModel.updateLocation(latitude, longitude, sessionManager.fetchUserId());
    }
}