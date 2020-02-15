package com.greymatter.snowline.UI;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.greymatter.snowline.Data.Constants;
import com.greymatter.snowline.DataParsers.JSONParser;
import com.greymatter.snowline.DataParsers.StopParser;
import com.greymatter.snowline.Handlers.HomeActivityHelper;
import com.greymatter.snowline.Handlers.LinkGenerator;
import com.greymatter.snowline.Handlers.MapHandler;
import com.greymatter.snowline.Handlers.RequestHandler;
import com.greymatter.snowline.Objects.Stop;
import com.greymatter.snowline.Objects.StopSchedule;
import com.greymatter.snowline.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class HomeActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback{
    private FloatingActionButton moreOptionsButton;
    private View.OnClickListener homeScreenClicksListener;
    private PopupMenu optionsMenu;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationClient;
    private LinkGenerator linkGenerator;
    private ArrayList<Stop> nearbyStops;
    private MapHandler mapHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        moreOptionsButton = findViewById(R.id.more_options_act_home);
        linkGenerator = new LinkGenerator();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.onCreate(savedInstanceState);
        mapFragment.getMapAsync(this);
        mapHandler = new MapHandler(HomeActivity.this, HomeActivity.this, mapFragment);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mapHandler.fusedLocationClientOnSuccess(location);
                            Log.v("HOME ACTIVITY", mapHandler.getLastKnownLocation().toString());
                        }
                    }
                });
        setUpUIElements();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mapHandler.onRequestPermissionsResult(requestCode);
    }

    private void setUpUIElements(){
        setUpOptionsMenu();
        setUpButtonListener();
    }

    private void setUpButtonListener(){
        homeScreenClicksListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.more_options_act_home:
                        optionsMenu.show();
                        break;
                }
            }
        };
        //assign listener to the buttons
        moreOptionsButton.setOnClickListener(homeScreenClicksListener);
    }

    private void setUpOptionsMenu(){
        optionsMenu = new PopupMenu(this,moreOptionsButton);
        optionsMenu.inflate(R.menu.home_screen_options_menu);
        optionsMenu.setGravity(Gravity.RIGHT);
        optionsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_scr_find_stop_schedule:
                        startActivity(new Intent(HomeActivity.this, StopScheduleActivity.class));
                        break;
                    case R.id.home_scr_find_nearby_stops:
                        if (mapHandler.getLastKnownLocation() != null) {
                            findNearbyStops();
                            mapHandler.getCurrentMap().addCircle(new CircleOptions().
                                    center(mapHandler.getLastKnownLatLng()).
                                    radius(100).strokeColor(Color.rgb( 30,30,30)).
                                    strokeWidth(5).fillColor(Color.argb(100, 60,60,60)));
                            mapHandler.getCurrentMap().animateCamera(CameraUpdateFactory.newLatLng(mapHandler.getLastKnownLatLng()));
                        }
                        break;
                }
                return false;
            }
        });
    }

    public void findNearbyStops() {
        nearbyStops = HomeActivityHelper.getNearbyStops(mapHandler.getLastKnownLocation());

        for(Stop s:nearbyStops) {
            mapHandler.getCurrentMap().addMarker(new MarkerOptions().position
                    (new LatLng(Double.parseDouble(s.getCentre().getLatitude()),Double.parseDouble(s.getCentre().getLongitude())))
                    .title(s.getName()));
        }

        mapHandler.getCurrentMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for(Stop s : nearbyStops) {
                    if (marker.getTitle().compareTo(s.getName()) == 0){
                        Log.v("home activity",s.getNumber());
                        // get stop number
                        generateDigitalStopSign(s).show();
                        // new prompt "Find Schedule"
                        // find schedule
                    }
                }
                return false;
            }
        });
    }

    public AlertDialog generateDigitalStopSign(Stop stop) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        View view = HomeActivity.this.getLayoutInflater().inflate(R.layout.digital_stop_sign,null);
        TextView stopName = view.findViewById(R.id.digital_stop_name);
        stopName.setText(stop.getName());
        TextView stopNumber = view.findViewById(R.id.digital_stop_number);
        stopNumber.setText(stop.getNumber());
        builder.setView(view);
        return builder.create();
    }

    @Override
    protected void onStart() {
        mapFragment.onStart();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mapFragment.onStop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        mapFragment.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapFragment.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapFragment.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        mapFragment.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        mapFragment.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapHandler.onMapReady(googleMap);
    }

}
