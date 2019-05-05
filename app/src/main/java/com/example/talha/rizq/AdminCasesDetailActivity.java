package com.example.talha.rizq;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talha.rizq.Model.Cases;
import com.example.talha.rizq.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminCasesDetailActivity extends AppCompatActivity {

    private TextView cd_name, cd_description, cd_cnic, cd_account, cd_amount;
    private ImageView cd_image;
    private Button verify, reject, verified;
    private String cid = "";
    private int ver = 0;
    private boolean task1=false,task2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cases_detail);


        cid = getIntent().getStringExtra("cid");

        cd_name = (TextView)findViewById(R.id.admin_case_detail_name);
        cd_description = (TextView)findViewById(R.id.admin_case_detail_description);
        cd_cnic = (TextView)findViewById(R.id.admin_case_detail_cnic);
        cd_account = (TextView)findViewById(R.id.admin_case_detail_account);
        cd_amount = (TextView)findViewById(R.id.admin_case_detail_amount);
        cd_image = (ImageView)findViewById(R.id.admin_case_detail_image);
        verify = (Button) findViewById(R.id.admin_verify);
        reject = (Button) findViewById(R.id.admin_reject);
        verified = (Button) findViewById(R.id.admin_verified);

        final DatabaseReference CaseRef = FirebaseDatabase.getInstance().getReference().child("Cases");


        getCasesDetails(cid);

        CaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(cid).child("verified").getValue().toString().equals("0")){
                    ver = 0;
                    verified.setVisibility(View.INVISIBLE);
                    verify.setVisibility(View.VISIBLE);
                    reject.setVisibility(View.VISIBLE);
                }
                else{
                    ver = 1;
                    verified.setVisibility(View.VISIBLE);
                    verify.setVisibility(View.INVISIBLE);
                    reject.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if(ver==0) {
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        CaseRef.child(cid).child("verified").setValue("1");
                        Intent intent = new Intent(AdminCasesDetailActivity.this, AdminHomeNavActivity.class);
                        intent.putExtra("add_case", 2);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCase();
                    if(task1 & task2) {
                        Intent intent = new Intent(AdminCasesDetailActivity.this, AdminHomeNavActivity.class);
                        intent.putExtra("add_case", 2);
                        startActivity(intent);
                    }
                }
            });
        }


    }

    private void deleteCase() {

        DatabaseReference EventRef = FirebaseDatabase.getInstance().getReference().child("Cases");
        EventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    if (child.getKey().toString().equals(cid) ){
                        child.getRef().removeValue();
                    }
                }
                task1 = true;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference myCaseRef = FirebaseDatabase.getInstance().getReference().child("My Events").child("Users");
        myCaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    for (DataSnapshot grandChild : child.child("myCases").getChildren())
                    {
                        if (grandChild.getKey().toString().equals(cid) ){
                            grandChild.getRef().removeValue();
                        }
                    }
                }
                task2 = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getCasesDetails(String cid) {
        DatabaseReference casesRef = FirebaseDatabase.getInstance().getReference().child("Cases");
        casesRef.child(cid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Cases cases = dataSnapshot.getValue(Cases.class);

                    cd_description.setText(cases.getNeedy_name());
                    cd_description.setText(cases.getDescription());
                    cd_cnic.setText(cases.getCnic());
                    cd_account.setText(cases.getAccount());
                    cd_amount.setText("Amount :  "+cases.getCollected_amount()+"  /  "+cases.getNeeded_amount());
                    Picasso.get().load(cases.getImage()).into(cd_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
