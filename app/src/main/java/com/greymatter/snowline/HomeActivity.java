package com.greymatter.snowline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends Activity {
    private MapView maps;
    private GoogleMap currentMap;
    private FloatingActionButton moreOptionsButton;
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

        moreOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
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
