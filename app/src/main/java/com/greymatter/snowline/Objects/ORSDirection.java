package com.greymatter.snowline.Objects;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class ORSDirection {
    private ArrayList<LatLng> drawableRoute;
    public ORSDirection() {
        drawableRoute = new ArrayList<>();
    }

    public void addLatLng(LatLng latLng) {
        this.drawableRoute.add(latLng);
    }

    public ArrayList<LatLng> getDrawableRoute(){
        return drawableRoute;
    }

}
