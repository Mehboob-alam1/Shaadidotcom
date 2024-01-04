package com.mehboob.myshadi.adapters.homeAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.model.profilemodel.UserProfile;


import java.util.List;

public class ShortlistedAdapter extends RecyclerView.Adapter<ShortlistedAdapter.ShortlistHolder> {

public OnItemClickListener onItemClickListener;
    private List<UserProfile> userProfiles;
    private Context context;

    public ShortlistedAdapter(List<UserProfile> userProfiles, Context context) {
        this.userProfiles = userProfiles;
        this.context = context;
    }


    public interface OnItemClickListener {
        void onItemClick(UserProfile userProfile, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @NonNull
    @Override
    public ShortlistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shortlisted_layout, parent, false);
        return new ShortlistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortlistHolder holder, int position) {
        UserProfile userProfile = userProfiles.get(position);
        holder.bind(userProfile);
    }

    @Override
    public int getItemCount() {
        return userProfiles.size();
    }
    public void setMyMatches(List<UserProfile> myMatches) {
        this.userProfiles.clear();

        this.userProfiles.addAll(myMatches);
        notifyDataSetChanged();
    }


    public class ShortlistHolder extends RecyclerView.ViewHolder{

        private LinearLayout btnImagesS;

        private ImageView imgVerifiedS, imgProfileImageS;

        private TextView txtProfileNameS, txtYouAndPersonS, txtProfileAgeHeightS, txtProfileProfessionS, txtCommunitySubCommunityS, txtCityCountryS;
        public ShortlistHolder(@NonNull View itemView) {
            super(itemView);


            btnImagesS = itemView.findViewById(R.id.btnImagesS);
            imgVerifiedS = itemView.findViewById(R.id.imgVerifiedS);
            imgProfileImageS = itemView.findViewById(R.id.imgProfileImageS);
            txtProfileNameS = itemView.findViewById(R.id.txtProfileNameS);
            txtYouAndPersonS = itemView.findViewById(R.id.txtYouAndPersonS);
            txtProfileAgeHeightS = itemView.findViewById(R.id.txtProfileAgeHeightS);
            txtProfileProfessionS = itemView.findViewById(R.id.txtProfileProfessionS);
            txtCommunitySubCommunityS = itemView.findViewById(R.id.txtCommunitySubCommunityS);
            txtCityCountryS = itemView.findViewById(R.id.txtCityCountryS);


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(userProfiles.get(position), position);
                }
            });


        }


        @SuppressLint("SetTextI18n")
        void bind(UserProfile userProfile) {
//            countryNameTextView.setText(country.getName());


            if (userProfile.getIsVerified()) {
                imgVerifiedS.setVisibility(View.VISIBLE);
            }
            try {
                Glide.with(context).load(userProfile.getImageUrl())
                        .placeholder(R.drawable.profile)
                        .into(imgProfileImageS);
            } catch (Exception e) {
                Log.d("imageNotFound", "imageNotFound" + e.getLocalizedMessage());
            }
            txtProfileNameS.setText(userProfile.getFullName());
            if (userProfile.getGender().equals("Female")) {
                txtYouAndPersonS.setText("You and her");
            }
            if (userProfile.getGender().equals("Male")) {
                txtYouAndPersonS.setText("You and him");
            }

            txtProfileAgeHeightS.setText(userProfile.getDob() + " yrs," + userProfile.getHeight());
            txtProfileProfessionS.setText(userProfile.getWorkAs());

            txtCommunitySubCommunityS.setText(userProfile.getCommunity() + "," + userProfile.getSubCommunity());
            txtCityCountryS.setText(userProfile.getCityName() + "," + userProfile.getLivingIn());


            btnImagesS.setOnClickListener(view -> {
                Toast.makeText(context, "" + userProfile.getImages().size(), Toast.LENGTH_SHORT).show();
            });

        }
    }
}
