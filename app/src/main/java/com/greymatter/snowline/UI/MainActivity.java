package com.greymatter.snowline.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.greymatter.snowline.Data.Constants;
import com.greymatter.snowline.DataParsers.JSONParser;
import com.greymatter.snowline.Handlers.LinkGenerator;
import com.greymatter.snowline.R;
import com.greymatter.snowline.Handlers.RequestHandler;
import com.greymatter.snowline.Objects.Route;
import com.greymatter.snowline.Objects.RouteVariant;
import com.greymatter.snowline.Adapters.ScheduleListAdapter;
import com.greymatter.snowline.Objects.StopSchedule;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity: ";
    private EditText stopNumber;
    private Button findSchedule;
    private ListView scheduleList;
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
                        linkGenerator = linkGenerator.generateStopScheduleLink(stopNumber.getText().toString())
                                .addTime(LocalDateTime.now()).usage(Constants.USAGE_LONG);

                        Log.v(TAG, "Fetching schedule information");

                        StopSchedule stopSchedule = JSONParser.getStopScheduleInfo(RequestHandler.makeRequest(linkGenerator));

                        synchronized (isFetchComplete){
                            localList = new ArrayList<>();
                            for(Route r : stopSchedule.getRoutes()){
                                System.out.println(r.getRouteVariants());
                                localList.addAll(r.getRouteVariants());
                            }
                            isFetchComplete.notifyAll();

                        }
                        Log.v(TAG, "Fetching schedule information complete");
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
