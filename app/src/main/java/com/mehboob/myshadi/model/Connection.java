package com.mehboob.myshadi.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sent_connections")

public class Connection {

    @ColumnInfo(name = "connectionFromId")
    private String connectionFromId;
    @ColumnInfo(name = "connectionToId")
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String connectionToId;
    @ColumnInfo(name = "timeStamp")

    private String timeStamp;
    @ColumnInfo(name = "combinedId")


    private String combinedId;
    @ColumnInfo(name = "status")

    private String status;
    @ColumnInfo(name = "connected")

    private boolean connected;
    @ColumnInfo(name = "connectionFromGender")

    private String connectionFromGender;
    @ColumnInfo(name = "connectionToGender")

    private String connectionToGender;

    public String getConnectionFromGender() {
        return connectionFromGender;
    }

    public void setConnectionFromGender(String connectionFromGender) {
        this.connectionFromGender = connectionFromGender;
    }

    public String getConnectionToGender() {
        return connectionToGender;
    }

    public void setConnectionToGender(String connectionToGender) {
        this.connectionToGender = connectionToGender;
    }

    public Connection() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Connection(String connectionFromId, String connectionToId, String timeStamp, String combinedId, String status, boolean connected, String connectionFromGender, String connectionToGender) {
        this.connectionFromId = connectionFromId;
        this.connectionToId = connectionToId;
        this.timeStamp = timeStamp;
        this.combinedId = combinedId;
        this.status = status;
        this.connected = connected;
        this.connectionFromGender = connectionFromGender;
        this.connectionToGender = connectionToGender;
    }

    public String getConnectionFromId() {
        return connectionFromId;
    }

    public void setConnectionFromId(String connectionFromId) {
        this.connectionFromId = connectionFromId;
    }

    public String getConnectionToId() {
        return connectionToId;
    }

    public void setConnectionToId(String connectionToId) {
        this.connectionToId = connectionToId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCombinedId() {
        return combinedId;
    }

    public void setCombinedId(String combinedId) {
        this.combinedId = combinedId;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "connectionFromId='" + connectionFromId + '\'' +
                ", connectionToId='" + connectionToId + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", combinedId='" + combinedId + '\'' +
                '}';
    }
}
