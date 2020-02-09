package com.greymatter.snowline.Objects;

import java.util.ArrayList;

public class StopSchedule {
    private Stop stop;
    private ArrayList<RouteVariant> routes;

    public StopSchedule(){
        routes = new ArrayList<>();
    }

    public void addRouteVariant(RouteVariant routeVariant){
        routes.add(routeVariant);
    }

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

    public ArrayList<RouteVariant> getRoutes() {
        return routes;
    }
}
