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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditEventActivity extends AppCompatActivity {

    String event;
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
        setContentView(R.layout.activity_edit_event);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = extras.getString("event");
        }
        
        // Define class variables
        tv1 = (TextView) findViewById(R.id.textView4);
        et1 = (EditText) findViewById(R.id.editText7);
        et2 = (EditText) findViewById(R.id.editText8);
        et3 = (EditText) findViewById(R.id.editText9);
        bt1 = (Button) findViewById(R.id.button6);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putRequest();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        getRequest();
    }

    private void getRequest(){
        String url = "https://cs496-vtrung.appspot.com/api/event/" + event;

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        //String test = response;
                        //finishPut();
                        try {
                            parseResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        finishPut();
                    }
                }
        );
        requestQueue.add(sr);
    }

    private void parseResponse(String response) throws JSONException {
        JSONArray jr = new JSONArray(response);
        JSONObject jo = jr.getJSONObject(0);

        tv1.setText(jo.getString("Name"));
        et1.setText(jo.getString("Description"));
        et2.setText(jo.getString("Location"));
        et3.setText(jo.getString("Date"));
    }

    private void putRequest(){
        String url = "https://cs496-vtrung.appspot.com/api/event/" + event;

        StringRequest sr = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    finishPut();
                                }
                            }, 500
                        );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        finishPut();
                    }
                }
        ){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name", event);
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

    private void finishPut(){
        Intent i = new Intent(getApplicationContext(), GameActivity.class);
        i.putExtra("event",event);
        startActivity(i);
    }
}
