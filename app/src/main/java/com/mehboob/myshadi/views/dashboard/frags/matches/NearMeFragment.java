package com.mehboob.myshadi.views.dashboard.frags.matches;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.adapters.homeAdapters.MyMatchesAdapter;
import com.mehboob.myshadi.adapters.homeAdapters.NearMeAdapter;
import com.mehboob.myshadi.adapters.homeAdapters.NewMatchesAdapter;
import com.mehboob.myshadi.databinding.FragmentNearMeBinding;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.services.LocationTrack;
import com.mehboob.myshadi.utils.MatchPref;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.MatchMakingViewModel;
import com.mehboob.myshadi.views.activities.ProfileDetailedActivity;

import java.util.ArrayList;
import java.util.List;


public class NearMeFragment extends Fragment {


    private FragmentNearMeBinding binding;
    private FUPViewModel fupViewModel;
    LocationTrack locationTrack;
    private MatchMakingViewModel matchMakingViewModel;

    private NearMeAdapter nearMeAdapter;
    protected LocationManager locationManager;

    private BottomSheetDialog dialog;
    MatchPref matchPref;
    private SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fupViewModel = new ViewModelProvider(requireActivity()).get(FUPViewModel.class);

        matchMakingViewModel = new ViewModelProvider(requireActivity()).get(MatchMakingViewModel.class);
        matchPref = new MatchPref(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNearMeBinding.inflate(inflater, container, false);
        setRecyclerView();
        sessionManager = new SessionManager(requireActivity());

    enableLocations();

        return binding.getRoot();


    }

    private void setRecyclerView() {

        nearMeAdapter = new NearMeAdapter(new ArrayList<>(), requireActivity());
        binding.nearMeRecyclerView.setAdapter(nearMeAdapter);
        binding.nearMeRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));


        nearMeAdapter.setOnItemClickListener((userProfile, position) -> {
            Intent i = new Intent(requireContext(), ProfileDetailedActivity.class);
            i.putExtra("currentPerson", new Gson().toJson(userProfile));
            startActivity(i);
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        matchMakingViewModel.getBestMatchesPref(Integer.parseInt(matchPref.fetchPref().getMinAge()), Integer.parseInt(matchPref.fetchPref().getMaxAge())).observe(this, userMatches -> {
            if (userMatches != null && userMatches.size() != 0) {
                binding.lineNoData.getRoot().setVisibility(View.GONE);
                binding.nearMeRecyclerView.setVisibility(View.VISIBLE);
            }
            nearMeAdapter.setMyMatches(userMatches);

        });
    }










    private void enableLocations() {

        locationTrack = new LocationTrack(requireContext());


        if (locationTrack.canGetLocation()) {

            String latitude = String.valueOf(locationTrack.getLatitude());
            String longitude = String.valueOf(locationTrack.getLongitude());
            fupViewModel.updateLocation(latitude, longitude, sessionManager.fetchUserId());

        } else {
            DialogShow();
            //  locationTrack.showSettingsAlert();
        }
    }

    public void DialogShow() {

        dialog = new BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme);

        View bottomsheetView = LayoutInflater.from(requireActivity()).
                inflate(R.layout.permission_dialog, (LinearLayout) binding.getRoot().findViewById(R.id.permissionDialog));
        dialog.setContentView(bottomsheetView);
        dialog.show();
        dialog.setCancelable(false);


        AppCompatButton btnYes = bottomsheetView.findViewById(R.id.btnEnableLocation);
        AppCompatButton btnNot = bottomsheetView.findViewById(R.id.btnNotNow);

        btnYes.setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        });
        btnNot.setOnClickListener(v -> {
            dialog.dismiss();
            // DashboardActivity.finish();
        });

        dialog.show();
    }
}