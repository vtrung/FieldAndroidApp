package com.example.ving.fieldapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;

import java.util.*;


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

        if(name.length() > 0 && description.length() > 0){
            postRequest();
        }
    }

    private void postRequest(){
        String url = "https://cs496-vtrung.appspot.com/api/event";

        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    finishPost();
                                }
                            }, 500
                        );
                        
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        finishPost();
                    }
                }
        ){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",et1.getText().toString());
                params.put("time",et2.getText().toString());
                params.put("loc", et3.getText().toString());
                params.put("des",et4.getText().toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(sr);
    }

    public void finishPost(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
