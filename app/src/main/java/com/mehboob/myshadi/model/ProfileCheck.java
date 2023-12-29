package com.mehboob.myshadi.model;

public class ProfileCheck {

    private boolean profileCompleted;
    private boolean profileVerified;
    private String profileCreatedTime;
    private String userId;


    public ProfileCheck() {
    }

    public ProfileCheck(boolean profileCompleted, boolean profileVerified, String profileCreatedTime, String userId) {
        this.profileCompleted = profileCompleted;
        this.profileVerified = profileVerified;
        this.profileCreatedTime = profileCreatedTime;
        this.userId = userId;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }

    public boolean isProfileVerified() {
        return profileVerified;
    }

    public void setProfileVerified(boolean profileVerified) {
        this.profileVerified = profileVerified;
    }

    public String getProfileCreatedTime() {
        return profileCreatedTime;
    }

    public void setProfileCreatedTime(String profileCreatedTime) {
        this.profileCreatedTime = profileCreatedTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
