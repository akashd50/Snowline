package com.greymatter.snowline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.greymatter.snowline.Objects.RouteVariant;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ScheduleListAdapter extends BaseAdapter {
    private ArrayList<RouteVariant> routeVariantList;
    private LayoutInflater layoutInflater;
    private int listRowLayoutID;

    public ScheduleListAdapter(LayoutInflater inflater, int listRowLayoutId){
        //super();
        routeVariantList = new ArrayList<>();
        listRowLayoutID = listRowLayoutId;
        layoutInflater = inflater;
    }

    @Override
    public int getCount() {
        return routeVariantList.size();
    }

    @Override
    public Object getItem(int position) {
        return routeVariantList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = layoutInflater.inflate(listRowLayoutID, parent, false);

        TextView routeNum = vi.findViewById(R.id.route_number);
        TextView routeName = vi.findViewById(R.id.route_name);
        TextView routeTime = vi.findViewById(R.id.route_times);

        RouteVariant routeVariant = (RouteVariant)getItem(position);
        routeNum.setText(routeVariant.getRouteNumber());
        routeName.setText(routeVariant.getVariantName());
        routeTime.setText(routeVariant.getArrivalDateTime().format(DateTimeFormatter.ISO_LOCAL_TIME));

        return vi;
    }

    public void clear(){
        routeVariantList.clear();
    }

    public void addAll(ArrayList<RouteVariant> list){
        routeVariantList.addAll(list);
    }

}
