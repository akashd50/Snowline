package com.greymatter.snowline.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.greymatter.snowline.Adapters.ScheduleListRAdapter;
import com.greymatter.snowline.Data.Constants;
import com.greymatter.snowline.DataParsers.JSONParser;
import com.greymatter.snowline.Handlers.LinkGenerator;
import com.greymatter.snowline.R;
import com.greymatter.snowline.Handlers.RequestHandler;
import com.greymatter.snowline.Objects.RouteVariant;
import com.greymatter.snowline.Objects.StopSchedule;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class StopScheduleActivity extends AppCompatActivity {
    private final String TAG = "StopScheduleActivity: ";
    private EditText stopNumber;
    private Button findSchedule;
    private ArrayList<RouteVariant> localList;
    private final Boolean isFetchComplete = new Boolean(false);
    private LinkGenerator linkGenerator;
    private RecyclerView recyclerView;
    private ScheduleListRAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String stopNumberFromHome = intent.getStringExtra("stop_number");

        stopNumber = findViewById(R.id.enter_stop_num);
        findSchedule = findViewById(R.id.find_stop);
        recyclerView = (RecyclerView) findViewById(R.id.schedule_list);

        localList = new ArrayList<RouteVariant>();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ScheduleListRAdapter();
        recyclerView.setAdapter(mAdapter);

        findSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StopScheduleActivity.this,"Searching for Routes",Toast.LENGTH_LONG).show();
                fetchStopSchedule(stopNumber.getText().toString());
            }
        });

        if(stopNumberFromHome!=null && stopNumberFromHome.compareTo("")!=0){
            fetchStopSchedule(stopNumberFromHome);
        }
    }

    private void fetchStopSchedule(String stopNumber){
        linkGenerator = new LinkGenerator();
        linkGenerator.generateStopScheduleLink(stopNumber).apiKey()
                .addTime(LocalDateTime.now()).usage(Constants.USAGE_LONG);
        Log.v(TAG, "Fetching schedule information");

        StopSchedule stopSchedule = JSONParser.getStopScheduleUpdated(RequestHandler.makeRequest(linkGenerator).toString());

        mAdapter.updateLocalList(stopSchedule.getRoutes());
        mAdapter.notifyDataSetChanged();
    }
}
