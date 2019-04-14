package com.example.talha.rizq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, fullname, email, phone, password;
    private Button register;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = (Button) findViewById(R.id.register);
        username = (EditText) findViewById(R.id.register_username_input);
        fullname = (EditText) findViewById(R.id.register_fullname_input);
        email = (EditText) findViewById(R.id.register_email_input);
        phone = (EditText) findViewById(R.id.register_phone_input);
        password = (EditText) findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount(){
        String usrname = username.getText().toString();
        String fulname = fullname.getText().toString();
        String eml = email.getText().toString();
        String phn = phone.getText().toString();
        String pwd = password.getText().toString();

        if(TextUtils.isEmpty(usrname)){
            Toast.makeText(this,"Please write your User Name ...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(fulname)){
            Toast.makeText(this,"Please write your Full Name ...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(eml)){
            Toast.makeText(this,"Please write your Email ...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phn)){
            Toast.makeText(this,"Please write your Phone number ...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pwd)){
            Toast.makeText(this,"Please write your Password ...", Toast.LENGTH_SHORT).show();
        }
        else{

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateExistence(usrname, fulname, eml, phn, pwd);

        }
    }

    private void ValidateExistence(final String username, final String fullname, final String email, final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(username).exists())){

                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("username",username);
                    userdataMap.put("fullname",fullname);
                    userdataMap.put("email",email);
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    RootRef.child("Users").child(username).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"Congratulations. Your account has been created.",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this,"Network error. Please Try again",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else{
                    Toast.makeText(RegisterActivity.this,"This "+username+" already exists.",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this,"Try adding another Username",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
