package com.example.helloworld;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld.Fragments.ProductFragment;
import com.example.helloworld.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private TextInputLayout et_address, et_phoneNumber, et_userName;
    private CircleImageView iv_profile;
    private TextView settings_change_tv;

    private Uri imageUri;
    private String myUrl = "";
    private StorageReference storageProfilePictureReference;
    private String cheker = "";
    private StorageTask uploadTask;

    private final static String CLICKED_KEY = "clicked";


    private Toolbar toolbar;
    private ImageView iv_close , iv_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        et_userName = findViewById(R.id.settings_et_userName);
        et_address = findViewById(R.id.settings_et_address);
        et_phoneNumber = findViewById(R.id.settings_et_phoneNumber);
        iv_profile = findViewById(R.id.settings_profile_iv);
        settings_change_tv = findViewById(R.id.settings_change_tv);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Setting Fragment");
        setSupportActionBar(toolbar);


        storageProfilePictureReference = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        userInfoDisply(iv_profile, et_phoneNumber, et_userName, et_address);
        storageProfilePictureReference = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        settings_change_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheker = CLICKED_KEY;

                CropImage.activity(imageUri).setAspectRatio(1, 1)
                        .start(SettingsActivity.this);
            }
        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheker = CLICKED_KEY;

                CropImage.activity(imageUri).setAspectRatio(1, 1)
                        .start(SettingsActivity.this);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_fragment_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_save:
                if (cheker.equals(CLICKED_KEY)) {
                    userInfoSaved();
                } else {
                    updateOnlyUserInfo();
                }
                return true;

//            case R.id.settings_close:
//                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
//                startActivity(intent);
////                ProductFragment nextFrag = new ProductFragment();
////                getActivity().getSupportFragmentManager().beginTransaction()
////                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
////                        .addToBackStack(null)
////                        .commit();
//                return true;
        }
        return false;
    }

    private void userInfoDisply(CircleImageView iv_profile, TextInputLayout et_phoneNumber, TextInputLayout et_userName, TextInputLayout et_address) {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentOnlineUser.getPhone());
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("image").exists()) {

                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("userName").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(iv_profile);
                        et_userName.getEditText().setText(name);
                        et_address.getEditText().setText(address);
                        et_phoneNumber.getEditText().setText(phone);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            iv_profile.setImageURI(imageUri);
        } else {
            Toast.makeText(getBaseContext(), "Error Try again", Toast.LENGTH_SHORT).show();
//            Fragment frg = null;
//            frg = getFragmentManager().findFragmentByTag("Your_Fragment_TAG");
//            final FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.detach(frg);
//            ft.attach(frg);
//            ft.commit();
        }

    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userName", et_userName.getEditText().getText().toString());
        userMap.put("address", et_address.getEditText().getText().toString());
        userMap.put("phone", et_phoneNumber.getEditText().getText().toString());
        ref.child(Prevalent.CurrentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(getBaseContext(), HomeActivity.class));
        Toast.makeText(getBaseContext(), "Profile info updated successfully", Toast.LENGTH_SHORT).show();
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(et_userName.getEditText().getText().toString())) {
            Toast.makeText(getBaseContext(), "يرجى ادخال اسم السمتخدم", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_address.getEditText().getText().toString())) {
            Toast.makeText(getBaseContext(), "يرجى ادخال العنوان", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_phoneNumber.getEditText().getText().toString())) {
            Toast.makeText(getBaseContext(), "يرجى ادخال رقم الهاتف", Toast.LENGTH_SHORT).show();
        } else if (cheker.equals(CLICKED_KEY)) {
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getBaseContext());
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePictureReference.child(Prevalent.CurrentOnlineUser.getPhone() + ".jpg");
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downLoadUri = task.getResult();
                        myUrl = downLoadUri.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("userName", et_userName.getEditText().getText().toString());
                        userMap.put("address", et_address.getEditText().getText().toString());
                        userMap.put("phone", et_phoneNumber.getEditText().getText().toString());
                        userMap.put("image", myUrl);
                        ref.child(Prevalent.CurrentOnlineUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(getBaseContext(), HomeActivity.class));
                        Toast.makeText(getBaseContext(), "Profile info updated successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getBaseContext(), "image is not selected", Toast.LENGTH_SHORT).show();
        }
    }
}