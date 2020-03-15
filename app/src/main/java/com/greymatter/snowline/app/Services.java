package com.greymatter.snowline.app;

import android.content.Context;
import androidx.room.Room;

import com.greymatter.snowline.Data.database.AppDataDBHelper;
import com.greymatter.snowline.Data.database.LocalDatabase;
import com.greymatter.snowline.Data.database.StopDBHelper;
import com.greymatter.snowline.Data.database.StreetDBHelper;

public class Services {
    private static LocalDatabase localDatabase;
    private static AppDataDBHelper appDataDBHelper;
    private static StopDBHelper stopDBHelper;
    private static StreetDBHelper streetDBHelper;
    //private static ;

    public static LocalDatabase getDatabase(Context context){
         if(localDatabase==null) {
             localDatabase = Room.databaseBuilder(context,
                     LocalDatabase.class, "snowline").build();
         }
         return localDatabase;
    }

    public static AppDataDBHelper getAppDataDBHelper() {
        if(appDataDBHelper==null) {
            appDataDBHelper = new AppDataDBHelper(localDatabase);
        }
        return appDataDBHelper;
    }

    public static StopDBHelper getStopDBHelper() {
        if(stopDBHelper==null) {
            stopDBHelper = new StopDBHelper(localDatabase);
        }
        return stopDBHelper;
    }

    public static StreetDBHelper getStreetDBHelper() {
        if(streetDBHelper==null) {
            streetDBHelper = new StreetDBHelper(localDatabase);
        }
        return streetDBHelper;
    }
}
