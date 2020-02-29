package com.greymatter.snowline.UI;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.SearchManager;
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
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static com.greymatter.snowline.app.Constants.*;

import com.greymatter.snowline.Adapters.ScheduleListRAdapter;
import com.greymatter.snowline.Adapters.SearchViewAdapter;
import com.greymatter.snowline.Data.database.LocalDatabase;
import com.greymatter.snowline.Data.database.StopDBHelper;
import com.greymatter.snowline.Data.entities.StopEntity;
import com.greymatter.snowline.Handlers.OnActionListener;
import com.greymatter.snowline.Objects.Stop;
import com.greymatter.snowline.app.Constants;
import com.greymatter.snowline.Handlers.HomeActivityHelper;
import com.greymatter.snowline.Handlers.KeyboardVisibilityListener;
import com.greymatter.snowline.Handlers.LinkGenerator;
import com.greymatter.snowline.Handlers.Validator;
import com.greymatter.snowline.Objects.StopSchedule;
import com.greymatter.snowline.Objects.Vector;
import com.greymatter.snowline.R;
import com.greymatter.snowline.app.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PlanningTab implements KeyboardVisibilityListener {
    private Context context;
    private Activity parentActivity;
    private RelativeLayout mainLayout;
    private SearchView searchView;
    private Button dragView;
    private boolean searchBarHasFocus;
    private Vector translationDelta, locationBeforeAnim;
    private View.OnTouchListener onTouchListener;
    private LinkGenerator linkGenerator;
    private RecyclerView recyclerView;
    private ScheduleListRAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private StopDBHelper stopDBHelper;
    private SearchViewAdapter searchViewAdapter;
    //private OnActionListener onDBQueryFinished;
    private Handler dbQueryHandler;
    public PlanningTab(final Context context, RelativeLayout planningTab){
        this.context = context;
        this.parentActivity = (Activity)context;
        this.mainLayout = planningTab;
        this.searchView = mainLayout.findViewById(R.id.planning_tab_search_view);
        this.dragView = mainLayout.findViewById(R.id.planning_tab_drag_view);
        searchBarHasFocus = false;
        stopDBHelper = new StopDBHelper(Services.getDatabase(context));

        init();
        initRecyclerView();
        initSearchListeners();
        initSearchAdapters();
        initDBListeners();
    }

    private void init(){
        translationDelta = new Vector();
        locationBeforeAnim = new Vector();

        mainLayout.setVisibility(View.VISIBLE);
        animateLayout(1,Constants.getDisplayHeight(parentActivity)-200);

        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchEvent(v,event);
                return false;
            }
        };
        dragView.setOnTouchListener(onTouchListener);
    }

    private void initSearchListeners(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.v(PLANNING_TAB, "OnQueryTextSubmit callback");
                if(Validator.validateStopNumber(query)) {

                    Log.v("Logging DB Entries","------------------------------");

                    ArrayList<StopEntity> allStops = stopDBHelper.getAllStops();
                    for(StopEntity stopEntity:allStops){
                        Log.v(PLANNING_TAB,stopEntity.stopName);
                    }

                    linkGenerator = new LinkGenerator();
                    linkGenerator.generateStopScheduleLink(query).apiKey()
                            .addTime(LocalDateTime.now()).usage(Constants.USAGE_LONG);

                    StopSchedule stopSchedule = HomeActivityHelper.fetchStopSchedule(linkGenerator);
                    if(stopSchedule!=null) {
                        mAdapter.updateLocalList(stopSchedule.getRoutes());
                        mAdapter.notifyDataSetChanged();
                    }

                    Stop stop = stopSchedule.getStop();

                    final StopEntity stopEntity = new StopEntity();
                    if(stop!=null) {
                        stopEntity.key = Integer.parseInt(stop.getNumber());
                        stopEntity.stopName = stop.getName();
                        stopEntity.stopNumber = stop.getNumber();
                        stopEntity.direction = stop.getDirection();
                    }
                    if(!stopDBHelper.exists(query)){
                        stopDBHelper.addStop(stopEntity);
                    }
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
    }

    private void initSearchAdapters(){
        final SearchManager searchManager = (SearchManager)context.getSystemService(Context.SEARCH_SERVICE);
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

//        dbQueryHandler = new OnActionListener() {
//            @Override
//            public void onAction(Object object) {
//                Message completeMessage = handler.obtainMessage(0, object);
//                completeMessage.sendToTarget();
//            }
//        };
    }

    private void initRecyclerView(){
        recyclerView = mainLayout.findViewById(R.id.planning_tab_recycler_view);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ScheduleListRAdapter();
        recyclerView.setAdapter(mAdapter);
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

    public void onTouchEvent(View v, MotionEvent event){
        int x = (int)event.getRawX();
        int y = (int)event.getRawY();
        switch (event.getAction()){
            case ACTION_DOWN:
                Log.v(PLANNING_TAB, "Planning Tab Click Detected!");
                switch (v.getId()){
                    case R.id.planning_tab_drag_view:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                mainLayout.getLayoutParams();

                        translationDelta.x = x - (int)mainLayout.getTranslationX();
                        translationDelta.y = y - (int)mainLayout.getTranslationY();
                        break;
                    case R.id.planning_tab_search_view:
                        Log.v(PLANNING_TAB, "Planning Tab Search bar touch down Detected!");
                        break;
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
                        break;
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
                        break;

                    case R.id.planning_tab_search_view:
                        Log.v(PLANNING_TAB, "Planning Tab Search bar touch up Detected!");

                        break;
                }
                break;
        }
        mainLayout.invalidate();
    }


}
