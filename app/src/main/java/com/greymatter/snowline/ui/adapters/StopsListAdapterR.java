package com.greymatter.snowline.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greymatter.snowline.Objects.Route;
import com.greymatter.snowline.Objects.RouteVariant;
import com.greymatter.snowline.Objects.Stop;
import com.greymatter.snowline.R;

import java.util.ArrayList;
import java.util.HashMap;

public class StopsListAdapterR extends RecyclerView.Adapter<ListLineHolder> implements TypeCommonAdapter {
    private ArrayList<Stop> localList;
    private ArrayList<ArrayList<Route>> stopRouteInfo;
    private View.OnClickListener listClickListener;
    private Context context;

    public StopsListAdapterR(View.OnClickListener listClickListener){
        this.listClickListener = listClickListener;
        this.stopRouteInfo = new ArrayList<>();
        localList = new ArrayList<>();
    }

    public StopsListAdapterR(){
        localList = new ArrayList<>();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listClickListener = listener;
    }

    public void onNewDataAdded(ArrayList list){
        localList.clear();
        localList.addAll(list);
    }

    public void onNewDataAddedAddl(ArrayList<ArrayList<Route>> list){
        stopRouteInfo.clear();
        stopRouteInfo.addAll(list);
    }

    @Override
    public int getItemCount() {
        return localList.size();
    }

    public Stop getItem(int pos){
        return localList.get(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull ListLineHolder holder, int position) {
        TextView stopNum = holder.getLineView().findViewById(R.id.stop_format_stop_number);
        TextView stopName = holder.getLineView().findViewById(R.id.stop_format_stop_name);
        LinearLayout routesList = holder.getLineView().findViewById(R.id.stop_format_avail_routes_view);

        Stop stop = (Stop)getItem(position);

        if(!stopRouteInfo.isEmpty()) {
            ArrayList<Route> currentStopRoutes = stopRouteInfo.get(position);
            for (Route route : currentStopRoutes) {
                if (context != null) {
                    Button button = new Button(context);
                    button.setText(route.getNumber());
                    button.setBackgroundResource(R.drawable.stop_number_background);
                    routesList.addView(button);
                }
            }
        }

        routesList.invalidate();

        stopNum.setText(stop.getNumber());
        stopName.setText(stop.getName());
    }

    @NonNull
    @Override
    public ListLineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context == null) {
            context = parent.getContext();
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stop_format, parent, false);
        ListLineHolder listLine = new ListLineHolder(v);
        v.setOnClickListener(listClickListener);
        return listLine;
    }

    public void clear(){
        this.localList.clear();
    }
    public void notifyDatasetChanged() {
        this.notifyDataSetChanged();
    }
}
