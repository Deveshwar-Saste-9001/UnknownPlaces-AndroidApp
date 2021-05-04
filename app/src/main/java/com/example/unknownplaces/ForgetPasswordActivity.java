package com.example.unknownplaces;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText registerEmial;
    private Button resetPasswordButton;
    private TextView GoBack;

    private ViewGroup emailIconContainer;
    private ImageView emailIcon;
    private TextView emailIconTextView;
    private ProgressBar forgetPassprogressBar;

    private FirebaseAuth firebaseAuth;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        registerEmial = findViewById(R.id.forgot_password_email);
        resetPasswordButton = findViewById(R.id.ResetPassWordBtn);
        GoBack = findViewById(R.id.forget_password_goBack);
        emailIconContainer = findViewById(R.id.forget_pass_icon_container);
        emailIconTextView = findViewById(R.id.forget_pass_textview);
        loadingDialog = new Dialog(ForgetPasswordActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        firebaseAuth = FirebaseAuth.getInstance();

        registerEmial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPasswordButton.setEnabled(false);
                loadingDialog.show();
                firebaseAuth.sendPasswordResetEmail(registerEmial.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    emailIconTextView.setText("Reset Password Mail Send to your mail id please check inbox");
                                    emailIconTextView.setVisibility(View.VISIBLE);
                                    loadingDialog.dismiss();
                                    resetPasswordButton.setEnabled(true);
                                } else {
                                    String error = task.getException().getMessage();
                                    emailIconTextView.setText(error);
                                    emailIconTextView.setVisibility(View.VISIBLE);
                                    loadingDialog.dismiss();
                                    resetPasswordButton.setEnabled(true);
                                }

                            }
                        });
            }
        });


    }

    private void checkInputs() {
        if (TextUtils.isEmpty(registerEmial.getText())) {
            resetPasswordButton.setEnabled(false);
        } else {
            resetPasswordButton.setEnabled(true);

        }


    }
}
