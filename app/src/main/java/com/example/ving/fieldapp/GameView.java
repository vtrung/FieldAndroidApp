package com.example.ving.fieldapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class GameView extends AppCompatActivity {

    String event;
    String game;
    Button bt1;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = extras.getString("event");
            game = extras.getString("game");
        }

        bt1 = (Button) findViewById(R.id.button4);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGame();
            }
        });

        requestQueue = Volley.newRequestQueue(this);

    }

    private void deleteGame(){
        String url = "https://cs496-vtrung.appspot.com/api/game/" + event + "/" + game;

        StringRequest sr = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        // set delay
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        finishDelete();
                                    }
                                }, 500
                        );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        finishDelete();
                    }
                }
        );
        requestQueue.add(sr);
    }

    private void finishDelete(){
        Intent i = new Intent(getApplicationContext(), GameActivity.class);
        i.putExtra("event",event);
        startActivity(i);
    }

}
