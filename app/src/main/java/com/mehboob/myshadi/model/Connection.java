package com.mehboob.myshadi.model;

public class Connection {

    private String connectionFromId;
    private String connectionToId;
    private String timeStamp;

    private String combinedId;


    public Connection() {
    }

    public Connection(String connectionFromId, String connectionToId, String timeStamp, String combinedId) {
        this.connectionFromId = connectionFromId;
        this.connectionToId = connectionToId;
        this.timeStamp = timeStamp;
        this.combinedId = combinedId;
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
