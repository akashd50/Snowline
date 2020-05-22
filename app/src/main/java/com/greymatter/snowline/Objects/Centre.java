package com.greymatter.snowline.Objects;

import com.google.android.gms.maps.model.LatLng;

public class Centre {
    private String zone, utmX, utmY, latitude, longitude;
    public Centre(){}

    public String getZone() {
        return zone;
    }

    public Centre setZone(String zone) {
        this.zone = zone;
        return this;
    }

    public String getUtmX() {
        return utmX;
    }

    public Centre setUtmX(String utmX) {
        this.utmX = utmX;
        return this;
    }

    public String getUtmY() {
        return utmY;
    }

    public Centre setUtmY(String utmY) {
        this.utmY = utmY;
        return this;
    }

    public String getLatitude() {
        return latitude;
    }

    public Centre setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public LatLng getLatLng() {
        return new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
    }

    public String getLongitude() {
        return longitude;
    }

    public Centre setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }
}
