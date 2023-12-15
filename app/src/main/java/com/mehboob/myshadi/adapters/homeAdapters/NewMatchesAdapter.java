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

import java.util.List;

public class NewMatchesAdapter extends RecyclerView.Adapter<NewMatchesAdapter.Holder> {
    private List<UserProfile> newMatches;
    private Context context;

    public NewMatchesAdapter(List<UserProfile> newMatches, Context context) {
        this.newMatches = newMatches;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.matches_sample, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        UserProfile userProfile = newMatches.get(position);
        Glide.with(context)
                .load(userProfile.getImageUrl())
                .placeholder(R.drawable.profile_1)
                .into(holder.imgProfileImage);

        holder.txtProfileName.setText(userProfile.getFullName());
        holder.txtProfileInfo.setText(formatInfo(userProfile.getDob(), userProfile.getHeight(), userProfile.getCommunity(), userProfile.getSubCommunity(), userProfile.getLivingIn()));




        holder.btnSendConnects.setOnClickListener( view -> {


            // Have to notify the user that this person is interested in you

            // add to invited list
            //

        });
    }


    public String formatInfo(String age, String height, String community, String subCommunity, String livingIn) {

        return age + "yrs, " + height + ", " + community + ", \n" + subCommunity + ",\n" + livingIn;


    }

    @Override
    public int getItemCount() {
        return newMatches.size();
    }

    public void setNewMatches(List<UserProfile> newMatches) {

        this.newMatches.addAll(newMatches);
        notifyDataSetChanged();
    }

    public List<UserProfile> getNewMatches() {
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
        }
    }
}
