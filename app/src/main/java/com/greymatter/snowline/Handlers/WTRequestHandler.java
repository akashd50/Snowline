package com.greymatter.snowline.Handlers;

import android.os.Handler;
import android.util.Log;

import com.greymatter.snowline.Objects.WTRequest;
import com.greymatter.snowline.app.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class WTRequestHandler {
    public static StringBuilder makeRequest(final WTRequest WTRequest){
        final Boolean isFetchComplete = new Boolean(false);
        final StringBuilder stringBuilder = new StringBuilder();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (isFetchComplete){
                    stringBuilder.append(WTRequestHandler.makeRequestHelper(WTRequest));
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

    public static void makeBackgroundRequest(final WTRequest WTRequest, final Handler handler){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String response = WTRequestHandler.makeRequestHelper(WTRequest);
                handler.obtainMessage(Constants.SUCCESS, response);
            }
        });
        thread.start();
    }

    public static String makeRequestHelper(WTRequest WTRequest){
        Log.v("RequestHandler", WTRequest.getCurrentLink());
        String inputLine = "";
        String sample = "";

        URL obj = null;
        try {
            obj = new URL(WTRequest.getCurrentLink());
        } catch (MalformedURLException e) {e.printStackTrace();}

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) obj.openConnection();
        } catch (IOException e) {e.printStackTrace();}

        try {
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {e.printStackTrace();}

        //con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = 0;
        try {
            responseCode = con.getResponseCode();
        } catch (IOException e) {e.printStackTrace();}
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    sample+=inputLine;
                }
                in.close();
                // print result
                System.out.println(sample);
            } catch (IOException e) {
                e.printStackTrace();
            }
            con.disconnect();
        } else {
            System.out.println("GET request didn't work");
        }
        return sample;
    }
}
