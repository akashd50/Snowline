package com.greymatter.snowline.Handlers;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import com.greymatter.snowline.Data.Constants;
import com.greymatter.snowline.DataParsers.RouteParser;
import com.greymatter.snowline.DataParsers.StopParser;
import com.greymatter.snowline.DataParsers.StopScheduleParser;
import com.greymatter.snowline.Objects.Route;
import com.greymatter.snowline.Objects.Stop;
import com.greymatter.snowline.Objects.StopSchedule;
import com.greymatter.snowline.R;
import com.greymatter.snowline.UI.StopScheduleActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.greymatter.snowline.Data.Constants.HOME_ACTIVITY_HELPER;
import static com.greymatter.snowline.Data.Constants.STOP_SCHEDULE;

public class HomeActivityHelper {

//    public static void setUpPlanningTab(final Activity activity, final RelativeLayout planningTab){
//        planningTab.setVisibility(View.VISIBLE);
//        ObjectAnimator animation = ObjectAnimator.ofFloat(planningTab, "translationY",
//                Constants.getDisplayHeight(activity)-100);
//        animation.setDuration(1);
//        animation.start();
//
//        Button button = planningTab.findViewById(R.id.planning_tab_drag_view);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ObjectAnimator animation = ObjectAnimator.ofFloat(planningTab, "translationY",
//                        Constants.getDisplayHeight(activity)-((float)planningTab.getHeight())-100);
//                Log.v(Constants.HOME_ACTIVITY, ""+planningTab.getHeight());
//                animation.setDuration(200);
//                animation.start();
//
//                ViewGroup.LayoutParams params = planningTab.getLayoutParams();
//                Log.v(Constants.HOME_ACTIVITY, ""+(int)planningTab.getTranslationY());
//
//                params.height = (int)(Constants.getDisplayHeight(activity) - planningTab.getPivotY());
//                planningTab.setLayoutParams(params);
//            }
//        });
//    }

    public static void onTouchEvent(){

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

    public static AlertDialog generateStopDialog(final Stop stop, final Activity activity, ArrayList<Route> stopRoutes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.digital_stop_sign,null);

        LinearLayout mainRoutesLayout = view.findViewById(R.id.digital_all_routes_linear_layout);

        Toolbar toolbar = view.findViewById(R.id.digital_toolbar);
        toolbar.setTitle(stop.getName());

        Button stopNumber = view.findViewById(R.id.digital_stop_number);
        stopNumber.setText(stop.getNumber());

        LinearLayout subRoutesLayout = new LinearLayout(activity);
        subRoutesLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        subRoutesLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainRoutesLayout.addView(subRoutesLayout);

        for(int i=0;i<stopRoutes.size();i++) {
            Route r = stopRoutes.get(i);
            Button b = new Button(activity);
            b.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            b.setBackgroundResource(R.drawable.arrow_button_background);

            b.setText(r.getNumber());
            if(subRoutesLayout.getChildCount()<4){
                subRoutesLayout.addView(b);
            }else{
                subRoutesLayout = new LinearLayout(activity);
                subRoutesLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                subRoutesLayout.setOrientation(LinearLayout.HORIZONTAL);
                mainRoutesLayout.addView(subRoutesLayout);
                subRoutesLayout.addView(b);
            }

        }

        stopNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, StopScheduleActivity.class);
                intent.putExtra("stop_number", stop.getNumber());
                activity.startActivity(intent);
            }
        });
        builder.setView(view);

        AlertDialog toReturn = builder.create();
        toReturn.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return toReturn;
    }

    public static ArrayList<Route> getRoutes(Stop stop) {
        ArrayList<Route> routeArrayList = new ArrayList<>();
        final LinkGenerator linkGenerator = new LinkGenerator();
        linkGenerator.generateRoutesLink().apiKey().stop(stop.getNumber()).usage(Constants.USAGE_LONG);

        final StringBuilder stringBuilder = RequestHandler.makeRequest(linkGenerator);

        try {
            JSONObject result = new JSONObject(stringBuilder.toString());
            JSONArray routesArray = result.getJSONArray(Constants.ROUTES);
            for(int i=0; i < routesArray.length(); i++) {
                routeArrayList.add(RouteParser.parse(routesArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routeArrayList;
    }

    public static void setKeyboardVisibilityListener(final Activity activity, final KeyboardVisibilityListener keyboardVisibilityListener) {
        final View contentView = activity.findViewById(android.R.id.content);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                keyboardVisibilityListener.onKeyboardVisibilityChanged(isKeyboardOpen(activity));
            }
        });
    }

    public static boolean isKeyboardOpen(Activity activity){
        Rect visibleBounds = new Rect();
        final View contentView = activity.findViewById(android.R.id.content);
        contentView.getWindowVisibleDisplayFrame(visibleBounds);
        float heightDiff = contentView.getHeight() - visibleBounds.height();

        float converted = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f,
                activity.getResources().getDisplayMetrics());

        float marginOfError = Math.round(converted);
        return heightDiff > marginOfError;
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
