package com.example.talha.rizq;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.talha.rizq.Global.GlobalVariables;
import com.example.talha.rizq.Model.Events;
import com.example.talha.rizq.Prevalent.Prevalent;
import com.example.talha.rizq.ViewHolder.EventViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Events");
        Paper.init(this);
        final EditText inputSearch = (EditText) findViewById(R.id.inputSearch);


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
                                .setQuery(ProductsRef.orderByChild("location").startAt(s.toString()),Events.class)
                                .build();

                FirebaseRecyclerAdapter<Events,EventViewHolder> adapter1 =
                        new FirebaseRecyclerAdapter<Events, EventViewHolder>(options1) {
                            @Override
                            protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull final Events model) {
                                holder.event_desc.setText(model.getDescription());
                                holder.event_loc.setText("Location :"+model.getLocation());
                                holder.event_time.setText("Time : "+model.getTime());
                                //Picasso.get().load(model.getImage()).into(holder.productImage);

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(HomeActivity.this,EventDetailActivity.class);
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
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("Char1 :"+s);
                if(s.toString().isEmpty()){
                    onStart();
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.profile_name);
        CircleImageView userImage = headerView.findViewById(R.id.profile_image);

        username.setText(Prevalent.currentUser.getUsername());
        Picasso.get().load(Prevalent.currentUser.getImage()).placeholder(R.drawable.profile).into(userImage);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ProfileSettingsActivity.class);
                startActivity(intent);
            }
        });


        recyclerView = findViewById(R.id.event_recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
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
        final GlobalVariables noti = (GlobalVariables) getApplicationContext();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("EventNotifications","EventNotifications",NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"EventNotifications")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setContentTitle("RIZQ Event")
                .setContentText("A new Event has been added");
        Intent intent = new Intent(this,EventDetailActivity.class);
        intent.putExtra("eid",key);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        if(noti.getNotificationIsActive()){
            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            manager.notify(0,builder.build());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Events> options =
                new FirebaseRecyclerOptions.Builder<Events>().setQuery(ProductsRef , Events.class)
                        .build();

        FirebaseRecyclerAdapter<Events, EventViewHolder> adapter =
                new FirebaseRecyclerAdapter<Events, EventViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull final Events model) {
                        holder.event_desc.setText(model.getDescription());
                        holder.event_loc.setText("Location :"+model.getLocation());
                        holder.event_time.setText("Time : "+model.getTime());
                        //Picasso.get().load(model.getImage()).into(holder.productImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomeActivity.this,EventDetailActivity.class);
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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.usernav_myevents) {
            Intent intent = new Intent(HomeActivity.this,myEventsActivity.class);
            startActivity(intent);

        } else if (id == R.id.usernav_mycases) {

        } else if (id == R.id.usernav_settings) {
            Intent intent = new Intent(HomeActivity.this,SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.usernav_logout) {
            Paper.book().destroy();
            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
