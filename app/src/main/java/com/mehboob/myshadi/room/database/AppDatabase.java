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
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "UserData";

    public abstract UserDao userDao();

    public static volatile AppDatabase instance = null;


    public static AppDatabase getInstance(Context context) {

        if (instance == null) {


            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
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

        PopulateAsyncTask(AppDatabase database) {
            userDao = database.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
