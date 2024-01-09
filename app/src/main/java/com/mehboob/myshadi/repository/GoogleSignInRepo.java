package com.mehboob.myshadi.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mehboob.myshadi.model.profilemodel.UserAuth;

public class GoogleSignInRepo {

    private Application application;
    private MutableLiveData<Boolean> isLoggedIn;
    private FirebaseAuth auth;
    private MutableLiveData<UserAuth> authenticatedUserMutableLiveData;


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
                    String photoUrl = "";
                    String uid = firebaseUser.getUid();
                    String name = firebaseUser.getDisplayName();
                    String email = firebaseUser.getEmail();
                    String userId = firebaseUser.getUid();
                    if (firebaseUser.getPhotoUrl() != null) {
                        photoUrl = firebaseUser.getPhotoUrl().toString();
                    }
                    UserAuth user = new UserAuth(uid, name, email, true, isNewUser, true, userId, photoUrl);
                    user.setNew(isNewUser);
                    authenticatedUserMutableLiveData.postValue(user);
                    //  authenticatedUserMutableLiveData.setValue(user);
                    isLoggedIn.postValue(true);
                    Toast.makeText(application, "User posted successfully", Toast.LENGTH_SHORT).show();
                }
            } else {
                isLoggedIn.postValue(false);
                Toast.makeText(application, "" + authTask.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return authenticatedUserMutableLiveData;
    }


}
