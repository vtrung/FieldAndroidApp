package com.example.ving.fieldapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    TextView tv1;

    EditText et1;
    EditText et2;
    Button bt1;
    Button bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("login", this.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("login", false);
        editor.commit();

        tv1 = (TextView)findViewById(R.id.textError);
        et1 = (EditText)findViewById(R.id.editText);
        et2 = (EditText)findViewById(R.id.editText11);
        bt1 = (Button)findViewById(R.id.button8);
        bt2 = (Button)findViewById(R.id.button10);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLogin();
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCreate();
            }
        });


        requestQueue = Volley.newRequestQueue(this);

    }

    protected void clickLogin(){
        et1 = (EditText)findViewById(R.id.editText);
        et2 = (EditText)findViewById(R.id.editText11);
        String user = et1.getText().toString();
        String pass = et2.getText().toString();
        if(user.length() < 1 || pass.length() < 1){
            //print please fill out username and password
            tv1.setText("Please fill out username and password");
        } else {
            checkLogin(user, pass);
        }

    }

    protected void clickCreate(){
        Intent i = new Intent(getApplicationContext(), Create.class);
        startActivity(i);
    }
    protected void checkLogin(final String user, final String pass){
        String url = "https://cs496-vtrung.appspot.com/api/verify";

        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        processSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // log error
                        tv1.setText("Failed to Log in");
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
        Editor editor = sharedPreferences.edit();

        if(!response.equals("failed")){
            EditText txtname = (EditText)findViewById(R.id.editText);
            String user      =  txtname.getText().toString();
            editor.putBoolean("login", true);
            editor.putString("user", user);

            editor.commit();

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            //create token
        } else {
            editor.putBoolean("login", false);
            editor.putString("user", "");
            tv1.setText("Wrong Credentials");
            editor.commit();
            // message to application failed login, try again
        }
    }
}
