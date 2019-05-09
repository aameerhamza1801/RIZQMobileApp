package com.example.talha.rizq;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talha.rizq.Model.Users;
import com.example.talha.rizq.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button login;
    private CheckBox chkbox;
    private TextView admin, not_admin, forgot;
    private ProgressDialog loadingBar;
    private String parentDbName="Users";
    private ImageView logo;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.login_username_input);
        password = (EditText) findViewById(R.id.login_password_input);
        login = (Button) findViewById(R.id.login);
        admin = (TextView) findViewById(R.id.admin_panel_link);
        not_admin = (TextView) findViewById(R.id.not_admin_panel_link);
        chkbox = (CheckBox) findViewById(R.id.remember_me_chkb);
        logo = (ImageView) findViewById(R.id.login_logo);
        Paper.init(this);

        loadingBar = new ProgressDialog(this);


        rotationanimation(logo,"rotationY",0.0f,360f,4000);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setText("Admin Login");
                admin.setVisibility(View.INVISIBLE);
                not_admin.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        not_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setText("Login");
                admin.setVisibility(View.VISIBLE);
                not_admin.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }

    private void rotationanimation(View v,String property,float value1,float value2,int dur){
        ObjectAnimator animation = ObjectAnimator.ofFloat(v, property, value1, value2);
        animation.setDuration(dur);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
    }


    private void LoginUser() {
        String uname = username.getText().toString();
        String pwd = password.getText().toString();

        if(TextUtils.isEmpty(uname)){
            Toast.makeText(this,"Please enter your Username ...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pwd)){
            Toast.makeText(this,"Please enter your Password ...", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Logging in");
            loadingBar.setMessage("Please wait while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccess(uname, pwd);

        }
    }

    private void AllowAccess(final String uname, final String pwd) {
        if(chkbox.isChecked()){
            Paper.book().write(Prevalent.userUsernameKey,uname);
            Paper.book().write(Prevalent.userPasswordKey,pwd);
        }


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(uname).exists()){
                    Users usersData = dataSnapshot.child(parentDbName).child(uname).getValue(Users.class);
                    if(usersData.getUsername().equals(uname)){
                        if(usersData.getPassword().equals(pwd)){
                            if(parentDbName=="Admins"){
                                Toast.makeText(LoginActivity.this,"Logged In Successfully.",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this,AdminHomeNavActivity.class);
                                Prevalent.currentUser = usersData;
                                startActivity(intent);
                            }
                            else if (parentDbName=="Users"){
                                Toast.makeText(LoginActivity.this,"Logged In Successfully.",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                Prevalent.currentUser = usersData;
                                startActivity(intent);

                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"Wrong Password.",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                }
                else{
                    Toast.makeText(LoginActivity.this,"User with "+uname+" username does not exists.",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
