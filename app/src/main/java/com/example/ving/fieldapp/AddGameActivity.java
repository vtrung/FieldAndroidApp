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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;

import java.util.*;

public class AddGameActivity extends AppCompatActivity {

    String event;
    TextView tv1;
    EditText et1;
    EditText et2;
    Button bt1;
    Context context;



    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = extras.getString("event");
        }
        tv1 = (TextView) findViewById(R.id.textView3);
        tv1.setText("Add a New Game to Event: " + event);
        et1 = (EditText) findViewById(R.id.editText7);
        et2 = (EditText) findViewById(R.id.editText6);

        bt1 = (Button) findViewById(R.id.button5);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterRequest();
            }
        });

        context = getApplicationContext();

        requestQueue = Volley.newRequestQueue(this);
    }

    private void filterRequest(){
        String name = et1.getText().toString();
        String description = et2.getText().toString();
        if(name.isEmpty() || description.isEmpty()){
            //toast("name and description does not")
            CharSequence text = "Name and Description cannot be empty";

            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        postRequest();
    }
    private void postRequest(){
        String url = "https://cs496-vtrung.appspot.com/api/game";

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
                params.put("event", event);
                params.put("description",et2.getText().toString());
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

    private void finishPost(){
        Intent i = new Intent(getApplicationContext(), GameActivity.class);
        i.putExtra("event",event);
        startActivity(i); 
    }

    private void goHome(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
