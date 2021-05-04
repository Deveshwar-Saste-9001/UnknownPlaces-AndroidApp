package com.example.unknownplaces;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rey.material.widget.CheckBox;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;

public class Main2Activity extends AppCompatActivity {

    private SeekBar roadCondition, sefetylevel, touristlevel, adventurelevel, costlevel;
    private TextView roadvalue, sefetyvalue, touristvalue, adventurevalue, costvalue;
    private int roadProgress = 0, sefetyProgress = 0, touristProgress = 0, adventureProgress = 0, costProgress = 0;
    private ImageView placeImage;
    private Uri imageuri;
    private double longitude, latitude;
    private boolean updatePhoto = false;
    private EditText placeName, placeAddress, placeDiscription, placestate, placeContry, placepincode;
    private CheckBox hostelCheck, geusthouseCheck, campingspotCheck, otherCheck;
    private Button AddrequestBtn;
    private Dialog loadingDialog;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Place");
        roadCondition = findViewById(R.id.roadConditionseek);
        roadvalue = findViewById(R.id.roadvluetext);
        sefetylevel = findViewById(R.id.sefetyLevelseek);
        sefetyvalue = findViewById(R.id.sefetyvluetext);
        touristlevel = findViewById(R.id.toristLevelSeek);
        touristvalue = findViewById(R.id.touristvluetext);
        adventurelevel = findViewById(R.id.adventureLevelSeek);
        adventurevalue = findViewById(R.id.adventurevluetext);
        costlevel = findViewById(R.id.costLevelSeek);
        costvalue = findViewById(R.id.costvluetext);
        placeImage = findViewById(R.id.placeImageSelect);
        placeName = findViewById(R.id.placetitlenew);
        placeAddress = findViewById(R.id.placelocaladdress);
        placeDiscription = findViewById(R.id.placeDescription);
        hostelCheck = findViewById(R.id.hostelcheck);
        geusthouseCheck = findViewById(R.id.Guesthousecheck);
        campingspotCheck = findViewById(R.id.CampingSpotcheck);
        otherCheck = findViewById(R.id.Othercheck);
        placestate = findViewById(R.id.placelocalState);
        placeContry = findViewById(R.id.placelocalContry);
        placepincode = findViewById(R.id.placelocalpincode);
        AddrequestBtn = findViewById(R.id.requestPlaceBtnnew);

        loadingDialog = new Dialog(Main2Activity.this);
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        categoryName = getIntent().getStringExtra("Category");

