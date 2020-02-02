package com.greymatter.snowline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText stopNumber;
    private Button findSchedule;
    private ListView scheduleList;
   // private ArrayAdapter scheduleListAdapter;
    private ScheduleListAdapter myListAdapter;
    private ArrayList<RouteVariant> localList;
    private final Boolean isFetchComplete = new Boolean(false);
    private LinkGenerator linkGenerator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stopNumber = findViewById(R.id.enter_stop_num);
        findSchedule = findViewById(R.id.find_stop);
        scheduleList = findViewById(R.id.schedule_list);

        localList = new ArrayList<RouteVariant>();
        myListAdapter = new ScheduleListAdapter(this.getLayoutInflater(), R.layout.schedule_format);
        scheduleList.setAdapter(myListAdapter);

        findSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Searching for Routes",Toast.LENGTH_LONG).show();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        linkGenerator = new LinkGenerator();
                        linkGenerator = linkGenerator.generateStopScheduleLink(stopNumber.getText().toString()).addApiKey().
                                    addTime(LocalDateTime.of(2020,02,03,11,15,00));

                        String requestJson = RequestHandler.makeRequest(linkGenerator);
                        System.out.println("Current string: "+requestJson);
                        StopSchedule stopSchedule = JSONParser.getStopScheduleInfo(requestJson);
                        localList = new ArrayList<>();
                        for(Route r : stopSchedule.getRoutes()){
                            System.out.println(r.getRouteVariants());
                            localList.addAll(r.getRouteVariants());
                        }

                        synchronized (isFetchComplete){
                            isFetchComplete.notifyAll();
                        }
                    }
                });
                thread.start();

                synchronized (isFetchComplete) {
                    try {
                        isFetchComplete.wait();
                        myListAdapter.clear();
                        myListAdapter.addAll(localList);
                        myListAdapter.notifyDataSetChanged();
                    }catch (InterruptedException e){}
                }
            }
        });
    }
}
