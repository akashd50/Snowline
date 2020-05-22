package com.greymatter.snowline.DataParsers;

import com.google.android.gms.maps.model.LatLng;
import com.greymatter.snowline.Objects.ORSDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.greymatter.snowline.app.Constants;

public class ORSDirectionsParser {
    public static ORSDirection parse(JSONObject root) {
        ORSDirection direction = new ORSDirection();
        try{
            JSONArray features = root.getJSONArray(Constants.FEATURES);
            for(int i=0;i<features.length();i++) {
                JSONObject currFeature = features.getJSONObject(i);
                JSONObject geometry = currFeature.getJSONObject(Constants.GEOMTETRY);
                JSONArray coordinates = geometry.getJSONArray(Constants.COORDINATES);

                for(int j=0;j<coordinates.length();j++) {
                    JSONArray currCoord = coordinates.getJSONArray(j);
                    //System.out.println("Curr Coord X: "+currCoord.get(0) + "Curr Y: "+ currCoord.get(1));
                    direction.addLatLng(new LatLng(currCoord.getDouble(1), currCoord.getDouble(0)));
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return direction;
    }
}
