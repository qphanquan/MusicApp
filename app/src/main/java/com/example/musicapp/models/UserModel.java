package com.example.musicapp.models;

public class UserModel {
    private String UserName;
    private String AccountName;
    private String PassWord;

    private String  ConfirmPassword;

    public UserModel(){}

    public UserModel(String accountName, String password){
        this.AccountName = accountName;
        this.PassWord = password;;;
    }
    public UserModel(String userName, String accountName, String password, String confirmpassword){
        this.UserName = userName;
        this.AccountName = accountName;
        this.PassWord = password;
        this.ConfirmPassword = confirmpassword;

    }

    public String getUserName(){return UserName;}
    public void setUserName(String userName){UserName = userName;}

    public String getAccountName(){return AccountName;}
    public void setAccountName(String accountName){AccountName = accountName;}

    public String getPassWord(){return PassWord;}
    public void setPassWord(String passWord){PassWord = passWord;}

    public String getConfirmPassword(){return ConfirmPassword;}
    public void setConfirmPassword(String confirmPassword){ConfirmPassword = confirmPassword;}


}
