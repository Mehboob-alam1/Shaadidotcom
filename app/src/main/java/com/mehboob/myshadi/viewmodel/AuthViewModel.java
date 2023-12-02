package com.mehboob.myshadi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthCredential;
import com.mehboob.myshadi.model.profilemodel.UserAuth;
import com.mehboob.myshadi.repository.GoogleSignInRepo;

public class AuthViewModel extends AndroidViewModel {

    private GoogleSignInRepo googleSignInRepo;
    private MutableLiveData<UserAuth> userAuthMutableLiveData;
    private MutableLiveData<Boolean> loggedState;

    public AuthViewModel(@NonNull Application application) {
        super(application);

        googleSignInRepo=new GoogleSignInRepo(application);
        userAuthMutableLiveData=googleSignInRepo.getAuthenticatedUserMutableLiveData();
        loggedState=googleSignInRepo.getIsLoggedIn();

    }

    public void signInGoogle(AuthCredential authCredential){
        googleSignInRepo.firebaseSignInWithGoogle(authCredential);
    }

    public MutableLiveData<UserAuth> getUserAuthMutableLiveData() {
        return userAuthMutableLiveData;
    }

    public MutableLiveData<Boolean> getLoggedState() {
        return loggedState;
    }
}
