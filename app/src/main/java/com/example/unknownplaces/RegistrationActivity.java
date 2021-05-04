package com.example.unknownplaces;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private TextInputEditText userName, userMobile, userEmail, userPassword, conPassword;
    private Button DoneBtn;
    private TextView alreadyuser;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        loadingDialog = new Dialog(RegistrationActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        userName = findViewById(R.id.user_name);
        userMobile = findViewById(R.id.userMobile);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.user_password);
        conPassword = findViewById(R.id.com_user_password);
        DoneBtn = findViewById(R.id.doneRegistartion);
        alreadyuser = findViewById(R.id.alreadyuser);
        DoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(userName.getText())) {
                    if (!TextUtils.isEmpty(userMobile.getText())) {
                        if (!TextUtils.isEmpty(userEmail.getText())) {
                            if (!TextUtils.isEmpty(userPassword.getText())) {
                                if (!TextUtils.isEmpty(conPassword.getText())) {
                                    if (conPassword.getText().toString().equals(userPassword.getText().toString())) {
                                        createUser(userName.getText().toString(), userMobile.getText().toString(), userEmail.getText().toString(), userPassword.getText().toString());
                                    } else {
                                        conPassword.requestFocus();
                                        Toast.makeText(RegistrationActivity.this, "Password Doesn't Match", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    conPassword.requestFocus();
                                    Toast.makeText(RegistrationActivity.this, "Please Confirm Your Password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                userPassword.requestFocus();
                                Toast.makeText(RegistrationActivity.this, "Password Field can't be Empty", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            userEmail.requestFocus();
                            Toast.makeText(RegistrationActivity.this, "Email Field can't be Empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        userMobile.requestFocus();
                        Toast.makeText(RegistrationActivity.this, "Mobile Field can't be Empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    userName.requestFocus();
                    Toast.makeText(RegistrationActivity.this, "Name Field can't be Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void createUser(final String name, final String mobile, final String email, String password) {
        loadingDialog.show();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        if (email.matches(emailPattern)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("Name", name);
                        userMap.put("Mobile", mobile);
                        userMap.put("Email", email);
                        userMap.put("Profile", "default");
                        userMap.put("UserId", FirebaseAuth.getInstance().getUid());
                        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).set(userMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent homeIntent = new Intent(RegistrationActivity.this, HomeActivity.class);
                                            startActivity(homeIntent);
                                            finish();
                                            Toast.makeText(RegistrationActivity.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(RegistrationActivity.this, "Something wrong please try Again!", Toast.LENGTH_SHORT).show();
                                        }
                                        loadingDialog.dismiss();

                                    }
                                });
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, "Something wrong please try Again!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else {
            loadingDialog.dismiss();
            userEmail.requestFocus();
            Toast.makeText(RegistrationActivity.this, "Please Provide Valid Email", Toast.LENGTH_SHORT).show();
        }

    }
}
