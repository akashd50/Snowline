package com.greymatter.snowline.Objects;

import java.util.ArrayList;

public class Route {
    private String key, name, number;
    private ArrayList<RouteVariant> routeVariants;

    public Route(String keyName, String routeVariant, String schTime, String number){
        name = keyName;
        this.number = number;
        routeVariants = new ArrayList<>();
    }

    public Route(){routeVariants = new ArrayList<>();}

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<RouteVariant> getRouteVariants() {
        return routeVariants;
    }

    public void setRouteVariants(ArrayList<RouteVariant> routeVariants) {
        this.routeVariants = routeVariants;
    }

    public String toString(){
        return this.name;
    }


}
