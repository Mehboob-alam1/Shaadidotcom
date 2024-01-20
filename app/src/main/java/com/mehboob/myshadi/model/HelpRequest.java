package com.mehboob.myshadi.model;

public class HelpRequest {
    private String userId;
    private String userName;

    private String userGender;

    private String pushId;

    private String typeOfIssue;
    private String subject;
    private String description;


    public HelpRequest() {
    }

    public HelpRequest(String userId, String userName, String userGender, String pushId, String typeOfIssue, String subject, String description) {
        this.userId = userId;
        this.userName = userName;
        this.userGender = userGender;
        this.pushId = pushId;
        this.typeOfIssue = typeOfIssue;
        this.subject = subject;
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getTypeOfIssue() {
        return typeOfIssue;
    }

    public void setTypeOfIssue(String typeOfIssue) {
        this.typeOfIssue = typeOfIssue;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
