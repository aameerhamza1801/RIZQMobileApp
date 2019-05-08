package com.example.talha.rizq;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talha.rizq.Model.Events;
import com.example.talha.rizq.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class EventDetailActivity extends AppCompatActivity {

    private TextView event_desc, event_loc, event_time;
    private Button volunteer;
    private String eid = "";
    private int already = 0;
    private Boolean task1 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        eid = getIntent().getStringExtra("eid");

        event_desc = (TextView) findViewById(R.id.event_detail_description);
        event_loc = (TextView) findViewById(R.id.event_detail_location);
        event_time = (TextView) findViewById(R.id.event_detail_time);
        volunteer = (Button) findViewById(R.id.event_detail_volunteer);

        getEventDetails(eid);

        final DatabaseReference myEvents1 = FirebaseDatabase.getInstance().getReference().child("My Events").
                child("Users").child(Prevalent.currentUser.getUsername()).child("myEvents").child(eid);
        myEvents1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    volunteer.setVisibility(View.INVISIBLE);

                    already = 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            if (already == 0) {
                volunteer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToMyEvents();
                    }
                });
            }
    }





    private void addToMyEvents() {
        String currentDate, currentTime;
        Calendar clickDate = Calendar.getInstance();

        SimpleDateFormat cD = new SimpleDateFormat("MM dd, yyyy");
        currentDate = cD.format(clickDate.getTime());

        SimpleDateFormat cT = new SimpleDateFormat("HH:mm:ss a");
        currentTime = cT.format(clickDate.getTime());


        final DatabaseReference myEvents = FirebaseDatabase.getInstance().getReference().child("My Events");

        final HashMap<String,Object> myeventMap = new HashMap<>();
        myeventMap.put("eid",eid);
        myeventMap.put("description",event_desc.getText().toString());
        myeventMap.put("location",event_loc.getText().toString());
        myeventMap.put("time",event_time.getText().toString());

        myEvents.child("Users").child(Prevalent.currentUser.getUsername()).child("myEvents")
                .child(eid).updateChildren(myeventMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            myEvents.child("Admins").child(Prevalent.currentUser.getUsername()).child("myEvents")
                                    .child(eid).updateChildren(myeventMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(EventDetailActivity.this, "Added to myEvents List", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(EventDetailActivity.this,HomeActivity.class);
                                                intent.putExtra("add_case", 1);
                                                startActivity(intent);
                                            }
                                        }
                                    });

                        }
                    }
                });

    }

    private void getEventDetails(String eid) {
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events");
        eventRef.child(eid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Events event = dataSnapshot.getValue(Events.class);

                    event_desc.setText(event.getDescription());
                    event_loc.setText(event.getLocation());
                    event_time.setText(event.getTime());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
