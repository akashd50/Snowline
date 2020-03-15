package com.greymatter.snowline.UI.helpers;

import android.app.AlertDialog;
import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.greymatter.snowline.DataParsers.StopParser;
import com.greymatter.snowline.Handlers.LinkGenerator;
import com.greymatter.snowline.Handlers.MapHandler;
import com.greymatter.snowline.Handlers.RequestHandler;
import com.greymatter.snowline.Objects.Stop;
import com.greymatter.snowline.app.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlanningTabUIHelper {

    public static void updateMap(MapHandler mapHandler, int distance, ArrayList<Stop> stopList){
        GoogleMap currentMap = mapHandler.getCurrentMap();

        currentMap.clear();
        currentMap.addCircle(new CircleOptions().
                center(mapHandler.getLastKnownLatLng()).
                radius(distance).strokeColor(Color.rgb( 30,30,30)).
                strokeWidth(5).fillColor(Color.argb(100, 60,60,60)));

        for(Stop s: stopList) {
            MarkerOptions marker = new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(s.getCentre().getLatitude()),
                            Double.parseDouble(s.getCentre().getLongitude())))
                    .title(s.getName());
            currentMap.addMarker(marker).setTag(s);
        }
    }

    public static void findNearbyStops(int distance) {


        //ArrayList<Stop> nearbyStops = HomeActivityHelper.getNearbyStops(mapHandler.getLastKnownLocation(), distance);



//        mapHandler.getCurrentMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                AlertDialog stopInfoDialog = HomeActivityHelper.generateStopDialog(
//                        (Stop)marker.getTag(),parentActivity,
//                        HomeActivityHelper.getRoutes(((Stop)marker.getTag())));
//                stopInfoDialog.show();
//                return false;
//            }
//        });
    }

    public static ArrayList<Stop> getNearbyStops(Location location, int radius){
        ArrayList<Stop> nearbyStops = new ArrayList<>();
        final LinkGenerator linkGenerator = new LinkGenerator();
        linkGenerator.generateStopLink().apiKey()
                .latLon(location.getLatitude()+"", location.getLongitude()+"")
                .distance(radius+"");

        final StringBuilder stringBuilder = RequestHandler.makeRequest(linkGenerator);

        try {
            JSONObject result = new JSONObject(stringBuilder.toString());
            JSONArray stopsArray = result.getJSONArray(Constants.STOPS);
            for(int i=0; i < stopsArray.length(); i++) {
                nearbyStops.add(StopParser.parse(stopsArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nearbyStops;
    }
}
