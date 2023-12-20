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

    @Query("SELECT * FROM userMatches " +
            "WHERE gender = :gender AND religion = :religion AND marital_status = :maritalStatus " +
            "AND stateName = :stateName AND cityName = :cityName")
    LiveData<List<UserProfileEntity>> getUserProfilesWithPreferences(String gender, String religion, String maritalStatus, String stateName, String cityName);


    /// latest profile in order
    @Query("SELECT * FROM userMatches WHERE dob BETWEEN :minDob AND :maxDob ORDER BY time DESC")
    LiveData<List<UserProfileEntity>> getUserProfilesByAgeRange(Date minDob, Date maxDob);

}
