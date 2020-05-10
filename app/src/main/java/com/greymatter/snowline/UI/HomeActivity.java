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
import com.greymatter.snowline.UI.helpers.HomeActivityUIHelper;
import com.greymatter.snowline.Handlers.MapHandler;
import com.greymatter.snowline.R;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class HomeActivity extends FragmentActivity{
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

        mapHandler = new MapHandler(HomeActivity.this, HomeActivity.this, mapFragment);
        planningTab = new PlanningTab(this, (RelativeLayout)findViewById(R.id.planning_tab), mapHandler);

        mapFragment.getMapAsync(mapHandler);
        //mapHandler.fusedLocationClientListener();

        setUpUIElements();
        setUpOnTouchEventListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mapHandler.onRequestPermissionsResult(requestCode);
    }

    private void setUpUIElements() {
        HomeActivityUIHelper.setKeyboardVisibilityListener(HomeActivity.this, planningTab);
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

    private void setUpOnTouchEventListener(){
        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int)event.getRawX();
                int y = (int)event.getRawY();

                switch (event.getAction()){
                    case ACTION_DOWN:

                        break;
                    case ACTION_MOVE:

                        break;
                    case ACTION_UP:

                        break;
                }

                return false;
            }
        };
    }
}