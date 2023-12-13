package com.mehboob.myshadi.views.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityPickPhotosBinding;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.models.User;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.TinyDB;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class PickPhotosActivity extends AppCompatActivity implements FUPViewModel.ProfileCompletionCallback {
    private ActivityPickPhotosBinding binding;
    private List<Uri> selectedImages = new ArrayList<>();
    List<ImageView> imageViewList;
    private TinyDB tinyDB;
    private SessionManager sessionManager;
    private FUPViewModel fupViewModel;
    private UserViewModel userViewModel;

    private ArrayList<String> images;

    private User userData;

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

        getImagesToViews();


        binding.btnContinue.setOnClickListener(view -> {

            if (!selectedImages.isEmpty()) {

                binding.lottieLoading.setVisibility(View.VISIBLE);
                uploadData();


            } else {
                Utils.showSnackBar(this, "Add at least one photo");
            }
        });


    }


    private void uploadData() {
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
                false);
        fupViewModel.uploadUserProfile(selectedImages, profile);

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


//        fupViewModel.ge(userData.getUserId()).observe(this, aBoolean -> {
//
//
//
//            if (aBoolean) {
//
//                binding.lottieLoading.setVisibility(View.GONE);
//                binding.lottieLoading.cancelAnimation();
//                Utils.showSnackBar(this,"Profile created successfully");
//                startActivity(new Intent(PickPhotosActivity.this, SetPreferencesActivity.class));
//            }else{
//                binding.lottieLoading.setVisibility(View.GONE);
//                binding.lottieLoading.cancelAnimation();
//                Utils.showSnackBar(this,"Something went wrong");
//            }
//
//
//        });
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
                    Uri selectedImageUri = result.getData().getData();

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


        Log.d("PickPhotosActivity", "onProfileCompletion: "+isProfileComplete);
    }
}