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
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.MatchMakingViewModel;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false);



        setProfileData();

        setNewMatchesRecyclerView();


        return binding.getRoot();
    }

    private void setNewMatchesRecyclerView() {

        newMatchesAdapter = new NewMatchesAdapter( new ArrayList<>(),getActivity().getApplication());
        layoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,true);


        layoutManager.setStackFromEnd(true);
        binding.recyclerNewMatches.setLayoutManager(layoutManager);
    }

    private void setProfileData() {


        fupViewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), userProfile -> {
            Toast.makeText(requireContext(), userProfile.getUserId(), Toast.LENGTH_SHORT).show();

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

        matchMakingViewModel= new ViewModelProvider(requireActivity()).get(MatchMakingViewModel.class);


    }


    @Override
    public void onResume() {
        super.onResume();


        matchMakingViewModel.getBestRecentMatchedProfiles().observe(getViewLifecycleOwner(), userProfiles -> {

            if (userProfiles!=null){



                binding.lineNewMatches.setVisibility(View.VISIBLE);
                binding.txtNewMatchesCount.setVisibility(View.VISIBLE);
                binding.txtNewMatchesCount.setText("(" +userProfiles.size() +")");

                newMatchesAdapter.setNewMatches(userProfiles);
            }




        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}