package com.mehboob.myshadi.views.dashboard.frags;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.ContactsContract;
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
import com.google.gson.Gson;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.homeAdapters.ConnectedUsersAdapter;
import com.mehboob.myshadi.databinding.FragmentInboxBinding;
import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.viewmodel.ConnectionViewModel;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.views.activities.ChatActivity;

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


        adapter.setOnItemClickListener((connection, position) -> {


            checkIfUserAcceptedConnection(connection);
            // creation of room


        });
    }

    private void checkIfUserAcceptedConnection(Connection connection) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ConnectionIRecieved")
                .child(connection.getConnectionToId())
                .child(connection.getConnectionFromId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Boolean accepted = snapshot.child("connected").getValue(Boolean.class);
                    String status = snapshot.child("status").getValue(String.class);

                    if (accepted && status.equals("connected")) {
                        Intent i = new Intent(requireContext(), ChatActivity.class);
                        i.putExtra("user", new Gson().toJson(connection));
                        startActivity(i);
                    } else {
                        Utils.showSnackBar(requireActivity(), connection.getConnectionToName() + " didn't accepted your connection yet");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.showSnackBar(requireActivity(), error.getMessage());
            }
        });
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