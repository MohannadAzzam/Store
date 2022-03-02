package com.example.helloworld.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld.HomeActivity;
import com.example.helloworld.MainActivity;
import com.example.helloworld.Prevalent.Prevalent;
import com.example.helloworld.R;
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

import static android.app.Activity.RESULT_OK;
import static com.example.helloworld.AdminAddNewProductActivity.GALLARYPIC;


public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextInputLayout et_address, et_phoneNumber, et_userName;
    private CircleImageView iv_profile;
    private TextView settings_change_tv;

    private Uri imageUri;
    private String myUrl = "";
    private StorageReference storageProfilePictureReference;
    private String cheker = "";
    private StorageTask uploadTask;

    private final static String CLICKED_KEY = "clicked";


    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        et_userName = v.findViewById(R.id.settings_et_userName);
        et_address = v.findViewById(R.id.settings_et_address);
        et_phoneNumber = v.findViewById(R.id.settings_et_phoneNumber);
        iv_profile = v.findViewById(R.id.settings_profile_iv);
        settings_change_tv = v.findViewById(R.id.settings_change_tv);


        storageProfilePictureReference = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        userInfoDisply(iv_profile, et_phoneNumber, et_userName, et_address);
        storageProfilePictureReference = FirebaseStorage.getInstance().getReference().child("Profile pictures");

//        settings_change_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cheker = CLICKED_KEY;
//
//                CropImage.activity(imageUri).setAspectRatio(1, 1)
//                        .start((Activity) getContext());
//            }
//        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheker = CLICKED_KEY;

                CropImage.activity(imageUri).setAspectRatio(1, 1)
                        .start(getContext(),SettingsFragment.this);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            iv_profile.setImageURI(imageUri);
        } else {
            Toast.makeText(getContext(), "Error Try again", Toast.LENGTH_SHORT).show();
//            Fragment frg = null;
//            frg = getFragmentManager().findFragmentByTag("Your_Fragment_TAG");
//            final FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.detach(frg);
//            ft.attach(frg);
//            ft.commit();
        }


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
//                        String phone = snapshot.getValue().toString();
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.setting_fragment_menu, menu);
        return;

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

            case R.id.settings_edit:
                enableFields();
//                Intent intent = new Intent(getContext(),ProductFragment.class);
//                startActivity(intent);
//                ProductFragment nextFrag = new ProductFragment();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
//                        .addToBackStack(null)
//                        .commit();
                return true;
        }
        return false;
    }
    private void enableFields(){
        iv_profile.setEnabled(true);
        et_phoneNumber.setEnabled(true);
        et_address.setEnabled(true);
        et_userName.setEnabled(true);
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userName", et_userName.getEditText().getText().toString());
        userMap.put("address", et_address.getEditText().getText().toString());
        userMap.put("phone", et_phoneNumber.getEditText().getText().toString());
        ref.child(Prevalent.CurrentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(getContext(), HomeActivity.class));
        Toast.makeText(getContext(), "Profile info updated successfully", Toast.LENGTH_SHORT).show();
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(et_userName.getEditText().getText().toString())) {
            Toast.makeText(getContext(), "يرجى ادخال اسم السمتخدم", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_address.getEditText().getText().toString())) {
            Toast.makeText(getContext(), "يرجى ادخال العنوان", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_phoneNumber.getEditText().getText().toString())) {
            Toast.makeText(getContext(), "يرجى ادخال رقم الهاتف", Toast.LENGTH_SHORT).show();
        } else if (cheker.equals(CLICKED_KEY)) {
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
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

                        startActivity(new Intent(getContext(), HomeActivity.class));
                        Toast.makeText(getContext(), "Profile info updated successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "image is not selected", Toast.LENGTH_SHORT).show();
        }
    }
}