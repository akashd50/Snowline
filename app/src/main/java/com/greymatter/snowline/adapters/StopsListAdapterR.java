package com.greymatter.snowline.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greymatter.snowline.Objects.Stop;
import com.greymatter.snowline.R;

import java.util.ArrayList;

public class StopsListAdapterR extends RecyclerView.Adapter<ListLineHolder> {
    private ArrayList<Stop> localList;
    private View.OnClickListener listClickListener;
    public StopsListAdapterR(View.OnClickListener listClickListener){
        this.listClickListener = listClickListener;
        localList = new ArrayList<>();
    }
    public void updateLocalList(ArrayList list){
        localList.clear();
        localList.addAll(list);
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

        Stop stop = (Stop)getItem(position);
        stopNum.setText(stop.getNumber());
        stopName.setText(stop.getName());
    }

    @NonNull
    @Override
    public ListLineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stop_format, parent, false);
        ListLineHolder listLine = new ListLineHolder(v);
        v.setOnClickListener(listClickListener);
        return listLine;
    }

    public void clear(){
        this.localList.clear();
    }
}
