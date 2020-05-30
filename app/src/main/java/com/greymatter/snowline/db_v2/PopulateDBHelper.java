package com.greymatter.snowline.db_v2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.greymatter.snowline.Objects.GlobalData;
import com.greymatter.snowline.R;
import com.greymatter.snowline.app.Constants;
import com.greymatter.snowline.db_v2.contracts.GlobalDataContract;
import com.greymatter.snowline.db_v2.helpers.GlobalDataDBHelper;
import com.greymatter.snowline.db_v2.helpers.ShapesDBHelper;
import com.greymatter.snowline.db_v2.helpers.TripsDBHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PopulateDBHelper {
    private static TripsDBHelper tripsDBHelper;
    private static ShapesDBHelper shapesDBHelper;
    private static GlobalDataDBHelper globalDataDBHelper;
    private static int tripsInsertCounter, shapesInsertCounter;
    private static boolean isActive;

    public static void populateGlobalData() {
        isActive = true;
        tripsDBHelper = new TripsDBHelper();
        shapesDBHelper = new ShapesDBHelper();

        globalDataDBHelper = new GlobalDataDBHelper();
        if(!globalDataDBHelper.getData().moveToNext()) {
            String[] toInsert = {"0", "0", "false", "false"};
            globalDataDBHelper.insert(toInsert);
            tripsInsertCounter = 0;
            shapesInsertCounter = 0;
        }else{
            tripsInsertCounter = globalDataDBHelper.getData().getObject().getTripsTableId();
            tripsDBHelper.deleteWhereIdGreaterThan(tripsInsertCounter);

            shapesInsertCounter = globalDataDBHelper.getData().getObject().getShapesTableId();
            shapesDBHelper.deleteWhereIdGreaterThan(shapesInsertCounter);
        }

        Log.v("Starting Trips DB population from: ", tripsInsertCounter+"");
        Log.v("Starting Shapes DB population from: ", shapesInsertCounter+"");
    }

    public static void populateTransitDB(final Context context, final Handler handler) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean loadingNewDB = false;
                if(!globalDataDBHelper.getData().getObject().isTripsTableLoaded()) {
                    populateTripsHelper(context);
                    if(isActive) {
                        GlobalData data = globalDataDBHelper.getData().getObject();
                        data.setTripsTableLoaded(true);
                        globalDataDBHelper.update(data);
                    }
                    loadingNewDB = true;
                }
                if(!globalDataDBHelper.getData().getObject().isShapesTableLoaded()) {
                    populateShapesHelper(context);
                    if(isActive) {
                        GlobalData data = globalDataDBHelper.getData().getObject();
                        data.setShapesTableLoaded(true);
                        globalDataDBHelper.update(data);
                    }
                    loadingNewDB = true;
                }

                if (!loadingNewDB) {
                    Log.v("PopulateDB", "All Tables Already Loaded");
                }
                handler.obtainMessage(Constants.SUCCESS).sendToTarget();
            }
        }).start();
    }

    private static void populateTripsHelper(Context context) {
        InputStreamReader inputStreamReader = new InputStreamReader(context.getResources().openRawResource(R.raw.trips));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = "";

        int dbUpdateLineCounter = tripsInsertCounter;
        int prevDbUpdateBreak = dbUpdateLineCounter;

        try {
            line = bufferedReader.readLine();

            Log.v("Trips DB loading", line);
            int tempIndex = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if(!isActive) { break; }

                if(tempIndex >= tripsInsertCounter) {

                    String[] tokens = line.split(",");
                    tripsDBHelper.insert(dbUpdateLineCounter, tokens);

                    if(dbUpdateLineCounter - prevDbUpdateBreak > 500) {
                        Log.v("PopulateDB", "500 Lines, saving progress!");
                        Log.v("PopulateDB", "Currently at ---> " + dbUpdateLineCounter);

                        GlobalData data = globalDataDBHelper.getData().getObject();
                        data.setTripsTableId(dbUpdateLineCounter);
                        globalDataDBHelper.update(data);

                        prevDbUpdateBreak = dbUpdateLineCounter;
                    }

                    dbUpdateLineCounter++;
                }
                tempIndex++;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void populateShapesHelper(Context context) {
        InputStreamReader inputStreamReader = new InputStreamReader(context.getResources().openRawResource(R.raw.shapes));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = "";

        int dbUpdateLineCounter = shapesInsertCounter;
        int prevDbUpdateBreak = dbUpdateLineCounter;

        try {
            line = bufferedReader.readLine();

            Log.v("Shapes DB loading", line);
            int tempIndex = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if(!isActive) { break; }

                if(tempIndex >= shapesInsertCounter) {

                    String[] tokens = line.split(",");
                    shapesDBHelper.insert(dbUpdateLineCounter, tokens);

                    if(dbUpdateLineCounter - prevDbUpdateBreak > 1000) {
                        Log.v("PopulateDB", "1000 Lines, saving progress!");
                        Log.v("PopulateDB", "Currently at ---> " + dbUpdateLineCounter);

                        GlobalData data = globalDataDBHelper.getData().getObject();
                        data.setShapesTableId(dbUpdateLineCounter);
                        globalDataDBHelper.update(data);

                        prevDbUpdateBreak = dbUpdateLineCounter;
                    }

                    dbUpdateLineCounter++;
                }
                tempIndex++;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void onDestroy() {
        isActive = false;
    }
}
