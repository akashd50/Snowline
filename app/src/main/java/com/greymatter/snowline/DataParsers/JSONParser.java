package com.greymatter.snowline.DataParsers;

import android.util.Log;

import com.greymatter.snowline.Objects.Route;
import com.greymatter.snowline.Objects.RouteVariant;
import com.greymatter.snowline.Objects.StopSchedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.greymatter.snowline.app.Constants.*;

public class JSONParser {
    protected String currentSring;
    public JSONParser(){
        //currentSring = input;
    }

    public static StopSchedule getStopScheduleUpdated(String scheduleInfo){
        StopSchedule toReturn = new StopSchedule();
        try{
            JSONObject stopSchedule  = new JSONObject(scheduleInfo).getJSONObject(STOP_SCHEDULE);

            toReturn.setStop(StopParser.parse(stopSchedule.getJSONObject(STOP)));

            JSONArray routeSchedules = stopSchedule.getJSONArray(ROUTE_SCHEDULES);

            for(int i=0;i<routeSchedules.length();i++){
                //for each route under the current stop
                JSONObject routeScheduleCurr = routeSchedules.getJSONObject(i);
                Route currentRoute = RouteParser.parse(routeScheduleCurr.getJSONObject(ROUTE));

                JSONArray scheduledStops = routeScheduleCurr.getJSONArray(SCHEDULED_STOPS);
                for(int j=0;j<scheduledStops.length();j++){
                    //for each variant under the current route, add it to the list
                    JSONObject currentVariant = scheduledStops.getJSONObject(j);
                    RouteVariant toAdd = RouteVariantParser.parse(currentVariant);
                    toAdd.setRouteInfo(currentRoute);
                    toReturn.addRouteVariant(toAdd);

                    Log.v("JSON Parser ", toAdd.toString());
                }
            }
        }catch (JSONException e){}
        return toReturn;
    }


/*    public static StopSchedule getStopScheduleInfo(String currentSring){
        StopSchedule toReturn = new StopSchedule();
        try {
            JSONObject reader  = new JSONObject(currentSring);
            JSONObject stopSch = reader.getJSONObject(STOP_SCHEDULE);
            JSONObject stop = stopSch.getJSONObject(STOP);
            JSONArray routeSchedules = stopSch.getJSONArray(ROUTE_SCHEDULES);

            System.out.println(new StopParser().parse(stop));

            toReturn.setStopName(stop.getString(NAME));
            for(int i=0;i<routeSchedules.length();i++){
                Route route = new Route();
                route.setName(routeSchedules.getJSONObject(i).getJSONObject(ROUTE).getString(NAME));
                route.setNumber(routeSchedules.getJSONObject(i).getJSONObject(ROUTE).getString(NUMBER));
                JSONArray scheduledStops = routeSchedules.getJSONObject(i).getJSONArray(SCHEDULED_STOPS);
                ArrayList<RouteVariant> variants = new ArrayList<>();
                for(int j = 0;j<scheduledStops.length();j++){
                    RouteVariant variant = new RouteVariant(route.getNumber());
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
    }*/
}
