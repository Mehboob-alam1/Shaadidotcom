package com.mehboob.myshadi.room.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mehboob.myshadi.room.entities.UserProfileData;
@Dao
public interface UserProfileDataDao {

    @Insert
    void insertUserProfile(UserProfileData userProfile);

    @Query("delete from userProfileOwnData")
    void deleteUserProfileData();

    @Query("Select * from userProfileOwnData")
    LiveData<UserProfileData> getUserProfileLiveData();
}
