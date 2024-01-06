package com.mehboob.myshadi.views.dashboard.frags;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.homeAdapters.ConnectedUsersAdapter;
import com.mehboob.myshadi.databinding.FragmentInboxBinding;
import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.viewmodel.ConnectionViewModel;
import com.mehboob.myshadi.viewmodel.FUPViewModel;

import java.util.ArrayList;
import java.util.List;


public class InboxFragment extends Fragment {


    private FragmentInboxBinding binding;
    private ConnectionViewModel connectionViewModel;
    private FUPViewModel fupViewModel;


    private ConnectedUsersAdapter adapter;

    private SessionManager sessionManager;
    private List<UserMatches> userMatchesList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionViewModel = new ViewModelProvider(this).get(ConnectionViewModel.class);
        fupViewModel = new ViewModelProvider(this).get(FUPViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInboxBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(requireActivity());

        setRecyclerView();


        return binding.getRoot();
    }

    private void setRecyclerView() {

        adapter = new ConnectedUsersAdapter(new ArrayList<>(), requireActivity());
        binding.allConnectedUsersRecyclerView.setAdapter(adapter);
        binding.allConnectedUsersRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
    }


    @Override
    public void onResume() {
        super.onResume();


        connectionViewModel.getConnectedUsers(sessionManager.fetchUserId()).observe(getViewLifecycleOwner(), connections -> {

            if (connections != null) {
                adapter.setNewProfiles(connections);


                binding.lineNoData.getRoot().setVisibility(View.GONE);
                binding.allConnectedUsersRecyclerView.setVisibility(View.VISIBLE);
            }

        });


    }



}