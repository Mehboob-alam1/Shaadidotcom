package com.mehboob.myshadi.room.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.entities.UserMatches;

import java.util.List;

@Dao
public interface RecentMatchesDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecentMatches(List<UserMatches> userProfiles);

    @Query("Delete from userMatches")
    void deleteUserData();
    @Query("SELECT * FROM userMatches")
    LiveData<List<UserMatches>> getAllUserProfiles();

    @Query("SELECT * FROM userMatches WHERE time > :weekAgoTimestamp ORDER BY time DESC")
    LiveData<List<UserMatches>> getUserProfilesCreatedLastWeek(long weekAgoTimestamp);
}
