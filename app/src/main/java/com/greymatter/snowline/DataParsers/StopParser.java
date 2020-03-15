package com.greymatter.snowline.DataParsers;

import com.greymatter.snowline.Objects.Stop;

import org.json.JSONException;
import org.json.JSONObject;
import static com.greymatter.snowline.app.Constants.*;

public class StopParser extends JSONParser {

    public static Stop parse(JSONObject object){
        Stop stop = new Stop();
        try {
            stop.setName(object.getString(NAME));
            stop.setNumber(object.getString(NUMBER));
            stop.setDirection(object.getString(DIRECTION));
            stop.setStreet(StreetParser.parse(object.getJSONObject(STREET)));
            stop.setCrossStreet(StreetParser.parse(object.getJSONObject(CROSS_STREET)));
            stop.setCentre(CentreParser.parse(object.getJSONObject(CENTER)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stop;
    }
}
