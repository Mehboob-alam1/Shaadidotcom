package com.mehboob.myshadi.adapters.homeAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mehboob.myshadi.R;

import com.mehboob.myshadi.model.Connection;
import java.util.List;

public class ConnectedUsersAdapter extends RecyclerView.Adapter<ConnectedUsersAdapter.ConnectedHolder> {

    private List<Connection> connections;
    private Context context;

    public OnItemClickListener onItemClickListener;


    public ConnectedUsersAdapter(List<Connection> connections, Context context) {
        this.connections = connections;
        this.context = context;


    }

    public interface OnItemClickListener {
        void onItemClick(Connection connection, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ConnectedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.notification_layout, parent, false);
        return new ConnectedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectedHolder holder, int position) {

        Connection data = connections.get(position);
        holder.bind(data);

    }


    public void setNewProfiles(List<Connection> data) {

        this.connections.clear();
        this.connections.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return connections.size();
    }

    public class ConnectedHolder extends RecyclerView.ViewHolder {
        private ImageView userProfileImage;
        private TextView txtUserName, txtUserMessage, txtTime;

        public ConnectedHolder(@NonNull View itemView) {
            super(itemView);

            userProfileImage = itemView.findViewById(R.id.userProfileImageNotif);
            txtUserName = itemView.findViewById(R.id.txtUserNameNotif);
            txtUserMessage = itemView.findViewById(R.id.txtUserMessageNotif);
            txtTime = itemView.findViewById(R.id.txtTimeNotif);


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(connections.get(position), position);
                }
            });
        }


        void bind(Connection connection) {


            try {

                Glide.with(context).load(connection.getConnectionToImage())
                        .placeholder(R.drawable.profile)
                        .into(userProfileImage);
                txtUserName.setText(connection.getConnectionToName());
                txtUserMessage.setText("You are connected now");

                txtTime.setText("");
            } catch (NullPointerException e) {
                Log.d("Exception", e.getLocalizedMessage());
            }
        }
    }
}
