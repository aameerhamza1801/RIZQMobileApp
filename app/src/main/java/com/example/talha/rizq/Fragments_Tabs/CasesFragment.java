package com.example.talha.rizq.Fragments_Tabs;


import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.talha.rizq.AddNewCase;
import com.example.talha.rizq.CasesDetailActivity;
import com.example.talha.rizq.EventDetailActivity;
import com.example.talha.rizq.HomeActivity;
import com.example.talha.rizq.Model.Cases;
import com.example.talha.rizq.Model.Events;
import com.example.talha.rizq.Prevalent.Prevalent;
import com.example.talha.rizq.R;
import com.example.talha.rizq.ViewHolder.CasesViewHolder;
import com.example.talha.rizq.ViewHolder.EventViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class CasesFragment extends Fragment {




    private DatabaseReference CasesRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    public CasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cases, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        CasesRef = FirebaseDatabase.getInstance().getReference().child("Cases");

        final EditText inputSearch = (EditText) view.findViewById(R.id.cases_inputSearch);

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
                FirebaseRecyclerOptions<Cases> options1 =
                        new FirebaseRecyclerOptions.Builder<Cases>()
                                .setQuery(CasesRef.orderByChild("description").startAt(s.toString()),Cases.class)
                                .build();

                FirebaseRecyclerAdapter<Cases,CasesViewHolder> adapter1 =
                        new FirebaseRecyclerAdapter<Cases, CasesViewHolder>(options1) {
                            @Override
                            protected void onBindViewHolder(@NonNull CasesViewHolder holder, int position, @NonNull final Cases model) {
                                holder.case_name.setText(model.getNeedy_name());
                                holder.case_desc.setText(model.getDescription());
                                holder.case_account.setText(model.getAccount());
                                holder.case_amount.setText(" "+model.getCollected_amount()+ "  /  "+model.getNeeded_amount());
                                Picasso.get().load(model.getImage()).into(holder.case_image);

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(),CasesDetailActivity.class);
                                        intent.putExtra("cid",model.getCid());
                                        startActivity(intent);
                                    }
                                });
                            }

                            @NonNull
                            @Override
                            public CasesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cases_user_layout, viewGroup,false);
                                CasesViewHolder holder = new CasesViewHolder(view);
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






        final FloatingActionButton cases_fab;
        cases_fab = (FloatingActionButton) view.findViewById(R.id.support_add_button);

        cases_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddNewCase.class);
                startActivity(intent);
            }
        });


        recyclerView = view.findViewById(R.id.cases_recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);



    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Cases> options =
                new FirebaseRecyclerOptions.Builder<Cases>()
                        .setQuery(CasesRef, Cases.class)
                        .build();


        FirebaseRecyclerAdapter<Cases, CasesViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cases, CasesViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CasesViewHolder holder, int position, @NonNull final Cases model)
                    {
                        holder.case_name.setText(model.getNeedy_name());
                        holder.case_desc.setText(model.getDescription());
                        holder.case_account.setText(model.getAccount());
                        holder.case_amount.setText(" "+model.getCollected_amount()+ "  /  "+model.getNeeded_amount());
                        Picasso.get().load(model.getImage()).into(holder.case_image);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(),CasesDetailActivity.class);
                                intent.putExtra("cid",model.getCid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CasesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cases_user_layout, parent, false);
                        CasesViewHolder holder = new CasesViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


}
