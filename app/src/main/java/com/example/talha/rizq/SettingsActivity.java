package com.example.talha.rizq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.talha.rizq.Global.GlobalVariables;
import com.example.talha.rizq.Global.GlobalVariables;

public class SettingsActivity extends AppCompatActivity {

    private Switch notification, dark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        notification = (Switch) findViewById(R.id.switch_notifications);
        dark = (Switch) findViewById(R.id.switch_dark);
        final GlobalVariables globalNotifications = (GlobalVariables) getApplicationContext();

        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    globalNotifications.setNotificationIsActive(true);
                }
                else {
                    globalNotifications.setNotificationIsActive(false);
                }
            }
        });


        dark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                }
                else {

                }
            }
        });

    }
}
