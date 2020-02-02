package com.greymatter.snowline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText stopNumber;
    private Button findSchedule;
    private ListView scheduleList;
    private ArrayAdapter scheduleListAdapter;
    private ArrayList localList;
    private final Boolean isFetchComplete = new Boolean(false);
    static String GET_URL = "https://api.winnipegtransit.com/v3/stops/60140/schedule.json?api-key=8G55aku8pgETTxnuI5N&start=2020-03-02T11:00:03";
    private LinkGenerator linkGenerator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stopNumber = findViewById(R.id.enter_stop_num);
        findSchedule = findViewById(R.id.find_stop);
        scheduleList = findViewById(R.id.schedule_list);
        localList = new ArrayList<String>();
        scheduleListAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, localList);
        scheduleList.setAdapter(scheduleListAdapter);
        linkGenerator = new LinkGenerator();
        findSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Searching for Routes",Toast.LENGTH_LONG).show();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            localList = getSchedule(stopNumber.getText().toString());
                            System.out.println(localList);
                            synchronized (isFetchComplete){
                                isFetchComplete.notifyAll();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();

                synchronized (isFetchComplete) {
                    try {
                        isFetchComplete.wait();
                        scheduleListAdapter.clear();
                        scheduleListAdapter.addAll(localList);
                        scheduleListAdapter.notifyDataSetChanged();
                    }catch (InterruptedException e){}
                }
            }
        });

    }

    private ArrayList<String> getSchedule(String stopNumber) throws IOException {
        linkGenerator = linkGenerator.generateStopScheduleLink(stopNumber).addApiKey().addTime(LocalDateTime.of(2020,02,03,11,15,00));
        System.out.println("Current lINK : "+linkGenerator.getCurrentLink());
        ArrayList routeList = new ArrayList<String>();
        String sample = "";
        URL obj = new URL(linkGenerator.getCurrentLink());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        //con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                sample+=inputLine;
            }
            in.close();

            // print result
            System.out.println(sample);
        } else {
            System.out.println("GET request not worked");
        }

        try {
            JSONObject reader  = new JSONObject(sample);
            JSONObject stopSch = reader.getJSONObject("stop-schedule");
            JSONArray rs = stopSch.getJSONArray("route-schedules");

            for(int i=0;i<rs.length();i++){
                routeList.add(rs.getJSONObject(i).getJSONObject("route").getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return routeList;

    }
}
