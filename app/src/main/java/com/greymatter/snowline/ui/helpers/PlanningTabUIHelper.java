package com.greymatter.snowline.ui.helpers;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.greymatter.snowline.Data.database.StopDBHelper;
import com.greymatter.snowline.Data.entities.StopEntity;
import com.greymatter.snowline.DataParsers.StopParser;
import com.greymatter.snowline.DataParsers.StopScheduleParser;
import com.greymatter.snowline.Handlers.LinkGenerator;
import com.greymatter.snowline.Handlers.MapHandler;
import com.greymatter.snowline.Handlers.RequestHandler;
import com.greymatter.snowline.Objects.Stop;
import com.greymatter.snowline.Objects.StopSchedule;
import com.greymatter.snowline.Objects.TypeCommon;
import com.greymatter.snowline.app.Constants;
import com.greymatter.snowline.app.Services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.greymatter.snowline.app.Constants.HOME_ACTIVITY_HELPER;
import static com.greymatter.snowline.app.Constants.STOP_SCHEDULE;

public class PlanningTabUIHelper {
    private static StopDBHelper stopDBHelper;

    public static void init(Context context) {
        stopDBHelper = new StopDBHelper(Services.getDatabase(context));
    }

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

    public static void findNearbyStops(String address) {
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

    public static ArrayList<TypeCommon> getNearbyStops(Location location, int radius){
        ArrayList<TypeCommon> nearbyStops = new ArrayList<>();
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

    public static void addToDB(Stop stop){
        final StopEntity stopEntity = new StopEntity();
        if(stop!=null) {
            stopEntity.key = Integer.parseInt(stop.getNumber());
            stopEntity.stopName = stop.getName();
            stopEntity.stopNumber = stop.getNumber();
            stopEntity.direction = stop.getDirection();
            if(!stopDBHelper.find(stop.getNumber())){
                stopDBHelper.addStop(stopEntity);
            }
        }
    }

    public static StopSchedule fetchStopSchedule(LinkGenerator linkGenerator){

        Log.v(HOME_ACTIVITY_HELPER, "Fetching schedule information");

        String json = RequestHandler.makeRequest(linkGenerator).toString();
        StopSchedule stopSchedule = null;
        try {
            stopSchedule = StopScheduleParser.parse(new JSONObject(json)
                    .getJSONObject(STOP_SCHEDULE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stopSchedule;
    }
}
