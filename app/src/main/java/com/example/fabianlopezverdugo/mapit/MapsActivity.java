package com.example.fabianlopezverdugo.mapit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static java.lang.Math.abs;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        SensorEventListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    final int PERMISSION_LOCATION = 111;
    final int PERMISSION_CAMERA = 222;
    private static boolean check = false;
    final int PERMISSION_WRITE = 333;
    private MarkerOptions userMarker;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private PlaceDataSource dataSource;
    private List<Place> values;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dataSource = new PlaceDataSource(this);
        dataSource.open();

        values = dataSource.getAllPlaces();

        // Create an instance of GoogleAPIClient.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
            Log.v("CAMERA","Requesting permissions");
        } else {
            Log.v("CAMERA","Permission good");
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE);
            Log.v("CAMERA","Requesting permissions");
        } else {
            Log.v("CAMERA","Permission good");
        }
        checkForSensors();
    }

    private void checkForSensors(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); /* The Sensor Manager for the Accelerometer */
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){ /* Check if it's available */
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); /* Obtain the Sensor from the manager and keep it in the mSensor Variable */
            System.out.println("There is an accelerometer");
        }
        else {
            // In this part we could exit the application because we don't have an accelerometer
            System.out.println("No Accelerometer Detected");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationServices();
                    Log.v("M_THING","Permission granted, starting location services");
                }else{
                    //Show dialog saying i cant run your location you denied permission
                    Log.v("M_THING","Permission not granted");
                }
            }
            case  PERMISSION_CAMERA:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("CAMERA","Permission granted");
                }else{
                    //Show dialog saying i cant run your location you denied permission
                    Log.v("CAMERA","Permission not granted");
                }
            }
            case  PERMISSION_WRITE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("WRITE","Permission granted");
                }else{
                    //Show dialog saying i cant run your location you denied permission
                    Log.v("WRITE","Permission not granted");
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        System.out.println("Connecting to Google services...");
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        System.out.println("Disconnecting from Google services...");
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
            Log.v("M_THING","Requesting permissions");
        } else {
            Log.v("M_THING","Starting Location services from onConnected");
            startLocationServices();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Suspended connection...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Couldnt connect...", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.v("M_THING","Latitude"+location.getLatitude()+"|Longitude:"+location.getLongitude());
        System.out.println("Latitude"+location.getLatitude()+"|Longitude:"+location.getLongitude());
        setMarkers(new LatLng(location.getLatitude(),location.getLongitude()));
        mCurrentLocation = location;

    }

    public void startLocationServices() {
        Log.v("M_THING","Starting Location Services Called");
        try {
            System.out.println("Shit 1");
            LocationRequest req = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,req,this);
            Log.v("M_THING","Requesting location updates");
        } catch(SecurityException exception){
            //Show dialog to say user we cant get location unless they give the app permission
            System.out.println("Shit 2");
            Log.v("M_THING",exception.toString());
        }
    }

    public void setMarkers(LatLng latlng){
        if (userMarker==null){
            userMarker = new MarkerOptions()
                    .position(latlng)
                    .title("Current Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(userMarker);
            Log.v("M_THING","Current Location lat:"+latlng.latitude+"long:"+latlng.longitude);
            System.out.println("Latitude"+latlng.latitude+"|Longitude:"+latlng.longitude);
        }


        for (int i=0; i<values.size();i++){
            Place place = new Place();
            place = values.get(i);
            System.out.println(place.toString());
            LatLng newlatlng = new LatLng((double)place.getLatitude(),(double)place.getLongitude());
            MarkerOptions newMarker = new MarkerOptions()
                    .position(newlatlng)
                    .title(place.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            this.mMap.addMarker(newMarker);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        SensorEvent sensorEvent = event;
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){ /* used to identify if the event was triggered by the accelerometer or the gyroscope */
            //System.out.println("x="+event.values[0]+"|y="+event.values[1]+"|z="+event.values[2]);
            if (abs(event.values[0])>10){
                Toast.makeText(getApplicationContext(),"Shake Gesture :)", Toast.LENGTH_SHORT).show();
                float[] results = new float[1];
                for (int i=0; i<values.size();i++){
                    Place place = new Place();
                    place = values.get(i);
                    LatLng newlatlng = new LatLng((double)place.getLatitude(),(double)place.getLongitude());
                    Location.distanceBetween(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude(),newlatlng.latitude,newlatlng.longitude,results);
                    System.out.println("Results: "+results[0]);
                }
                System.out.println("I should go to an intent");
                Intent intent = new Intent(this,PreviewActivity.class);
                startActivity(intent);
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
