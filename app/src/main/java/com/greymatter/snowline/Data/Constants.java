package com.greymatter.snowline.Data;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class Constants {
    public static final String BASE_ADDRESS = "https://api.winnipegtransit.com";
    public static final String API_KEY = "8G55aku8pgETTxnuI5N";
    public static final int USAGE_SHORT = 1001;
    public static final int USAGE_LONG = 1002;

    //Activity Tags
    public static final String HOME_ACTIVITY = "Home Activity: INFO";
    public static final String HOME_ACTIVITY_HELPER = "Home Activity Helper: INFO";
    public static final String PLANNING_TAB = "Planning Tab: INFO";

    //parser constants
    public static final String STOP_SCHEDULE = "stop-schedule";
    public static final String STOP = "stop";
    public static final String STOPS = "stops";
    public static final String STREET = "street";
    public static final String COVERAGE = "coverage";
    public static final String CROSS_STREET = "cross-street";
    public static final String CENTER = "centre";
    public static final String KEY = "key";
    public static final String TYPE = "type";
    public static final String UTM = "utm";
    public static final String GEOGRAPHIC = "geographic";
    public static final String ZONE = "zone";
    public static final String UTMX = "x";
    public static final String UTMY= "y";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE= "longitude";

    public static final String ROUTE_SCHEDULES = "route-schedules";
    public static final String NAME = "name";
    public static final String ROUTE = "route";
    public static final String ROUTES = "routes";
    public static final String NUMBER = "number";
    public static final String TIMES = "times";
    public static final String ARRIVAL = "arrival";
    public static final String DEPARTURE = "departure";
    public static final String SCHEDULED = "scheduled";
    public static final String ESTIMATED = "estimated";
    public static final String VARIANT = "variant";
    public static final String SCHEDULED_STOPS = "scheduled-stops";
    public static final String DIRECTION = "direction";
    public static final String CANCELLED = "cancelled";
    public static final String BUS = "bus";
    public static final String BIKE_RACK = "bike-rack";
    public static final String WIFI = "wifi";

    public static int getDisplayHeight(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return height;
    }

    public static int getDisplayWidth(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.heightPixels;
        return width;
    }

    public static int getRealDisplayHeight(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return height;
    }

    public static int getRealDisplayWidth(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        int width = displayMetrics.heightPixels;
        return width;
    }

    public static int getNavigationBarHeight(Activity activity) {
        int usableHeight = getDisplayHeight(activity);
        int realHeight = getRealDisplayHeight(activity);
        if (realHeight > usableHeight)
            return realHeight - usableHeight;
        return 0;
    }
}
