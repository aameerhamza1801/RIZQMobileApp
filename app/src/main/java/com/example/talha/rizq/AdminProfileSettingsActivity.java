package com.example.talha.rizq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talha.rizq.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class AdminProfileSettingsActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    private TextView change_image, close, update, username;
    private EditText fullname, email, phone;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicRef;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile_settings);


        storageProfilePicRef = FirebaseStorage.getInstance().getReference().child("Admin Profile Pictures");

        profile_image = (CircleImageView) findViewById(R.id.image_psettings_admin);
        change_image =(TextView) findViewById(R.id.change_image_psettings_admin);
        close =(TextView) findViewById(R.id.close_psettings_admin);
        update =(TextView) findViewById(R.id.update_psettings_admin);
        username =(TextView) findViewById(R.id.username_psettings_admin);
        fullname =(EditText) findViewById(R.id.fullname_psettings_admin);
        email =(EditText) findViewById(R.id.email_psettings_admin);
        phone =(EditText) findViewById(R.id.phone_psettings_admin);


        UserInfoDisplay(profile_image,username,fullname,email,phone);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfileSettingsActivity.this,AdminHomeNavActivity.class);
                startActivity(intent);
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked")){
                    UserInfoSaved();
                }
                else {
                    UpdateOnlyUserInfo();
                }
            }
        });

        Picasso.get().load(Prevalent.currentUser.getImage()).placeholder(R.drawable.profile).into(profile_image);
        username.setText(Prevalent.currentUser.getUsername());
        fullname.setText(Prevalent.currentUser.getFullname());
        email.setText(Prevalent.currentUser.getEmail());
        phone.setText(Prevalent.currentUser.getPhone());
        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";
                CropImage.activity(imageUri).setAspectRatio(1,1)
                        .start(AdminProfileSettingsActivity.this);
            }
        });

    }


    private void UpdateOnlyUserInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Admins");

        HashMap<String,Object> userMap = new HashMap<>();
        userMap.put("username",username.getText().toString());
        userMap.put("fullname",fullname.getText().toString());
        userMap.put("email",email.getText().toString());
        userMap.put("phone",phone.getText().toString());
        ref.child(Prevalent.currentUser.getUsername()).updateChildren(userMap);

        startActivity(new Intent(AdminProfileSettingsActivity.this,HomeActivity.class));
        Toast.makeText(AdminProfileSettingsActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profile_image.setImageURI(imageUri);
        }
        else{
            Toast.makeText(this,"Error. Try Again!",Toast.LENGTH_SHORT);
            startActivity(new Intent(AdminProfileSettingsActivity.this,ProfileSettingsActivity.class));
        }
    }

    private void UserInfoSaved() {
        if(TextUtils.isEmpty(username.getText().toString())){
            Toast.makeText(this,"Missing Username",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(fullname.getText().toString())){
            Toast.makeText(this,"Missing Full Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email.getText().toString())){
            Toast.makeText(this,"Missing Email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone.getText().toString())){
            Toast.makeText(this,"Missing Phone #",Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked")){
            UploadImage();
        }
    }

    private void UploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updated Profile");
        progressDialog.setMessage("Please wait while account information is being updated");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri!=null){
            final StorageReference fileRef = storageProfilePicRef.child(Prevalent.currentUser.getUsername()+".jpg");
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Admins");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("username",username.getText().toString());
                        userMap.put("fullname",fullname.getText().toString());
                        userMap.put("email",email.getText().toString());
                        userMap.put("phone",phone.getText().toString());
                        userMap.put("image",myUrl);
                        ref.child(Prevalent.currentUser.getUsername()).updateChildren(userMap);

                        progressDialog.dismiss();
                        startActivity(new Intent(AdminProfileSettingsActivity.this,AdminHomeNavActivity.class));
                        Toast.makeText(AdminProfileSettingsActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(AdminProfileSettingsActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        else {
            Toast.makeText(AdminProfileSettingsActivity.this, "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }



    private void UserInfoDisplay(final CircleImageView profile_image, final TextView username, final EditText fullname, final EditText email, final EditText phone) {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child(Prevalent.currentUser.getUsername());
        System.out.println(Prevalent.currentUser.getUsername());


        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    if (dataSnapshot.child("image").exists()) {
                        String image = dataSnapshot.child("image").getValue().toString();

                        Picasso.get().load(image).into(profile_image);

                    }
                    String uname = dataSnapshot.child("username").getValue().toString();
                    String fname = dataSnapshot.child("fullname").getValue().toString();
                    String eml = dataSnapshot.child("image").getValue().toString();
                    String phn = dataSnapshot.child("phone").getValue().toString();
                    username.setText(uname);
                    fullname.setText(fname);
                    email.setText(eml);
                    phone.setText(phn);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
