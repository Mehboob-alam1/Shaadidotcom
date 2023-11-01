package com.mehboob.myshadi.repository;

import static android.provider.Settings.System.getString;

import android.app.Application;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mehboob.myshadi.R;

import java.util.concurrent.Executor;

public class GoogleSignInRepo  {

    private Application application;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private FirebaseAuth auth;

    private
    GoogleSignInOptions gso;

    private GoogleSignInClient googleSignInClient;

    public GoogleSignInRepo(Application application) {
        this.application = application;

        firebaseUserMutableLiveData = new MutableLiveData<>();
        auth = FirebaseAuth.getInstance();


        if (auth.getCurrentUser() != null) {
            firebaseUserMutableLiveData.postValue(auth.getCurrentUser());
        }
    }


}
