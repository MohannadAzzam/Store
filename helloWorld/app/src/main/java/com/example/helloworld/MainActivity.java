package com.example.helloworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.helloworld.Model.User;
import com.example.helloworld.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {


    private final int SPLASH_DISPLAY_LENGTH = 1000;

    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Paper.init(this);

        loadingBar = new ProgressDialog(this);


        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        if (UserPasswordKey != "" && UserPhoneKey != ""){
            if (  !(TextUtils.isEmpty(UserPhoneKey))  && !(TextUtils.isEmpty(UserPasswordKey)) ){

                allowAccess(UserPhoneKey,UserPasswordKey);

                loadingBar.setTitle("تسجيل الدخول");
                loadingBar.setMessage("الرجاء الانتظار..");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void allowAccess(String UserPhoneKey, String UserPasswordKey) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(UserPhoneKey).exists()){
                    User userData = snapshot.child("Users").child(UserPhoneKey).getValue(User.class);

                    if (userData.getPhone().equals(UserPhoneKey)){

                        if (userData.getPassword().equals(UserPasswordKey)){
                            Toast.makeText(getBaseContext(), "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                            Prevalent.CurrentOnlineUser = userData;
                            startActivity(intent);

                        }else {
                            loadingBar.dismiss();
                            Toast.makeText(getBaseContext(), "كلمة المرور خاطئة", Toast.LENGTH_SHORT).show();
                        }

                    }

                }else {
                    Toast.makeText(getBaseContext(), "لا يوجد حساب بهذا الرقم"+UserPhoneKey, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
//                    Toast.makeText(LoginActivity.this, "الرجاء إنشاء حساب جديد", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}