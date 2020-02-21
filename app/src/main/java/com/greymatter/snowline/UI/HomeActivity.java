package com.greymatter.snowline.UI;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.greymatter.snowline.Handlers.HomeActivityHelper;
import com.greymatter.snowline.Handlers.LinkGenerator;
import com.greymatter.snowline.Handlers.MapHandler;
import com.greymatter.snowline.Objects.Route;
import com.greymatter.snowline.Objects.Stop;
import com.greymatter.snowline.R;
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
    private LinearLayout sliderLayout;
    private Button sliderUp, sliderDown, sliderValue;
    private RelativeLayout planningTab;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        moreOptionsButton = findViewById(R.id.more_options_act_home);
        sliderLayout = findViewById(R.id.slider_layout);
        sliderDown = findViewById(R.id.slider_down);
        sliderUp = findViewById(R.id.slider_up);
        sliderValue = findViewById(R.id.slider_current_value);
        planningTab = findViewById(R.id.planning_tab);
        sliderValue.setText("500");

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
                            if(mapHandler.getCurrentMap()!=null){
                                mapHandler.getCurrentMap().
                                        animateCamera(CameraUpdateFactory.newLatLngZoom(mapHandler.getLastKnownLatLng(),15));

                            }
                        }
                    }
                });
        setUpUIElements();
        setUpPlanningTab();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mapHandler.onRequestPermissionsResult(requestCode);
    }

    private void setUpUIElements(){
        setUpOptionsMenu();
        setUpButtonListener();
    }

    private void setUpPlanningTab(){
        planningTab.setVisibility(View.VISIBLE);
        ObjectAnimator animation = ObjectAnimator.ofFloat(planningTab, "translationY",
                Constants.getDisplayHeight(this)-150);
        animation.setDuration(1);
        animation.start();

        Button button = planningTab.findViewById(R.id.planning_tab_drag_view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animation = ObjectAnimator.ofFloat(planningTab, "translationY",
                        Constants.getDisplayHeight(HomeActivity.this)-600);
                animation.setDuration(200);
                animation.start();
            }
        });
    }

    private void setUpButtonListener(){
        homeScreenClicksListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.more_options_act_home:
                        optionsMenu.show();
                        break;
                    case R.id.slider_up:
                        sliderValue.setText((Integer.parseInt(sliderValue.getText().toString())+ 100)+"");
                        findNearbyStops(Integer.parseInt(sliderValue.getText().toString()));
                        break;
                    case R.id.slider_down:
                        sliderValue.setText((Integer.parseInt(sliderValue.getText().toString())- 100)+"");
                        findNearbyStops(Integer.parseInt(sliderValue.getText().toString()));
                        break;
                }
            }
        };
        //assign listener to the buttons
        sliderUp.setOnClickListener(homeScreenClicksListener);
        sliderDown.setOnClickListener(homeScreenClicksListener);
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

                            if(sliderLayout.getVisibility()!=View.VISIBLE) {
                                sliderLayout.setVisibility(View.VISIBLE);
                                ObjectAnimator animation = ObjectAnimator.ofFloat(sliderLayout, "translationX", 200f, 0f);
                                animation.setDuration(500);
                                animation.start();
                            }

                            findNearbyStops(Integer.parseInt(sliderValue.getText().toString()));

                            mapHandler.getCurrentMap().
                                    animateCamera(CameraUpdateFactory.newLatLng(mapHandler.getLastKnownLatLng()));
                        }
                        break;
                }
                return false;
            }
        });
    }

    public void findNearbyStops(int distance) {
        mapHandler.getCurrentMap().clear();

        mapHandler.getCurrentMap().addCircle(new CircleOptions().
                center(mapHandler.getLastKnownLatLng()).
                radius(distance).strokeColor(Color.rgb( 30,30,30)).
                strokeWidth(5).fillColor(Color.argb(100, 60,60,60)));

        nearbyStops = HomeActivityHelper.getNearbyStops(mapHandler.getLastKnownLocation(), distance);

        for(Stop s: nearbyStops) {
            MarkerOptions marker = new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(s.getCentre().getLatitude()),Double.parseDouble(s.getCentre().getLongitude())))
                    .title(s.getName());

            mapHandler.getCurrentMap().addMarker(marker).setTag(s);
        }

        mapHandler.getCurrentMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                AlertDialog stopInfoDialog = generateDigitalStopSign((Stop)marker.getTag(),HomeActivityHelper.getRoutes(((Stop)marker.getTag())));

                stopInfoDialog.show();

                return false;
            }
        });
    }

    public AlertDialog generateDigitalStopSign(final Stop stop, ArrayList<Route> stopRoutes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        View view = HomeActivity.this.getLayoutInflater().inflate(R.layout.digital_stop_sign,null);

        LinearLayout mainRoutesLayout = view.findViewById(R.id.digital_all_routes_linear_layout);

        Toolbar toolbar = view.findViewById(R.id.digital_toolbar);
        toolbar.setTitle(stop.getName());

        Button stopNumber = view.findViewById(R.id.digital_stop_number);
        stopNumber.setText(stop.getNumber());

        LinearLayout subRoutesLayout = new LinearLayout(HomeActivity.this);
        subRoutesLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        subRoutesLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainRoutesLayout.addView(subRoutesLayout);

        for(int i=0;i<stopRoutes.size();i++) {
            Route r = stopRoutes.get(i);
            Button b = new Button(HomeActivity.this);
            b.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            b.setBackgroundResource(R.drawable.arrow_button_background);

            b.setText(r.getNumber());
            if(subRoutesLayout.getChildCount()<4){
                subRoutesLayout.addView(b);
            }else{
                subRoutesLayout = new LinearLayout(HomeActivity.this);
                subRoutesLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                subRoutesLayout.setOrientation(LinearLayout.HORIZONTAL);
                mainRoutesLayout.addView(subRoutesLayout);
                subRoutesLayout.addView(b);
            }

        }

        stopNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,StopScheduleActivity.class);
                intent.putExtra("stop_number", stop.getNumber());
                startActivity(intent);
            }
        });
        builder.setView(view);

        AlertDialog toReturn = builder.create();
        toReturn.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return toReturn;
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

        // draw the sample route here

    }

}
