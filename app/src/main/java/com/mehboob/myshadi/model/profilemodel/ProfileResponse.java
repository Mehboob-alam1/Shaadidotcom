package com.mehboob.myshadi.model.profilemodel;

public class ProfileResponse {


    private boolean isUpload;

    private String message;

    public ProfileResponse(boolean isUpload, String message) {
        this.isUpload = isUpload;
        this.message = message;
    }

    public ProfileResponse() {
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
