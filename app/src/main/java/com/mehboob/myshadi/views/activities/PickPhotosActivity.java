package com.mehboob.myshadi.views.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.databinding.ActivityPickPhotosBinding;

import java.util.ArrayList;
import java.util.List;

public class PickPhotosActivity extends AppCompatActivity {
    private ActivityPickPhotosBinding binding;
    private List<Uri> selectedImages = new ArrayList<>();
    List<ImageView> imageViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPickPhotosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getImagesToViews();



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

                    if (selectedImages!=null){
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
}