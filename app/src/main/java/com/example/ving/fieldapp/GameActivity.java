package com.example.ving.fieldapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class GameActivity extends AppCompatActivity {

    String event;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;

    ListView lv1;
    Button bt1;
    Button bt2;
    Button bt3;
    ImageButton ib1;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = extras.getString("event");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tv1 = (TextView) findViewById(R.id.textViewGames1);
        tv1.setText("Event: " + event);
        tv2 = (TextView) findViewById(R.id.textViewGames2);
        tv3 = (TextView) findViewById(R.id.textViewGames3);
        tv4 = (TextView) findViewById(R.id.textViewGames4);
        

        bt1 = (Button) findViewById(R.id.button2);
        bt2 = (Button) findViewById(R.id.button3);
        bt3 = (Button) findViewById(R.id.button4);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGame();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEvent();
            }
        });

        ib1 = (ImageButton) findViewById(R.id.imageButton2);
        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });

        lv1 = (ListView) findViewById(R.id.listView1);
        requestQueue = Volley.newRequestQueue(this);

        getGames();
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
        tv2.setText("Description: " + jo.getString("Description"));
        tv3.setText("Location: " + jo.getString("Location"));
        tv4.setText("Time: " + jo.getString("Date"));
    }

    private void getGames(){
        String url = "https://cs496-vtrung.appspot.com/api/game/" + Uri.encode(event);
        JsonArrayRequest jq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        tv1.setText("Event: " + event + " - Found " + String.valueOf(response.length()));
                        try {
                            processGames(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                //tv1.setText(error.getMessage());
            }
        });
        requestQueue.add(jq);
    }


    private void processGames(JSONArray response) throws JSONException {
        ArrayList<String> array = new ArrayList<String>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonobject = response.getJSONObject(i);
            String name = jsonobject.getString("Name");
            String des = jsonobject.getString("Description");
            array.add(name);
        }
        String[] stringArray = array.toArray(new String[0]);
        setGameList(stringArray);
    }

    private void setGameList(String[] array){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, array);

        lv1.setAdapter(adapter);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) lv1.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();

                selectGame(itemValue);
            }

        });
    }

    private void selectGame(String game){
        Intent i = new Intent(getApplicationContext(), GameView.class);
        i.putExtra("event", event);
        i.putExtra("game",game);
        startActivity(i);
    }

    private void addGame(){
        Intent i = new Intent(getApplicationContext(), AddGameActivity.class);
        i.putExtra("event", event);
        startActivity(i);
    }

    private void editEvent(){
        Intent i = new Intent(getApplicationContext(), EditEventActivity.class);
        i.putExtra("event", event);
        startActivity(i);
    }

    private void deleteEvent(){
        String url = "https://cs496-vtrung.appspot.com/api/event/" + Uri.encode(event);

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
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    private void goHome(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}

