package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.musicapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;



import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

public class LoginActivity extends AppCompatActivity {
    EditText emailAddressTxt;
    EditText passwordTxt;
    Button loginBtn;
    TextView signupTxtView;
    TextView forgotpassTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Init();
    }

    private void Init() {
        this.emailAddressTxt = this.findViewById(R.id.login_emailAddress);
        this.passwordTxt = this.findViewById(R.id.login_password);
        this.loginBtn = this.findViewById(R.id.login_btn);
        this.signupTxtView = this.findViewById(R.id.goto_signup);
        this.forgotpassTxtView = this.findViewById(R.id.Txtforgotpass);

        this.signupTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SignUpActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        this.forgotpassTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ForgotpasswordActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        this.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = emailAddressTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                loginWithFirebase(emailAddress, password);
                //checkUserCredentials(accountName, password);
            }
        });
    }

    private void loginWithFirebase(String email, String pass){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            //finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login account failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkUserCredentials(String accountName, String password) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User")
                .whereEqualTo("accountName", accountName)
                .whereEqualTo("passWord", password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // User found
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Close the login activity to prevent going back to it
                        } else {
                            // User not found
                            Toast.makeText(LoginActivity.this, "Account or password is incorrect", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
