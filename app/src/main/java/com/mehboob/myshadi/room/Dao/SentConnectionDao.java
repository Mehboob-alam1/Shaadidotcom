package com.mehboob.myshadi.room.Dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mehboob.myshadi.model.Connection;

import java.util.List;

@Dao
public interface SentConnectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSentConnection(Connection sentConnection);

    @Query("SELECT * FROM sent_connections")
    LiveData<List<Connection>> getSentConnections();
}
