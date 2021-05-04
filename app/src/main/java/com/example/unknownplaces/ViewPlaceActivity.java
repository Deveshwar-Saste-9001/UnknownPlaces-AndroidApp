package com.example.unknownplaces;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewPlaceActivity extends AppCompatActivity {
    private Button getDirectionBtn, submitReviewBtn;
    private ImageView placeImageFull;
    private Dialog loadingDialog;
    private TextView placeTitle, placeAddress, placeRatingmini, placetotalRatingmini, placeDecription;
    private ProgressBar roadProgress, sefetyProgress, touristProgress, adventureProgress, costProgress;
    private RecyclerView reviewRecyclerView;
    private RatingBar ratingBar;
    private EditText review;
    private String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_place);
        getDirectionBtn = findViewById(R.id.getDirectionBtn);
        placeImageFull = findViewById(R.id.placeimagefull);
        submitReviewBtn = findViewById(R.id.submitReviewBtn);
        placeTitle = findViewById(R.id.placeTitleFull);
        placeAddress = findViewById(R.id.placeAddressFull);
        placeRatingmini = findViewById(R.id.tv_product_rating_miniView_wish1);
        placetotalRatingmini = findViewById(R.id.totalRetingmintext);
        placeDecription = findViewById(R.id.placeDecriptiontext);
        roadProgress = findViewById(R.id.roadprogressbar);
        sefetyProgress = findViewById(R.id.sefetyProgressbar);
        touristProgress = findViewById(R.id.toristProgressbar);
        adventureProgress = findViewById(R.id.AdventureProgressbar);
        costProgress = findViewById(R.id.costProgressbar);
        review = findViewById(R.id.addrevireedit);

        reviewRecyclerView = findViewById(R.id.reviewRecyclerview);
        loadingDialog = new Dialog(ViewPlaceActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        placeId = getIntent().getStringExtra("placeId");


        FirebaseFirestore.getInstance().collection("PLACES").document(placeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Glide.with(ViewPlaceActivity.this).load(task.getResult().get("place_Image").toString()).into(placeImageFull);
                    placeTitle.setText(task.getResult().get("place_Name").toString());
                    placeAddress.setText(task.getResult().get("place_address").toString() + " " + task.getResult().get("place_state").toString() + " " + task.getResult().get("place_country").toString() + " " + task.getResult().get("place_pincode").toString());
                    placeDecription.setText(task.getResult().get("place_description").toString());
                    roadProgress.setProgress(Integer.parseInt(String.valueOf(task.getResult().getLong("road_condition"))));
                    sefetyProgress.setProgress(Integer.parseInt(String.valueOf(task.getResult().getLong("sefety_level"))));
                    touristProgress.setProgress(Integer.parseInt(String.valueOf(task.getResult().getLong("tourist_level"))));
                    costProgress.setProgress(Integer.parseInt(String.valueOf(task.getResult().getLong("cost_level"))));
                    adventureProgress.setProgress(Integer.parseInt(String.valueOf(task.getResult().getLong("adventure_level"))));


                }
                loadingDialog.dismiss();
            }
        });


    }

}
