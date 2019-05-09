package com.example.talha.rizq;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
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

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button register, login;
    private ProgressDialog loadingBar;
    private ObjectAnimator animRotationY;
    private ImageView logo;
    private TextView tag1,tag2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (Button) findViewById(R.id.btn_main_register);
        login = (Button) findViewById(R.id.btn_main_login);
        logo = (ImageView) findViewById(R.id.iv_logo);
        tag1 = (TextView) findViewById(R.id.tv_tag1);
        tag2 = (TextView) findViewById(R.id.tv_tag2);

        loadingBar = new ProgressDialog(this);
        Paper.init(this);

        //Animation rotation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
        //rotation.setFillAfter(true);
        //logo.startAnimation(rotation);

        //  Rotation Animation

        ObjectAnimator animation = ObjectAnimator.ofFloat(logo, "rotationY", 0.0f, 360f);
        animation.setDuration(3600);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();

        // Translation Animation

        translateanimation(280,-200,0,0,3000,tag1);
        translateanimation(-150,230,0,0,3000,tag2);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserUsernameKey = Paper.book().read(Prevalent.userUsernameKey);
        String UserPasswordKey = Paper.book().read(Prevalent.userPasswordKey);
        if(UserUsernameKey!="" && UserPasswordKey!=""){
            if(!TextUtils.isEmpty(UserUsernameKey) && !TextUtils.isEmpty(UserPasswordKey)){
                directLoginAccess(UserUsernameKey,UserPasswordKey);

                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please wait ...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    private void translateanimation(int fromx,int tox,int fromy,int toy,int dur,View v){
        TranslateAnimation cloud_moving = new TranslateAnimation(
                Animation.ABSOLUTE, fromx,
                Animation.ABSOLUTE, tox,
                Animation.ABSOLUTE, fromy,
                Animation.ABSOLUTE, toy
        );

        cloud_moving.setDuration(dur);
        cloud_moving.setFillAfter(true);
        cloud_moving.setStartOffset(1000);
        cloud_moving.setRepeatCount(Animation.INFINITE);
        cloud_moving.setRepeatMode(Animation.REVERSE);
        v.startAnimation(cloud_moving);
    }

    private void directLoginAccess(final String uname, final String pass) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(uname).exists()){
                    Users usersData = dataSnapshot.child("Users").child(uname).getValue(Users.class);
                    if(usersData.getUsername().equals(uname)){
                        if(usersData.getPassword().equals(pass)){
                            Toast.makeText(MainActivity.this,"Logged In Successfully.",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            Prevalent.currentUser = usersData;
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Wrong Password.",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                }
                else{
                    Toast.makeText(MainActivity.this,"User with "+uname+" username does not exists.",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
