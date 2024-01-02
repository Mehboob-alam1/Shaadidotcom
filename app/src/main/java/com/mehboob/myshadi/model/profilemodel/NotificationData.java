package com.mehboob.myshadi.model.profilemodel;

public class NotificationData {

    private String fromImageUrl;
    private String fromUserName;

    private String fromUserId;
    private String fromMessage;
    private String userId;

    public NotificationData() {
    }

    public NotificationData(String fromImageUrl, String fromUserName, String fromUserId, String fromMessage, String userId) {
        this.fromImageUrl = fromImageUrl;
        this.fromUserName = fromUserName;
        this.fromUserId = fromUserId;
        this.fromMessage = fromMessage;
        this.userId = userId;
    }


    public String getFromImageUrl() {
        return fromImageUrl;
    }

    public void setFromImageUrl(String fromImageUrl) {
        this.fromImageUrl = fromImageUrl;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromMessage() {
        return fromMessage;
    }

    public void setFromMessage(String fromMessage) {
        this.fromMessage = fromMessage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}