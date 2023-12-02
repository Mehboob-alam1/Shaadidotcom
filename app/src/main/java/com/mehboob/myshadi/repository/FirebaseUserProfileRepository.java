package com.mehboob.myshadi.repository;

import androidx.lifecycle.MutableLiveData;

public class FirebaseUserProfileRepository {


    private MutableLiveData<Boolean> uploadStatus= new MutableLiveData<>();

    public MutableLiveData<Boolean> getUploadStatus() {
        return uploadStatus;
    }




}
