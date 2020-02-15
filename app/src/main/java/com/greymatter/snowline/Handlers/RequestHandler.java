package com.greymatter.snowline.Handlers;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RequestHandler {

    public RequestHandler(){

    }

    public static StringBuilder makeRequest(final LinkGenerator linkGenerator){
        final Boolean isFetchComplete = new Boolean(false);
        final StringBuilder stringBuilder = new StringBuilder();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (isFetchComplete){
                    stringBuilder.append(RequestHandler.makeRequestHelper(linkGenerator));
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

    public static String makeRequestHelper(LinkGenerator linkGenerator){
        Log.v("RequestHandler",linkGenerator.getCurrentLink());
        String inputLine = "";
        String sample = "";

        URL obj = null;
        try {
            obj = new URL(linkGenerator.getCurrentLink());
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

        } else {
            System.out.println("GET request didn't work");
        }
        return sample;
    }
}
