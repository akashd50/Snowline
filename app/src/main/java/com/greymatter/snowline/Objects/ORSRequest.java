package com.greymatter.snowline.Objects;

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

    public String getBaseAddress() {
        return baseAddress;
    }

    public List getRouteCoordinates() {
        return this.routeCoordinates;
    }

    public String routeCoordinatesAsJson() {
        StringBuilder toReturn = new StringBuilder();
        for(LatLng latLng : routeCoordinates) {
            toReturn.append("[" + latLng.latitude + ","+latLng.longitude + "]");
        }
        return toReturn.toString();
    }
}
