package com.greymatter.snowline.DataParsers;

import com.greymatter.snowline.Objects.Route;
import com.greymatter.snowline.Objects.RouteVariant;
import com.greymatter.snowline.Objects.StopSchedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import static com.greymatter.snowline.Data.Constants.*;

public class JSONParser {
    protected String currentSring;
    public JSONParser(){
        //currentSring = input;
    }

    public static StopSchedule getStopScheduleInfo(String currentSring){
        StopSchedule toReturn = new StopSchedule();
        try {
            JSONObject reader  = new JSONObject(currentSring);
            JSONObject stopSch = reader.getJSONObject(STOP_SCHEDULE);
            JSONObject stop = stopSch.getJSONObject(STOP);
            JSONArray routeSchedules = stopSch.getJSONArray(ROUTE_SCHEDULES);

            System.out.println(new StopParser().parseStopInfo(stop));

            toReturn.setStopName(stop.getString(NAME));
            for(int i=0;i<routeSchedules.length();i++){
                Route route = new Route();
                route.setRouteName(routeSchedules.getJSONObject(i).getJSONObject(ROUTE).getString(NAME));
                route.setRouteNumber(routeSchedules.getJSONObject(i).getJSONObject(ROUTE).getString(NUMBER));
                JSONArray scheduledStops = routeSchedules.getJSONObject(i).getJSONArray(SCHEDULED_STOPS);
                ArrayList<RouteVariant> variants = new ArrayList<>();
                for(int j = 0;j<scheduledStops.length();j++){
                    RouteVariant variant = new RouteVariant(route.getRouteNumber());
                    String dateTime = scheduledStops.getJSONObject(j).getJSONObject(TIMES).getJSONObject(ARRIVAL).getString(SCHEDULED);
                    variant.setArrivalDateTime(LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    variant.setVariantName(scheduledStops.getJSONObject(j).getJSONObject(VARIANT).getString(NAME));
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
