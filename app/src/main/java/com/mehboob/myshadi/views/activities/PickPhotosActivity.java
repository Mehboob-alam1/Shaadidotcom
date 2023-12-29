package com.mehboob.myshadi.views.activities;



import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityPickPhotosBinding;
import com.mehboob.myshadi.model.ProfileCheck;
import com.mehboob.myshadi.model.profilemodel.Preferences;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.models.User;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.TinyDB;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PickPhotosActivity extends AppCompatActivity implements FUPViewModel.ProfileCompletionCallback, LocationListener {
    private ActivityPickPhotosBinding binding;

    protected LocationManager locationManager;

    private List<Uri> selectedImages = new ArrayList<>();
    List<ImageView> imageViewList;
    private TinyDB tinyDB;
    private SessionManager sessionManager;
    private FUPViewModel fupViewModel;
    private UserViewModel userViewModel;

    private ArrayList<String> images;

    private User userData;

    private String latitude;
    private String longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPickPhotosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fupViewModel = new ViewModelProvider(this).get(FUPViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        tinyDB = new TinyDB(this);
        sessionManager = new SessionManager(this);


        userViewModel.getLiveData().observe(this, user -> userData = user);

        // Check for runtime permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            int MY_PERMISSIONS_REQUEST_LOCATION = 123;
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        }

        getImagesToViews();


        binding.btnContinue.setOnClickListener(view -> {

            if (!selectedImages.isEmpty()) {

                binding.lottieLoading.setVisibility(View.VISIBLE);
                uploadData();


            } else {
                Utils.showSnackBar(this, "Add at least one photo");
            }
        });
        binding.imgBack.setOnClickListener(view -> finish());


    }


    private void uploadData() {
        String time= String.valueOf(System.currentTimeMillis());
        UserProfile profile = new UserProfile(sessionManager.fetchProfileFor(),
                sessionManager.fetchGender(),
                sessionManager.fetchFullName(),
                sessionManager.fetchDob(),
                sessionManager.fetchReligion(),
                sessionManager.fetchCommunity(),
                sessionManager.fetchLivingIn(),
                sessionManager.fetchEmail(),
                sessionManager.fetchPhoneNumber(),
                sessionManager.fetchCountryCode(),
                sessionManager.fetchStateName(),
                sessionManager.fetchStateCode(),
                sessionManager.fetchCityName(),
                sessionManager.fetchSubCommunity(),
                sessionManager.fetchMaritalStatus(),
                sessionManager.fetchChildren(),
                sessionManager.fetchHeight(),
                sessionManager.fetchDiet(),
                sessionManager.fetchQualifications(),
                sessionManager.fetchCollege(),
                sessionManager.fetchIncome(),
                sessionManager.fetchWorkWith(),
                sessionManager.fetchWorkAs(),
                "",
                userData.getUserId()
                , new ArrayList<>(),
                false
                , "Free",
                false,
               time,
                new Preferences(),
                latitude, longitude,
                sessionManager.fetchAboutMe(),
                sessionManager.fetchDateBirth());
        fupViewModel.uploadUserProfile(selectedImages, profile);


        fupViewModel.uploadChecks(new ProfileCheck(true,false,time,userData.getUserId()));

        fupViewModel.getCheckIfUpload().observe(this, aBoolean -> {

            if (aBoolean) {

                binding.lottieLoading.setVisibility(View.GONE);
                binding.lottieLoading.cancelAnimation();
                Utils.showSnackBar(this, "Profile created successfully");
                startActivity(new Intent(PickPhotosActivity.this, SetPreferencesActivity.class));
            } else {
                binding.lottieLoading.setVisibility(View.GONE);
                binding.lottieLoading.cancelAnimation();
                Utils.showSnackBar(this, "Something went wrong");
            }


        });


    }


    private void getImagesToViews() {
        imageViewList = new ArrayList<>();
        imageViewList.add(binding.imgPick1);
        imageViewList.add(binding.imgPick2);
        imageViewList.add(binding.imgPick3);
        imageViewList.add(binding.imgPick4);
        imageViewList.add(binding.imgPick5);
        imageViewList.add(binding.imgPick6);

        for (ImageView imageView : imageViewList) {
            imageView.setOnClickListener(view -> openGallery());
        }
    }

    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        resultLauncher.launch(intent);
    }

    // Handle the result of picking an image
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Handle the selected image
                    Uri selectedImageUri = Objects.requireNonNull(result.getData()).getData();

                    // Update ViewModel with selected image
                    selectedImages.add(selectedImageUri);
                    // viewModel.setSelectedImages(selectedImages);

                    if (selectedImages != null) {

                        updateImageViews(selectedImages);
                    }
                }
            }
    );

    private void updateImageViews(List<Uri> images) {
        for (int i = 0; i < imageViewList.size(); i++) {
            if (i < images.size()) {
                // Set the selected image to the corresponding ImageView
                imageViewList.get(i).setImageURI(images.get(i));
            } else {
                // If there are fewer selected images than ImageViews, reset the ImageViews
                imageViewList.get(i).setImageResource(R.drawable.img_photo);
            }
        }
    }


    @Override
    public void onProfileCompletion(boolean isProfileComplete) {


        Log.d("PickPhotosActivity", "onProfileCompletion: " + isProfileComplete);
    }

    @Override
    public void onLocationChanged(Location location) {


        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());

        fupViewModel.updateLocation(latitude, longitude,userData.getUserId());

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }
}