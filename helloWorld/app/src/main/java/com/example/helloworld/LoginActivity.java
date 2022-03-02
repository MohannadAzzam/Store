package com.example.helloworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld.Model.User;
import com.example.helloworld.Prevalent.Prevalent;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout et_password, et_phoneNumber;
    private CheckBox cb_rememberMe;
    private TextView tv_forgetPassword, tv_clickHere, tv_admin, tv_notAdmin , tv_have_not_acc;
    private Button btn_login;

    private ProgressDialog loadingBar;

    private String DB_NAME ="Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        et_password = findViewById(R.id.login_et_password);
        et_phoneNumber = findViewById(R.id.login_et_phoneNumber);
        cb_rememberMe = findViewById(R.id.login_cb_rememberme);
        tv_forgetPassword = findViewById(R.id.login_tv_forgotpassword);
        tv_clickHere = findViewById(R.id.login_tv_clickhere);
        tv_have_not_acc = findViewById(R.id.tv_have_not_acc);

        tv_admin = findViewById(R.id.login_tv_admin);
        tv_notAdmin = findViewById(R.id.login_tv_notAdmin);

        btn_login = findViewById(R.id.login_btn_login);

        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        tv_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_login.setText("التسجيل كمشرف");
                tv_admin.setVisibility(View.GONE);
                tv_notAdmin.setVisibility(View.VISIBLE);
                cb_rememberMe.setVisibility(View.INVISIBLE);
                tv_clickHere.setVisibility(View.INVISIBLE);
                tv_have_not_acc.setVisibility(View.INVISIBLE);
                DB_NAME = "Admins";

            }
        });

        tv_notAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_login.setText("تسجيل الدخول");
                tv_admin.setVisibility(View.VISIBLE);
                tv_notAdmin.setVisibility(View.GONE);
                tv_clickHere.setVisibility(View.VISIBLE);
                tv_have_not_acc.setVisibility(View.VISIBLE);
                cb_rememberMe.setVisibility(View.VISIBLE);
                DB_NAME = "Users";
            }
        });

        tv_clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }


    private void loginUser() {

        String phone = et_phoneNumber.getEditText().getText().toString().trim();
        String password = et_password.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "ادخل رقم الهاتف", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "ادخل كلمة المرور", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("تسجيل الدخول");
            loadingBar.setMessage("الرجاء الانتظار..");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAccessToAccount(phone, password);
        }
    }
    private void allowAccessToAccount(final String phone, final String password) {

        if (cb_rememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(DB_NAME).child(phone).exists()){
                    User userData = snapshot.child(DB_NAME).child(phone).getValue(User.class);

                    if (userData.getPhone().equals(phone)){

                        if (userData.getPassword().equals(password)){
                            if (DB_NAME.equals("Admins")){
                                Toast.makeText(LoginActivity.this, "تم تسجيل الدخول كمشرف بنجاح", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getBaseContext(),AdminCategoryActivity.class);
                                startActivity(intent);
                                loadingBar.dismiss();

                            }else if(DB_NAME.equals("Users")) {
                                Toast.makeText(LoginActivity.this, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                                Prevalent.CurrentOnlineUser = userData;
                                startActivity(intent);
                            }
                        }else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "كلمة المرور خاطئة", Toast.LENGTH_SHORT).show();
                        }

                    }

                }else {
                    Toast.makeText(LoginActivity.this, " لا يوجد حساب بهذا الرقم "+phone, Toast.LENGTH_SHORT).show();
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