package com.mehboob.myshadi.views.dashboard.frags.matches;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.homeAdapters.ShortlistedAdapter;
import com.mehboob.myshadi.databinding.FragmentHomeBinding;
import com.mehboob.myshadi.databinding.FragmentShortListedBinding;
import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.MatchMakingViewModel;
import com.mehboob.myshadi.viewmodel.ShortlistViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class ShortListedFragment extends Fragment {

    private FragmentShortListedBinding binding;

    private ShortlistViewModel viewModel;
    private MatchMakingViewModel matchMakingViewModel;

    private ShortlistedAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ShortlistViewModel.class);
        matchMakingViewModel = new ViewModelProvider(requireActivity()).get(MatchMakingViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentShortListedBinding.inflate(inflater, container, false);


        setRecyclerView();


        return binding.getRoot();
    }

    private void setRecyclerView() {

        adapter = new ShortlistedAdapter(new ArrayList<>(), requireActivity());
        binding.shortlistedRecyclerView.setAdapter(adapter);
        binding.shortlistedRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));


        adapter.setOnItemClickListener((userProfile, position) -> {

        });


    }

    @Override
    public void onResume() {
        super.onResume();


        matchMakingViewModel.getConnectedUserIds().observe(getViewLifecycleOwner(), connections -> {


            viewModel.fetchShortlistedProfiles(connections);


            viewModel.getListUserProfiles().observe(getViewLifecycleOwner(), userProfiles -> {
                adapter.setMyMatches(userProfiles);
            });

        });
    }
}