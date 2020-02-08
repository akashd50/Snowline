package com.greymatter.snowline.DataParsers;

import com.greymatter.snowline.Objects.Centre;
import static com.greymatter.snowline.Data.Constants.*;

import org.json.JSONException;
import org.json.JSONObject;

public class CentreParser extends JSONParser {
    private Centre centre;
    public CentreParser(){
        centre = new Centre();}

    public Centre parseCenterInfo(JSONObject object){
        try {
            centre = utmInfo(object.getJSONObject(UTM));
            centre = geographicInfo(object.getJSONObject(GEOGRAPHIC));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return centre;
    }

    public Centre utmInfo(JSONObject object){
        try {
            centre.setZone(object.getString(ZONE));
            centre.setUtmX(object.getString(UTMX));
            centre.setUtmY(object.getString(UTMY));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return centre;
    }

    public Centre geographicInfo(JSONObject object){
        try {
            centre.setZone(object.getString(LATITUDE));
            centre.setUtmX(object.getString(LONGITUDE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return centre;
    }
}
