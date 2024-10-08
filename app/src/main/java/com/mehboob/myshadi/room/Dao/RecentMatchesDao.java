package com.mehboob.myshadi.room.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mehboob.myshadi.model.profilemodel.UserProfile;
import com.mehboob.myshadi.room.entities.UserMatches;

import java.util.Date;
import java.util.List;

@Dao
public interface RecentMatchesDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecentMatches(List<UserMatches> userProfiles);



    @Delete
    void deleteUserMatches(UserMatches userMatches);
    @Query("SELECT * FROM userMatches")
    LiveData<List<UserMatches>> getAllUserProfiles();

    @Query("SELECT * FROM userMatches WHERE time > :weekAgoTimestamp ORDER BY time DESC")
    LiveData<List<UserMatches>> getUserProfilesCreatedLastWeek(long weekAgoTimestamp);

    @Query("SELECT * from userMatches where city_name =:city and community =:community and sub_community =:subCommunity and marital_status =:maritalStatus and dob between :minDob and :maxDob order by time desc")

    LiveData<List<UserMatches>> getBestMatchesPref( String city, String community, String subCommunity, String maritalStatus, long minDob, long maxDob);

    /// latest profile in order


    @Query("SELECT * FROM userMatches " +
            "WHERE (:userLatitude - latitude) * (:userLatitude - latitude) + " +
            "(:userLongitude - longitude) * (:userLongitude - longitude) <= :radiusSquared " +
            "ORDER BY (:userLatitude - latitude) * (:userLatitude - latitude) + " +
            "(:userLongitude - longitude) * (:userLongitude - longitude) ASC " +
            "LIMIT :limit")
    LiveData<List<UserMatches>> getNearestProfiles(double userLatitude, double userLongitude, double radiusSquared, int limit);

  

}
