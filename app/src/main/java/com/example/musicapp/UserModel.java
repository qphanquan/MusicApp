package com.example.musicapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserModel {
    private String Mail;

    private String Password;
    private String Confirmpassword;

    public UserModel(String string, String cursorString, String s, String string1){}

    public UserModel(String mail, String password){
        Mail = mail;
        Password = password;
    }

    public UserModel(String mail, String password, String confirmpassword){
        Mail = mail;
        Password = password;
        Confirmpassword = confirmpassword;;
    }

    public String getMail(){return Mail;}
    public void  setMail(String mail){Mail = mail;}

    public String getPassword(){return Password;}
    public void setPassword(String password){Password = password;}

    public String getConfirmpassword(){return Confirmpassword;}
    public void setConfirmpassword(String confirmpassword){Confirmpassword = confirmpassword;}

}