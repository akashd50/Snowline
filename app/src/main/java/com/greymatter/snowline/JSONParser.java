package com.greymatter.snowline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                    variant.setArrivalTime(scheduledStops.getJSONObject(j).getJSONObject("times").getJSONObject("arrival").getString("scheduled"));
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
