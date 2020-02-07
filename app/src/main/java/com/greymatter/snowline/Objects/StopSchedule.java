package com.greymatter.snowline.Objects;

import com.greymatter.snowline.Objects.Route;

import java.util.ArrayList;

public class StopSchedule {

    private String stopName, stopNumber;
    private ArrayList<Route> routes;

    public StopSchedule(){
        routes = new ArrayList<>();
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public void setStopNumber(String stopNumber) {
        this.stopNumber = stopNumber;
    }

    public String getStopName() {
        return stopName;
    }

    public String getStopNumber() {
        return stopNumber;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }
}
