package com.example.ving.fieldapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    String event;
    TextView tv1;
    ListView lv1;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = extras.getString("event");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tv1 = (TextView) findViewById(R.id.textView1);
        tv1.setText(event);

        lv1 = (ListView) findViewById(R.id.listView1);
        requestQueue = Volley.newRequestQueue(this);

        getGames();
    }

    private void getGames(){
        String url = "https://cs496-vtrung.appspot.com/api/game/" + event;
        JsonArrayRequest jq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        tv1.setText("Found " + String.valueOf(response.length()));
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
                tv1.setText(error.getMessage());
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
}
