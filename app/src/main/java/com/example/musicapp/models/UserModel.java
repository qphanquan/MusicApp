package com.example.musicapp.models;

public class UserModel {
    private String UserName;
    private String Email;
    private String Password;
    private String ConfirmPassword;

    public UserModel(String userName, String email, String password, String confirmPassword) {
        UserName = userName;
        Email = email;
        Password = password;
        ConfirmPassword = confirmPassword;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }
}
