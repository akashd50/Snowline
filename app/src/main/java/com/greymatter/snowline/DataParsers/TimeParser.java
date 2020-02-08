package com.greymatter.snowline.DataParsers;

import com.greymatter.snowline.Objects.Times;

import org.json.JSONException;
import org.json.JSONObject;

import static com.greymatter.snowline.Data.Constants.*;

public class TimeParser extends JSONObject {
    public static Times parseTimesInfo(JSONObject object){
        Times times = new Times();
        try {
            parseArrivalInfo(times,object.getJSONObject(ARRIVAL));
            parseDepartureInfo(times,object.getJSONObject(DEPARTURE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return times;
    }

    public static void parseArrivalInfo(Times times, JSONObject object){
        try {
            times.setScheduledArrival(object.getString(SCHEDULED));
            times.setEstimatedArrival(object.getString(ESTIMATED));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void parseDepartureInfo(Times times, JSONObject object){
        try {
            times.setScheduledDeparture(object.getString(SCHEDULED));
            times.setEstimatedDeparture(object.getString(ESTIMATED));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
