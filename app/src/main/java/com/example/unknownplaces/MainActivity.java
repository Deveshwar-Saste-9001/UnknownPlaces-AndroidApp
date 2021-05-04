package com.example.unknownplaces;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (CurrentUser == null) {
                    Intent mainintent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(mainintent);
                    finish();
                } else {
                    FirebaseFirestore.getInstance().collection("Users").document(CurrentUser.getUid()).update("Last seen", FieldValue.serverTimestamp())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent Homeintent = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(Homeintent);
                                        finish();
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

        }, 3000);

    }


}
