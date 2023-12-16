package com.mehboob.myshadi.views.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.model.profilemodel.UserAuth;
import com.mehboob.myshadi.utils.Utils;
import com.mehboob.myshadi.databinding.FragmentSignInBinding;
import com.mehboob.myshadi.viewmodel.AuthViewModel;

import java.util.Objects;


public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;
    private NavController navController;
    private AuthViewModel authViewModel;

    private GoogleSignInClient googleSignInClient;

    private final int RC_SIGN_IN = 100;
    private UserAuth userAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(AuthViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();



        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(getActivity().getApplication(), googleSignInOptions);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.safelyNavigate(navController, R.id.action_signInFragment_to_profileForActivity, savedInstanceState);
            }
        }, 2000);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Handle the back press here
                        // For example, navigate to a different fragment or perform some other action
                        // If you want to finish the hosting activity, call finish on the activity
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }
                });

        return binding.getRoot();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        binding.btnSignUpGoogle.setOnClickListener(view1 -> {
            authViewModel.getUserAuthMutableLiveData().observe(getViewLifecycleOwner(), userAuth -> {
                this.userAuth = userAuth;
                if (userAuth == null && !userAuth.isAuthenticated()) {
                    //sigin
                    Intent intent = googleSignInClient.getSignInIntent();
                    // Start activity for result
                    startActivityForResult(intent, RC_SIGN_IN);
                } else {
                    Toast.makeText(requireActivity().getApplication(), "Already authenticated", Toast.LENGTH_SHORT).show();
                }

            });

        });
    }


    private void signInWithGoogleAuthCredential(AuthCredential googleAuthCredential) {
        authViewModel.signInGoogle(googleAuthCredential);

    }

    @Override
    public void onResume() {
        super.onResume();


    }


}