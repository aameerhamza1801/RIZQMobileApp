package com.example.talha.rizq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.paperdb.Paper;

public class AdminHomeActivity extends AppCompatActivity {

    private Button logout,events,cases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logout = (Button) findViewById(R.id.admin_logout);
        events = (Button) findViewById(R.id.admin_events);
        cases = (Button) findViewById(R.id.admin_cases);

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this,AddNewEventActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminHomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
