package com.mehboob.myshadi.model;

public class ChatMessages {

    private String senderId;
    private String recieverId;
    private String message;
    private String pushId;
    private String timeStamp;


    public ChatMessages(String senderId, String recieverId, String message, String pushId, String timeStamp) {
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.message = message;
        this.pushId = pushId;
        this.timeStamp = timeStamp;
    }

    public ChatMessages() {
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "ChatMessages{" +
                "senderId='" + senderId + '\'' +
                ", recieverId='" + recieverId + '\'' +
                ", message='" + message + '\'' +
                ", pushId='" + pushId + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
