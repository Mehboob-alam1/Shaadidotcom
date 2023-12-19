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
import com.mehboob.myshadi.adapters.CountriesAdapter;
import com.mehboob.myshadi.databinding.MymatchesLayoutBinding;
import com.mehboob.myshadi.json.Countries;
import com.mehboob.myshadi.model.profilemodel.UserProfile;

import java.util.List;

public class MyMatchesAdapter extends RecyclerView.Adapter<MyMatchesAdapter.Holder> {
    private List<UserProfile> myMatches;
    private Context context;
    public OnItemClickListener onItemClickListener;


    public MyMatchesAdapter(List<UserProfile> newMatches, Context context) {
        this.myMatches = newMatches;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(UserProfile userProfile, int position);
    }

    public void setOnItemClickListener(MyMatchesAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mymatches_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        UserProfile userProfile = myMatches.get(position);
        holder.bind(userProfile);
    }

    @Override
    public int getItemCount() {
        return myMatches.size();
    }

    public void setMyMatches(List<UserProfile> myMatches) {

        this.myMatches.addAll(myMatches);
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private LinearLayout btnImagesM;

        private ImageView imgVerifiedM, imgProfileImageM;

        private TextView txtProfileNameM, txtYouAndPersonM, txtProfileAgeHeightM, txtProfileProfessionM, txtCommunitySubCommunityM, txtCityCountryM, btnConnectNowM;

        public Holder(@NonNull View itemView) {
            super(itemView);

            btnImagesM = itemView.findViewById(R.id.btnImagesM);
            imgVerifiedM = itemView.findViewById(R.id.imgVerifiedM);
            imgProfileImageM = itemView.findViewById(R.id.imgProfileImageM);
            txtProfileNameM = itemView.findViewById(R.id.txtProfileNameM);
            txtYouAndPersonM = itemView.findViewById(R.id.txtYouAndPersonM);
            txtProfileAgeHeightM = itemView.findViewById(R.id.txtProfileAgeHeightM);
            txtProfileProfessionM = itemView.findViewById(R.id.txtProfileProfessionM);
            txtCommunitySubCommunityM = itemView.findViewById(R.id.txtCommunitySubCommunityM);
            txtCityCountryM = itemView.findViewById(R.id.txtCityCountryM);
            btnConnectNowM = itemView.findViewById(R.id.btnConnectNowM);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(myMatches.get(position), position);
                }
            });


        }

        @SuppressLint("SetTextI18n")
        void bind(UserProfile userProfile) {
//            countryNameTextView.setText(country.getName());
            if (userProfile.getIsVerified()) {
                imgVerifiedM.setVisibility(View.VISIBLE);
                try {
                    Glide.with(context).load(userProfile.getImageUrl())
                            .placeholder(R.drawable.profile)
                            .into(imgProfileImageM);
                } catch (Exception e) {
                    Log.d("imageNotFound", "imageNotFound" + e.getLocalizedMessage());
                }
                txtProfileNameM.setText(userProfile.getFullName());
                if (userProfile.getGender().equals("Female")) {
                    txtYouAndPersonM.setText("You and her");
                }
                if (userProfile.getGender().equals("Male")) {
                    txtYouAndPersonM.setText("You and him");
                }

                txtProfileAgeHeightM.setText(userProfile.getDob() + " yrs," + userProfile.getHeight());
                txtProfileProfessionM.setText(userProfile.getWorkAs());

                txtCommunitySubCommunityM.setText(userProfile.getCommunity() + "," + userProfile.getSubCommunity());
                txtCityCountryM.setText(userProfile.getCityName() + "," + userProfile.getLivingIn());


                btnImagesM.setOnClickListener(view -> {
                    Toast.makeText(context, "" + userProfile.getImages().size(), Toast.LENGTH_SHORT).show();
                });

                btnConnectNowM.setOnClickListener(view -> {
                    Toast.makeText(context, "Upgrade to premium to get connected", Toast.LENGTH_SHORT).show();
                });
            }
        }
    }
}
