package com.greymatter.snowline.Handlers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.greymatter.snowline.R;
import com.greymatter.snowline.UI.HomeActivity;

public class MapHandler implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private Context context;
    private Location lastKnownLocation;
    private LatLng lastKnownLatLng;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap currentMap;
    private SupportMapFragment mapFragment;
    private Activity activity;

    public MapHandler(Activity activity, Context context, SupportMapFragment fragment){
        this.activity = activity;
        this.context = context;
        mapFragment = fragment;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public void fusedLocationClientListener(){
        fusedLocationClient.getLastLocation()
        .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    fusedLocationClientOnSuccess(location);
                    Log.v("HOME ACTIVITY", getLastKnownLocation().toString());
                    if(currentMap!=null){
                        currentMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getLastKnownLatLng(),15));
                    }
                }
            }
        });
    }

    public void fusedLocationClientOnSuccess(Location lastKnownLocation){
        this.lastKnownLocation = lastKnownLocation;
        lastKnownLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
    }

    public void onRequestPermissionsResult(int requestCode) {
        currentMap.setMyLocationEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        currentMap = googleMap;
        currentMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                activity, R.raw.map_style_json));

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},66);
            }
        } else {
            // Permission has already been granted
            currentMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public LatLng getLastKnownLatLng() {
        return lastKnownLatLng;
    }

    public GoogleMap getCurrentMap() {
        return currentMap;
    }
}
