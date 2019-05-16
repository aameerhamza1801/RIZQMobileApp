package com.example.talha.rizq;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talha.rizq.Model.myEvents;
import com.example.talha.rizq.Prevalent.Prevalent;
import com.example.talha.rizq.ViewHolder.myEventsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class myEventsActivity extends AppCompatActivity {

    private  RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView total_events;
    int numEvents = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        recyclerView = findViewById(R.id.myevents_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        total_events = (TextView) findViewById(R.id.total_events);

        EventCount();
    }

    private void EventCount() {
        final DatabaseReference myeventsCountRef = FirebaseDatabase.getInstance().getReference().child("My Events").
                child("Users").child(Prevalent.currentUser.getUsername()).child("myEvents");

        myeventsCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    count +=1;
                }
                total_events.setText(Integer.toString(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference myeventsListRef = FirebaseDatabase.getInstance().getReference().child("My Events");

        FirebaseRecyclerOptions<myEvents> options = new FirebaseRecyclerOptions.Builder<myEvents>()
                .setQuery(myeventsListRef.child("Users")
                        .child(Prevalent.currentUser.getUsername()).child("myEvents"),myEvents.class).build();

        FirebaseRecyclerAdapter<myEvents,myEventsViewHolder> adapter = new FirebaseRecyclerAdapter<myEvents, myEventsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myEventsViewHolder holder, int position, @NonNull final myEvents model) {
                holder.myevent_descr.setText(model.getDescription());
                holder.myevent_loca.setText("Location : "+model.getLocation());
                holder.myevent_tim.setText("Time : "+model.getTime());

                //numEvents=numEvents + 1;
                //total_events.setText(String.valueOf(numEvents));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Detail",
                                "Delete"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(myEventsActivity.this);
                        builder.setTitle("myEvent Options :");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    Intent intent = new Intent(myEventsActivity.this,EventDetailActivity.class);
                                    intent.putExtra("eid",model.getEid());
                                    startActivity(intent);
                                }
                                if(which==1){
                                    myeventsListRef.child("Users").child(Prevalent.currentUser.getUsername())
                                            .child("myEvents").child(model.getEid()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        numEvents = numEvents -1;

                                                        //total_events.setText(String.valueOf(numEvents));
                                                        Toast.makeText(myEventsActivity.this, "Event Removed Succesfully", Toast.LENGTH_SHORT).show();
                                                        EventCount();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public myEventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.myevents_layout,viewGroup,false);
                myEventsViewHolder holder = new myEventsViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
