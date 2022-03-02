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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    private TextInputLayout et_password,et_phoneNumber, et_userName;
    //    private CheckBox cb_rememberMe;
//    private TextView tv_forgetPassword, tv_clickHere,tv_admin;
    private Button btn_register;
    private TextView register_tv_login;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        et_password = findViewById(R.id.register_et_password);
        et_phoneNumber = findViewById(R.id.register_et_phoneNumber);
        et_userName = findViewById(R.id.register_et_userName);
//        tv_forgetPassword = findViewById(R.id.login_tv_forgotpassword);
//        tv_clickHere = findViewById(R.id.login_tv_clickhere);
//        tv_admin = findViewById(R.id.login_tv_admin);
        btn_register = findViewById(R.id.register_btn_register);
        register_tv_login = findViewById(R.id.register_tv_login);

        register_tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        loadingBar = new ProgressDialog(this);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
                finish();
//                                Intent intent = new Intent(getBaseContext(),LoginActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void createAccount(){
        String userName = et_userName.getEditText().getText().toString().trim();
        String phone = et_phoneNumber.getEditText().getText().toString().trim();
        String password = et_password.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(userName)){
            Toast.makeText(this, "ادخل اسم المستخدم", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "ادخل رقم الهاتف", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "ادخل كلمة المرور", Toast.LENGTH_SHORT).show();
        } else {

            loadingBar.setTitle("إنشاء حساب");
            loadingBar.setMessage("الرجاء الانتظار..");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(userName, phone, password);
        }
    }

    private void ValidatePhoneNumber(String userName, String phone, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (   !(snapshot.child("Users").child(phone).exists())  ){

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("userName",userName);
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "تم إنشاء الحساب بنجاح", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getBaseContext(),LoginActivity.class);
                                        loadingBar.dismiss();
                                        startActivity(intent);

                                    }else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "خطأ بالشبكة", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else {
                    Toast.makeText(RegisterActivity.this, " هذا الرقم مستخدم من قبل "+phone, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "يرجى استخدام رقم آخر", Toast.LENGTH_SHORT).show();

//                    Intent intent = new Intent(getBaseContext(),LoginActivity.class);
//                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
