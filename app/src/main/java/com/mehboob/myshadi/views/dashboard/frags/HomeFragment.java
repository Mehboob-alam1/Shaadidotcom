package com.mehboob.myshadi.views.dashboard.frags;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mehboob.myshadi.R;

import com.mehboob.myshadi.adapters.homeAdapters.NewMatchesAdapter;
import com.mehboob.myshadi.databinding.FragmentHomeBinding;
import com.mehboob.myshadi.model.profilemodel.Preferences;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.MatchMakingViewModel;
import com.mehboob.myshadi.views.activities.AddBioActivity;
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

    private UserProfile userProfileData;

    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(requireActivity());


        getProfileUpdates(sessionManager.fetchUserId());


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
        binding.recyclerNewMatches.setLayoutManager(new LinearLayoutManager(requireActivity()));
    }


    private void getProfileUpdates(String userId) {


        fupViewModel.getProfile(userId);
    }

    private void setProfileData() {


        fupViewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), userProfile -> {

            userProfileData = userProfile;

//            Toast.makeText(requireContext(), userProfile.getUserId(), Toast.LENGTH_SHORT).show();

            Glide.with(requireContext()).load(userProfile.getImageUrl())
                    .placeholder(R.drawable.profile_1)
                    .into(binding.profileImage);
            binding.txtName.setText(userProfile.getFullName());
            binding.txtRegNo.setText(Utils.extractFirst8Characters(userProfile.getUserId()));
            if (userProfile.getIsVerified()) {
                binding.imgVerified.setImageResource(R.drawable.baseline_verified_user_24);
            }

            binding.txtAccountType.setText(userProfile.getAccountType());

            binding.btnEditProfile.setOnClickListener(view -> {
                startActivity(new Intent(requireActivity(), EditProfileActivity.class));
            });
            binding.btnUpgradeNow.setOnClickListener(view -> {
                startActivity(new Intent(requireActivity(), UpgradePremiumActivity.class));
            });
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