package com.mehboob.myshadi.views.dashboard.frags.matches;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.homeAdapters.MyMatchesAdapter;
import com.mehboob.myshadi.adapters.homeAdapters.NearMeAdapter;
import com.mehboob.myshadi.adapters.homeAdapters.NewMatchesAdapter;
import com.mehboob.myshadi.adapters.homeAdapters.NewMatchesAdapterHome;
import com.mehboob.myshadi.databinding.FragmentNearMeBinding;
import com.mehboob.myshadi.databinding.FragmentNewBinding;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.utils.MatchPref;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.MatchMakingViewModel;

import java.util.ArrayList;


public class NewFragment extends Fragment {


    private FragmentNewBinding binding;
    private FUPViewModel fupViewModel;

    private MatchMakingViewModel matchMakingViewModel;

    private NewMatchesAdapterHome adapter;

    MatchPref matchPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fupViewModel = new ViewModelProvider(requireActivity()).get(FUPViewModel.class);

        matchMakingViewModel = new ViewModelProvider(requireActivity()).get(MatchMakingViewModel.class);
        matchPref=new MatchPref(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewBinding.inflate(inflater, container, false);
        setRecyclerView();




        return binding.getRoot();



    }

    private void setRecyclerView() {

        adapter = new NewMatchesAdapterHome(new ArrayList<>(), requireActivity());
        binding.myMatchesRecyclerView.setAdapter(adapter);
        binding.myMatchesRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();

        matchMakingViewModel.getUserProfilesCreatedLastWeek().observe(this, userMatches -> {
            if (userMatches != null && userMatches.size() != 0) {
                binding.lineNoData.getRoot().setVisibility(View.GONE);
                binding.myMatchesRecyclerView.setVisibility(View.VISIBLE);
            }
            adapter.setMyMatches(userMatches);
            Toast.makeText(requireContext(), ""+userMatches.toString(), Toast.LENGTH_SHORT).show();

        });
    }
}