package com.greymatter.snowline.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.greymatter.snowline.R;

public class HomeActivity extends Activity {
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
            }
        });

        setUpUIElements();
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
}
