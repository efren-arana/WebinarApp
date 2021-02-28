package com.company.webinarapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMarkerDragListener {

    Button cancelMapButton, guardar;

    private GoogleMap mMap;

    Location locationthis=null;
    LocationManager locationManager;
    String coordXFin="", coordYFin="";
    boolean ya_ubico=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        cancelMapButton = (Button)findViewById(R.id.cancel_map_button);
        cancelMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
        guardar = (Button)findViewById(R.id.save_map_button);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!coordXFin.equalsIgnoreCase(""))
                {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("coordX",coordXFin);
                    returnIntent.putExtra("coordY",coordYFin);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"todavia no hay ubicacion",Toast.LENGTH_LONG).show();
                }

            }
        });

//        mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.s010_1mapagv);
//        mapFragment.getMapAsync(this);
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
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        mMap.setMinZoomPreference(12);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(MapActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        //  UI_Mapa();

        //Todo acoplo para unirlo con la escucha locListener ahi
        Log.e("inicio manager","posi");
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, locListener, Looper.getMainLooper());
        else
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locListener, Looper.getMainLooper());

        if(locationthis!=null)
        {
            mover_camara(locationthis);
        }

    }

    public LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(location!=null)
            {
                locationthis=location;
                mover_camara(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    private void mover_camara( Location location) {
        if (mMap != null&&!ya_ubico) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marcador en Guayaquil").draggable(true));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.setOnMarkerDragListener(this);
            coordXFin=String.valueOf(location.getLatitude());
            coordYFin=String.valueOf(location.getLongitude());
            ya_ubico=true;
        }
    }
    @SuppressLint("LongLogTag")
    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.i( "Inicio al presionar","drag "+marker.getPosition());
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.i("Final de presionar","drag "+marker.getPosition());
        LatLng nueva_ubi= marker.getPosition();
        coordXFin=String.valueOf(nueva_ubi.latitude);
        coordYFin=String.valueOf(nueva_ubi.longitude);
    }

}