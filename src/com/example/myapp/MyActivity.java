package com.example.myapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URL;

public class MyActivity extends Activity implements SensorEventListener, LocationListener {
    // Start with some variables
    private SensorManager sensorMan;
    private Sensor accelerometer;

    private LocationManager locationManager;

    static public MyActivity me;

    private Challenge c = null;

    private boolean done = false;

    private String id;

    private double distance = 9;

    private Location prev = null;

    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.question);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                id = ((EditText)findViewById(R.id.userInput)).getText().toString();
                setContentView(R.layout.main);
                TextView text = (TextView) findViewById(R.id.test);
                text.setText(id);
                OpenUrl.text = text;
                new OpenUrl().execute("http://coderelay.cloudapp.net:8182/receive/?id=" + id);
                Button b2 = (Button) findViewById(R.id.challengeButton);
                b2.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                        new GetChallenge().execute("http://coderelay.cloudapp.net:8182/work/?id=" + id);
                    }
                });
                final Button b = (Button) findViewById(R.id.close);
                b.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        me.finish();
                    }
                });
            }
        });
        /*
        setContentView(R.layout.main);
        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        TextView text = (TextView)findViewById(R.id.test);
        OpenUrl.text = text;
        text.setText("");*/

        //new OpenUrl().execute("http://localhost:8182/work/");
    }

    @Override
    public void onResume() {
        super.onResume();
       /* sensorMan.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_UI);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
       // sensorMan.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
       /* if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = FloatMath.sqrt(x * x + y * y + z * z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 3 && !done) {
                done = true;
                TextView text = (TextView)findViewById(R.id.test);
                text.setText("undoing any work stop");
                new OpenUrl().execute("http://coderelay.cloudapp.net:8182/work/?id=0");
            }
        }*/
    }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // required method
        }

    @Override
    public void onLocationChanged(Location location) {
        if(prev != null)
        {
            distance += Math.abs(location.distanceTo(prev));
        }
        prev = location;
        if(c.getLength() < distance)
        {
            clearChallenge();
            locationManager.removeUpdates(me);
            OpenUrl.text.setText("Challenge done");
            distance = 0;
            return;
        }
        OpenUrl.text.setText(String.valueOf(distance));
    }

    @Override
    public void onProviderDisabled(String provider) {

        /******** Called when User off Gps *********/

    }

    @Override
    public void onProviderEnabled(String provider) {

        /******** Called when User on Gps  *********/

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void startChallenge(int i)
    {
        if(i == -1)
        {
            c = null;
            return;
        }
        else if(c != null)
        {
            return;
        }
        c = Challenge.getChallenge(i);
        switch(c)
        {
            case DISTANCE:
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, me);
                TextView text = (TextView) findViewById(R.id.test);
                text.setText("you have to go 100 meters");
                break;
            case PUSHUP:
        }
    }

    public void clearChallenge()
    {
        new OpenUrl().execute("http://coderelay.cloudapp.net:8182/work/?id=" + id + "&clear=true");
        startChallenge(-1);
    }
}
