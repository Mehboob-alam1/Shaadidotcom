package com.mehboob.myshadi.room.Dao;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mehboob.myshadi.room.models.User;

@Dao
public interface UserDao {


    //    @Insert
//    void  insert(LiveData<User> userMutableLiveData);
    @Insert
    void insert(User userLiveData);

    @Query("Delete from UserData")
    void deleteUserData();

    @Query("Select * from UserData")
    LiveData<User> getUserData();
}
