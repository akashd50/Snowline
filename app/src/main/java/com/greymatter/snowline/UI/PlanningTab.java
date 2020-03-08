package com.greymatter.snowline.UI;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static com.greymatter.snowline.app.Constants.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.greymatter.snowline.Adapters.ScheduleListRAdapter;
import com.greymatter.snowline.Adapters.SearchViewAdapter;
import com.greymatter.snowline.Adapters.StopsListAdapterR;
import com.greymatter.snowline.Data.database.StopDBHelper;
import com.greymatter.snowline.Data.entities.StopEntity;
import com.greymatter.snowline.Handlers.MapHandler;
import com.greymatter.snowline.Objects.Stop;
import com.greymatter.snowline.UI.helpers.PlanningTabUIHelper;
import com.greymatter.snowline.app.Constants;
import com.greymatter.snowline.UI.helpers.HomeActivityHelper;
import com.greymatter.snowline.Handlers.KeyboardVisibilityListener;
import com.greymatter.snowline.Handlers.LinkGenerator;
import com.greymatter.snowline.Handlers.Validator;
import com.greymatter.snowline.Objects.StopSchedule;
import com.greymatter.snowline.Objects.Vector;
import com.greymatter.snowline.R;
import com.greymatter.snowline.app.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PlanningTab implements KeyboardVisibilityListener, View.OnTouchListener{
    private Context context;
    private Activity parentActivity;
    private RelativeLayout mainLayout;
    private SearchView searchView;
    private Button dragView, findNearbyStops;
    private boolean searchBarHasFocus;
    private Vector translationDelta, locationBeforeAnim;
    private LinkGenerator linkGenerator;
    private RecyclerView planningTabListView;
    private ScheduleListRAdapter scheduleListAdapter;
    private StopsListAdapterR stopListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private StopDBHelper stopDBHelper;
    private SearchViewAdapter searchViewAdapter;
    private Handler dbQueryHandler;
    private MapHandler mapHandler;
    public PlanningTab(final Context context, RelativeLayout planningTab, MapHandler mapHandler){
        this.context = context;
        this.parentActivity = (Activity)context;
        this.mainLayout = planningTab;
        this.mapHandler = mapHandler;
        searchBarHasFocus = false;
        stopDBHelper = new StopDBHelper(Services.getDatabase(context));

        findViews();
        initRecyclerViewAdapters();

        init();
        initButtonsListener();
        initRecyclerView();
        initSearchListeners();
        initSearchAdapters();
        initDBListeners();
    }

    private void findViews(){
        this.searchView = mainLayout.findViewById(R.id.planning_tab_search_view);
        this.dragView = mainLayout.findViewById(R.id.planning_tab_drag_view);
        this.findNearbyStops = mainLayout.findViewById(R.id.planning_tab_find_nearby_stops);
    }

    private void init(){
        translationDelta = new Vector();
        locationBeforeAnim = new Vector();

        mainLayout.setVisibility(View.VISIBLE);
        animateLayout(1,Constants.getDisplayHeight(parentActivity)-
                ((float)Constants.getDisplayHeight(parentActivity))/3);

        dragView.setOnTouchListener(this);
        mainLayout.setOnTouchListener(this);
    }

    private void initButtonsListener(){
        View.OnClickListener buttonsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.planning_tab_find_nearby_stops:
                        if (mapHandler.getLastKnownLocation() != null) {

                            displayNearbyStops(500);
                            mapHandler.getCurrentMap().
                                    animateCamera(CameraUpdateFactory.newLatLng(mapHandler.getLastKnownLatLng()));


                        }
                        break;
                }
            }
        };

        findNearbyStops.setOnClickListener(buttonsListener);
    }

    private void initSearchListeners(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.v(PLANNING_TAB, "OnQueryTextSubmit callback");
                if(Validator.validateStopNumber(query)) {
                    Log.v("Logging DB Entries","------------------------------");

                    StopSchedule currSchedule = displayStopSchedule(query);
                    addToDB(currSchedule.getStop());
                }
                if (searchBarHasFocus) {
                    searchBarHasFocus = false;
                    animateLayout(300, locationBeforeAnim.y);
                }

                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.v(PLANNING_TAB, "onQueryTextChange callback");
                if(!searchBarHasFocus){
                    searchBarHasFocus = true;
                    animateLayout(300, 0);
                }

                stopDBHelper.getSimilar(newText, dbQueryHandler);

                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.v(PLANNING_TAB, "onFocusChange callback");

                if(hasFocus){
                    if(!searchBarHasFocus) {

                        planningTabListView.setAdapter(scheduleListAdapter);

                        searchBarHasFocus = true;
                        Log.v(PLANNING_TAB, "Focused Search bar");
                        animateLayout(300, 0);
                    }
                }else{
                    if(searchBarHasFocus){
                        searchBarHasFocus = false;
                        Log.v(PLANNING_TAB, "Not Focused Search bar");

                        Log.v(PLANNING_TAB, "Location Before Anim - "+ locationBeforeAnim.y);

                        animateLayout(300, locationBeforeAnim.y);
                    }
                }
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                Log.v(PLANNING_TAB, "OnSuggestionListener: [select] "+ position);
                //displayStopSchedule((String)searchViewAdapter.getItem(position));
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Log.v(PLANNING_TAB, "OnSuggestionListener: [click] "+ position);
                displayStopSchedule(searchViewAdapter.getSuggestionText(position));
                return true;
            }
        });
    }

    private void initSearchAdapters(){
        //final SearchManager searchManager = (SearchManager)context.getSystemService(Context.SEARCH_SERVICE);
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(((Activity) context).getComponentName()));
        searchViewAdapter = new SearchViewAdapter(context, null, false);
        searchView.setSuggestionsAdapter(searchViewAdapter);


    }

    private void initDBListeners(){
        dbQueryHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Cursor cursor = (Cursor)msg.obj;
                if(cursor.getCount()>0) {
                    cursor.moveToNext();
                    Log.v("Cursor: ", "" + cursor.getInt(0));
                    searchViewAdapter.swapCursor(cursor);
                }else{
                    searchViewAdapter.swapCursor(null);
                }
            }
        };
    }

    private void initRecyclerViewAdapters(){
        View.OnClickListener listClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = planningTabListView.getChildLayoutPosition(v);
                Stop stopSelected = stopListAdapter.getItem(itemPosition);
                if(stopSelected!=null){
                    displayStopSchedule(stopSelected.getNumber());
                }
            }
        };

        scheduleListAdapter = new ScheduleListRAdapter();
        stopListAdapter = new StopsListAdapterR(listClickListener);
    }

    private void initRecyclerView(){
        planningTabListView = mainLayout.findViewById(R.id.planning_tab_recycler_view);

        layoutManager = new LinearLayoutManager(context);
        planningTabListView.setLayoutManager(layoutManager);

        planningTabListView.setAdapter(scheduleListAdapter);
    }

    public void animateLayout(int duration, float finalPos){

        locationBeforeAnim.y = (int)mainLayout.getTranslationY();

        Log.v(PLANNING_TAB, "Animate - viewTranslationY - "+(int)mainLayout.getTranslationY());
        Log.v(PLANNING_TAB, "Location Before Anim - Animate Layout - "+locationBeforeAnim.y);

        ObjectAnimator animation = ObjectAnimator.ofFloat(mainLayout, "y", finalPos);
        Log.v(PLANNING_TAB, "Animate - viewHeight - " + mainLayout.getHeight());
        animation.setDuration(duration);
        animation.start();

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                mainLayout.getLayoutParams();
        layoutParams.height = (int)(Constants.getDisplayHeight(parentActivity) - finalPos);
        mainLayout.setLayoutParams(layoutParams);
    }

    public void onKeyboardVisibilityChanged(boolean keyboardVisible) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int)event.getRawX();
        int y = (int)event.getRawY();
        boolean eventHandled = false;
        switch (event.getAction()){
            case ACTION_DOWN:
                Log.v(PLANNING_TAB, "Planning Tab Click Detected!");
                switch (v.getId()){
                    case R.id.planning_tab_drag_view:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                mainLayout.getLayoutParams();

                        translationDelta.x = x - (int)mainLayout.getTranslationX();
                        translationDelta.y = y - (int)mainLayout.getTranslationY();
                        return true;
                    case R.id.planning_tab_search_view:
                        Log.v(PLANNING_TAB, "Planning Tab Search bar touch down Detected!");
                        return true;

                    case R.id.planning_tab:

                        return true;
                }

                break;
            case ACTION_MOVE:
                Log.v(PLANNING_TAB, "Planning Tab Click Move Detected!");

                switch (v.getId()){
                    case R.id.planning_tab_drag_view:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                                mainLayout.getLayoutParams();

                        mainLayout.setTranslationY(y-translationDelta.y);
                        layoutParams.height = Constants.getDisplayHeight(parentActivity) - (y-translationDelta.y);
                        mainLayout.setLayoutParams(layoutParams);
                        return true;

                    case R.id.planning_tab:

                        return true;
                }
                break;
            case ACTION_UP:
                Log.v(PLANNING_TAB, "Planning Tab Click Up Detected!");

                switch (v.getId()){
                    case R.id.planning_tab_drag_view:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                                mainLayout.getLayoutParams();

                        mainLayout.setTranslationY(y-translationDelta.y);
                        layoutParams.height = Constants.getDisplayHeight(parentActivity) - (y-translationDelta.y);
                        mainLayout.setLayoutParams(layoutParams);
                        return true;

                    case R.id.planning_tab_search_view:
                        Log.v(PLANNING_TAB, "Planning Tab Search bar touch up Detected!");
                        return true;

                    case R.id.planning_tab:

                        return true;
                }
                break;
        }
        mainLayout.invalidate();
        return false;
    }

    public void displayNearbyStops(int distance) {
        //update the adapter
        planningTabListView.setAdapter(stopListAdapter);

        ArrayList<Stop> nearbyStops = PlanningTabUIHelper.getNearbyStops(
                mapHandler.getLastKnownLocation(), distance);
        stopListAdapter.updateLocalList(nearbyStops);
        stopListAdapter.notifyDataSetChanged();

        PlanningTabUIHelper.updateMap(mapHandler,distance, nearbyStops);

        mapHandler.getCurrentMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                displayStopSchedule(((Stop)marker.getTag()).getNumber());
                return false;
            }
        });
    }

    public StopSchedule displayStopSchedule(String stopNumber){
        planningTabListView.setAdapter(scheduleListAdapter);

        linkGenerator = new LinkGenerator();
        linkGenerator.generateStopScheduleLink(stopNumber).apiKey()
                .addTime(LocalDateTime.now()).usage(Constants.USAGE_LONG);

        StopSchedule stopSchedule = HomeActivityHelper.fetchStopSchedule(linkGenerator);
        if(stopSchedule!=null) {
            scheduleListAdapter.updateLocalList(stopSchedule.getRoutes());
            scheduleListAdapter.notifyDataSetChanged();
        }
        return stopSchedule;
    }

    public void addToDB(Stop stop){
        final StopEntity stopEntity = new StopEntity();
        if(stop!=null) {
            stopEntity.key = Integer.parseInt(stop.getNumber());
            stopEntity.stopName = stop.getName();
            stopEntity.stopNumber = stop.getNumber();
            stopEntity.direction = stop.getDirection();
            if(!stopDBHelper.find(stop.getNumber())){
                stopDBHelper.addStop(stopEntity);
            }
        }
    }

}
