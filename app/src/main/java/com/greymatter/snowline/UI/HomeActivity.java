package com.greymatter.snowline.UI;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.greymatter.snowline.R;

public class HomeActivity extends Activity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    private MapView maps;
    private GoogleMap currentMap;
    private FloatingActionButton moreOptionsButton;
    private View.OnClickListener homeScreenClicksListener;
    private PopupMenu optionsMenu;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        maps = findViewById(R.id.mapView);
        moreOptionsButton = findViewById(R.id.more_options_act_home);

        maps.onCreate(savedInstanceState);
        maps.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                currentMap = googleMap;
                if (ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                    } else {
                        ActivityCompat.requestPermissions(HomeActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},66);
                    }
                } else {
                    // Permission has already been granted
                }

            }
        });

        setUpUIElements();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        currentMap.setMyLocationEnabled(true);

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
                        startActivity(new Intent(HomeActivity.this, MainActivity.class));
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        maps.onStart();
        super.onStart();
    }

    @Override
    protected void onStop() {
        maps.onStop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        maps.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        maps.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        maps.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        maps.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        maps.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
