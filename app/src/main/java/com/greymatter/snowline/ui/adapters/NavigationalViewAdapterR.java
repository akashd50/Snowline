package com.greymatter.snowline.ui.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greymatter.snowline.Objects.RouteVariant;
import com.greymatter.snowline.Objects.Stop;
import com.greymatter.snowline.Objects.TypeCommon;
import com.greymatter.snowline.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class NavigationalViewAdapterR extends RecyclerView.Adapter<ListLineHolder> {
    private Context context;
    private ArrayList<ArrayList<TypeCommon>> localList;
    private HashMap<String, TypeCommonAdapter> localAdapters;
    private HashMap<String, RecyclerView> localViews;
    private HashMap<String, Handler> localHandlers;
    private HashMap<String, View.OnClickListener> localListeners;
    private View.OnClickListener navViewListener;

    public NavigationalViewAdapterR(Context context, View.OnClickListener listener) {
        this.context = context;
        localList = new ArrayList<>();
        localAdapters = new HashMap<>();
        localViews = new HashMap<>();
        localHandlers = new HashMap<>();
        localListeners = new HashMap<>();
        navViewListener = listener;
    }

    public void onNewDataAdded(ArrayList<TypeCommon> list, Handler handler){
        localViews.clear();

        localList.add(list);
        localHandlers.put(list.toString(), handler);
        this.notifyDataSetChanged();
    }

    public void remove(int index) {
        ArrayList toRemove = localList.remove(index);
        localHandlers.remove(toRemove.toString());
        localListeners.remove(toRemove.toString());
        localAdapters.remove(toRemove.toString());
        localViews.remove(toRemove.toString());
    }

    @Override
    public int getItemCount() {
        return localList.size();
    }

    public ArrayList<TypeCommon> getList(int pos){
        return localList.get(pos);
    }

    public TypeCommonAdapter getAdapter(int index) {
        return localAdapters.get(localList.get(index).toString());
    }

    public RecyclerView getView(int index) {
        return localViews.get(localList.get(index).toString());
    }

    @Override
    public void onBindViewHolder(@NonNull ListLineHolder holder, int position) {
        ArrayList currentList = localList.get(position);
        RecyclerView view = holder.getLineView().findViewById(R.id.nav_tab_object_rec_view);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        view.setLayoutManager(manager);

        if(localList.get(position).get(0) instanceof RouteVariant) {
            ScheduleListAdapterR adapterR = null;
            View.OnClickListener listener = null;
            if(position >= localListeners.size()) {
                listener = getNewListener(localHandlers.get(currentList.toString()));
                localListeners.put(currentList.toString(), listener);
            }else{
                listener = localListeners.get(currentList.toString());
            }

            if(position >= localAdapters.size()) {
                adapterR = new ScheduleListAdapterR();
                localAdapters.put(currentList.toString(), adapterR);
            }else{
                adapterR = (ScheduleListAdapterR)localAdapters.get(currentList.toString());
            }

            view.setAdapter(adapterR);
            adapterR.updateLocalList(localList.get(position));
            adapterR.notifyDataSetChanged();
            localViews.put(currentList.toString(), view);

        }else if(localList.get(position).get(0) instanceof Stop) {
            StopsListAdapterR adapterR = null;
            View.OnClickListener listener = null;
            if(position >= localListeners.size()) {
                listener = getNewListener(localHandlers.get(currentList.toString()));
                localListeners.put(currentList.toString(), listener);
            }else{
                listener = localListeners.get(currentList.toString());
            }

            if(position >= localAdapters.size()) {
                adapterR = new StopsListAdapterR(listener);
                localAdapters.put(currentList.toString(), adapterR);
            }else{
                adapterR = (StopsListAdapterR)localAdapters.get(currentList.toString());
            }

            view.setAdapter(adapterR);
            adapterR.updateLocalList(localList.get(position));
            adapterR.notifyDataSetChanged();
            localViews.put(currentList.toString(), view);
        }
    }

    private View.OnClickListener getNewListener(final Handler handler) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.obtainMessage(0, v).sendToTarget();
            }
        };
    }

    @NonNull
    @Override
    public ListLineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nav_tab_object, parent, false);
        v.setOnClickListener(navViewListener);
        ListLineHolder listLine = new ListLineHolder(v);
        return listLine;
    }

    public void clear(){
        localList.clear();
    }
}
