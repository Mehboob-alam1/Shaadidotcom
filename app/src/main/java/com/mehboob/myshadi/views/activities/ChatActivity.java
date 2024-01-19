package com.mehboob.myshadi.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.homeAdapters.ChatAdapter;
import com.mehboob.myshadi.databinding.ActivityChatBinding;
import com.mehboob.myshadi.model.ChatMessages;
import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.viewmodel.ChatViewModel;
import com.mehboob.myshadi.viewmodel.FUPViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private Connection connection;
    private FUPViewModel fupViewModel;
    private SessionManager sessionManager;
    private ChatViewModel chatViewModel;
    private boolean isVerified;
    private String token;

    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fupViewModel = new ViewModelProvider(this).get(FUPViewModel.class);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        sessionManager = new SessionManager(this);
        setChatRecyclerView();

        Type type = new TypeToken<Connection>() {
        }.getType();
        connection = new Gson().fromJson(getIntent().getStringExtra("user"), type);

        try {
            binding.txtName.setText(connection.getConnectionToName());
            Glide.with(this).load(connection.getConnectionToImage())
                    .into(binding.circleImageView);
        } catch (Exception e) {
            Log.d("Exception", e.getLocalizedMessage());

        }

        getUserToken(connection);

        fupViewModel.getProfile(sessionManager.fetchUserId());


        Log.d("TokenVerified", token + " " + isVerified);



    }

    private void setChatRecyclerView() {

        List<ChatMessages> list= new ArrayList<>();
        list.add(new ChatMessages("","","Hello my name is khan","",""));
        list.add(new ChatMessages("","","Hello my name is khan","",""));





    }

    private void getUserToken(Connection connection) {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("userProfiles")
                .child(connection.getConnectionToGender())
                .child(connection.getConnectionToId());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    token = snapshot.child("token").getValue(String.class);
                    binding.btnSendMessage.setOnClickListener(v -> {

                        if (binding.etMessage.getText().toString() != null) {

                            String message = binding.etMessage.getText().toString();
                            String pushId = UUID.randomUUID().toString();
                            String timeStamp = String.valueOf(System.currentTimeMillis());
                            ChatMessages msg = new ChatMessages(connection.getConnectionFromId(), connection.getConnectionToId(),
                                    message, pushId, timeStamp);
                            binding.etMessage.setText("");

                            chatViewModel.setMessage(msg, connection, token);



                        }
                    });
                } else {
                    Log.d("Token", "Something went wrong");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Token", error.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        fupViewModel.getUserProfileLiveData().observe(this, userProfileData -> {
            if (userProfileData != null) {
                if (userProfileData.isVerified()) {
                    isVerified = userProfileData.isVerified();
                    binding.notVerifiedLayout.setVisibility(View.GONE);
                    binding.relativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        chatViewModel.getMessage(sessionManager.fetchUserId(), connection.getConnectionToId()).observe(this, chatMessages -> {


            Log.d("Messages", chatMessages.toString());
            chatAdapter = new ChatAdapter(chatMessages, this);
            binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.chatRecyclerView.setAdapter(chatAdapter);
            binding.chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);


        });


    }
}