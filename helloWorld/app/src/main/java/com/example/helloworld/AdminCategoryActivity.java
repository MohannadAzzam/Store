package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminCategoryActivity extends AppCompatActivity {
    private CardView cv_shirt, cv_dress;
    private CardView cv_glasses, cv_hats;
    private CardView cv_phones, cv_laptops;
    private CardView cv_headphones, cv_smartWatches;

    public final static String CATEGORY_KEY = "category";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.chose_category);


        cv_shirt = findViewById(R.id.admincatregory_cv_shirt);
        cv_dress = findViewById(R.id.admincatregory_cv_dress);
        cv_glasses = findViewById(R.id.admincatregory_cv_glasses);
        cv_hats = findViewById(R.id.admincatregory_cv_hats);
        cv_phones = findViewById(R.id.admincatregory_cv_phones);
        cv_laptops = findViewById(R.id.admincatregory_cv_laptps);
        cv_headphones = findViewById(R.id.admincatregory_cv_headphones);
        cv_smartWatches = findViewById(R.id.admincatregory_cv_smartwatches);

        cv_shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentMethod("Shirts");
            }
        });
        cv_dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentMethod("Dresses");
            }
        });
        cv_glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentMethod("Glasses");
            }
        });
        cv_hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentMethod("Hats");
            }
        });
        cv_phones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentMethod("Phones");
            }
        });
        cv_laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentMethod("Laptops");
            }
        });
        cv_headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentMethod("HeadPhones");
            }
        });
        cv_smartWatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentMethod("Smart Watches");
            }
        });
    }

    private void intentMethod(String value) {
        Intent intent = new Intent(getBaseContext(), AdminAddNewProductActivity.class);
        intent.putExtra(CATEGORY_KEY, value);
        startActivity(intent);
    }
}