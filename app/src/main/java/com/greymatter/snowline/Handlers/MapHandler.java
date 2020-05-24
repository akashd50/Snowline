package com.greymatter.snowline.Handlers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.greymatter.snowline.Objects.ORSDirection;
import com.greymatter.snowline.R;
import com.greymatter.snowline.app.Constants;

public class MapHandler extends LocationCallback implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {
    private Context context;
    private Location lastKnownLocation;
    private LatLng lastKnownLatLng;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap currentMap;
    private SupportMapFragment mapFragment;
    private Activity activity;
    private boolean followUserLocation;
    private float currentZoomLevel;

    public MapHandler(Activity activity, Context context, SupportMapFragment fragment){
        this.activity = activity;
        this.context = context;
        this.mapFragment = fragment;
        this.currentZoomLevel = Constants.ZOOM_STREETS;
        this.followUserLocation = true;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        LocationRequest locationRequest = LocationRequest.create().setInterval(10000).setFastestInterval(5000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationClient.requestLocationUpdates(locationRequest, this, Looper.getMainLooper());
    }

    private void setLocation(Location lastKnownLocation){
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

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION},66);
        } else {
            // Permission has already been granted
            currentMap.setMyLocationEnabled(true);
        }
    }

    public void drawRouteOnMap(ORSDirection direction) {
        currentMap.addPolyline(new PolylineOptions().clickable(true).addAll(direction.getDrawableRoute()));
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        setFollowUserLocation(true);
        setCurrentZoomLevel(Constants.ZOOM_STREETS);
    }

    @Override
    public boolean onMyLocationButtonClick() {

        return false;
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        for (Location location : locationResult.getLocations()) {
            System.out.println(location.toString());
            setLocation(location);
            if(followUserLocation()) {
                animate(getLastKnownLatLng());
            }
        }
    }

    public void animate(LatLng location) {
        currentMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(location, getCurrentZoomLevel())));
    }

    public boolean followUserLocation() {
        return followUserLocation;
    }

    public void setFollowUserLocation(boolean followUserLocation) {
        this.followUserLocation = followUserLocation;
    }

    public float getCurrentZoomLevel() {
        return currentZoomLevel;
    }

    public void setCurrentZoomLevel(float currentZoomLevel) {
        this.currentZoomLevel = currentZoomLevel;
    }

    public void clear() {
        currentMap.clear();
    }

    public void addMarker(LatLng latLng, String title, Object tag) {
        MarkerOptions marker = new MarkerOptions()
                .position(latLng)
                .title(title);
        currentMap.addMarker(marker).setTag(tag);
    }

    @Override
    public void onLocationAvailability(LocationAvailability locationAvailability) {
        super.onLocationAvailability(locationAvailability);
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
