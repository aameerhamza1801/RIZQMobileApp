package com.example.talha.rizq;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talha.rizq.Model.Cases;
import com.example.talha.rizq.Model.Events;
import com.example.talha.rizq.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CasesDetailActivity extends AppCompatActivity {

    private TextView cd_name, cd_description, cd_cnic, cd_account, cd_amount;
    private ImageView cd_image;
    private Button donate;
    private String cid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases_detail);

        cid = getIntent().getStringExtra("cid");

        cd_name = (TextView)findViewById(R.id.user_case_detail_name);
        cd_description = (TextView)findViewById(R.id.user_case_detail_description);
        cd_cnic = (TextView)findViewById(R.id.user_case_detail_cnic);
        cd_account = (TextView)findViewById(R.id.user_case_detail_account);
        cd_amount = (TextView)findViewById(R.id.user_case_detail_amount);
        cd_image = (ImageView)findViewById(R.id.user_case_detail_image);
        donate = (Button) findViewById(R.id.donate);


        getCasesDetails(cid);

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CharSequence options[] = new CharSequence[]{
                        "Paypal",
                        "Jazz Cash",
                        "Through Rizq"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(CasesDetailActivity.this);
                builder.setTitle("Payment Options :");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            Intent intent = new Intent(CasesDetailActivity.this,PaypalPaymentActivity.class);
                            intent.putExtra("cid",cid);
                            startActivity(intent);
                        }
                        else if(which == 1){
                            Intent intent = new Intent(CasesDetailActivity.this,JazzCashActivity.class);
                            intent.putExtra("cid",cid);
                            startActivity(intent);
                        }
                        else if(which == 2){

                        }
                    }
                });
                builder.show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("add_case", 3);
        startActivity(intent);
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
