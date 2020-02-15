package com.greymatter.snowline.Handlers;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.greymatter.snowline.Data.Constants;
import com.greymatter.snowline.DataParsers.StopParser;
import com.greymatter.snowline.Objects.Stop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivityHelper {

    public static ArrayList<Stop> getNearbyStops(Location location){
        ArrayList<Stop> nearbyStops = new ArrayList<>();
        final LinkGenerator linkGenerator = new LinkGenerator();
        linkGenerator.generateStopLink().apiKey()
                .latLon(location.getLatitude()+"", location.getLongitude()+"")
                .distance("500");

        final StringBuilder stringBuilder = RequestHandler.makeRequest(linkGenerator);

        try {
            JSONObject result = new JSONObject(stringBuilder.toString());
            JSONArray stopsArray = result.getJSONArray(Constants.STOPS);
            for(int i=0; i < stopsArray.length(); i++) {
                nearbyStops.add(StopParser.parseStopInfo(stopsArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nearbyStops;
    }
}