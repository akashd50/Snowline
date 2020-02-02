package com.greymatter.snowline;

public class RouteVariant {
    private String arrivalTime, variantName, routeNumber;
    public RouteVariant(String routeNumber){this.routeNumber = routeNumber;}

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getRouteNumber(){return routeNumber;}

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String toString(){
        return this.routeNumber + " - " +this.variantName + " -> " + this.arrivalTime;
    }
}
