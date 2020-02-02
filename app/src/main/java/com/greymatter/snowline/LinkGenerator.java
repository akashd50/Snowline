package com.greymatter.snowline;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LinkGenerator {

    private String BASE_ADDRESS = "https://api.winnipegtransit.com";

    public String getCurrentLink() {
        return currentLink;
    }

    public void setCurrentLink(String currentLink) {
        this.currentLink += currentLink;
    }

    private String currentLink;
    public LinkGenerator(){
        currentLink = "";
    }

    public LinkGenerator generateStopScheduleLink (String stopNumber){
        this.setCurrentLink(BASE_ADDRESS + "/v3/stops/"+ stopNumber + "/schedule.json");
        return this;
    }

    public LinkGenerator addApiKey () {
        this.setCurrentLink("?api-key=8G55aku8pgETTxnuI5N");
        return this;
    }

    public LinkGenerator addTime (LocalDateTime dateTime) {
        this.setCurrentLink("&start="+ dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return this;
    }
}
