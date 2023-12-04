package com.mehboob.myshadi.model.profilemodel;

public class UserProfile {


    private String profileFor;
    private String gender;
    private String fullName;
    private String dob;
    private String religion;
    private String community;
    private String livingIn;
    private String email;
    private String phoneNumber;
    private String countryCode;
    private String stateName;
    private String stateCode;
    private String cityName;
    private String subCommunity;

    private String maritalStatus;

    private String children;

    private String height;

    private String diet;
    private String qualifications;

    private String college;

    private String income;

    private String worksWith;

    private String workAs;

    private String imageUrl;
    private String userId;


    public UserProfile() {
    }

    public UserProfile(String profileFor, String gender, String fullName, String dob, String religion, String community, String livingIn, String email, String phoneNumber, String countryCode, String stateName, String stateCode, String cityName, String subCommunity, String maritalStatus, String children, String height, String diet, String qualifications, String college, String income, String worksWith, String workAs, String imageUrl, String userId) {
        this.profileFor = profileFor;
        this.gender = gender;
        this.fullName = fullName;
        this.dob = dob;
        this.religion = religion;
        this.community = community;
        this.livingIn = livingIn;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
        this.stateName = stateName;
        this.stateCode = stateCode;
        this.cityName = cityName;
        this.subCommunity = subCommunity;
        this.maritalStatus = maritalStatus;
        this.children = children;
        this.height = height;
        this.diet = diet;
        this.qualifications = qualifications;
        this.college = college;
        this.income = income;
        this.worksWith = worksWith;
        this.workAs = workAs;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getWorksWith() {
        return worksWith;
    }

    public void setWorksWith(String worksWith) {
        this.worksWith = worksWith;
    }

    public String getWorkAs() {
        return workAs;
    }

    public void setWorkAs(String workAs) {
        this.workAs = workAs;
    }

    public String getProfileFor() {
        return profileFor;
    }

    public void setProfileFor(String profileFor) {
        this.profileFor = profileFor;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public String getLivingIn() {
        return livingIn;
    }

    public void setLivingIn(String livingIn) {
        this.livingIn = livingIn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getSubCommunity() {
        return subCommunity;
    }

    public void setSubCommunity(String subCommunity) {
        this.subCommunity = subCommunity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "profileFor='" + profileFor + '\'' +
                ", gender='" + gender + '\'' +
                ", fullName='" + fullName + '\'' +
                ", dob='" + dob + '\'' +
                ", religion='" + religion + '\'' +
                ", community='" + community + '\'' +
                ", livingIn='" + livingIn + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", stateName='" + stateName + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", cityName='" + cityName + '\'' +
                ", subCommunity='" + subCommunity + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
