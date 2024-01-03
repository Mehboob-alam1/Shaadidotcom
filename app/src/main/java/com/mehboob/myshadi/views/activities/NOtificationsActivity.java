package com.mehboob.myshadi.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.NotificationsAdapter;
import com.mehboob.myshadi.databinding.ActivityNotificationsBinding;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.NotificationViewModel;

import java.util.ArrayList;

public class NOtificationsActivity extends AppCompatActivity {
private ActivityNotificationsBinding binding;

private NotificationViewModel viewModel;
private NotificationsAdapter adapter;
private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        sessionManager= new SessionManager(this);

        viewModel.fetchNotificationsIRecieved(sessionManager.fetchUserId());
        setRecyclerView();
    }

    private void setRecyclerView() {

        adapter= new NotificationsAdapter(new ArrayList<>(),this);
        binding.notificationsRecyclerView.setAdapter(adapter);
        binding.notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener((notificationData, position) -> {

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

       viewModel.getNotificationsDataList().observe(this,notificationData -> {

           if (notificationData!=null){
               binding.notificationsRecyclerView.setVisibility(View.VISIBLE);
               binding.lineNoData.getRoot().setVisibility(View.GONE);
           }
           adapter.setNewNotifications(notificationData);
       });
    }
}