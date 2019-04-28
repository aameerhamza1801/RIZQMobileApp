package com.example.talha.rizq;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.talha.rizq.Model.Cases;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminCasesDetailActivity extends AppCompatActivity {

    private TextView cd_name, cd_description, cd_cnic, cd_account, cd_amount;
    private ImageView cd_image;
    private Button verify, reject;
    private String cid = "";

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

        DatabaseReference CaseRef = FirebaseDatabase.getInstance().getReference().child("Cases");
        DatabaseReference myCaseRef = FirebaseDatabase.getInstance().getReference().child("My Events");


        getCasesDetails(cid);



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
