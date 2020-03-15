package com.greymatter.snowline.DataParsers;

import com.greymatter.snowline.Objects.Centre;
import static com.greymatter.snowline.app.Constants.*;

import org.json.JSONException;
import org.json.JSONObject;

public class CentreParser extends JSONParser {

    public static Centre parse(JSONObject object){
        Centre centre = new Centre();
        try {
            centre = utmInfo(centre, object.getJSONObject(UTM));
            centre = geographicInfo(centre, object.getJSONObject(GEOGRAPHIC));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return centre;
    }

    public static Centre utmInfo(Centre centre, JSONObject object){
        try {
            centre.setZone(object.getString(ZONE));
            centre.setUtmX(object.getString(UTMX));
            centre.setUtmY(object.getString(UTMY));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return centre;
    }

    public static Centre geographicInfo(Centre centre, JSONObject object){
        try {
            centre.setLatitude(object.getString(LATITUDE));
            centre.setLongitude(object.getString(LONGITUDE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return centre;
    }
}
