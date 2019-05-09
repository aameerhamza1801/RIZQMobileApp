package com.example.talha.rizq.Fragments_Tabs;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.talha.rizq.EventDetailActivity;
import com.example.talha.rizq.Global.GlobalVariables;
import com.example.talha.rizq.HomeActivity;
import com.example.talha.rizq.Model.Events;
import com.example.talha.rizq.R;
import com.example.talha.rizq.ViewHolder.EventViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Events");
        final EditText inputSearch = (EditText) view.findViewById(R.id.inputSearch);


        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("Char :"+s);
                if(s.toString().isEmpty()){
                    onStart();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FirebaseRecyclerOptions<Events> options1 =
                        new FirebaseRecyclerOptions.Builder<Events>()
                                .setQuery(ProductsRef.orderByChild("location").startAt(s.toString().toLowerCase()),Events.class)
                                .build();

                FirebaseRecyclerAdapter<Events,EventViewHolder> adapter1 =
                        new FirebaseRecyclerAdapter<Events, EventViewHolder>(options1) {
                            @Override
                            protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull final Events model) {

                                holder.card.setAnimation(AnimationUtils.loadAnimation(recyclerView.getContext(),R.anim.fade_scale_animation));

                                holder.event_desc.setText(model.getDescription());
                                holder.event_loc.setText("Location :"+model.getLocation());
                                holder.event_time.setText("Time : "+model.getTime());
                                //Picasso.get().load(model.getImage()).into(holder.productImage);

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(),EventDetailActivity.class);
                                        intent.putExtra("eid",model.getEid());
                                        startActivity(intent);
                                    }
                                });
                            }

                            @NonNull
                            @Override
                            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_user_layout, viewGroup,false);
                                EventViewHolder holder = new EventViewHolder(view);
                                return holder;
                            }
                        };
                recyclerView.setAdapter(adapter1);
                adapter1.startListening();
                //runAnimation(recyclerView);
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("Char1 :"+s);
                if(s.toString().isEmpty()){
                    onStart();
                }
            }
        });


        recyclerView = view.findViewById(R.id.event_recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ProductsRef.addChildEventListener(new ChildEventListener() {

            private long attachTime = System.currentTimeMillis();

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Events Event = dataSnapshot.getValue(Events.class);
                System.out.println("Add "+dataSnapshot.getKey()+" to UI after "+s);
                if(Event.getCurrentTime()>attachTime){
                    addEventNotification(dataSnapshot.getKey());
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addEventNotification(String key) {
        final GlobalVariables noti = (GlobalVariables) getActivity().getApplicationContext();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("EventNotifications","EventNotifications",NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(),"EventNotifications")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setContentTitle("RIZQ Event")
                .setContentText("A new Event has been added");
        Intent intent = new Intent(getActivity(),EventDetailActivity.class);
        intent.putExtra("eid",key);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        if(noti.getNotificationIsActive()){
            NotificationManagerCompat manager = NotificationManagerCompat.from(getActivity());
            manager.notify(0,builder.build());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Events> options =
                new FirebaseRecyclerOptions.Builder<Events>().setQuery(ProductsRef , Events.class)
                        .build();

        FirebaseRecyclerAdapter<Events, EventViewHolder> adapter =
                new FirebaseRecyclerAdapter<Events, EventViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull final Events model) {

                        holder.card.setAnimation(AnimationUtils.loadAnimation(recyclerView.getContext(),R.anim.fade_scale_animation));

                        holder.event_desc.setText(model.getDescription());
                        holder.event_loc.setText("Location :"+model.getLocation());
                        holder.event_time.setText("Time : "+model.getTime());
                        //Picasso.get().load(model.getImage()).into(holder.productImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(),EventDetailActivity.class);
                                intent.putExtra("eid",model.getEid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_user_layout, viewGroup,false);
                        EventViewHolder holder = new EventViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

        //runAnimation(recyclerView);




    }

    /*private void runAnimation(RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_falldown);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }*/
}
