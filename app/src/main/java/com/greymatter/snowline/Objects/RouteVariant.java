package com.greymatter.snowline.Objects;

public class RouteVariant extends Route {
    private boolean cancelled;
    private Times timeinfo;
    private String variantKey, variantName;
    private Bus busInfo;

    public RouteVariant(String rKey, String rName, String rNumber){super(rKey, rNumber, rName);}
    public RouteVariant(Route route){super(route.getKey(), route.getNumber(), route.getName());}
    public RouteVariant(){}

    public RouteVariant setRouteInfo(Route route){
        super.setKey(route.getKey());
        super.setName(route.getName());
        super.setNumber(route.getNumber());
        return this;
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
        return super.getNumber() + " - " + this.variantName + " -> " + this.timeinfo.getEstimatedArrival();
    }
}
