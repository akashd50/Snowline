package com.greymatter.snowline.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.greymatter.snowline.Objects.RouteVariant;
import com.greymatter.snowline.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ScheduleListAdapter extends BaseAdapter {
    private ArrayList<RouteVariant> routeVariantList;
    private LayoutInflater layoutInflater;
    private ListView listView;
    private int listRowLayoutID;
    //private ArrayList<Boolean> listClicks;

    public ScheduleListAdapter(ListView listView, LayoutInflater inflater, int listRowLayoutId){
        //super();
        routeVariantList = new ArrayList<>();
        listRowLayoutID = listRowLayoutId;
        layoutInflater = inflater;
        this.listView = listView;
        //listClicks = new ArrayList<>();
        //this.listClicks = new ArrayList<>();

        //onClickListener();
    }

    public void updateClickList(ArrayList<Boolean> list){
        //this.listClicks = list;
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
        //TextView routeInfo = vi.findViewById(R.id.route_info_view);
        //if(position<listClicks.size() && listClicks.get(position)){
        //    routeInfo.setVisibility(View.VISIBLE);
       // }else{
      //      routeInfo.setVisibility(View.INVISIBLE);
       // }

        RouteVariant routeVariant = (RouteVariant)getItem(position);
        routeNum.setText(routeVariant.getNumber());
        routeName.setText(routeVariant.getVariantName());
        routeTime.setText(LocalDateTime.parse(routeVariant.getTimeinfo().getEstimatedArrival(),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME).format(DateTimeFormatter.ofPattern("hh:mm a")));

        return vi;
    }

    public void clear(){
        routeVariantList.clear();
    }

    public void addAll(ArrayList<RouteVariant> list){
        routeVariantList.addAll(list);
    }

}
