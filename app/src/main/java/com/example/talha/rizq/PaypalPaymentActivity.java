package com.example.talha.rizq;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.talha.rizq.Configuration.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class PaypalPaymentActivity extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE = 7070;


    private String cid = "";


    private static PayPalConfiguration configuration= new PayPalConfiguration().
            environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).
            clientId(Config.PAYPAL_CLIENT_ID);

    Button pay;
    EditText amount;
    String amt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_payment);


        cid = getIntent().getStringExtra("cid");

        // Start Paypal Service
        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
        startService(intent);


        amount = (EditText)findViewById(R.id.paypal_amount);
        pay = (Button)findViewById(R.id.paypal_button);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessPayment();
            }
        });

    }

    private void ProcessPayment() {
        amt = amount.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amt)),"USD",
                "Donate for RIZQ",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this,PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PAYPAL_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation!=null){
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);

                        startActivity(new Intent(this,PaypalPaymentDetailsActivity.class).
                                putExtra("Payment Amount",amt).
                                putExtra("Payment Details",paymentDetails).
                                putExtra("cid",cid)
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(resultCode==Activity.RESULT_CANCELED) {


                Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_SHORT).show();

            } else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){


                Toast.makeText(this, "Invalid Inputs, Try again ", Toast.LENGTH_SHORT).show();

                
            }

        }
    }
}
