package com.example.talha.rizq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaypalPaymentDetailsActivity extends AppCompatActivity {

    TextView txtId,txtAmount,txtStatus;
    Button back;
    private String cid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_payment_details);



        txtId = (TextView) findViewById(R.id.txtId);
        txtAmount = (TextView) findViewById(R.id.txtAmount);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        back = (Button) findViewById(R.id.paypal_back);

        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");

        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("Payment Details"));
            showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("Payment Amount"));

        } catch (JSONException e){
            e.printStackTrace();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PaypalPaymentDetailsActivity.this,CasesDetailActivity.class);
                intent1.putExtra("cid",cid);
                startActivity(intent1);
            }
        });


    }

    private void showDetails(JSONObject response, String payment_amount) {
        try{
            txtId.setText(response.getString("id"));
            txtAmount.setText("$"+payment_amount);
            txtStatus.setText(response.getString("state"));

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
