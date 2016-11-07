package com.example.ving.fieldapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class GameView extends AppCompatActivity {

    String event;
    String game;

    TextView tv1;
    TextView tv2;
    TextView tv3;

    Button bt1;
    Button bt2;

    ImageButton ib1;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);

//        getActionBar().setTitle("GameView");
//        getSupportActionBar().setTitle("GameView");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = extras.getString("event");
            game = extras.getString("game");
        }

        tv1 = (TextView)findViewById(R.id.textView2);
        tv1.setText("Event: " + event);
        tv2 = (TextView)findViewById(R.id.textView6);
        tv2.setText("Game: " + game);
        tv3 = (TextView)findViewById(R.id.textView8);

        bt1 = (Button) findViewById(R.id.game_view_button1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGame();
            }
        });

        bt2 = (Button) findViewById(R.id.game_view_button2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGame();
            }
        });

        ib1 = (ImageButton) findViewById(R.id.imageButton2);
        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        getGame();
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
        tv3.setText("Description: " + jo.getString("Description"));
    }

    private void deleteGame(){
        String url = "https://cs496-vtrung.appspot.com/api/game/" + Uri.encode(event) + "/" + Uri.encode(game);

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

    private void editGame(){
        Intent i = new Intent(getApplicationContext(), EditGameActivity.class);
        i.putExtra("event",event);
        i.putExtra("game",game);
        startActivity(i);
    }

    private void goHome(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
