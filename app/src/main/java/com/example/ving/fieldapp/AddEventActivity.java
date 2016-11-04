package com.example.ving.fieldapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AddEventActivity extends AppCompatActivity {

    TextView tv1;
    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;
    Button bt1;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        tv1 = (TextView)findViewById(R.id.textView);
        tv1.setText("Select an Event");

        et1 = (EditText)findViewById(R.id.editText2);
        et2 = (EditText)findViewById(R.id.editText3);
        et3 = (EditText)findViewById(R.id.editText4);
        et4 = (EditText)findViewById(R.id.editText5);

        bt1 = (Button) findViewById(R.id.button);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });


        requestQueue = Volley.newRequestQueue(this);

    }

    public void addEvent(){
        String name = et1.getText().toString();
        String description = et2.getText().toString();
        String date = et3.getText().toString();
        String location = et4.getText().toString();

    }
}
