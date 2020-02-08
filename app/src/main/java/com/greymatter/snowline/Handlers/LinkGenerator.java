package com.greymatter.snowline.Handlers;

import com.greymatter.snowline.Data.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LinkGenerator {


    private String currentLink;
    public LinkGenerator(){
        currentLink = "";
    }

    public LinkGenerator generateStopScheduleLink (String stopNumber){
        this.updateCurrentLink(Constants.BASE_ADDRESS + "/v3/stops/"+ stopNumber + "/schedule.json?api-key="+Constants.API_KEY);
        return this;
    }

    public LinkGenerator usage(int usage){
        if(usage==Constants.USAGE_LONG) this.updateCurrentLink("&usage=long");
        else if(usage==Constants.USAGE_SHORT) this.updateCurrentLink("&usage=short");
        return this;
    }

    public LinkGenerator addTime (LocalDateTime dateTime) {
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
