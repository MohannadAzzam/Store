package com.example.helloworld;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private TextInputLayout et_productName, et_productPrice, et_productDesc;
    private ImageView iv_addProductImage;
    private Button btn_add;

    private Toolbar toolbar;

    public final static int GALLARYPIC = 1;
    private Uri imageUri;
    private String description, price, ProductName, saveCurrentDate, saveCurrentTime;
    private String ProductRandomKey;
    private String downloadImageUrl;
    private StorageReference ProductImagesRef;
    String categoryName;
    private DatabaseReference ProductRef;

    public final static String CATEGORY_KEY = "category";


    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.Add_product);

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");

        et_productDesc = findViewById(R.id.add_et_productdesc);
        et_productName = findViewById(R.id.add_et_productname);
        et_productPrice = findViewById(R.id.add_et_productdprice);

        iv_addProductImage = findViewById(R.id.add_iv_camera);

        loadingBar = new ProgressDialog(this);

        btn_add = findViewById(R.id.add_product);

        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        iv_addProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallary();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                validateProductData();
                description = Objects.requireNonNull(et_productDesc.getEditText()).getText().toString().trim();
                price = Objects.requireNonNull(et_productPrice.getEditText()).getText().toString().trim();
                ProductName = Objects.requireNonNull(et_productName.getEditText()).getText().toString().trim();

                if (imageUri == null) {
                    Toast.makeText(getBaseContext(), "يرجى إضافة صورة للمنتج", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(description)) {
                    Toast.makeText(getBaseContext(), "يرجى إضافة وصف للمنتج", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(price)) {
                    Toast.makeText(getBaseContext(), "يرجى إضافة سعر للمنتج", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ProductName)) {
                    Toast.makeText(getBaseContext(), "يرجى إضافة اسم للمنتج", Toast.LENGTH_SHORT).show();
                } else {
                    StoreImageInformation();
                }
            }
        });


        Intent intent = getIntent();
//        categoryName = getIntent().getStringExtra(CATEGORY_KEY).toString();
        categoryName = intent.getStringExtra(CATEGORY_KEY);
//        Toast.makeText(this, ""+a, Toast.LENGTH_SHORT).show();


    }

    private void validateProductData() {
        description = Objects.requireNonNull(et_productDesc.getEditText()).getText().toString().trim();
        price = Objects.requireNonNull(et_productPrice.getEditText()).getText().toString().trim();
        ProductName = Objects.requireNonNull(et_productName.getEditText()).getText().toString().trim();

        if (imageUri == null) {
            Toast.makeText(this, "يرجى إضافة صورة للمنتج", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "يرجى إضافة وصف للمنتج", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "يرجى إضافة سعر للمنتج", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ProductName)) {
            Toast.makeText(this, "يرجى إضافة اسم للمنتج", Toast.LENGTH_SHORT).show();
        } else {
            StoreImageInformation();
        }
    }

    private void StoreImageInformation() {

        loadingBar.setTitle("إضافة منتج جديد");
        loadingBar.setMessage("الرجاء الانتظار..");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM-dd,yyyy ");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        ProductRandomKey = saveCurrentDate + saveCurrentTime;

        StorageReference filepath = ProductImagesRef.child(imageUri.getLastPathSegment() + ProductRandomKey + ".jpg");
        final UploadTask uploadTask = filepath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, message + "", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }
                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "getting product Image url successfully", Toast.LENGTH_SHORT).show();

                            saveProductInfoInDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoInDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", ProductRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", description);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", categoryName);
        productMap.put("price", price);
        productMap.put("productName", ProductName);

        ProductRef.child(ProductRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminAddNewProductActivity.this, "product is added successfully", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                    Intent intent = new Intent(getBaseContext(),AdminCategoryActivity.class);
                    startActivity(intent);

                } else {
                    String message = task.getException().toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }

    private void OpenGallary() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLARYPIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLARYPIC && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            iv_addProductImage.setImageURI(imageUri);
        }
    }
}