package com.example.talha.rizq.Fragments_Tabs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.talha.rizq.AddNewCase;
import com.example.talha.rizq.AddNewEventActivity;
import com.example.talha.rizq.AdminEventsDetailActivity;
import com.example.talha.rizq.EventDetailActivity;
import com.example.talha.rizq.Model.Events;
import com.example.talha.rizq.R;
import com.example.talha.rizq.ViewHolder.AdminEventsViewHolder;
import com.example.talha.rizq.ViewHolder.EventViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminEventsFragment extends Fragment {

    private DatabaseReference EventsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public AdminEventsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        EventsRef = FirebaseDatabase.getInstance().getReference().child("Events");

        final EditText inputSearch = (EditText) view.findViewById(R.id.admin_events_inputSearch);


        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("Char :" + s);
                if (s.toString().isEmpty()) {
                    onStart();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FirebaseRecyclerOptions<Events> options1 =
                        new FirebaseRecyclerOptions.Builder<Events>()
                                .setQuery(EventsRef.orderByChild("location").startAt(s.toString()), Events.class)
                                .build();

                FirebaseRecyclerAdapter<Events, AdminEventsViewHolder> adapter1 =
                        new FirebaseRecyclerAdapter<Events, AdminEventsViewHolder>(options1) {
                            @Override
                            protected void onBindViewHolder(@NonNull AdminEventsViewHolder holder, int position, @NonNull final Events model) {
                                holder.event_desc.setText(model.getDescription());
                                holder.event_loc.setText("Location :" + model.getLocation());
                                holder.event_time.setText("Time : " + model.getTime());
                                //Picasso.get().load(model.getImage()).into(holder.productImage);

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(), AdminEventsDetailActivity.class);
                                        intent.putExtra("eid", model.getEid());
                                        startActivity(intent);
                                    }
                                });
                            }

                            @NonNull
                            @Override
                            public AdminEventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_admin_layout, viewGroup, false);
                                AdminEventsViewHolder holder = new AdminEventsViewHolder(view);
                                return holder;
                            }
                        };
                recyclerView.setAdapter(adapter1);
                adapter1.startListening();
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("Char1 :" + s);
                if (s.toString().isEmpty()) {
                    onStart();
                }
            }
        });


        final FloatingActionButton events_fab;
        events_fab = (FloatingActionButton) view.findViewById(R.id.admin_events_add_button);

        events_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddNewEventActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.admin_events_recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Events> options =
                new FirebaseRecyclerOptions.Builder<Events>().setQuery(EventsRef , Events.class)
                        .build();

        FirebaseRecyclerAdapter<Events, AdminEventsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Events, AdminEventsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminEventsViewHolder holder, int position, @NonNull final Events model) {
                        holder.event_desc.setText(model.getDescription());
                        holder.event_loc.setText("Location :"+model.getLocation());
                        holder.event_time.setText("Time : "+model.getTime());
                        //Picasso.get().load(model.getImage()).into(holder.productImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(),AdminEventsDetailActivity.class);
                                intent.putExtra("eid",model.getEid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminEventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_admin_layout, viewGroup,false);
                        AdminEventsViewHolder holder = new AdminEventsViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


}
