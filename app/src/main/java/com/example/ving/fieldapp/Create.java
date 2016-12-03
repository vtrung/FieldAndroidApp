package com.example.ving.fieldapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Create extends AppCompatActivity {

    RequestQueue requestQueue;

    TextView tv1;
    EditText et1;
    EditText et2;
    Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        tv1 = (TextView) findViewById(R.id.textError1);
        et1 = (EditText) findViewById(R.id.editUser1);
        et2 = (EditText) findViewById(R.id.editUser2);
        bt1 = (Button) findViewById(R.id.button11);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitClick();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
    }

    protected void submitClick(){
        et1 = (EditText) findViewById(R.id.editUser1);
        et2 = (EditText) findViewById(R.id.editUser2);
        String user = et1.getText().toString();
        String pass = et2.getText().toString();
        if(user.length() < 1 || pass.length() < 1){
            //print please fill out username and password
            tv1.setText("Please fill out username and password");
        } else {
            createUser(user, pass);
        }

    }

    protected void createUser(final String user,final String pass){
        String url = "https://cs496-vtrung.appspot.com/api/person";

        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        processSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tv1.setText("Failed to create user");
                        processFailure();
                    }
                }
        ){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", user);
                params.put("password", pass);

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

    protected void processSuccess(String response){
        if(!response.equals("failed")) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        } else {
            tv1.setText("Failed to create user");
        }
    }

    protected void processFailure(){
        //print failed
    }
}

