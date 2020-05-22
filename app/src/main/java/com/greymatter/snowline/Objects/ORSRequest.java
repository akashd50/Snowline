package com.greymatter.snowline.Objects;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.greymatter.snowline.app.Constants;

import java.util.ArrayList;
import java.util.List;

public class ORSRequest {
    private String baseAddress;
    private ArrayList<LatLng> routeCoordinates;

    public ORSRequest () {
        routeCoordinates = new ArrayList<>();
    }

    public ORSRequest asGeoJson(){
        this.baseAddress = Constants.ORS_GEO_JSON_BASE;
        return this;
    }

    public ORSRequest addCoordinate(LatLng latLng){
        this.routeCoordinates.add(latLng);
        return this;
    }

    public ORSRequest addAllCoordinates(ArrayList<Stop> stops) {
        for(Stop stop : stops) {
            this.routeCoordinates.add(stop.getCentre().getLatLng());
        }
        return this;
    }

    public String getBaseAddress() {
        return baseAddress;
    }

    public List getRouteCoordinates() {
        return this.routeCoordinates;
    }

    public String routeCoordinatesAsJson() {
        Log.v("Route Coords Size", routeCoordinates.size()+"");

        StringBuilder toReturn = new StringBuilder();
        boolean first = true;
        toReturn.append("[");
        for(LatLng latLng : routeCoordinates) {
            if(first) {
                toReturn.append("[" + latLng.longitude + ","+latLng.latitude + "]");
                first = false;
            }else {
                toReturn.append(",[" + latLng.longitude + ","+latLng.latitude + "]");
            }
        }
        toReturn.append("]");
        return toReturn.toString();
    }
}
