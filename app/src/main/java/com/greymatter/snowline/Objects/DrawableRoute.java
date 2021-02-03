package com.greymatter.snowline.Objects;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DrawableRoute {
    private ArrayList<LatLng> drawableRoute;
    private ArrayList<Stop> drawableStops;
    private Stop userStopLocation;

    public DrawableRoute() {
        drawableRoute = new ArrayList<>();
        drawableStops = new ArrayList<>();
    }

    public Stop getUserStop() {
        return userStopLocation;
    }

    public void setUserStop(Stop userStop) {
        this.userStopLocation = userStop;
    }

    public void addToDrawableRoute(LatLng latLng) {
        this.drawableRoute.add(latLng);
    }
    public void addToDrawableStops(Stop stop) { this.drawableStops.add(stop); }
    public void addToDrawableStops(List<Stop> stops) { this.drawableStops.addAll(stops); }
    public List<LatLng> getDrawableRoute(){
        return drawableRoute;
    }
    public List<Stop> getDrawableStops() { return this.drawableStops; }
}
