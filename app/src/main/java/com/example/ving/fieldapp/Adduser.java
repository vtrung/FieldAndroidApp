package com.example.ving.fieldapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Adduser extends AppCompatActivity {

    String event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = extras.getString("event");
        }

    }
}
