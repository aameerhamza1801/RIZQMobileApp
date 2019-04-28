package com.example.talha.rizq;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.example.talha.rizq.Model.Cases;
import com.example.talha.rizq.Model.myEvents;
import com.example.talha.rizq.Prevalent.Prevalent;
import com.example.talha.rizq.ViewHolder.CasesViewHolder;
import com.example.talha.rizq.ViewHolder.myEventsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class myCasesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView total_cases;
    int numCases = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cases);

        recyclerView = findViewById(R.id.mycases_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        total_cases = (TextView) findViewById(R.id.total_cases);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference mycasesListRef = FirebaseDatabase.getInstance().getReference().child("My Events");

        FirebaseRecyclerOptions<Cases> options = new FirebaseRecyclerOptions.Builder<Cases>()
                .setQuery(mycasesListRef.child("Users")
                        .child(Prevalent.currentUser.getUsername()).child("myCases"),Cases.class).build();

        FirebaseRecyclerAdapter<Cases,CasesViewHolder> adapter = new FirebaseRecyclerAdapter<Cases, CasesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CasesViewHolder holder, int position, @NonNull final Cases model) {
                holder.case_name.setText(model.getNeedy_name());
                holder.case_desc.setText(model.getDescription());
                holder.case_account.setText(model.getAccount());
                holder.case_amount.setText(" "+model.getCollected_amount()+ "  /  "+model.getNeeded_amount());
                Picasso.get().load(model.getImage()).into(holder.case_image);

                numCases=numCases + 1;
                total_cases.setText(String.valueOf(numCases));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Detail",
                                "Delete"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(myCasesActivity.this);
                        builder.setTitle("My Cases Options :");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    Intent intent = new Intent(myCasesActivity.this,CasesDetailActivity.class);
                                    intent.putExtra("cid",model.getCid());
                                    startActivity(intent);
                                }
                                if(which==1){
                                    mycasesListRef.child("Users").child(Prevalent.currentUser.getUsername())
                                            .child("myCases").child(model.getCid()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        numCases = numCases -1;
                                                        total_cases.setText(String.valueOf(numCases));
                                                        Toast.makeText(myCasesActivity.this, "Case Removed Succesfully", Toast.LENGTH_SHORT).show();

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
            public CasesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cases_user_layout,viewGroup,false);
                CasesViewHolder holder = new CasesViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
