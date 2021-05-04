package com.example.unknownplaces;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText loginid, loginPassword;
    private Button signup, signin;
    private TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginid = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginuser_password);
        signup = findViewById(R.id.SignupBtn);
        signin = findViewById(R.id.SignInbtn);
        forgetPassword = findViewById(R.id.forgetPassword);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RegIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(RegIntent);
                finish();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(loginid.getText())) {
                    if (!TextUtils.isEmpty(loginPassword.getText())) {
                        LoginUser(loginid.getText().toString(), loginPassword.getText().toString());
                    } else {
                        loginPassword.requestFocus();
                        Toast.makeText(LoginActivity.this, "Password Field can't be empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loginid.requestFocus();
                    Toast.makeText(LoginActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void LoginUser(String email, String Password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "Login Successfuly", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
