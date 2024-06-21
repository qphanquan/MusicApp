package com.example.musicapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    EditText userNameTxt;

    EditText accoutNameTxt;
    EditText passWordTxt;

    EditText confirmPasswordTxt;

    Button createaccountTxt;

    TextView userTextView;
    TextView emptyView;
    private List<UserModel> userList;

    private void Init(){
        this.userNameTxt = this.findViewById(R.id.sigup_UserName);
        this.accoutNameTxt = this.findViewById(R.id.sigup_AccountName);
        this.passWordTxt = this.findViewById(R.id.sigup_password);
        this.confirmPasswordTxt = this.findViewById(R.id.sigup_confirmPassword);
        this.createaccountTxt = this.findViewById(R.id.sigup_btn);
        this.userTextView = this.findViewById(R.id.user_text_view);
        this.emptyView = this.findViewById(R.id.empty_view);
        this.createaccountTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel userModel = new UserModel(userNameTxt.getText().toString(), accoutNameTxt.getText().toString(), passWordTxt.getText().toString(), confirmPasswordTxt.getText().toString());
                userList.add(userModel);
                updateUserTextView();
                updateEmptyView();
            }
        });

    }

    private void updateUserTextView() {
        StringBuilder userDisplay = new StringBuilder();
        for (UserModel user : userList) {
            userDisplay.append(user.getAccountName()).append("\n");
        }
        userTextView.setText(userDisplay.toString());
    }

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
    }
    private void loadUser(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            userList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                UserModel user = document.toObject(UserModel.class);
                                user.setAccountName(document.getId());
                                userList.add(user);
                            }
                            updateUserTextView();
                            updateEmptyView();
                        } else {

                            // Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
   }

    private void updateEmptyView() {
        if (userList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }

    }

}