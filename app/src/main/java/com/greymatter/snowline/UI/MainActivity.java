package com.greymatter.snowline.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.greymatter.snowline.Adapters.ScheduleListRAdapter;
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
    //private ListView scheduleList;
    //private ScheduleListAdapter myListAdapter;
    private ArrayList<RouteVariant> localList;
    private final Boolean isFetchComplete = new Boolean(false);
    private LinkGenerator linkGenerator;
    //private ArrayList<Boolean> listClicks;

    private RecyclerView recyclerView;
    private ScheduleListRAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stopNumber = findViewById(R.id.enter_stop_num);
        findSchedule = findViewById(R.id.find_stop);
        //scheduleList = findViewById(R.id.schedule_list);
        recyclerView = (RecyclerView) findViewById(R.id.schedule_list);

        localList = new ArrayList<RouteVariant>();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ScheduleListRAdapter();
        recyclerView.setAdapter(mAdapter);

        /*myListAdapter = new ScheduleListAdapter(scheduleList, this.getLayoutInflater(), R.layout.schedule_format);
        scheduleList.setAdapter(myListAdapter);

        scheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                *//*if(listClicks.get(position)) {
                    listClicks.set(position,false);

                    TextView routeInfo = view.findViewById(R.id.route_info_view);
                    routeInfo.setText("");
                }
                else {
                    listClicks.set(position,true);
                    TextView routeInfo = view.findViewById(R.id.route_info_view);
                    routeInfo.setText("Sample text");
                }*//*

                //scheduleList.postInvalidate();
            }
        });*/

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

                        StopSchedule stopSchedule = JSONParser.getStopScheduleUpdated(RequestHandler.makeRequest(linkGenerator));

                        synchronized (isFetchComplete){
                            localList = new ArrayList<>();
                            localList.addAll(stopSchedule.getRoutes());

//                            listClicks = new ArrayList<>();
//                            for(int i=0;i<localList.size();i++){
//                                listClicks.add(new Boolean(false));
//                            }
//                            myListAdapter.updateClickList(listClicks);

                            isFetchComplete.notifyAll();
                        }
                        Log.v(TAG, "Fetching schedule information complete");
                    }
                });
                thread.start();

                synchronized (isFetchComplete) {
                    try {
                        isFetchComplete.wait();
//                        myListAdapter.clear();
//                        myListAdapter.addAll(localList);
//                        myListAdapter.notifyDataSetChanged();
                        mAdapter.updateLocalList(localList);
                        mAdapter.notifyDataSetChanged();
                        //mAdapter.notify();
                    }catch (InterruptedException e){}
                }
            }
        });
    }
}
