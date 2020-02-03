package com.greymatter.snowline;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.MapView;

public class HomeActivity extends Activity {
    private MapView maps;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        maps = findViewById(R.id.mapView);
        maps.onCreate(savedInstanceState);
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
