package com.mehboob.myshadi.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mehboob.myshadi.room.Dao.UserDao;
import com.mehboob.myshadi.room.database.AppDatabase;
import com.mehboob.myshadi.room.models.User;

public class DataRepository {
    private AppDatabase database;
    private LiveData<User> userData;
    private Application application;


    public DataRepository(Application application) {
        this.application = application;
        database = AppDatabase.getInstance(application);
    }

    public void insertUser(LiveData<User> userLiveData) {
new InsertAsyncTask(database).execute(userLiveData);
    }

    public LiveData<User> getUserData() {
        return userData;
    }

    static class InsertAsyncTask extends AsyncTask<LiveData<User>, Void, Void> {
        private UserDao userDao;

        InsertAsyncTask(AppDatabase appDatabase) {
            userDao = appDatabase.userDao();
        }

        @Override
        protected Void doInBackground(LiveData<User>... liveData) {

            userDao.insert(liveData[0].getValue());
            return null;
        }
    }
}
