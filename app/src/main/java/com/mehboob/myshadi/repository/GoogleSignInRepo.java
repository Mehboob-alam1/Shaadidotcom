package com.mehboob.myshadi.repository;

import static android.provider.Settings.System.getString;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

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
import com.mehboob.myshadi.model.UserAuth;

import java.util.concurrent.Executor;

public class GoogleSignInRepo  {

    private Application application;
    private MutableLiveData<Boolean> isLoggedIn;
    private FirebaseAuth auth;
private  MutableLiveData<UserAuth> authenticatedUserMutableLiveData;


//    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
//    private CollectionReference usersRef = rootRef.collection(USERS);
    public GoogleSignInRepo(Application application) {
        this.application = application;

        isLoggedIn = new MutableLiveData<>();
        auth = FirebaseAuth.getInstance();
        authenticatedUserMutableLiveData = new MutableLiveData<>();



    }

    public MutableLiveData<Boolean> getIsLoggedIn() {
        return isLoggedIn;
    }



    public MutableLiveData<UserAuth> getAuthenticatedUserMutableLiveData() {
        return authenticatedUserMutableLiveData;
    }



    // Sign in using Google
    public MutableLiveData<UserAuth> firebaseSignInWithGoogle(AuthCredential googleAuthCredential) {


        auth.signInWithCredential(googleAuthCredential).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                Boolean isNewUser = authTask.getResult().getAdditionalUserInfo().isNewUser();
                FirebaseUser firebaseUser = auth.getCurrentUser();

                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();
                    String name = firebaseUser.getDisplayName();
                    String email = firebaseUser.getEmail();
                     String userId= firebaseUser.getUid();

                    UserAuth user = new UserAuth(uid, name, email,true,isNewUser,true,userId);
                    user.setNew(isNewUser);
                    authenticatedUserMutableLiveData.postValue(user);
                    authenticatedUserMutableLiveData.setValue(user);
                    isLoggedIn.postValue(true);
                    Toast.makeText(application, "User posted successfully", Toast.LENGTH_SHORT).show();
                }
            } else {
                isLoggedIn.postValue(false);
                Toast.makeText(application, ""+authTask.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return authenticatedUserMutableLiveData;
    }


}
