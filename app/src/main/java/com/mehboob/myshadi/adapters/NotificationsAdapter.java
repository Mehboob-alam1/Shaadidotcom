package com.mehboob.myshadi.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.animation.content.Content;
import com.bumptech.glide.Glide;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.homeAdapters.NearMeAdapter;
import com.mehboob.myshadi.model.profilemodel.NotificationData;
import com.mehboob.myshadi.room.entities.UserMatches;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationHolder>{

    private List<NotificationData> notificationDataList;
    private Context context;

    public OnItemClickListener onItemClickListener;

    public NotificationsAdapter(List<NotificationData> notificationDataList, Context context) {
        this.notificationDataList = notificationDataList;
        this.context = context;
    }
    public interface OnItemClickListener {
        void onItemClick(NotificationData notificationData, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      View view = LayoutInflater.from(context).inflate(R.layout.notification_layout,parent,false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {

      NotificationData data=   notificationDataList.get(position);
        holder.bind(data);

    }


    public void setNewNotifications(List<NotificationData> data){

        this.notificationDataList.clear();

        this.notificationDataList.addAll(data);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return notificationDataList.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {
        private ImageView userProfileImage;
        private TextView txtUserName, txtUserMessage, txtTime;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);

            userProfileImage = itemView.findViewById(R.id.userProfileImageNotif);
            txtUserName = itemView.findViewById(R.id.txtUserNameNotif);
            txtUserMessage = itemView.findViewById(R.id.txtUserMessageNotif);
            txtTime = itemView.findViewById(R.id.txtTimeNotif);


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(notificationDataList.get(position), position);
                }
            });
        }


        void bind(NotificationData notificationData){

            try{

                Glide.with(context).load(notificationData.getFromImageUrl())
                        .placeholder(R.drawable.profile)
                        .into(userProfileImage);
                txtUserName.setText(notificationData.getFromUserName());
                txtUserMessage.setText(notificationData.getFromMessage());

                txtTime.setText(notificationData.getTime());
            }catch (NullPointerException e){
                Log.d("Exception",e.getLocalizedMessage());
            }
        }
    }
}
