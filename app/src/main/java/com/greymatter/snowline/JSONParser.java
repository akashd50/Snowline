package com.greymatter.snowline;

import com.greymatter.snowline.Objects.Route;
import com.greymatter.snowline.Objects.RouteVariant;
import com.greymatter.snowline.Objects.StopSchedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class JSONParser {
    public String currentSring;
    public JSONParser(String input){
        //currentSring = input;
    }

    public static StopSchedule getStopScheduleInfo(String currentSring){
        StopSchedule toReturn = new StopSchedule();
        try {
            JSONObject reader  = new JSONObject(currentSring);
            JSONObject stopSch = reader.getJSONObject("stop-schedule");
            JSONObject stop = stopSch.getJSONObject("stop");
            JSONArray routeSchedules = stopSch.getJSONArray("route-schedules");

            toReturn.setStopName(stop.getString("name"));

            for(int i=0;i<routeSchedules.length();i++){
                Route route = new Route();
                route.setRouteName(routeSchedules.getJSONObject(i).getJSONObject("route").getString("name"));
                route.setRouteNumber(routeSchedules.getJSONObject(i).getJSONObject("route").getString("number"));
                JSONArray scheduledStops = routeSchedules.getJSONObject(i).getJSONArray("scheduled-stops");
                ArrayList<RouteVariant> variants = new ArrayList<>();
                for(int j = 0;j<scheduledStops.length();j++){
                    RouteVariant variant = new RouteVariant(route.getRouteNumber());
                    String dateTime = scheduledStops.getJSONObject(j).getJSONObject("times").getJSONObject("arrival").getString("scheduled");
                    variant.setArrivalDateTime(LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    variant.setVariantName(scheduledStops.getJSONObject(j).getJSONObject("variant").getString("name"));
                    variants.add(variant);
                }
                route.setRouteVariants(variants);
                toReturn.getRoutes().add(route);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
