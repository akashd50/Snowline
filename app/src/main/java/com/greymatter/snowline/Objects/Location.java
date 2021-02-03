package com.greymatter.snowline.Objects;

import com.google.android.gms.maps.model.LatLng;

public class Location implements TypeCommon {
    private LatLng location;
    public Location(LatLng loc) {
        this.location = loc;
    }

    public Location() { }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String toString() {
        return "Nearby Stops";
    }
}
