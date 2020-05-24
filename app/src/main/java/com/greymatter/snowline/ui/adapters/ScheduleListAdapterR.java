package com.greymatter.snowline.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greymatter.snowline.Objects.RouteVariant;
import com.greymatter.snowline.R;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ScheduleListAdapterR extends RecyclerView.Adapter<ListLineHolder> implements TypeCommonAdapter {
    private ArrayList<RouteVariant> localList;
    private View.OnClickListener listener;
    private Context context;
    public ScheduleListAdapterR() {
        localList = new ArrayList<>();
    }
    public ScheduleListAdapterR(View.OnClickListener listener) {
        localList = new ArrayList<>();
        this.listener = listener;
    }

    public void onNewDataAdded(ArrayList list){
        localList.clear();
        localList.addAll(list);
    }

    @Override
    public int getItemCount() {
        return localList.size();
    }

    public RouteVariant getItem(int pos){
        return localList.get(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull ListLineHolder holder, int position) {
        boolean contextNotNull = context != null;
        TextView routeNum = holder.getLineView().findViewById(R.id.route_number);
        TextView routeName = holder.getLineView().findViewById(R.id.route_name);
        TextView routeTime = holder.getLineView().findViewById(R.id.route_times);

        RouteVariant routeVariant = (RouteVariant)getItem(position);
        routeNum.setText(routeVariant.getNumber());
        routeName.setText(routeVariant.getVariantName());

        String time = routeVariant.getTimeinfo().getEstimatedArrival();
        if(time!=null) {
            LocalDateTime toDateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalTime busTime = toDateTime.toLocalTime();
            long busTimeHour = busTime.getHour();
            long busMinute = busTime.getMinute();

            long hourDifference = busTimeHour-LocalTime.now().getHour();
            long minuteDifference = busMinute - LocalTime.now().getMinute();
            if(hourDifference ==0 &&  minuteDifference < 30) {
                String toSet = busMinute - LocalTime.now().getMinute() + " mins";
                routeTime.setText(toSet);
                if(minuteDifference < 10) {
                    if(contextNotNull) {
                        routeTime.setTextColor(context.getColor(R.color.color_light_red));
                        routeTime.setShadowLayer(15, 0,0, Color.rgb(252, 0, 0));
                    }
                }
            }else {
                routeTime.setText(LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        .format(DateTimeFormatter.ofPattern("hh:mm a")));
            }
        }
    }

    @NonNull
    @Override
    public ListLineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_format, parent, false);
        v.setOnClickListener(listener);
        ListLineHolder listLine = new ListLineHolder(v);
        return listLine;
    }

    public void clear(){
        this.localList.clear();
    }
    public void notifyDatasetChanged() {
        this.notifyDataSetChanged();
    }
}
