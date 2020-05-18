package com.greymatter.snowline.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.greymatter.snowline.Objects.RouteVariant;
import com.greymatter.snowline.Objects.Stop;
import com.greymatter.snowline.Objects.TypeCommon;
import com.greymatter.snowline.Objects.Vector;
import com.greymatter.snowline.R;
import com.greymatter.snowline.ui.adapters.NavigationalViewAdapterR;
import com.greymatter.snowline.ui.adapters.ScheduleListAdapterR;
import com.greymatter.snowline.ui.adapters.StopsListAdapterR;
import com.greymatter.snowline.app.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class PlanningTabNavigationalView {
    private static LinearLayout linearLayout;
    private static LinearLayoutManager mainRecyclerViewLayoutManager;
    private static RecyclerView mainRecyclerView;
    private static NavigationalViewAdapterR mainAdapter;
    private static View.OnClickListener mainListener;
    private static ArrayList<ArrayList<TypeCommon>> data;
    private static int currLayoutIndex = -1;

    public static void init(int layoutID, Context ctx) {
        linearLayout = ((Activity) ctx).findViewById(layoutID);
        mainRecyclerView = ((Activity) ctx).findViewById(R.id.nav_bar_rec_view);
        mainRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mainRecyclerViewLayoutManager = new LinearLayoutManager(ctx, RecyclerView.HORIZONTAL, false);
        mainRecyclerView.setLayoutManager(mainRecyclerViewLayoutManager);
        mainAdapter = new NavigationalViewAdapterR(ctx, mainListener);
        mainRecyclerView.setAdapter(mainAdapter);
        mainRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                currLayoutIndex = getCurrentItem();
                Log.v(Constants.NAV_TAB, "onScrolled: Current Index => "+ currLayoutIndex);
            }
        });

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mainRecyclerView);

        data = new ArrayList<>();
    }

    public static void addView(ArrayList<TypeCommon> viewData, Handler handler) {
        if(hasNext()){
            mainAdapter.remove(currLayoutIndex+1);
        }
        data.add(viewData);
        mainAdapter.onNewDataAdded(viewData, handler);
        currLayoutIndex++;
        next();
    }

    private static int getCurrentItem(){
        return ((LinearLayoutManager)mainRecyclerView.getLayoutManager())
                .findFirstVisibleItemPosition();
    }

    public static boolean hasNext() {
        return mainRecyclerView.getAdapter() != null &&
                getCurrentItem() < (mainRecyclerView.getAdapter().getItemCount()- 1);
    }

    public static RecyclerView getCurrentView() {
        return mainAdapter.getView(currLayoutIndex);
    }

    public static NavigationalViewAdapterR getNavViewAdapter() {
        return mainAdapter;
    }

    public static int getCurrentIndex() {
        return currLayoutIndex;
    }

    public static void removeCurrentViewFromDisplay(){
        if(currLayoutIndex>=0) {
            //linearLayout.removeView(views.get(currLayoutIndex));
        }
    }

    public static void next() {
        RecyclerView.Adapter adapter = mainRecyclerView.getAdapter();
        if (adapter == null)
            return;

        int position = getCurrentItem();
        int count = adapter.getItemCount();
        if (position < (count -1))
            setCurrentItem(position + 1, true);
    }

    private static void setCurrentItem(int position, boolean smooth){
        if (smooth)
            mainRecyclerView.smoothScrollToPosition(position);
        else
            mainRecyclerView.scrollToPosition(position);
    }

    public static void onTouchDown(int x, int y) {
        Log.v(Constants.NAV_TAB, "OnTouchDown {"+x + ", "+ y+"}");
        //translationDelta.x = x - (int)getCurrentView().getTranslationX();
        //translationDelta.y = y - (int)getCurrentView().getTranslationY();
    }

    public static void onTouchMove(int x, int y) {
        Log.v(Constants.NAV_TAB, "OnTouchMove {"+x + ", "+ y+"}");
        //getCurrentView().setTranslationX(x-translationDelta.x);
    }

    public static void setOnTouchListener(View.OnTouchListener onTouchListener) {
        linearLayout.setOnTouchListener(onTouchListener);
    }
}
