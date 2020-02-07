package com.greymatter.snowline.Objects;

import java.time.LocalDateTime;

public class RouteVariant {
    private LocalDateTime arrivalDateTime;
    private String arrivalTime, variantName, routeNumber;
    public RouteVariant(String routeNumber){this.routeNumber = routeNumber;}

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

    public String toString(){
        return this.routeNumber + " - " +this.variantName + " -> " + this.arrivalTime;
    }
}
