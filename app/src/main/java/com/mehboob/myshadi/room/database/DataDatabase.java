package com.mehboob.myshadi.room.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mehboob.myshadi.room.Dao.UserDao;
import com.mehboob.myshadi.room.models.User;

@Database(entities = {User.class}, version = 1)
public abstract class DataDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "UserData";

    public abstract UserDao userDao();


    private static volatile DataDatabase instance = null;

    public static DataDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (DataDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, DataDatabase.class, DATABASE_NAME)
                            .addCallback(callback)
                            .build();
                }
            }
        }

        return instance;
    }


    static Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsyncTask(instance);
        }
    };


    static class PopulateAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDao userDao;

        private UserDao dao;
        public PopulateAsyncTask(DataDatabase dataDatabase) {
            userDao = dataDatabase.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            userDao.deleteUserData();
            return null;
        }
    }
}
