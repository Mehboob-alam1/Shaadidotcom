package com.mehboob.myshadi.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mehboob.myshadi.R;
import com.mehboob.myshadi.Utils;
import com.mehboob.myshadi.databinding.FragmentSignInBinding;
import com.mehboob.myshadi.databinding.FragmentSignUpBinding;
import com.mehboob.myshadi.model.UserAuth;
import com.mehboob.myshadi.viewmodel.AuthViewModel;


public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;
    private NavController navController;

    private AuthViewModel authViewModel;

    private GoogleSignInClient googleSignInClient;

    private final int RC_SIGN_IN = 100;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(getActivity().getApplication(), googleSignInOptions);





        binding.btnLogin.setOnClickListener(view -> {
          //  navController.navigate(R.id.action_signUpFragment_to_signInFragment);


            Utils.safelyNavigate(navController,R.id.action_signUpFragment_to_signInFragment,savedInstanceState);
        });
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        navController = Navigation.findNavController(view);
        binding.btnSignUpGoogle.setOnClickListener(view1 -> {
            Intent intent = googleSignInClient.getSignInIntent();
            // Start activity for result
            startActivityForResult(intent, RC_SIGN_IN);
        });

        authViewModel.getUserAuthMutableLiveData().observe(getViewLifecycleOwner(), new Observer<UserAuth>() {
            @Override
            public void onChanged(UserAuth userAuth) {
                if (userAuth.isAuthenticated()) {
                    Utils.safelyNavigate(navController,R.id.action_signUpFragment_to_signInFragment,savedInstanceState);
                }
            }
        });

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

}