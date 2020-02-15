package com.greymatter.snowline.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greymatter.snowline.Objects.RouteVariant;
import com.greymatter.snowline.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ScheduleListRAdapter extends RecyclerView.Adapter<ScheduleListRAdapter.ListLineHolder> {
    private ArrayList<RouteVariant> localList;
    public ScheduleListRAdapter(){localList = new ArrayList<>();}
    public void updateLocalList(ArrayList list){
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
        TextView routeNum = holder.lineView.findViewById(R.id.route_number);
        TextView routeName = holder.lineView.findViewById(R.id.route_name);
        TextView routeTime = holder.lineView.findViewById(R.id.route_times);

        RouteVariant routeVariant = (RouteVariant)getItem(position);
        routeNum.setText(routeVariant.getNumber());
        routeName.setText(routeVariant.getVariantName());
        routeTime.setText(LocalDateTime.parse(routeVariant.getTimeinfo().getEstimatedArrival(),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME).format(DateTimeFormatter.ofPattern("hh:mm a")));
        //TextView routeInfo = vi.findViewById(R.id.route_info_view);
    }

    @NonNull
    @Override
    public ListLineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_format, parent, false);
        ListLineHolder listLine = new ListLineHolder(v);
        return listLine;
    }

    public static class ListLineHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View lineView;
        public ListLineHolder(View v) {
            super(v);
            lineView = v;
        }
    }
}
