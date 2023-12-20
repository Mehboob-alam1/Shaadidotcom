package com.mehboob.myshadi.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mehboob.myshadi.room.Dao.UserDao;
import com.mehboob.myshadi.room.database.DataDatabase;
import com.mehboob.myshadi.room.models.User;

public class DataRepository {

    private Application application;
    private LiveData<User> userLiveData;

    private DataDatabase dataDatabase;

    public DataRepository(Application application) {
        this.application = application;
        dataDatabase = DataDatabase.getInstance(application);
        userLiveData = dataDatabase.userDao().getUserData();


    }


    public long checkIfInserted() {
        if (getUserMutableLiveData().getValue() != null && getUserMutableLiveData().getValue().isCreated()) {
            return 1;
        } else {
            return 0;
        }
    }

    public void insertData(User userMutableLiveData) {
        new InsertAsyncTask(dataDatabase).execute(userMutableLiveData);
    }

    public LiveData<User> getUserMutableLiveData() {
        return userLiveData;
    }

    static class InsertAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        InsertAsyncTask(DataDatabase dataDatabase) {
            userDao = dataDatabase.userDao();
        }


        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }
}
