package com.mehboob.myshadi.viewmodel;

import android.app.Application;
import android.hardware.lights.LightsManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mehboob.myshadi.model.ChatMessages;
import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.repository.ChatRepository;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    private ChatRepository chatRepository;



    public ChatViewModel(@NonNull Application application) {
        super(application);
        chatRepository= new ChatRepository(application);

    }

    public void setMessage(ChatMessages message, Connection connection, String token){
        chatRepository.setMessage(message,connection,token);
    }


    public MutableLiveData<List<ChatMessages>> getMessage(String senderId,String recieverId ){
      return   chatRepository.getMessages(senderId,recieverId);
    }

}
