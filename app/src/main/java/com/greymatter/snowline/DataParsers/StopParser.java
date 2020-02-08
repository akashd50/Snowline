package com.greymatter.snowline.DataParsers;

import com.greymatter.snowline.Objects.Stop;

import org.json.JSONException;
import org.json.JSONObject;
import static com.greymatter.snowline.Data.Constants.*;

public class StopParser extends JSONParser {
    private Stop stop;
    private StreetParser streetParser;
    private CentreParser centreParser;

    public StopParser(){
        stop = new Stop();
        streetParser = new StreetParser();
        centreParser = new CentreParser();
    }

    public Stop parseStopInfo(JSONObject object){
        try {
            stop.setName(object.getString(NAME));
            stop.setNumber(object.getString(NUMBER));
            stop.setName(object.getString(DIRECTION));
            stop.setStreet(streetParser.parseStreetInfo(object.getJSONObject(STREET)));
            stop.setCrossStreet(streetParser.parseStreetInfo(object.getJSONObject(CROSS_STREET)));
            stop.setCentre(centreParser.parseCenterInfo(object.getJSONObject(CENTER)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stop;
    }
}
