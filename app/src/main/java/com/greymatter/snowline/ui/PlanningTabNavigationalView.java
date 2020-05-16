package com.greymatter.snowline.ui;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greymatter.snowline.Objects.RouteVariant;
import com.greymatter.snowline.Objects.Stop;
import com.greymatter.snowline.R;
import com.greymatter.snowline.adapters.ScheduleListAdapterR;

import java.util.ArrayList;
import java.util.HashMap;

public class PlanningTabNavigationalView {
    private static LinearLayout linearLayout;
    private static ArrayList<RecyclerView> views;
    private static HashMap<String, LinearLayoutManager> linearLayoutManagers;
    private static HashMap<String, ArrayList<RouteVariant>> viewsData;
    private static HashMap<String,  View.OnClickListener> recyclerViewListeners;

    public static void init(int layoutID, Context ctx){
        linearLayout = ((Activity)ctx).findViewById(layoutID);
        linearLayoutManagers = new HashMap<>();
        views = new ArrayList<>();
        viewsData = new HashMap<>();
        recyclerViewListeners = new HashMap<>();
    }

    public static void addRecyclerView(Context context, ArrayList<RouteVariant> stops) {
        ScheduleListAdapterR adapterR = new ScheduleListAdapterR();
        LinearLayoutManager manager = new LinearLayoutManager(context);

        RecyclerView view = new RecyclerView(context);
        view.setMinimumWidth(linearLayout.getWidth());
        view.setMinimumHeight(1000);
        view.setBackgroundResource(R.color.color_dark_navy_blue);
        view.setLayoutManager(manager);
        view.setAdapter(adapterR);

        adapterR.updateLocalList(stops);
        adapterR.notifyDataSetChanged();

        linearLayout.addView(view);
        views.add(view);
        linearLayoutManagers.put(view.toString(), manager);
        viewsData.put(view.toString(), stops);

        linearLayout.invalidate();
    }
}
