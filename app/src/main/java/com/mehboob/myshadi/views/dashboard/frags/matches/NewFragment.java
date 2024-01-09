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
import com.mehboob.myshadi.adapters.homeAdapters.NearMeAdapter;
import com.mehboob.myshadi.adapters.homeAdapters.NewMatchesAdapter;
import com.mehboob.myshadi.adapters.homeAdapters.NewMatchesAdapterHome;
import com.mehboob.myshadi.databinding.FragmentNearMeBinding;
import com.mehboob.myshadi.databinding.FragmentNewBinding;
import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.utils.MatchPref;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.MatchMakingViewModel;
import com.mehboob.myshadi.views.activities.ProfileDetailedActivity;

import java.util.ArrayList;
import java.util.List;


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

        adapter.setOnItemClickListener((userProfile, position) -> {
            Intent i = new Intent(requireContext(), ProfileDetailedActivity.class);
            i.putExtra("currentPerson", new Gson().toJson(userProfile));
            startActivity(i);
        });


        adapter.setOnConnectClickListener((userMatches, position) -> {
            fupViewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), userProfileData -> {
                Connection connection = new Connection(userProfileData.getUserId(),
                        userMatches.getUserId(), String.valueOf(System.currentTimeMillis()),
                        userMatches.getUserId() + "_" + userProfileData.getUserId(), "Pending", false, userProfileData.getGender(), userMatches.getGender(), userMatches.getImageUrl(), userMatches.getImageUrl(),userMatches.getFullName(),userProfileData.getFullName());
                matchMakingViewModel.sendNotification(connection, userMatches, userProfileData);

                matchMakingViewModel.getConnectionSent().observe(getViewLifecycleOwner(), aBoolean -> {
                    if (aBoolean) {
                        Toast.makeText(requireActivity(), "Connection sent", Toast.LENGTH_SHORT).show();


                        matchMakingViewModel.deleteUserMatches(userMatches);

                        matchMakingViewModel.insertConnection(connection);
                        userMatches.setConnectionSent(true);
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(requireActivity(), "Connection not sent ", Toast.LENGTH_SHORT).show();
                    }
                });
            });

        });
    }

    @Override
    public void onResume() {
        super.onResume();

        matchMakingViewModel.getUserProfilesCreatedLastWeek().observe(getViewLifecycleOwner(),userMatches -> {


            List<Connection> connectedUserIds = matchMakingViewModel.getConnectedUserIds().getValue();
            List<UserMatches> filteredRecentMatches = new ArrayList<>();

            if (connectedUserIds!=null) {

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
                binding.myMatchesRecyclerView.setVisibility(View.VISIBLE);
                binding.lineNoData.getRoot().setVisibility(View.GONE);
                adapter.setMyMatches(filteredRecentMatches);
                adapter.notifyDataSetChanged();

            }else{
                binding.myMatchesRecyclerView.setVisibility(View.VISIBLE);
                binding.lineNoData.getRoot().setVisibility(View.GONE);

                adapter.setMyMatches(userMatches);





            }
        });



    }
}