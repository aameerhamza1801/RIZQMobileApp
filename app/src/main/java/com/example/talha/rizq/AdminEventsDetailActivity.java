package com.example.talha.rizq;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.talha.rizq.Model.Events;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminEventsDetailActivity extends AppCompatActivity {

    private TextView event_desc, event_loc, event_time;
    private Button delete;
    private String eid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_events_detail);

        eid = getIntent().getStringExtra("eid");

        event_desc = (TextView) findViewById(R.id.admin_event_detail_description);
        event_loc = (TextView) findViewById(R.id.admin_event_detail_location);
        event_time = (TextView) findViewById(R.id.admin_event_detail_time);
        delete = (Button) findViewById(R.id.admin_event_detail_delete);

        getEventDetails(eid);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
            }
        });

    }

    private void deleteEvent() {
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
