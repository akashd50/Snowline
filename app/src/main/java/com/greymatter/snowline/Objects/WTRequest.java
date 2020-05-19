package com.greymatter.snowline.Objects;

import com.greymatter.snowline.app.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WTRequest {
    private String currentLink;
    public WTRequest(){
        currentLink = "";
    }

    public WTRequest generateStopScheduleLink (String stopNumber){
        this.updateCurrentLink(Constants.BASE_ADDRESS + "/v3/stops/"+ stopNumber + "/schedule.json");
        return this;
    }

    public WTRequest apiKey () {
        this.updateCurrentLink("?api-key="+Constants.API_KEY);
        return this;
    }

    public WTRequest generateStopLink () {
        this.updateCurrentLink(Constants.BASE_ADDRESS + "/v3/stops.json");
        return this;
    }

    public WTRequest generateRoutesLink () {
        this.updateCurrentLink(Constants.BASE_ADDRESS + "/v3/routes.json");
        return this;
    }

    public WTRequest stop(String stop) {
        this.updateCurrentLink("&stop="+stop);
        return this;
    }

    public WTRequest latLon (String lat, String lon) {
        this.updateCurrentLink("&lat="+lat+"&lon="+lon);
        return this;
    }

    public WTRequest distance (String distance) {
        this.updateCurrentLink("&distance="+distance);
        return this;
    }



    public WTRequest usage(int usage){
        if(usage==Constants.USAGE_LONG) this.updateCurrentLink("&usage=long");
        else if(usage==Constants.USAGE_SHORT) this.updateCurrentLink("&usage=short");
        return this;
    }

    public WTRequest addTime (LocalDateTime dateTime) {
        this.updateCurrentLink("&start="+ dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return this;
    }

    public String getCurrentLink() {
        return currentLink;
    }

    private void updateCurrentLink(String currentLink) {
        this.currentLink += currentLink;
    }
}
