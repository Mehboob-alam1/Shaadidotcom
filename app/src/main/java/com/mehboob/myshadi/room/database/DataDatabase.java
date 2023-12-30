package com.mehboob.myshadi.room.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.common.collect.HashBasedTable;
import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.Dao.RecentMatchesDao;
import com.mehboob.myshadi.room.Dao.UserDao;
import com.mehboob.myshadi.room.Dao.UserProfileDataDao;
import com.mehboob.myshadi.room.entities.ArrayListConverter;
import com.mehboob.myshadi.room.entities.PreferencesConverter;
import com.mehboob.myshadi.room.entities.UserMatches;
import com.mehboob.myshadi.room.entities.UserProfileData;
import com.mehboob.myshadi.room.models.User;

import java.util.List;

@Database(entities = {User.class, UserMatches.class,UserProfileData.class}, version = 1, exportSchema = false)
@TypeConverters({PreferencesConverter.class, ArrayListConverter.class,})

public abstract class DataDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "UserData";

    public abstract UserDao userDao();

    public abstract RecentMatchesDao recentMatchesDao();

    public abstract UserProfileDataDao userProfileDataDao();

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
            new InserUserAsyncTask(instance);
        }
    };


    static class PopulateAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDao userDao;


        public PopulateAsyncTask(DataDatabase dataDatabase) {
            userDao = dataDatabase.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            userDao.deleteUserData();
            return null;
        }
    }


    static class InserUserAsyncTask extends AsyncTask<List<UserProfile>, Void, Void> {
        private RecentMatchesDao recentMatchesDao;


        public InserUserAsyncTask(DataDatabase dataDatabase) {
            recentMatchesDao = dataDatabase.recentMatchesDao();
        }

        @Override
        protected Void doInBackground(List<UserProfile>... lists) {


            return null;
        }
    }

    static class InsertProfileTask extends AsyncTask<UserProfileData, Void, Void> {
        private UserProfileDataDao userProfileDataDao;

        public InsertProfileTask(DataDatabase dataDatabase) {
            userProfileDataDao = dataDatabase.userProfileDataDao();
        }

        @Override
        protected Void doInBackground(UserProfileData... userProfileData) {
            userProfileDataDao.deleteUserProfileData();
            return null;
        }
    }
}
