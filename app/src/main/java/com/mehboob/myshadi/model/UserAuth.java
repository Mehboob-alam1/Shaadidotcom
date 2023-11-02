package com.mehboob.myshadi.model;

public class UserAuth {

    private String userName;
    private String name;
    private String email;
    private  boolean isAuthenticated;
    private boolean isNew;
    private boolean isCreated;

    private String userId;

    public UserAuth(String userName, String name, String email, boolean isAuthenticated, boolean isNew, boolean isCreated,String userId) {
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.isAuthenticated = isAuthenticated;
        this.isNew = isNew;
        this.isCreated = isCreated;
        this.userId=userId;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }
}
