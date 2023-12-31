package com.mehboob.myshadi.adapters.homeAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.MatchesSampleBinding;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.utils.Utils;

import java.util.List;

public class NewMatchesAdapter extends RecyclerView.Adapter<NewMatchesAdapter.Holder> {
    private List<UserMatches> newMatches;
    private Context context;
    public OnItemClickListener onItemClickListener;

    public NewMatchesAdapter(List<UserMatches> newMatches, Context context) {
        this.newMatches = newMatches;
        this.context = context;
    }
    public interface OnItemClickListener {
        void onItemClick(UserMatches userProfile, int position);
    }

    public void setOnItemClickListener(NewMatchesAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.matches_sample, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        UserMatches userProfile = newMatches.get(position);
        Glide.with(context)
                .load(userProfile.getImageUrl())
                .placeholder(R.drawable.profile_1)
                .into(holder.imgProfileImage);

        holder.txtProfileName.setText(userProfile.getFullName());
        holder.txtProfileInfo.setText(formatInfo(userProfile.getDob(), userProfile.getHeight(), userProfile.getCommunity(), userProfile.getSubCommunity(), userProfile.getLivingIn()));




        holder.btnSendConnects.setOnClickListener( view -> {
           sendConnection(userProfile.getUserId());

        });
    }

    private void sendConnection(String userId) {

    }


    public String formatInfo(String age, String height, String community, String subCommunity, String livingIn) {

        return age + "yrs, " + height + ", " + community + ", \n" + subCommunity + ",\n" + livingIn;


    }

    @Override
    public int getItemCount() {
        return newMatches.size();
    }

    public void setNewMatches(List<UserMatches> newMatches) {
        this.newMatches.clear();

        this.newMatches.addAll(newMatches);
        notifyDataSetChanged();
    }

    public List<UserMatches> getNewMatches() {
        return newMatches;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView imgProfileImage;
        private TextView txtProfileName, txtProfileInfo;
        private Button btnSendConnects;

        public Holder(@NonNull View itemView) {
            super(itemView);

            imgProfileImage = itemView.findViewById(R.id.imgProfileImage);
            txtProfileName = itemView.findViewById(R.id.txtProfileName);
            txtProfileInfo = itemView.findViewById(R.id.txtProfileInfo);
            btnSendConnects = itemView.findViewById(R.id.btnSendConnects);




            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(newMatches.get(position), position);
                }
            });
        }
    }
}
