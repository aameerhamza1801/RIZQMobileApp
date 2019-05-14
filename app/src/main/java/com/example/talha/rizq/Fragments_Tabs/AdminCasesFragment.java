package com.example.talha.rizq.Fragments_Tabs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.example.talha.rizq.AdminCasesDetailActivity;
import com.example.talha.rizq.CasesDetailActivity;
import com.example.talha.rizq.Model.Cases;
import com.example.talha.rizq.R;
import com.example.talha.rizq.ViewHolder.AdminCasesViewHolder;
import com.example.talha.rizq.ViewHolder.CasesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class AdminCasesFragment extends Fragment {

    private DatabaseReference CasesRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    public AdminCasesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_cases, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        CasesRef = FirebaseDatabase.getInstance().getReference().child("Cases");

        final EditText inputSearch = (EditText) view.findViewById(R.id.admin_cases_inputSearch);

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
                FirebaseRecyclerOptions<Cases> options1 =
                        new FirebaseRecyclerOptions.Builder<Cases>()
                                .setQuery(CasesRef.orderByChild("description").startAt(s.toString()), Cases.class)
                                .build();

                FirebaseRecyclerAdapter<Cases, AdminCasesViewHolder> adapter1 =
                        new FirebaseRecyclerAdapter<Cases, AdminCasesViewHolder>(options1) {
                            @Override
                            protected void onBindViewHolder(@NonNull AdminCasesViewHolder holder, int position, @NonNull final Cases model) {

                                holder.card.setAnimation(AnimationUtils.loadAnimation(recyclerView.getContext(),R.anim.fade_scale_animation));

                                holder.case_name.setText(model.getNeedy_name());
                                holder.case_desc.setText(model.getDescription());
                                holder.case_account.setText(model.getAccount());
                                holder.case_amount.setText(" " + model.getCollected_amount() + "  /  " + model.getNeeded_amount());
                                Picasso.get().load(model.getImage()).into(holder.case_image);

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(),AdminCasesDetailActivity.class);
                                        intent.putExtra("cid",model.getCid());
                                        startActivity(intent);
                                    }
                                });
                            }

                            @NonNull
                            @Override
                            public AdminCasesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cases_admin_layout, viewGroup, false);
                                AdminCasesViewHolder holder = new AdminCasesViewHolder(view);
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

        recyclerView = view.findViewById(R.id.admin_cases_recycler_menu);
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


        FirebaseRecyclerAdapter<Cases, AdminCasesViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cases, AdminCasesViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminCasesViewHolder holder, int position, @NonNull final Cases model)
                    {

                        holder.card.setAnimation(AnimationUtils.loadAnimation(recyclerView.getContext(),R.anim.fade_scale_animation));

                        holder.case_name.setText(model.getNeedy_name());
                        holder.case_desc.setText(model.getDescription());
                        holder.case_account.setText(model.getAccount());
                        holder.case_amount.setText(" " + model.getCollected_amount() + "  /  " + model.getNeeded_amount());
                        Picasso.get().load(model.getImage()).into(holder.case_image);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), AdminCasesDetailActivity.class);
                                intent.putExtra("cid", model.getCid());
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public AdminCasesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cases_admin_layout, parent, false);
                        AdminCasesViewHolder holder = new AdminCasesViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
