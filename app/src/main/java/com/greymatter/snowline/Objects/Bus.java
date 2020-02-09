package com.greymatter.snowline.Objects;

public class Bus {
    private String key;
    private boolean bikeRack, wifi;
    public Bus(){

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean hasBikeRack() {
        return bikeRack;
    }

    public void setBikeRack(boolean bikeRack) {
        this.bikeRack = bikeRack;
    }

    public boolean hasWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }
}
