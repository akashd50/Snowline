package com.greymatter.snowline.ui;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
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

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static com.greymatter.snowline.app.Constants.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.greymatter.snowline.Handlers.InputHandler;
import com.greymatter.snowline.Objects.Location;
import com.greymatter.snowline.Objects.RouteVariant;
import com.greymatter.snowline.ui.adapters.ScheduleListAdapterR;
import com.greymatter.snowline.ui.adapters.SearchViewAdapter;
import com.greymatter.snowline.ui.adapters.StopsListAdapterR;
import com.greymatter.snowline.Handlers.MapHandler;
import com.greymatter.snowline.Objects.Stop;
import com.greymatter.snowline.ui.helpers.PlanningTabUIHelper;
import com.greymatter.snowline.app.Constants;
import com.greymatter.snowline.Handlers.KeyboardVisibilityListener;
import com.greymatter.snowline.Handlers.Validator;
import com.greymatter.snowline.Objects.StopSchedule;
import com.greymatter.snowline.Objects.Vector;
import com.greymatter.snowline.R;

import java.util.ArrayList;

public class PlanningTab implements KeyboardVisibilityListener, View.OnTouchListener{
    private Context context;
    private Activity parentActivity;
    private RelativeLayout mainLayout;
    private SearchView searchView;
    private Button dragView, findNearbyStops;
    private boolean searchBarHasFocus;
    private Vector translationDelta, locationBeforeAnim;
    private SearchViewAdapter searchViewAdapter;
    private Handler dbQueryHandler, wtSimilarStopsQueryHandler;
    private MapHandler mapHandler;
    private InputHandler inputHandler;

