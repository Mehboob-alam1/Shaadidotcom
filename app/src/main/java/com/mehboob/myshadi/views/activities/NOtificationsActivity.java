package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.NotificationsAdapter;
import com.mehboob.myshadi.databinding.ActivityNotificationsBinding;
import com.mehboob.myshadi.model.profilemodel.NotificationData;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.NotificationViewModel;

import java.util.ArrayList;

public class NOtificationsActivity extends AppCompatActivity {
    private ActivityNotificationsBinding binding;

    private NotificationViewModel viewModel;
    private FUPViewModel fupViewModel;
    private NotificationsAdapter adapter;
    private SessionManager sessionManager;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        fupViewModel = new ViewModelProvider(this).get(FUPViewModel.class);
        sessionManager = new SessionManager(this);

        viewModel.fetchNotificationsIRecieved(sessionManager.fetchUserId());
        setRecyclerView();



    }

    private void setRecyclerView() {

        adapter = new NotificationsAdapter(new ArrayList<>(), this);
        binding.notificationsRecyclerView.setAdapter(adapter);
        binding.notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener((notificationData, position) -> {


            getData(notificationData);


        });

    }

    private void getData(NotificationData notificationData) {
        fupViewModel.getAnyUserProfile(notificationData.getFromUserId(), notificationData.getFromGender()).observe(this, userMatches -> {
            if (userMatches != null) {
                 i = new Intent(this, ConnectionRecivedDetailedActivity.class);
                i.putExtra("currentPerson", new Gson().toJson(userMatches));
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.getNotificationsDataList().observe(this, notificationData -> {

            if (notificationData != null) {
                binding.notificationsRecyclerView.setVisibility(View.VISIBLE);
                binding.lineNoData.getRoot().setVisibility(View.GONE);
            }
            adapter.setNewNotifications(notificationData);
        });
    }
}