        findViewById(R.id.open_place_picker_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlacePicker.IntentBuilder()
                        .setLatLong(19.8753, 75.34425)
                        .showLatLong(true)
                        .setMapRawResourceStyle(R.raw.map_style)
                        .setMapZoom(9)
                        .setMapType(MapType.NORMAL)
                        .build(Main2Activity.this);
                startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
            }
        });
        /////seek Bar code start
        roadCondition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                roadProgress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                roadvalue.setText(roadProgress + "/" + roadCondition.getMax());
            }
        });
        sefetylevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sefetyProgress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sefetyvalue.setText(sefetyProgress + "/" + roadCondition.getMax());
            }
        });
        touristlevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                touristProgress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                touristvalue.setText(touristProgress + "/" + roadCondition.getMax());
            }
        });
        adventurelevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                adventureProgress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                adventurevalue.setText(adventureProgress + "/" + roadCondition.getMax());
            }
        });
        costlevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                costProgress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                costvalue.setText(costProgress + "/" + roadCondition.getMax());
            }
        });
        //////seek Bar  code end
        placeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VERSION.SDK_INT >= VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/+");
                        startActivityForResult(galleryIntent, 1);
                    } else {
                        ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                    }
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/+");
                    startActivityForResult(galleryIntent, 1);
                }
            }
        });
        AddrequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(placestate.getText())) {
                    if (!TextUtils.isEmpty(placeName.getText())) {
                        if (!TextUtils.isEmpty(placeAddress.getText())) {
                            if (!TextUtils.isEmpty(placeDiscription.getText())) {
                                addPlace();
                            } else {
                                placeDiscription.requestFocus();
                                Toast.makeText(Main2Activity.this, "please add place decription", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            placeAddress.requestFocus();
                            Toast.makeText(Main2Activity.this, "please add place address", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        placeName.requestFocus();
                        Toast.makeText(Main2Activity.this, "please add place decription", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Main2Activity.this, "Please select location of place", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addPlace() {
        if (updatePhoto) {
            loadingDialog.show();
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
            String datefinal = df.format(c);
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("places/" + placeName.getText().toString() + datefinal + ".jpg");
            if (imageuri != null) {
                Glide.with(Main2Activity.this).asBitmap().load(imageuri).into(new ImageViewTarget<Bitmap>(placeImage) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                imageuri = task.getResult();
                                                Map<String, Object> updatedata = new HashMap<>();
                                                updatedata.put("verified", (boolean) false);
                                                updatedata.put("place_Image", task.getResult().toString());
                                                updatedata.put("place_Name", placeName.getText().toString());
                                                updatedata.put("place_state", placestate.getText().toString());
                                                updatedata.put("place_country", placeContry.getText().toString());
                                                updatedata.put("place_pincode", placepincode.getText().toString());
                                                updatedata.put("place_latitude", (double) latitude);
                                                updatedata.put("place_longitude", (double) longitude);
                                                updatedata.put("place_address", placeAddress.getText().toString());
                                                updatedata.put("place_description", placeDiscription.getText().toString());
                                                updatedata.put("road_condition", roadProgress);
                                                updatedata.put("sefety_level", sefetyProgress);
                                                updatedata.put("tourist_level", touristProgress);
                                                updatedata.put("adventure_level", adventureProgress);
                                                updatedata.put("cost_level", costProgress);
                                                updatedata.put("Category", categoryName);
                                                String hostel = "";
                                                String guesthouse = "";
                                                String campingspot = "";
                                                String other = "";
                                                if (hostelCheck.isChecked()) {
                                                    hostel = "hostel";
                                                }
                                                if (geusthouseCheck.isChecked()) {
                                                    guesthouse = "guesthouse";
                                                }
                                                if (campingspotCheck.isChecked()) {
                                                    campingspot = "camping spot";
                                                }
                                                if (otherCheck.isChecked()) {
                                                    other = "other";
                                                }
                                                updatedata.put("places_to_stay", hostel + " " + guesthouse + " " + campingspot + " " + other);

                                                FirebaseFirestore.getInstance().collection("PLACES").document(placeName.getText().toString())
                                                        .set(updatedata).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseFirestore.getInstance().collection("Category").document(categoryName).collection("PLACES").document(categoryName + "PLACES").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                    long placeseslistSize = (long) documentSnapshot.get("no_of_places");
                                                                    Map<String, Object> categoryPlace = new HashMap<>();
                                                                    categoryPlace.put("no_of_places", (long) (placeseslistSize + 1));
                                                                    categoryPlace.put("places_ID_" + (placeseslistSize + 1), placeName.getText().toString());
                                                                    categoryPlace.put("verified_" + (placeseslistSize + 1), false);
                                                                    FirebaseFirestore.getInstance().collection("Category").document(categoryName).collection("PLACES").document(categoryName + "PLACES").update(categoryPlace);
                                                                }
                                                            });
                                                            Toast.makeText(Main2Activity.this, "Your Request submited successfully", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(Main2Activity.this, error, Toast.LENGTH_SHORT).show();
                                                        }
                                                        loadingDialog.dismiss();
                                                    }
                                                });

                                            } else {
                                                loadingDialog.dismiss();
                                                String error = task.getException().getMessage();
                                                Toast.makeText(Main2Activity.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    loadingDialog.dismiss();
                                    String error = task.getException().getMessage();
                                    Toast.makeText(Main2Activity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                    }
                });
            }


        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                try {
                    AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);
                    Geocoder geocoder = new Geocoder(Main2Activity.this, Locale.getDefault());

                    List<Address> addresses = geocoder.getFromLocation(addressData.getLatitude(), addressData.getLongitude(), 1);
                    ((TextView) findViewById(R.id.address_data_text_view)).setText(addressData.toString());
                    placepincode.setText(addresses.get(0).getPostalCode());
                    placestate.setText(addresses.get(0).getAdminArea());
                    latitude = addressData.getLatitude();
                    longitude = addressData.getLongitude();
                    placeContry.setText(addresses.get(0).getCountryName());
                } catch (Exception e) {
                    Log.e("MainActivity", e.getMessage());
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        imageuri = data.getData();
                        updatePhoto = true;
                        Glide.with(Main2Activity.this).load(imageuri).apply(new RequestOptions().placeholder(R.drawable.slingbannerplace)).into(placeImage);

                    } else {
                        Toast.makeText(Main2Activity.this, "Image not found!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
