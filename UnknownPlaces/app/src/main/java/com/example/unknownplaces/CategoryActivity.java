package com.example.unknownplaces;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView placesRecyclerViewNew;
    private Button requestNewplace;
    private String categoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.categorytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        categoryTitle = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(categoryTitle);

        requestNewplace = findViewById(R.id.addplaceBtn);
        placesRecyclerViewNew = findViewById(R.id.placesRecyclerviewNew);

        requestNewplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addplaceIntent = new Intent(CategoryActivity.this, Main2Activity.class);
                startActivity(addplaceIntent);

            }
        });


    }
}
