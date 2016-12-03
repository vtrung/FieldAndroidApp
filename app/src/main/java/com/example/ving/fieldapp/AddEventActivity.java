package com.example.ving.fieldapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.TextView;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.content.Context;
import android.net.Uri;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;

import java.util.*;
import java.math.*;
import java.util.regex.Pattern;


public class AddEventActivity extends AppCompatActivity implements LocationListener {

    TextView tv1;
    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;

    Button bt1;
    Button bt2;
    RequestQueue requestQueue;
    Context context;
    String user;
    LocationManager lm = null;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        SharedPreferences settings = this.getSharedPreferences("login", this.MODE_PRIVATE);
        user = settings.getString("user", "");

        tv1 = (TextView)findViewById(R.id.textView);
        tv1.setText("Add a New Event");

        et1 = (EditText)findViewById(R.id.editText2);
        et2 = (EditText)findViewById(R.id.editText3);
        et3 = (EditText)findViewById(R.id.editText4);
        et4 = (EditText)findViewById(R.id.editText5);

        bt1 = (Button) findViewById(R.id.button);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });

        bt2 = (Button) findViewById(R.id.button9);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //placepicker();
                //getLastBestLocation();
                getLocation();
            }
        });

        context = getApplicationContext();

        requestQueue = Volley.newRequestQueue(this);
        lm = (LocationManager)getSystemService(this.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


    }

    public void getLocation(){
        Location loc = getLastBestLocation();
        double lat = loc.getLatitude();
        double lon = loc.getLongitude();
        BigDecimal bdlat = new BigDecimal(lat);
        bdlat = bdlat.round(new MathContext(6));
        double roundlat = bdlat.doubleValue();

        BigDecimal bdlon = new BigDecimal(lon);
        bdlon = bdlon.round(new MathContext(6));
        double roundlon = bdlon.doubleValue();

        et4.setText(String.valueOf(roundlat) + ',' + String.valueOf(roundlon));
    }

    public void addEvent(){
        String s = "abcdefà";
        Pattern p = Pattern.compile("[^a-zA-Z0-9_-]");

        String name = et1.getText().toString();
        String description = et2.getText().toString();
        String date = et3.getText().toString();
        String location = et4.getText().toString();
        if(name.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()){
            CharSequence text = "No inputs cannot be empty";

            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
//        if(p.matcher(name).find()){
//            CharSequence text = "Name can only contain alphanumeric, '_', and '-' characters";
//
//            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
//            toast.show();
//            return;
//        }
        if(name.length() > 0 && description.length() > 0){
            postRequest();
        }
    }

    private void postRequest(){
        String url = "https://cs496-vtrung.appspot.com/api/eventperson";

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
                params.put("username", user);
                params.put("name",et1.getText().toString());
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

    public void finishPost(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    private void placepicker(){
        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(i);
    }

    private Location getLastBestLocation() {
        Location locationGPS = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            return locationGPS;
        }
        else {
            return locationNet;
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        //tv1.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        //getLocation();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    private void goHome(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

}
