package com.example.ving.fieldapp;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;

import java.util.*;
import java.util.regex.Pattern;

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
    Context context;

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

        context = getApplicationContext();
        requestQueue = Volley.newRequestQueue(this);
        getEvent();

    }

    private void getEvent(){
        String url ="https://cs496-vtrung.appspot.com/api/event/" + Uri.encode(event);
        JsonObjectRequest jq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //tv1.setText("Found " + String.valueOf(response.length()));
                        try {
                            processEvent(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                // tv1.setText(error.getMessage());
            }
        });
        requestQueue.add(jq);
    }

    private void processEvent(JSONObject jo) throws JSONException {
        tv1.setText("Event: " + jo.getString("Name"));
        et1.setText(jo.getString("Description"));
        et2.setText(jo.getString("Location"));
        et3.setText(jo.getString("Date"));
    }

    private void filterRequest(){
        String s = "abcdefà";
        Pattern p = Pattern.compile("[^a-zA-Z0-9_-]");
        boolean hasSpecialChar = p.matcher(s).find();
        String description = et1.getText().toString();
        String time = et2.getText().toString();
        String location = et3.getText().toString();
        if(description.isEmpty() || time.isEmpty() || location.isEmpty()){
            CharSequence text = "Description, Time and Location cannot be empty";

            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
    }

    private void putRequest(){
        String url = "https://cs496-vtrung.appspot.com/api/event/" + Uri.encode(event);

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
                params.put("des",et1.getText().toString());
                params.put("loc", et2.getText().toString());
                params.put("time",et3.getText().toString());

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

    private void goHome(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
