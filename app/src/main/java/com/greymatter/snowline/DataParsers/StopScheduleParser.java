package com.greymatter.snowline.DataParsers;

import android.util.Log;
import com.greymatter.snowline.Objects.Route;
import com.greymatter.snowline.Objects.RouteVariant;
import com.greymatter.snowline.Objects.StopSchedule;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static com.greymatter.snowline.Data.Constants.*;

public class StopScheduleParser {
    public static StopSchedule parse(JSONObject object){
        StopSchedule toReturn = new StopSchedule();
        try{
            JSONObject stopSchedule = object;
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
}
