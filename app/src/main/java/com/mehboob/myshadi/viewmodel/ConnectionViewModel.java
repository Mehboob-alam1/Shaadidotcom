package com.mehboob.myshadi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mehboob.myshadi.model.Connection;
import com.mehboob.myshadi.repository.MakeConnectionRepository;

import java.util.List;

public class ConnectionViewModel extends AndroidViewModel {

    private MakeConnectionRepository repository;
    private MutableLiveData<List<Connection>> connectedUsers;
    private MutableLiveData<Boolean> connected;
    public ConnectionViewModel(@NonNull Application application) {
        super(application);

        repository= new MakeConnectionRepository(application);

        connected=repository.getConnected();
    }

    public void connectBothUsers(Connection connection ){
        repository.connectBothUsers(connection);
    }

    public MutableLiveData<List<Connection>> getConnectedUsers(String userId) {

        connectedUsers=repository.getConnectedProfiles(userId);
        return connectedUsers;
    }

    public MutableLiveData<Boolean> getConnected() {
        return connected;
    }
}
