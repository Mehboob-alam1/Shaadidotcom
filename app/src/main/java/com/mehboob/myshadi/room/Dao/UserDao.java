package com.mehboob.myshadi.room.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mehboob.myshadi.room.models.User;

@Dao
public interface UserDao {



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

    @Query("Delete From User")
    void deleteUser();

    @Query("Select * from User")
    LiveData<User> getUserData();
}
