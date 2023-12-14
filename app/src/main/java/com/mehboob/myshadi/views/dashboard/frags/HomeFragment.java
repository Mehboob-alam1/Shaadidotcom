package com.mehboob.myshadi.views.dashboard.frags;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mehboob.myshadi.R;

import com.mehboob.myshadi.databinding.FragmentHomeBinding;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.views.dashboard.EditProfileActivity;
import com.mehboob.myshadi.views.dashboard.premium.UpgradePremiumActivity;


public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;

    private FUPViewModel fupViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false);



        setProfileData();


        return binding.getRoot();
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

    }
}