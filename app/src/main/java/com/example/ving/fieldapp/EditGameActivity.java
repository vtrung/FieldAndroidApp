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
import android.widget.Toast;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.*;

public class EditGameActivity extends AppCompatActivity {

    String game;
    String event;
    TextView tv1;
    TextView tv2;
    EditText et1;
    EditText et2;
    Button bt1;
    RequestQueue requestQueue;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_game);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = extras.getString("event");
            game = extras.getString("game");
        }

        tv1 = (TextView) findViewById(R.id.textView5);
        tv1.setText("Game: " + game);
        et1 = (EditText) findViewById(R.id.editText10);
        bt1 = (Button) findViewById(R.id.button7);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putRequest();
            }
        });
        context = getApplicationContext();

        requestQueue = Volley.newRequestQueue(this);

        getGame();
    }

    private void getRequest(){
        String url = "https://cs496-vtrung.appspot.com/api/game/" + Uri.encode(event) + "/" + Uri.encode(game);

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
    }

    private void filterRequest(){
        String s = "abcdefà";
        Pattern p = Pattern.compile("[^a-zA-Z0-9_-]");
        boolean hasSpecialChar = p.matcher(s).find();
        String description = et1.getText().toString();
        if(description.isEmpty()){
            CharSequence text = "Name and Description cannot be empty";

            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
    }
    private void putRequest(){
        String url ="https://cs496-vtrung.appspot.com/api/game/" + Uri.encode(event) + "/" + Uri.encode(game);

        StringRequest sr = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        // set delay
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
                params.put("description",et1.getText().toString());

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

    private void getGame(){
        String url ="https://cs496-vtrung.appspot.com/api/game/" + Uri.encode(event) + "/" + Uri.encode(game);
        JsonObjectRequest jq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //tv1.setText("Found " + String.valueOf(response.length()));
                        try {
                            processGame(response);
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

    private void processGame(JSONObject jo) throws JSONException {
        et1.setText(jo.getString("Description"));
    }

    private void finishPut(){
        Intent i = new Intent(getApplicationContext(), GameView.class);
        i.putExtra("event",event);
        i.putExtra("game",game);
        startActivity(i);
    }

    private void goHome(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
