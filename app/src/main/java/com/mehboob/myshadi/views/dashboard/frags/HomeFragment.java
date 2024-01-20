package com.mehboob.myshadi.views.dashboard.frags;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.mehboob.myshadi.R;

import com.mehboob.myshadi.adapters.SliderAdapter;
import com.mehboob.myshadi.adapters.homeAdapters.NewMatchesAdapter;
import com.mehboob.myshadi.databinding.FragmentHomeBinding;

import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.model.Slider;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.room.entities.UserProfileData;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.MatchMakingViewModel;
import com.mehboob.myshadi.viewmodel.NotificationViewModel;
import com.mehboob.myshadi.views.activities.AccountSettingActivity;
import com.mehboob.myshadi.views.activities.AddBioActivity;
import com.mehboob.myshadi.views.activities.HelpSupportActivity;
import com.mehboob.myshadi.views.activities.NOtificationsActivity;
import com.mehboob.myshadi.views.activities.ProfileDetailedActivity;
import com.mehboob.myshadi.views.activities.SetPreferencesActivity;
import com.mehboob.myshadi.views.dashboard.EditProfileActivity;
import com.mehboob.myshadi.views.dashboard.premium.UpgradePremiumActivity;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements LocationListener {


    private FragmentHomeBinding binding;
    private ArrayList<Slider> list;

    private FUPViewModel fupViewModel;

    private MatchMakingViewModel matchMakingViewModel;

    private NotificationViewModel viewModel;
    private NavController navController;

    private NewMatchesAdapter newMatchesAdapter;
    private LinearLayoutManager layoutManager;

    private UserProfileData userProfileData;

    private SessionManager sessionManager;
    private SliderAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(requireActivity());


        //
        list=new ArrayList<>();


        setProfileData();

        setRecyclerView();

        fetchBanners();


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

        binding.lineRateApp.setOnClickListener(v -> {

            rateApp();
        });

        binding.btnUpgradeNow.setOnClickListener(view -> {

            Utils.safelyNavigate(navController, R.id.action_homeFragment_to_premiumFragment, savedInstanceState);

        });
        binding.btnSellAllNewMatch.setOnClickListener(v -> {


            Utils.safelyNavigate(navController, R.id.action_homeFragment_to_matchesFragment, savedInstanceState);


        });

        binding.lineAccountSettings.setOnClickListener(v -> {

           startActivity(new Intent(requireActivity(), AccountSettingActivity.class));
        });

        binding.lineHelpSupport.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), HelpSupportActivity.class));
        });

        return binding.getRoot();
    }

    private void rateApp() {

        Uri uri = Uri.parse("market://details?id=" + requireActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + requireActivity().getPackageName())));
        }
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
                        userMatches.getUserId() + "_" + userProfileData.getUserId(), "Pending", false, userProfileData.getGender(), userMatches.getGender(), userMatches.getImageUrl(), userMatches.getImageUrl(), userMatches.getFullName(), userProfileData.getFullName());
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


        fupViewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), userProfile -> {


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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        navController = Navigation.findNavController(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        matchMakingViewModel.getConnectedUserIds().observe(getViewLifecycleOwner(), connectedUserIds -> {
            Log.d("Connections", connectedUserIds.toString());


            matchMakingViewModel.getUserProfilesCreatedLastWeek().observe(getViewLifecycleOwner(), userMatches -> {


                List<UserMatches> filteredRecentMatches = new ArrayList<>();


                if (connectedUserIds != null) {
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
    private void setSlider() {
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSlider.setIndicatorSelectedColor(Color.WHITE);
        binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        binding.imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
        binding.imageSlider.startAutoCycle();

    }

    private void fetchBanners() {
   DatabaseReference  databaseReference= FirebaseDatabase.getInstance().getReference();


        databaseReference.child("Banners").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String imageUrl = snapshot1.child("imageUrl").getValue(String.class);
                        String imageLink = snapshot1.child("imageLink").getValue(String.class);
                        String pushId = snapshot1.child("pushId").getValue(String.class);

//                        Toast.makeText(StartScreen.this, ""+data, Toast.LENGTH_SHORT).show();
                        list.add(new Slider(imageLink, imageUrl, pushId));
                    }

                    adapter = new SliderAdapter(list,getContext());
                    binding.imageSlider.setSliderAdapter(adapter);
                    setSlider();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}