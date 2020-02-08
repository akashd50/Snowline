package com.greymatter.snowline.DataParsers;

import com.greymatter.snowline.Objects.Route;
import org.json.JSONException;
import org.json.JSONObject;
import static com.greymatter.snowline.Data.Constants.*;

public class RouteParser {
    public static Route parseRouteInfo(JSONObject object){
        Route route = new Route();
        try {
            route.setName(object.getString(NAME));
            route.setNumber(object.getString(NUMBER));
            route.setKey(object.getString(KEY));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return route;
    }
}
