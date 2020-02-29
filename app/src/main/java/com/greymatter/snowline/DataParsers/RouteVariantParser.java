package com.greymatter.snowline.DataParsers;

import com.greymatter.snowline.Objects.RouteVariant;
import org.json.JSONException;
import org.json.JSONObject;
import static com.greymatter.snowline.app.Constants.*;

public class RouteVariantParser extends JSONObject {
    public static RouteVariant parse(JSONObject object){
        RouteVariant routeVariant = new RouteVariant();
        try {
            routeVariant.setKey(object.getString(KEY));
            routeVariant.setCancelled(object.getBoolean(CANCELLED));
            routeVariant.setTimeinfo(TimeParser.parse(object.getJSONObject(TIMES)));
            routeVariant.setBusInfo(BusParser.parse(object.getJSONObject(BUS)));

            parseVariantInfo(routeVariant, object.getJSONObject(VARIANT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routeVariant;
    }

    public static void parseVariantInfo(RouteVariant routeVariant, JSONObject object){
        try {
            routeVariant.setVariantName(object.getString(NAME));
            routeVariant.setVariantKey(object.getString(KEY));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
