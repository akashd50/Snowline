package com.greymatter.snowline;

import java.util.ArrayList;

public class Route {
    private String routeName;
    private String routeNumber;
    private ArrayList<RouteVariant> routeVariants;

    public Route(String keyName, String routeVariant, String schTime, String number){
        routeName = keyName;
        routeNumber = number;
        routeVariants = new ArrayList<>();
    }

    public Route(){routeVariants = new ArrayList<>();}

    public String getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(String routeNumber) {
        this.routeNumber = routeNumber;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public ArrayList<RouteVariant> getRouteVariants() {
        return routeVariants;
    }

    public void setRouteVariants(ArrayList<RouteVariant> routeVariants) {
        this.routeVariants = routeVariants;
    }

    public String toString(){
        return this.routeName;
    }


}
