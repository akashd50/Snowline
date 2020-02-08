package com.greymatter.snowline.DataParsers;

import com.greymatter.snowline.Objects.Stop;

import org.json.JSONException;
import org.json.JSONObject;
import static com.greymatter.snowline.Data.Constants.*;

public class StopParser extends JSONParser {

    public static Stop parseStopInfo(JSONObject object){
        Stop stop = new Stop();
        try {
            stop.setName(object.getString(NAME));
            stop.setNumber(object.getString(NUMBER));
            stop.setName(object.getString(DIRECTION));
            stop.setStreet(StreetParser.parseStreetInfo(object.getJSONObject(STREET)));
            stop.setCrossStreet(StreetParser.parseStreetInfo(object.getJSONObject(CROSS_STREET)));
            stop.setCentre(CentreParser.parseCenterInfo(object.getJSONObject(CENTER)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stop;
    }
}
