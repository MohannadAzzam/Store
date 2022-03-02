package com.example.helloworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.helloworld.Model.Product;
import com.example.helloworld.databinding.ActivityProductDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProductDetailsActivity extends AppCompatActivity {
    ActivityProductDetailsBinding binding;
    private int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String PRODUCT_ID = getIntent().getStringExtra("pid");

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(R.string.product_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getProductDetails(PRODUCT_ID);

        int num = Integer.parseInt(binding.ProductDetailsTvNumber.getText().toString());


        binding.ProductDetailsTvMinuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (num <= 10 && num >1 ) {
                    counter = counter - 1;
                    binding.ProductDetailsTvNumber.setText(counter + "");
//                }
            }
        });

        binding.ProductDetailsTvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (num <= 1 && num <=10 ) {
                    counter = counter + 1;
                    binding.ProductDetailsTvNumber.setText(counter + "");
//                }
            }
        });

    }

    private void getProductDetails(String PRODUCT_ID) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(PRODUCT_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Product product = snapshot.getValue(Product.class);
                    binding.ProductDetailsTvName.setText(product.getProductName());
                    binding.ProductDetailsTvPrice.setText("NIS " + product.getPrice());
                    binding.ProductDetailsTvDescription.setText(product.getDescription());
                    Picasso.get().load(product.getImage()).into(binding.ProductDetailsIv);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}