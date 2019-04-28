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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddNewEventActivity extends AppCompatActivity {

    private String desc, loc, time, currentDate, currentTime, EventRandomKey;
    private EditText event_desc, event_loc, event_time;
    private Button add;
    private DatabaseReference eventsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        eventsRef = FirebaseDatabase.getInstance().getReference().child("Events");

        event_desc = (EditText) findViewById(R.id.new_event_description);
        event_loc = (EditText) findViewById(R.id.new_event_location);
        event_time = (EditText) findViewById(R.id.new_event_time);
        add = (Button) findViewById(R.id.new_event_add);

        loadingBar = new ProgressDialog(this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateEventInfo();
            }
        });

    }

    private void ValidateEventInfo() {
        desc = event_desc.getText().toString();
        loc = event_loc.getText().toString();
        time = event_time.getText().toString();

        if(TextUtils.isEmpty(desc)){
            Toast.makeText(this, "Please Enter Event Description", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(loc)){
            Toast.makeText(this, "Please Enter Event Location", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(time)){
            Toast.makeText(this, "Please Enter Event Time", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreEventInfo();
        }
    }

    private void StoreEventInfo() {

        loadingBar.setTitle("Adding Event");
        loadingBar.setMessage("Wait while event is being added");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calender = Calendar.getInstance();
        SimpleDateFormat cDate = new SimpleDateFormat("MM dd, yyyy");
        currentDate = cDate.format(calender.getTime());

        SimpleDateFormat cTime = new SimpleDateFormat("HH:mm:ss a");
        currentTime = cTime.format(calender.getTime());

        EventRandomKey = currentDate+currentTime;

        HashMap<String , Object> eventMap = new HashMap<>();
        eventMap.put("eid",EventRandomKey);
        eventMap.put("description",desc);
        eventMap.put("location",loc);
        eventMap.put("time",time);

        eventsRef.child(EventRandomKey).updateChildren(eventMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    loadingBar.dismiss();
                    Toast.makeText(AddNewEventActivity.this, "Event added Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddNewEventActivity.this,AdminHomeNavActivity.class);
                    startActivity(intent);
                }
                else {
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(AddNewEventActivity.this, "Error :"+message, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
