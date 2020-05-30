package com.greymatter.snowline.ui.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.greymatter.snowline.DataParsers.ORSDirectionsParser;
import com.greymatter.snowline.DataParsers.RouteParser;
import com.greymatter.snowline.DataParsers.StopParser;
import com.greymatter.snowline.DataParsers.StopScheduleParser;
import com.greymatter.snowline.Handlers.ORSRequestHandler;
import com.greymatter.snowline.Objects.ORSDirection;
import com.greymatter.snowline.Objects.ORSRequest;
import com.greymatter.snowline.Objects.Route;
import com.greymatter.snowline.Objects.RouteVariant;
import com.greymatter.snowline.Objects.WTRequest;
import com.greymatter.snowline.Handlers.MapHandler;
import com.greymatter.snowline.Handlers.WTRequestHandler;
import com.greymatter.snowline.Objects.Stop;
import com.greymatter.snowline.Objects.StopSchedule;
import com.greymatter.snowline.Objects.TypeCommon;
import com.greymatter.snowline.app.Constants;
import com.greymatter.snowline.db_v2.containers.ShapeCursorContainer;
import com.greymatter.snowline.db_v2.containers.TripCursorContainer;
import com.greymatter.snowline.db_v2.contracts.TripsContract;
import com.greymatter.snowline.db_v2.helpers.ShapesDBHelper;
import com.greymatter.snowline.db_v2.helpers.TripsDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import static com.greymatter.snowline.app.Constants.HOME_ACTIVITY_HELPER;
import static com.greymatter.snowline.app.Constants.STOP;
import static com.greymatter.snowline.app.Constants.STOP_SCHEDULE;

public class PlanningTabUIHelper {

    public static void init(Context context) {

    }

    public static void updateMap(MapHandler mapHandler, int distance, ArrayList<Stop> stopList){
        GoogleMap currentMap = mapHandler.getCurrentMap();

        currentMap.clear();
        currentMap.addCircle(new CircleOptions().
                center(mapHandler.getLastKnownLatLng()).
                radius(distance).strokeColor(Color.rgb( 30,30,30)).
                strokeWidth(5).fillColor(Color.argb(100, 60,60,60)));

        for(Stop s: stopList) {
            mapHandler.addMarker(s.getCentre().getLatLng(), s.getName(), s);
        }
    }

    public static ArrayList<Stop> getRouteStops(RouteVariant routeVariant){
        ArrayList<Stop> routeStopsList = new ArrayList<>();

        WTRequest request = new WTRequest();
        request = request.base().v3().stops().asJson().route(routeVariant.getNumber());
        StringBuilder response = WTRequestHandler.makeRequest(request);

        try {
            JSONObject root = new JSONObject(response.toString());
            JSONArray stopsList = root.getJSONArray(Constants.STOPS);
            for(int i=0;i<50;i++) {
                JSONObject stop = stopsList.getJSONObject(i);
                Stop currentStop = StopParser.parse(stop);
                routeStopsList.add(currentStop);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return routeStopsList;
    }

    public static ORSDirection getDrawableRoute(RouteVariant variant) {
        ORSDirection toReturn = new ORSDirection();

        ContentValues params = new ContentValues();
        params.put(TripsContract.TripsEntry.C_ROUTE_ID, variant.getKey());
        params.put(TripsContract.TripsEntry.C_TRIP_HEADSIGN, variant.getVariantName());

        TripCursorContainer tripCursorContainer = new TripsDBHelper().get(params);
        if(tripCursorContainer.moveToNext()) {
            String shapeID = tripCursorContainer.getShapeID();
            ShapeCursorContainer shapeCursorContainer = new ShapesDBHelper().get(shapeID);
            while(shapeCursorContainer.moveToNext()){
                toReturn.addLatLng(shapeCursorContainer.getLatLng());
            }
        }
        return toReturn;
    }

    public static ArrayList<TypeCommon> getNearbyStops(Location location, int radius){
        ArrayList<TypeCommon> nearbyStops = new ArrayList<>();
        final WTRequest WTRequest = new WTRequest();
        WTRequest.base().v3().stops().asJson().latLon(location.getLatitude()+"", location.getLongitude()+"")
                .distance(radius+"");

        final StringBuilder stringBuilder = WTRequestHandler.makeRequest(WTRequest);

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

    public static StopSchedule fetchStopSchedule(String stopNumber){
        WTRequest request = new WTRequest();
        request.base().v3().stops().add(stopNumber).schedule().asJson().addTime(
                LocalDateTime.now()).usage(Constants.USAGE_LONG);

        Log.v(HOME_ACTIVITY_HELPER, "Fetching schedule information");

        String json = WTRequestHandler.makeRequest(request).toString();
        StopSchedule stopSchedule = null;
        try {
            stopSchedule = StopScheduleParser.parse(new JSONObject(json)
                    .getJSONObject(STOP_SCHEDULE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stopSchedule;
    }

    public static Stop fetchStopInfo(String stopNumber) {
        WTRequest request = new WTRequest();
        request.base().v3().stops().add(stopNumber).asJson();

        Log.v(HOME_ACTIVITY_HELPER, "Fetching schedule information");

        String json = WTRequestHandler.makeRequest(request).toString();
        Stop stop = null;
        try {
            stop = StopParser.parse(new JSONObject(json).getJSONObject(STOP));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stop;
    }

    public static ArrayList<ArrayList<Route>> fetchStopsRoutesInfo(ArrayList<Stop> stops) {
        ArrayList<ArrayList<Route>> toReturn = new ArrayList<>();
        for(Stop s : stops) {
            toReturn.add(fetchStopRoutesInfo(s.getNumber()));
        }
        return toReturn;
    }

    public static ArrayList<Route> fetchStopRoutesInfo(String stopNumber) {
        ArrayList<Route> routes = new ArrayList<>();
        WTRequest request = new WTRequest();
        request.base().v3().routes().asJson().param(STOP,stopNumber);
        String response = WTRequestHandler.makeRequest(request).toString();

        try{
            JSONArray routesObj = new JSONObject(response).getJSONArray(Constants.ROUTES);
            for(int i=0;i<routesObj.length();i++) {
                routes.add(RouteParser.parse(routesObj.getJSONObject(i)));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return routes;
    }

    public static void fetchSimilarStops(final String query, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WTRequest request = new WTRequest();
                request.base().v3().stops().wildcard(query).asJson();
                String response = WTRequestHandler.makeRequest(request).toString();

                try{
                    ArrayList<Stop> similarStops = new ArrayList<>();
                    JSONArray stops = new JSONObject(response).getJSONArray(Constants.STOPS);
                    for(int i=0;i<stops.length();i++) {
                        similarStops.add(StopParser.parse(stops.getJSONObject(i)));
                    }

                    if(similarStops.size()>0) {
                        handler.obtainMessage(Constants.SUCCESS, similarStops).sendToTarget();
                    }else {
                        handler.obtainMessage(Constants.FAIL, similarStops).sendToTarget();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static ArrayList<Stop> fetchSimilarStops(final String query) {
        ArrayList<Stop> similarStops = new ArrayList<>();
        WTRequest request = new WTRequest();
        request.base().v3().stops().wildcard(query).asJson();
        String response = WTRequestHandler.makeRequest(request).toString();

        try{
            JSONArray stops = new JSONObject(response).getJSONArray(Constants.STOPS);
            for(int i=0;i<stops.length();i++) {
                similarStops.add(StopParser.parse(stops.getJSONObject(i)));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return similarStops;
    }
}
