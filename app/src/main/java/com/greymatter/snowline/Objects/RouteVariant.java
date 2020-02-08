package com.greymatter.snowline.Objects;

import java.time.LocalDateTime;

public class RouteVariant {
    private boolean cancelled;
    private LocalDateTime arrivalDateTime; //to be deleted
    private Times timeinfo;
    private String key, arrivalTime, variantKey, variantName, routeNumber;//to be deleted
    private Bus busInfo;
    public RouteVariant(String routeNumber){this.routeNumber = routeNumber;}
    public RouteVariant(){}

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public String getRouteNumber(){return routeNumber;}

    public void setArrivalDateTime(LocalDateTime dt) {
        this.arrivalDateTime = dt;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Times getTimeinfo() {
        return timeinfo;
    }

    public void setTimeinfo(Times timeinfo) {
        this.timeinfo = timeinfo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Bus getBusInfo() {
        return busInfo;
    }

    public void setBusInfo(Bus busInfo) {
        this.busInfo = busInfo;
    }

    public String getVariantKey() {
        return variantKey;
    }

    public void setVariantKey(String variantKey) {
        this.variantKey = variantKey;
    }

    public String toString(){
        return this.routeNumber + " - " +this.variantName + " -> " + this.arrivalTime;
    }
}
