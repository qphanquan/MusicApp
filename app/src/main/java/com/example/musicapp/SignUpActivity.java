package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.models.FavoriteModel;
import com.example.musicapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    EditText userNameTxt;
    EditText emailAddressTxt;
    EditText passWordTxt;
    EditText confirmPasswordTxt;
    Button createAccountBtn;
    TextView loginTxtView;

    String userName;
    String emailAddress;
    String password;
    String confirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Init();
    }

    private void Init(){
        this.userNameTxt = this.findViewById(R.id.signup_username);
        this.emailAddressTxt = this.findViewById(R.id.signup_emailAddress);
        this.passWordTxt = this.findViewById(R.id.signup_password);
        this.confirmPasswordTxt = this.findViewById(R.id.signup_confirmPassword);
        this.createAccountBtn = this.findViewById(R.id.create_account_btn);
        this.loginTxtView = this.findViewById(R.id.goto_login);
        this.createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = userNameTxt.getText().toString();
                emailAddress = emailAddressTxt.getText().toString();
                password =  passWordTxt.getText().toString();
                confirmPassword = confirmPasswordTxt.getText().toString();

                if(checkAccountUser(userName, emailAddress, password, confirmPassword)){
                    UserModel userModel = new UserModel(userName, emailAddress, password, confirmPassword);
                    addUser(userModel);
                }
            }
        });

        this.loginTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                v.getContext().startActivity(intent);
            }
        });

    }

    private boolean checkAccountUser(String user, String email, String password, String confirmPassword){

        if(TextUtils.isEmpty(user)){
            this.userNameTxt.setError("Not null");
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            this.emailAddressTxt.setError("Invalid email");
            return false;
        }

        if(password.length() < 6){
            this.passWordTxt.setError("Length should be 6 char");
            return false;
        }

        if(!password.equals(confirmPassword)){
            this.confirmPasswordTxt.setError("Password not matched");
            return false;
        }

        return true;
    }

    private void addUser(UserModel userModel) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userModel.getEmail(), userModel.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                            //finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Create account failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }