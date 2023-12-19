package com.mehboob.myshadi.views.dashboard.frags.matches;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.homeAdapters.MyMatchesAdapter;
import com.mehboob.myshadi.adapters.homeAdapters.NewMatchesAdapter;
import com.mehboob.myshadi.databinding.ActivityDashBoardBinding;
import com.mehboob.myshadi.databinding.FragmentHomeBinding;
import com.mehboob.myshadi.databinding.FragmentMyMatchesBinding;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.MatchMakingViewModel;

import java.util.ArrayList;


public class MyMatchesFragment extends Fragment {


    private FragmentMyMatchesBinding binding;


    private FUPViewModel fupViewModel;

    private MatchMakingViewModel matchMakingViewModel;

    private MyMatchesAdapter myMatchesAdapter;
    private LinearLayoutManager layoutManager;

    private UserProfile userProfileData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fupViewModel = new ViewModelProvider(requireActivity()).get(FUPViewModel.class);

        matchMakingViewModel = new ViewModelProvider(requireActivity()).get(MatchMakingViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentMyMatchesBinding.inflate(inflater, container, false);


        fupViewModel.getUserProfileLiveData().observe(requireActivity(), userProfile -> {
            userProfileData = userProfile;
        });


        setMatchesRecyclerView();

        if (userProfileData != null)
            matchMakingViewModel.checkMyProfileMatches(userProfileData);

        return binding.getRoot();
    }

    private void setMatchesRecyclerView() {

        myMatchesAdapter = new MyMatchesAdapter(new ArrayList<>(), getActivity().getApplication());
        layoutManager = new LinearLayoutManager(requireContext());


        layoutManager.setStackFromEnd(true);
        binding.myMatchesRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();


        matchMakingViewModel.getMyProfileMatches().observe(getViewLifecycleOwner(), userProfiles -> {

            if (userProfiles != null) {


                myMatchesAdapter.setMyMatches(userProfiles);
                binding.myMatchesRecyclerView.setVisibility(View.VISIBLE);
                binding.lineNoData.getRoot().setVisibility(View.GONE);
            }else{
                binding.myMatchesRecyclerView.setVisibility(View.GONE);
                binding.lineNoData.getRoot().setVisibility(View.VISIBLE);
            }


        });
    }
}