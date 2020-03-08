package com.greymatter.snowline.UI;

import android.location.Location;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.greymatter.snowline.UI.helpers.HomeActivityHelper;
import com.greymatter.snowline.Handlers.MapHandler;
import com.greymatter.snowline.R;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class HomeActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback{
    private View.OnTouchListener onTouchListener;
    private SupportMapFragment mapFragment;
    private MapHandler mapHandler;
    private PlanningTab planningTab;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.onCreate(savedInstanceState);
        mapFragment.getMapAsync(this);

        setUpOnTouchEventListener();

        mapHandler = new MapHandler(HomeActivity.this, HomeActivity.this, mapFragment);
        mapHandler.fusedLocationClientListener();
        planningTab = new PlanningTab(this, (RelativeLayout)findViewById(R.id.planning_tab), mapHandler);

        setUpUIElements();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mapHandler.onRequestPermissionsResult(requestCode);
    }

    private void setUpUIElements(){
        HomeActivityHelper.setKeyboardVisibilityListener(HomeActivity.this, planningTab);
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



    private void setUpOnTouchEventListener(){
        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int)event.getRawX();
                int y = (int)event.getRawY();

                switch (event.getAction()){

                    case ACTION_DOWN:
                        switch (v.getId()){
                        }

                        break;
                    case ACTION_MOVE:
                        switch (v.getId()){
                        }
                        break;
                    case ACTION_UP:
                        switch (v.getId()){

                        }
                        break;
                }

                return false;
            }
        };
    }
}