package com.example.talha.rizq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaypalPaymentDetailsActivity extends AppCompatActivity {

    TextView txtId,txtAmount,txtStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_payment_details);

        txtId = (TextView) findViewById(R.id.txtId);
        txtAmount = (TextView) findViewById(R.id.txtAmount);
        txtStatus = (TextView) findViewById(R.id.txtStatus);

        Intent intent = getIntent();

        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("Payment Details"));
            showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("Payment Amount"));

        } catch (JSONException e){
            e.printStackTrace();
        }


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
