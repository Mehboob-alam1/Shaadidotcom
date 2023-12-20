package com.mehboob.myshadi.views.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.utils.SessionManager;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.databinding.FragmentSignUpBinding;
import com.mehboob.myshadi.model.profilemodel.UserAuth;
import com.mehboob.myshadi.room.models.User;
import com.mehboob.myshadi.viewmodel.AuthViewModel;
import com.mehboob.myshadi.viewmodel.FUPViewModel;
import com.mehboob.myshadi.viewmodel.UserViewModel;
import com.mehboob.myshadi.views.activities.ProfileForActivity;
import com.mehboob.myshadi.views.dashboard.DashBoardActivity;

import java.io.File;
import java.io.FileOutputStream;


public class SignUpFragment extends Fragment implements FUPViewModel.ProfileCompletionCallback {

    private FragmentSignUpBinding binding;
    private NavController navController;

    private AuthViewModel authViewModel;

    private GoogleSignInClient googleSignInClient;

    private final int RC_SIGN_IN = 100;

    private UserViewModel userViewModel;
    private UserAuth userAuth;
    private FUPViewModel fupViewModel;

    private boolean ifProfileComplete;

    private ProgressDialog dialog;
    private SessionManager sessionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        fupViewModel = new ViewModelProvider(this).get(FUPViewModel.class);
        sessionManager = new SessionManager(requireActivity());

        authViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(AuthViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        dialog = new ProgressDialog(requireActivity());
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(getActivity().getApplication(), googleSignInOptions);


        binding.btnLogin.setOnClickListener(view -> {
            //  navController.navigate(R.id.action_signUpFragment_to_signInFragment);

            navigate(savedInstanceState);

        });


        return binding.getRoot();
    }

    private void checkProfile(String userId) {
        fupViewModel.checkProfileCompletion(userId, this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        navController = Navigation.findNavController(view);
        binding.btnSignUpGoogle.setOnClickListener(view1 -> {
            btnClickSignInGoogle();


        });


        authViewModel.getUserAuthMutableLiveData().observe(getViewLifecycleOwner(), new Observer<UserAuth>() {
            @Override
            public void onChanged(UserAuth userAuth) {
                if (userAuth.isAuthenticated()) {


                    insertUserToLocal(userAuth, savedInstanceState);

                }
            }
        });

    }

    public void btnClickSignInGoogle() {

        Intent intent = googleSignInClient.getSignInIntent();
        // Start activity for result
        startActivityForResult(intent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                // You can now use the GoogleSignInAccount to get the sign-in credential
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

                signInWithGoogleAuthCredential(credential);

                // Use the 'credential' as needed
            } catch (ApiException e) {
                // Handle sign-in failure
            }

        }
    }

    private void signInWithGoogleAuthCredential(AuthCredential googleAuthCredential) {
        authViewModel.signInGoogle(googleAuthCredential);

    }

    private void insertUserToLocal(UserAuth userAuth, Bundle savedInstanceState) {
        userAuth.setCreated(true);
        userViewModel.insertUser(new User(userAuth.getUserName(), userAuth.getName(), userAuth.getEmail(), userAuth.isAuthenticated()
                , userAuth.getUserId(), true));


    }

    public void navigate(Bundle savedInstanceState) {
        Utils.safelyNavigate(navController, R.id.action_signUpFragment_to_signInFragment, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        userViewModel.getLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null && user.isAuthenticated()) {
                // User is added to the Room database and authenticated, proceed with your app logic
                // For example, navigate to the main activity

                sessionManager.saveUserID(user.getUserId());

                Toast.makeText(requireActivity(), "User is already authenticated", Toast.LENGTH_SHORT).show();

                Toast.makeText(requireActivity(), "" + user.getEmail(), Toast.LENGTH_SHORT).show();
                checkProfile(user.getUserId());


            } else {
                // User not added or not authenticated, show the signup page
                // For example, display a fragment with the signup form

                dialog.dismiss();
                Log.d("room", "onCreateView: nothings");
            }
        });


    }


    @Override
    public void onProfileCompletion(boolean isProfileComplete) {


        Log.d("ProfileCompletion", "onProfileCompletion: " + isProfileComplete);
        if (isProfileComplete) {
            startActivity(new Intent(requireActivity(), DashBoardActivity.class));
            requireActivity().finishAffinity();


        } else {

            startActivity(new Intent(requireActivity(), ProfileForActivity.class));
            requireActivity().finishAffinity();
        }

        dialog.dismiss();

    }

    @Override
    public void onResume() {
        super.onResume();
        dialog.show();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        dialog.dismiss();
    }
}