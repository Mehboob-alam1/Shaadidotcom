package com.mehboob.myshadi.room.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "User")
public class User {
    @PrimaryKey(autoGenerate = false)

    @ColumnInfo(name = "userName")
    private String userName;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "isAuthenticated")
    private  boolean isAuthenticated;
@ColumnInfo(name = "userId")
    private String userId;
@ColumnInfo(name = "isCreated")
    private boolean isCreated;

    public User(String userName, String name, String email, boolean isAuthenticated, String userId, boolean isCreated) {
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.isAuthenticated = isAuthenticated;
        this.userId = userId;
        this.isCreated = isCreated;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", isAuthenticated=" + isAuthenticated +
                ", userId='" + userId + '\'' +
                ", isCreated=" + isCreated +
                '}';
    }
}
