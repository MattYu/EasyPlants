package com.example.gamingpc.easyplants.Models;

public class User {

    private int userId;
    private String userName;
    private Password userPass;
    private String userEmail;
    private int userType;
    private int userSensorCount;
    private int userNotificationOn;
    private int userDeleted;

    public User(int userId, String userName, String userPass, String userEmail, int userType, int userSensorCount, int userNotificationOn, int userDeleted) {
        this.userId = userId;
        this.userName = userName;
        this.userPass = new Password(userPass);
        this.userEmail = userEmail;
        this.userType = userType;
        this.userSensorCount = userSensorCount;
        this.userNotificationOn = userNotificationOn;
        this.userDeleted = userDeleted;
    }

    public User(String userName, String userPass, String userEmail) {
        this.userId = -1;
        this.userType = 0;
        this.userSensorCount = 0;
        this.userNotificationOn = 1;
        this.userDeleted = 0;
        this.userName = userName;
        this.userPass = new Password(userPass);
        this.userEmail = userEmail;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPass() { //Should return a hashed version of the password
        return userPass.getHashedPassword();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getUserType() {
        return userType;
    }

    public int getUserSensorCount() {
        return userSensorCount;
    }

    public int getUserNotificationOn() {
        return userNotificationOn;
    }

    public int getUserDeleted() {
        return userDeleted;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void setUserSensorCount(int userSensorCount) {
        this.userSensorCount = userSensorCount;
    }

    public void setUserNotificationOn(int userNotificationOn) {
        this.userNotificationOn = userNotificationOn;
    }

    public void setUserDeleted(int userDeleted) {
        this.userDeleted = userDeleted;
    }

    public void setUserPass(String userPass) {
        this.userPass = new Password(userPass);
    }

}
