package com.mehboob.myshadi.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.myshadi.model.ChatMessages;
import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.room.entities.UserProfileData;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatRepository {

    private DatabaseReference messagesRef;
    private Application application;
    private MutableLiveData<List<ChatMessages>> chatMessages;
    private List<ChatMessages> chat;

    public ChatRepository(Application application) {
        messagesRef = FirebaseDatabase.getInstance().getReference("chatMessages");
        chat = new ArrayList<>();
        this.application = application;
        chatMessages = new MutableLiveData<>();

    }


    public void setMessage(ChatMessages message, Connection connection, String token) {

        String senderRoomId = message.getSenderId() + "-" + message.getRecieverId();
        String recieverRoomId = message.getSenderId() + "-" + message.getRecieverId();
        messagesRef.child(senderRoomId).child(message.getPushId())
                .setValue(message).addOnCompleteListener(task -> {
                    if (task.isComplete() && task.isSuccessful()) {

                        sendNotification(connection, token);
                        messagesRef.child(recieverRoomId).child(message.getPushId())
                                .setValue(message);
                    } else {

                    }
                }).addOnFailureListener(e -> {
                    Log.d("Exception", e.getLocalizedMessage());
                });

    }

    public void sendNotification(Connection connection, String token) {

        // current user name, message,currentUSerID,otherUSerToken


        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject notificationObject = new JSONObject();

            notificationObject.put("title", connection.getConnectionFromName());
            notificationObject.put("body", connection.getConnectionFromName() + " sent you a message");


            JSONObject dataObject = new JSONObject();
            dataObject.put("userId", connection.getConnectionFromId());
            dataObject.put("gender", connection.getConnectionFromGender());

            jsonObject.put("notification", notificationObject);
            jsonObject.put("data", dataObject);
            jsonObject.put("to", token);

            callApi(jsonObject);
        } catch (Exception e) {

        }
    }


    public void callApi(JSONObject jsonObject) {
        MediaType JSON = MediaType.get("application/json");

        OkHttpClient client = new OkHttpClient();

        String url = "https://fcm.googleapis.com/fcm/send";

        RequestBody requestBody = RequestBody.create(jsonObject.toString(), JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Authorization", "Bearer AAAAtT1kHVE:APA91bHLrXzauLb6Iw0bxzoFS6mkLhDvJRxECeM96INdpD3DeFjNAlx-YXTWgnMFTqz_eNyt8lGnBYpH7ks6ot2LO1J6H6IH7tbfEwuHXz9152YOOiXzy5tx7mfvop6kOZ9A_uXwLrk0")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {


                }
            }
        });
    }


    public MutableLiveData<List<ChatMessages>> getMessages(String senderId, String recieverId) {

        String roomId = senderId + "-" + recieverId;
        messagesRef.child(roomId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            chat.clear();
                            for (DataSnapshot snap : snapshot.getChildren()) {

                                ChatMessages chatMessages = snap.getValue(ChatMessages.class);
                                chat.add(chatMessages);

                            }
                            chatMessages.setValue(chat);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
return chatMessages;
    }
}
