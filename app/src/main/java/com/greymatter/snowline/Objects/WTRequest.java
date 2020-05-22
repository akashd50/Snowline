package com.greymatter.snowline.Objects;

import com.greymatter.snowline.app.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WTRequest {
    private String currentLink;
    public WTRequest(){
        currentLink = "";
    }

    public WTRequest apiKey () {
        this.update("?api-key="+Constants.API_KEY);
        return this;
    }

    public WTRequest v3() {
        this.update("/v3");
        return this;
    }

    public WTRequest base() {
        this.update(Constants.BASE_ADDRESS);
        return this;
    }

    public WTRequest stops() {
        this.update("/stops");
        return this;
    }

    public WTRequest schedule() {
        this.update("/schedule");
        return this;
    }

    public WTRequest asJson() {
        this.update(".json");
        this.apiKey();
        return this;
    }

    public WTRequest wildcard(String query) {
        this.update(":"+query);
        return this;
    }

    public WTRequest generateStopScheduleLink (String stopNumber){
        this.update(Constants.BASE_ADDRESS + "/v3/stops/"+ stopNumber + "/schedule.json");
        return this;
    }

    public WTRequest generateStopLink () {
        this.update(Constants.BASE_ADDRESS + "/v3/stops.json");
        return this;
    }

    public WTRequest generateRoutesLink () {
        this.update(Constants.BASE_ADDRESS + "/v3/routes.json");
        return this;
    }

    public WTRequest stop(String stop) {
        this.update("&stop="+stop);
        return this;
    }

    public WTRequest route(String route) {
        this.update("&route="+route);
        return this;
    }

    public WTRequest latLon (String lat, String lon) {
        this.update("&lat="+lat+"&lon="+lon);
        return this;
    }

    public WTRequest distance (String distance) {
        this.update("&distance="+distance);
        return this;
    }

    public WTRequest usage(int usage){
        if(usage==Constants.USAGE_LONG) this.update("&usage=long");
        else if(usage==Constants.USAGE_SHORT) this.update("&usage=short");
        return this;
    }

    public WTRequest addTime (LocalDateTime dateTime) {
        this.update("&start="+ dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return this;
    }

    public String getCurrentLink() {
        return currentLink;
    }

    private void update(String currentLink) {
        this.currentLink += currentLink;
    }

    public WTRequest add(String currentLink) {
        this.update("/"+currentLink);
        return this;
    }
}
