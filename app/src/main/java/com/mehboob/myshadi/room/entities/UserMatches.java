package com.mehboob.myshadi.room.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.mehboob.myshadi.model.profilemodel.Preferences;

import java.util.ArrayList;

@Entity(tableName = "userMatches",primaryKeys = {"userId"})
public class UserMatches {


@ColumnInfo(name="profile_for")
    private String profileFor;
    @ColumnInfo(name="gender")

    private String gender;

    @ColumnInfo(name="full_name")

    private String fullName;
    @ColumnInfo(name="dob")

    private String dob;
    @ColumnInfo(name="religion")

    private String religion;
    @ColumnInfo(name="community")

    private String community;
    @ColumnInfo(name="living_in")

    private String livingIn;
    @ColumnInfo(name="email")

    private String email;
    @ColumnInfo(name="phone_number")

    private String phoneNumber;
    @ColumnInfo(name="country_code")

    private String countryCode;
    @ColumnInfo(name="state_name")

    private String stateName;
    @ColumnInfo(name="state_code")

    private String stateCode;
    @ColumnInfo(name="city_name")

    private String cityName;
    @ColumnInfo(name="sub_community")

    private String subCommunity;
    @ColumnInfo(name="marital_status")


    private String maritalStatus;
    @ColumnInfo(name="children")


    private String children;
    @ColumnInfo(name="height")

    private String height;
    @ColumnInfo(name="diet")


    private String diet;
    @ColumnInfo(name="qualifications")

    private String qualifications;
    @ColumnInfo(name="college")


    private String college;
    @ColumnInfo(name="income")


    private String income;
    @ColumnInfo(name="works_with")

    private String worksWith;
    @ColumnInfo(name="works_as")


    private String workAs;
    @ColumnInfo(name="image_url")

    private String imageUrl;
    @ColumnInfo(name="userId")
@NonNull
    private String userId;
    @ColumnInfo(name="images")
@TypeConverters(ArrayListConverter.class)
    private ArrayList<String> images;

    @ColumnInfo(name="isProfile_complete")

    private boolean isProfileComplete;
    @ColumnInfo(name="account_type")

    private String accountType;
    @ColumnInfo(name="is_verified")

    private boolean isVerified;
    @ColumnInfo(name="time")

    private String time;

    @TypeConverters(PreferencesConverter.class)
    @ColumnInfo(name="preferences")

    private Preferences preferences;
    @ColumnInfo(name="latitude")

    private String latitude;
    @ColumnInfo(name="longitude")

    private String longitude;
    @ColumnInfo(name="about_me")

    private String aboutMe;
@ColumnInfo(name = "date_of_birth")
    private String date_of_birth;
@ColumnInfo(name = "token")
private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public boolean isProfileComplete() {
        return isProfileComplete;
    }

    public void setProfileComplete(boolean profileComplete) {
        isProfileComplete = profileComplete;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }


    @Override
    public String toString() {
        return "UserMatches{" +
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
                ", maritalStatus='" + maritalStatus + '\'' +
                ", children='" + children + '\'' +
                ", height='" + height + '\'' +
                ", diet='" + diet + '\'' +
                ", qualifications='" + qualifications + '\'' +
                ", college='" + college + '\'' +
                ", income='" + income + '\'' +
                ", worksWith='" + worksWith + '\'' +
                ", workAs='" + workAs + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", userId='" + userId + '\'' +
                ", images=" + images +
                ", isProfileComplete=" + isProfileComplete +
                ", accountType='" + accountType + '\'' +
                ", isVerified=" + isVerified +
                ", time='" + time + '\'' +
                ", preferences=" + preferences +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", aboutMe='" + aboutMe + '\'' +
                '}';
    }
}