    public PlanningTab(final Context context, RelativeLayout planningTab, MapHandler mapHandler){
        this.context = context;
        this.parentActivity = (Activity)context;
        this.mainLayout = planningTab;
        this.mapHandler = mapHandler;

        searchBarHasFocus = false;
        inputHandler = new InputHandler(500, new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.v("Running", "InputHandler");
                PlanningTabUIHelper.fetchSimilarStops(((String)msg.obj), wtSimilarStopsQueryHandler);
            }
        });
        findViews();

        init();
        initButtonsListener();
        initSearchListeners();
        initSearchAdapters();
        initCallBackHandlers();

        PlanningTabUIHelper.init(context);
        PlanningTabNavigationalView.init(R.id.planning_tab_navigation_bar, context);
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
                            PlanningTabNavigationalView.removeAllViews();
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
                if(Validator.validateStopNumber(query)) {
                    Log.v("Logging DB Entries","------------------------------");
                    StopSchedule currSchedule = displayStopSchedule(query);
                    //PlanningTabUIHelper.addToDB(currSchedule.getStop());
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
                inputHandler.update(newText);

                if(!searchBarHasFocus){
                    searchBarHasFocus = true;
                    animateLayout(300, 0);
                }

                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(!searchBarHasFocus) {
                        searchBarHasFocus = true;
                        animateLayout(300, 0);
                    }
                }else{
                    if(searchBarHasFocus){
                        searchBarHasFocus = false;
                        animateLayout(300, locationBeforeAnim.y);
                    }
                }
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                //displayStopSchedule((String)searchViewAdapter.getItem(position));
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Stop stopSelected = PlanningTabUIHelper.fetchStopInfo(searchViewAdapter.getSuggestionText(position));

                mapHandler.clear();
                mapHandler.addMarker(stopSelected.getCentre().getLatLng(), stopSelected.getName(), stopSelected);
                mapHandler.setFollowUserLocation(false);

                displayStopSchedule(searchViewAdapter.getSuggestionText(position));
                return true;
            }
        });
    }

    private void initSearchAdapters(){
        final SearchManager searchManager = (SearchManager)context.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(((Activity) context).getComponentName()));
        searchViewAdapter = new SearchViewAdapter(context, null, false);
        searchView.setSuggestionsAdapter(searchViewAdapter);

        searchView.setQueryHint("enter stop number or address..");
    }

    private void initCallBackHandlers(){
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

        wtSimilarStopsQueryHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                ArrayList<Stop> similarStops = (ArrayList<Stop>)msg.obj;
                String[] STOP_COLUMNS = {"_id", DB_STOP_NAME, DB_STOP_NUMBER, DB_STOP_DIRECTION};
                MatrixCursor cursor = new MatrixCursor(STOP_COLUMNS, similarStops.size());
                for(Stop s: similarStops) {
                    cursor.addRow(s.toArray());
                }

                if(similarStops.size()>0) searchViewAdapter.swapCursor(cursor);
                else searchViewAdapter.swapCursor(null);
            }
        };
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
                Log.v(PLANNING_TAB, "Planning Tab Down");
                switch (v.getId()){
                    case R.id.planning_tab:
                    case R.id.planning_tab_drag_view:
                        Log.v(PLANNING_TAB, "Planning Tab and drag view touch down Detected!");
                        translationDelta.x = x - (int)mainLayout.getTranslationX();
                        translationDelta.y = y - (int)mainLayout.getTranslationY();
                        return true;
                    case R.id.planning_tab_search_view:
                        Log.v(PLANNING_TAB, "Planning Tab Search bar touch down Detected!");
                        return true;
                }

                break;
            case ACTION_MOVE:
                Log.v(PLANNING_TAB, "Planning Tab Move");

                switch (v.getId()){
                    case R.id.planning_tab:
                    case R.id.planning_tab_drag_view:
                        translateMainLayoutTo(x,y,translationDelta.x, translationDelta.y);
                        return true;
                }
                break;
            case ACTION_UP:
                Log.v(PLANNING_TAB, "Planning Tab Up");

                switch (v.getId()){
                    case R.id.planning_tab_drag_view:
                        translateMainLayoutTo(x,y,translationDelta.x, translationDelta.y);
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

    public void translateMainLayoutTo(int x, int y, int dx, int dy) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mainLayout.getLayoutParams();

      //  if(mainLayout.getTranslationY() < 100 || mainLayout.getTranslationY() > getDisplayHeight(parentActivity)-100) return;
        if(y-dy < 5 || y-dy > getDisplayHeight(parentActivity)-100) return;

        mainLayout.setTranslationY(y-dy);

        //Log.v(PLANNING_TAB, "Relative Layout translation Y: " + mainLayout.getTranslationY());
        //Log.v(PLANNING_TAB, "Relative Layout display height: " + Constants.getDisplayHeight(parentActivity));

        layoutParams.height = (int) (Constants.getDisplayHeight(parentActivity) - (mainLayout.getTranslationY()));

        Log.v(PLANNING_TAB, "Relative Layout height: " + layoutParams.height);

        mainLayout.setLayoutParams(layoutParams);
    }

    public void displayNearbyStops(int distance) {
        ArrayList nearbyStops = PlanningTabUIHelper.getNearbyStops(
                mapHandler.getLastKnownLocation(), distance);

        //ArrayList additionalInfo = PlanningTabUIHelper.fetchStopsRoutesInfo(nearbyStops);

        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                View v = (View)msg.obj;
                Log.v(PLANNING_TAB, "Stop View On Click");
                int itemPosition = PlanningTabNavigationalView.getCurrentView().getChildLayoutPosition(v);
                Stop stopSelected = ((StopsListAdapterR)PlanningTabNavigationalView.getNavViewAdapter().getAdapter(PlanningTabNavigationalView.getCurrentIndex())).getItem(itemPosition);
                if(stopSelected!=null){
                    //clear map
                    mapHandler.clear();
                    mapHandler.addMarker(stopSelected.getCentre().getLatLng(), stopSelected.getName(), stopSelected);
                    mapHandler.setFollowUserLocation(false);

                    displayStopSchedule(stopSelected.getNumber());
                }
            }
        };

        //PlanningTabNavigationalView.addAdditionalStopData(nearbyStops.hashCode(), additionalInfo);
        PlanningTabNavigationalView.addView(nearbyStops, new Location(), handler);
        PlanningTabUIHelper.updateMap(mapHandler, distance, nearbyStops);
        mapHandler.getCurrentMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                displayStopSchedule(((Stop)marker.getTag()).getNumber());
                return false;
            }
        });
    }

    public StopSchedule displayStopSchedule(String stopNumber){
        //show schedule
        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                View v = (View)msg.obj;
                Log.v(PLANNING_TAB, "Route On Click");
                int itemPosition = PlanningTabNavigationalView.getCurrentView().getChildLayoutPosition(v);
                RouteVariant routeSelected = ((ScheduleListAdapterR)PlanningTabNavigationalView.getNavViewAdapter().getAdapter(PlanningTabNavigationalView.getCurrentIndex())).getItem(itemPosition);
                if(routeSelected!=null){
                    mapHandler.drawRouteOnMap(PlanningTabUIHelper.getDrawableRoute(routeSelected));
                    //PlanningTabUIHelper.getDrawableRoute(routeSelected);
                }
            }
        };

        Stop stop = PlanningTabUIHelper.fetchStopInfo(stopNumber);
        mapHandler.animate(stop.getCentre().getLatLng());

        StopSchedule stopSchedule = PlanningTabUIHelper.fetchStopSchedule(stopNumber);
        if(stopSchedule!=null) {
            PlanningTabNavigationalView.addView(stopSchedule.getRoutes(), stop, handler);
        }
        return stopSchedule;
    }

    public void onBackPressed() {
        inputHandler.onExit();
    }
}
