package com.greymatter.snowline.DataParsers;

import com.greymatter.snowline.Objects.Bus;
import org.json.JSONException;
import org.json.JSONObject;
import static com.greymatter.snowline.app.Constants.*;

public class BusParser extends JSONObject {
    public static Bus parse(JSONObject object){
        Bus bus = new Bus();
        try {
            bus.setKey(object.getString(KEY));
            bus.setBikeRack(object.getBoolean(BIKE_RACK));
            bus.setWifi(object.getBoolean(WIFI));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bus;
    }
}
