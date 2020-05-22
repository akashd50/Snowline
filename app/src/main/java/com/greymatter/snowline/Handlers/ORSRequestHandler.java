package com.greymatter.snowline.Handlers;

import android.util.Log;
import com.greymatter.snowline.Objects.ORSRequest;
import com.greymatter.snowline.app.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ORSRequestHandler {
    public static StringBuilder makeRequest(final ORSRequest request) {
        final Boolean isFetchComplete = Boolean.valueOf(false);
        final StringBuilder stringBuilder = new StringBuilder();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (isFetchComplete){
                    stringBuilder.append(makeRequestHelper(request));
                    isFetchComplete.notifyAll();
                }
            }
        });
        thread.start();

        synchronized (isFetchComplete) {
            try {
                isFetchComplete.wait();
                return stringBuilder;
            }catch (InterruptedException e){}
        }
        return null;
    }

    private static StringBuilder makeRequestHelper(ORSRequest request) {
        Log.v("RequestHandler",request.getBaseAddress());
        String inputLine = "";

        URL obj = null;
        try {
            obj = new URL(request.getBaseAddress());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) obj.openConnection();
        } catch (IOException e) {e.printStackTrace();}

        con.setDoOutput( true );
        con.setInstanceFollowRedirects( false );

        try {
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8");
            con.setRequestProperty("Authorization", Constants.ORS_API_KEY);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        con.setUseCaches( false );

        try(OutputStream os = con.getOutputStream()) {
            //[51.513069,-0.140784],[51.511106,-0.149796]
            //"+request.routeCoordinatesAsJson()+"
            String toWrite ="{\"coordinates\":"+request.routeCoordinatesAsJson()+"}";
            Log.v("To write ", toWrite);
            //Log.v("String Len", request.routeCoordinatesAsJson().length()+"");


            byte[] input = toWrite.getBytes("utf-8");
            os.write(input, 0, input.length);
        }catch (IOException e){
            e.printStackTrace();
        }

        int responseCode = 0;
        try {
            responseCode = con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.v("Response Code",""+responseCode);

        StringBuilder stringBuilder = new StringBuilder();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.v("Request Failed with code", responseCode+"");
        }
        con.disconnect();
        return stringBuilder;
    }
}
