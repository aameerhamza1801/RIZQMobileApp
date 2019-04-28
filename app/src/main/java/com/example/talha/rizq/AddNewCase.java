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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.Toast;

import com.example.talha.rizq.Fragments_Tabs.CasesFragment;
import com.example.talha.rizq.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddNewCase extends AppCompatActivity {


    private String Name, Description, Account, CNIC,currentDate, currentTime,Needed_Amount,Collected_Amount,Verified,CaseRandomKey ;
    private EditText name, description, account, cnic, needed_amount;
    private Button add_case;
    private ImageView image;
    private Uri imageUri;
    private String imageUrl = "";
    private StorageReference supportCasePicRef;
    private ProgressDialog loadingBar;
    private DatabaseReference CasesRef;
    private static final int GalleryPick = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_case);

        add_case = (Button)findViewById(R.id.add_case_button);
        name = (EditText)findViewById(R.id.add_case_name);
        description = (EditText)findViewById(R.id.add_case_description);
        account = (EditText)findViewById(R.id.add_case_account);
        cnic = (EditText)findViewById(R.id.add_case_cnic);
        needed_amount = (EditText)findViewById(R.id.add_case_amount);
        image = (ImageView)findViewById(R.id.add_case_image);

        CasesRef = FirebaseDatabase.getInstance().getReference().child("Cases");
        supportCasePicRef = FirebaseStorage.getInstance().getReference().child("SupportCase Pictures");
        loadingBar = new ProgressDialog(this);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        add_case.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validate_Info();
            }
        });
        
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
        else{
            Toast.makeText(this,"Error. Try Again!",Toast.LENGTH_SHORT);
            startActivity(new Intent(AddNewCase.this,AddNewCase.class));
        }
    }

    private void Validate_Info() {
        Name = name.getText().toString();
        Description = description.getText().toString();
        CNIC = cnic.getText().toString();
        Needed_Amount = needed_amount.getText().toString();
        Account = account.getText().toString();
        Collected_Amount = "0";
        Verified = "0";

        if(imageUri==null){
            Toast.makeText(this, "Please Select Case Image", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Name)){
            Toast.makeText(this, "Please Enter Needy Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description)){
            Toast.makeText(this, "Please Enter Case Description", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(CNIC)){
            Toast.makeText(this, "Please Enter your CNIC", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Account)){
            Toast.makeText(this, "Please Enter Account Information", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Needed_Amount)){
            Toast.makeText(this, "Please Enter the Amount Needed", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreCaseInfo();
        }
    }

    private void StoreCaseInfo() {
        loadingBar.setTitle("Adding Support Case");
        loadingBar.setMessage("Wait while case is being added");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calender = Calendar.getInstance();
        SimpleDateFormat cDate = new SimpleDateFormat("MM dd, yyyy");
        currentDate = cDate.format(calender.getTime());

        SimpleDateFormat cTime = new SimpleDateFormat("HH:mm:ss a");
        currentTime = cTime.format(calender.getTime());

        CaseRandomKey = currentDate+currentTime;


        final StorageReference filePath = supportCasePicRef.child(imageUri.getLastPathSegment() + CaseRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AddNewCase.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AddNewCase.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        imageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            imageUrl = task.getResult().toString();

                            Toast.makeText(AddNewCase.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });


    }

    private void SaveProductInfoToDatabase() {

        HashMap<String, Object> caseMap = new HashMap<>();
        caseMap.put("cid", CaseRandomKey);
        caseMap.put("needy_name", Name);
        caseMap.put("description", Description);
        caseMap.put("cnic", CNIC);
        caseMap.put("image", imageUrl);
        caseMap.put("account", Account);
        caseMap.put("needed_amount", Needed_Amount);
        caseMap.put("collected_amount", Collected_Amount);
        caseMap.put("verified", Verified);

        CasesRef.child(CaseRandomKey).updateChildren(caseMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            addToMyCases();

                            Intent intent = new Intent(AddNewCase.this, HomeActivity.class);
                            intent.putExtra("add_case", 3);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AddNewCase.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AddNewCase.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void addToMyCases() {

        final DatabaseReference myCases = FirebaseDatabase.getInstance().getReference().child("My Events");

        final HashMap<String,Object> mycasesMap = new HashMap<>();
        mycasesMap.put("cid", CaseRandomKey);
        mycasesMap.put("needy_name", Name);
        mycasesMap.put("description", Description);
        mycasesMap.put("cnic", CNIC);
        mycasesMap.put("image", imageUrl);
        mycasesMap.put("account", Account);
        mycasesMap.put("needed_amount", Needed_Amount);
        mycasesMap.put("collected_amount", Collected_Amount);
        mycasesMap.put("verified", Verified);

        myCases.child("Users").child(Prevalent.currentUser.getUsername()).child("myCases")
                .child(CaseRandomKey).updateChildren(mycasesMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            myCases.child("Admins").child(Prevalent.currentUser.getUsername()).child("myCases")
                                    .child(CaseRandomKey).updateChildren(mycasesMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(AddNewCase.this, "Added to My Cases List", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(AddNewCase.this,HomeActivity.class);
                                                intent.putExtra("add_case", 3);
                                                startActivity(intent);
                                            }
                                        }
                                    });

                        }
                    }
                });

    }
}
