package com.mehboob.myshadi.model.profilemodel;

public class Preferences {

    private String minAge;
    private String maxAge;
    private String minHeight;
    private String maxHeight;
    private String city;

    private String religion;

    private String community;

    private String subCommunity;

    private String maritalStatus;


    public Preferences(String minAge, String maxAge, String minHeight, String maxHeight, String city, String religion, String community, String subCommunity, String maritalStatus) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.city = city;
        this.religion = religion;
        this.community = community;
        this.subCommunity = subCommunity;
        this.maritalStatus = maritalStatus;
    }


    public Preferences(){

        this.minAge="21";
        this.maxAge="35";
        this.minHeight="Any";
        this.maxHeight="Any";
        this.city="Any";
        this.religion="Any";
        this.community="Any";
        this.subCommunity="Any";
        this.maritalStatus="Never Married";

    }
    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public String getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(String minHeight) {
        this.minHeight = minHeight;
    }

    public String getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(String maxHeight) {
        this.maxHeight = maxHeight;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getSubCommunity() {
        return subCommunity;
    }

    public void setSubCommunity(String subCommunity) {
        this.subCommunity = subCommunity;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
}
