package com.example.ving.fieldapp;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.*;
import android.view.View;
import android.view.Window;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toolbar;

import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView tv1;
    ListView lv1;
    Button bt1;

    ImageButton ib1;

    Button logoutbtn;
    TextView usertext;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_main);
        //logout();
        checkLoggedIn();

        tv1 = (TextView)findViewById(R.id.textView1);
        tv1.setText("Select an Event");

        bt1 = (Button) findViewById(R.id.button);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });

        logoutbtn = (Button) findViewById(R.id.logout);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        SharedPreferences settings = this.getSharedPreferences("login", this.MODE_PRIVATE);
        String user = settings.getString("user", "");
        usertext = (TextView) findViewById(R.id.userView);
        usertext.setText(user);

        ib1 = (ImageButton) findViewById(R.id.imageButton);
        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });


        lv1 = (ListView) findViewById(R.id.listView1);
        requestQueue = Volley.newRequestQueue(this);

        getEventList();

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        logout();
    }
//    @Override
//    public void onStop(){
//        super.onStop();
//        logout();
//    }

    private void getEventList(){
        String url ="https://cs496-vtrung.appspot.com/api/event";

        JsonArrayRequest jq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        tv1.setText("Select an Event: Found " + String.valueOf(response.length()));
                        try {
                            processEvents(response);
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


    private void processEvents(JSONArray response) throws JSONException {
        ArrayList<String> array = new ArrayList<String>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonobject = response.getJSONObject(i);
            String name = jsonobject.getString("Name");
            String des = jsonobject.getString("Description");
            array.add(name);
        }
        String[] stringArray = array.toArray(new String[0]);
        setEventList(stringArray);
    }


    private void setEventList(String[] list){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list);

        lv1.setAdapter(adapter);
        lv1.setOnItemClickListener(new OnItemClickListener() {

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

                selectEvent(itemValue);
            }

        });
    }
    private void selectEvent(String event){
        Intent i = new Intent(getApplicationContext(), GameActivity.class);
        i.putExtra("event",event);
        startActivity(i);
    }

    private void addEvent(){
        Intent i = new Intent(getApplicationContext(), AddEventActivity.class);
        startActivity(i);
    }

    private void goHome(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    private void checkLoggedIn(){
        SharedPreferences settings = this.getSharedPreferences("login", this.MODE_PRIVATE);
        Boolean result = settings.getBoolean("login", false);

        if(!result){
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }
    }

    private void logout(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("login",this.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("login", false);
        editor.putString("user", "");
        editor.commit();
        checkLoggedIn();
    }


}
