package com.mehboob.myshadi.views.dashboard.frags.matches;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.homeAdapters.MyMatchesAdapter;
import com.mehboob.myshadi.adapters.homeAdapters.NewMatchesAdapter;
import com.mehboob.myshadi.databinding.ActivityDashBoardBinding;
import com.mehboob.myshadi.databinding.FragmentHomeBinding;
import com.mehboob.myshadi.databinding.FragmentMyMatchesBinding;
import com.mehboob.myshadi.model.profilemodel.Preferences;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.entities.UserProfileData;
import com.mehboob.myshadi.utils.MatchPref;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.MatchMakingViewModel;
import com.mehboob.myshadi.views.activities.ProfileDetailedActivity;

import java.util.ArrayList;


public class MyMatchesFragment extends Fragment {


    private FragmentMyMatchesBinding binding;


    private FUPViewModel fupViewModel;

    private MatchMakingViewModel matchMakingViewModel;

    private MyMatchesAdapter myMatchesAdapter;
    private LinearLayoutManager layoutManager;

    private UserProfileData userProfileData;
    MatchPref matchPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fupViewModel = new ViewModelProvider(requireActivity()).get(FUPViewModel.class);

        matchMakingViewModel = new ViewModelProvider(requireActivity()).get(MatchMakingViewModel.class);
        matchPref = new MatchPref(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentMyMatchesBinding.inflate(inflater, container, false);


        fupViewModel.getUserProfileLiveData().observe(requireActivity(), userProfile -> {
            userProfileData = userProfile;
        });

        setRecyclerView();


        myMatchesAdapter.setOnItemClickListener((userProfile, position) -> {
            //
        });
        return binding.getRoot();
    }

    private void setRecyclerView() {

        myMatchesAdapter = new MyMatchesAdapter(new ArrayList<>(), requireActivity());
        binding.myMatchesRecyclerView.setAdapter(myMatchesAdapter);
        binding.myMatchesRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        myMatchesAdapter.setOnItemClickListener((userProfile, position) -> {
            Intent i = new Intent(requireContext(), ProfileDetailedActivity.class);
            i.putExtra("currentPerson", new Gson().toJson(userProfile));
            startActivity(i);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        matchMakingViewModel.getBestMatchesPref(Integer.parseInt(matchPref.fetchPref().getMinAge()), Integer.parseInt(matchPref.fetchPref().getMaxAge())).observe(this, userMatches -> {
            if (userMatches != null && userMatches.size() != 0) {
                binding.lineNoData.getRoot().setVisibility(View.GONE);
                binding.myMatchesRecyclerView.setVisibility(View.VISIBLE);
            }
            myMatchesAdapter.setMyMatches(userMatches);

            Toast.makeText(requireActivity(), "" + userMatches.toString(), Toast.LENGTH_SHORT).show();

        });
    }
}