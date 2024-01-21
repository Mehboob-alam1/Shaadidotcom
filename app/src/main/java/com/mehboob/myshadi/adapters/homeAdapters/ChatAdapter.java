package com.mehboob.myshadi.adapters.homeAdapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ItemRecieveBinding;
import com.mehboob.myshadi.databinding.ItemSentBinding;
import com.mehboob.myshadi.model.ChatMessages;
import com.mehboob.myshadi.utils.SessionManager;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {
    private List<ChatMessages> chatMessages;
    private Context context;

    private int ITEM_SENT = 1;
    private int ITEM_RECIEVED = 2;
    private SessionManager sessionManager;


    public ChatAdapter(List<ChatMessages> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_sent, parent, false);

            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_recieve, parent, false);

            return new RecieverViewHolder(view);
        }


    }
    @Override
    public int getItemViewType(int position) {
        ChatMessages chat = chatMessages.get(position);
        if (sessionManager.fetchUserId().equals(chat.getSenderId())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECIEVED;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

       ChatMessages chat=  chatMessages.get(position);

       if (holder.getClass()==SenderViewHolder.class){
           SenderViewHolder viewHolder= (SenderViewHolder) holder;
           viewHolder.binding.txtMessage.setText(chat.getMessage());
       }else {

           RecieverViewHolder viewHolder= (RecieverViewHolder) holder;

           viewHolder.binding.txtMessage.setText(chat.getMessage());
       }


    }



    @Override
    public int getItemCount() {
        return chatMessages.size();
    }


    public void setChatMessages(List<ChatMessages> chatMessages) {
        this.chatMessages.clear();

        chatMessages.addAll(chatMessages);
        notifyDataSetChanged();
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        ItemSentBinding binding;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemSentBinding.bind(itemView);
        }
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder {
        ItemRecieveBinding binding;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemRecieveBinding.bind(itemView);
        }
    }
}
