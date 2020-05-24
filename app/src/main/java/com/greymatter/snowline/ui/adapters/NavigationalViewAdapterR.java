package com.greymatter.snowline.ui.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greymatter.snowline.Objects.Route;
import com.greymatter.snowline.Objects.RouteVariant;
import com.greymatter.snowline.Objects.Stop;
import com.greymatter.snowline.Objects.TypeCommon;
import com.greymatter.snowline.R;

import java.util.ArrayList;
import java.util.HashMap;

public class NavigationalViewAdapterR extends RecyclerView.Adapter<ListLineHolder> {
    private Context context;
    private ArrayList<ArrayList<TypeCommon>> localList;
    private HashMap<Integer, TypeCommonAdapter> localAdapters;
    private HashMap<Integer, RecyclerView> localViews;
    private HashMap<Integer, Handler> localHandlers;
    private HashMap<Integer, View.OnClickListener> localListeners;
    private HashMap<Integer, TypeCommon> listItemsTitleData;
    private HashMap<Integer, ArrayList<ArrayList<Route>>> additionalStopsInfo;
    private View.OnClickListener navViewListener;

    public NavigationalViewAdapterR(Context context, View.OnClickListener listener) {
        this.context = context;
        localList = new ArrayList<>();
        localAdapters = new HashMap<>();
        localViews = new HashMap<>();
        localHandlers = new HashMap<>();
        localListeners = new HashMap<>();
        listItemsTitleData = new HashMap<>();
        additionalStopsInfo = new HashMap<>();
        navViewListener = listener;
    }

    public void onNewDataAdded(ArrayList<TypeCommon> list, TypeCommon infoRelatedTo, Handler handler){
        localViews.clear();
        listItemsTitleData.put(list.hashCode(), infoRelatedTo);
        localList.add(list);
        localHandlers.put(list.hashCode(), handler);
        this.notifyDataSetChanged();
    }

    public void addAdditionalStopData(int hashCode, ArrayList<ArrayList<Route>> stopsRoutes) {
        additionalStopsInfo.put(hashCode, stopsRoutes);
    }

    public void remove(int index) {
        ArrayList toRemove = localList.remove(index);
        localHandlers.remove(toRemove.hashCode());
        localListeners.remove(toRemove.hashCode());
        localAdapters.remove(toRemove.hashCode());
        localViews.remove(toRemove.hashCode());
        listItemsTitleData.remove(toRemove.hashCode());
        additionalStopsInfo.remove(toRemove.hashCode());
        this.notifyDataSetChanged();
    }

    public void removeAll() {
        localList.clear();
        localHandlers.clear();
        localListeners.clear();
        localAdapters.clear();
        localViews.clear();
        listItemsTitleData.clear();
        additionalStopsInfo.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return localList.size();
    }

    public ArrayList<TypeCommon> getList(int pos){
        return localList.get(pos);
    }

    public TypeCommonAdapter getAdapter(int index) {
        return localAdapters.get(localList.get(index).hashCode());
    }

    public RecyclerView getView(int index) {
        return localViews.get(localList.get(index).hashCode());
    }

    public TypeCommon getTitleForView(int index) {
        return listItemsTitleData.get(localList.get(index).hashCode());
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
                listener = getNewListener(localHandlers.get(currentList.hashCode()));
                localListeners.put(currentList.hashCode(), listener);
            }else{
                listener = localListeners.get(currentList.hashCode());
            }

            if(position >= localAdapters.size()) {
                adapterR = new ScheduleListAdapterR(listener);
                localAdapters.put(currentList.hashCode(), adapterR);
            }else{
                adapterR = (ScheduleListAdapterR)localAdapters.get(currentList.hashCode());
            }

            view.setAdapter(adapterR);
            adapterR.onNewDataAdded(localList.get(position));
            adapterR.notifyDataSetChanged();
            localViews.put(currentList.hashCode(), view);

        }else if(localList.get(position).get(0) instanceof Stop) {
            StopsListAdapterR adapterR = null;
            View.OnClickListener listener = null;
            if(position >= localListeners.size()) {
                listener = getNewListener(localHandlers.get(currentList.hashCode()));
                localListeners.put(currentList.hashCode(), listener);
            }else{
                listener = localListeners.get(currentList.hashCode());
            }

            if(position >= localAdapters.size()) {
                adapterR = new StopsListAdapterR(listener);
                localAdapters.put(currentList.hashCode(), adapterR);
            }else{
                adapterR = (StopsListAdapterR)localAdapters.get(currentList.hashCode());
            }

            view.setAdapter(adapterR);
            adapterR.onNewDataAdded(localList.get(position));
            if(additionalStopsInfo.get(currentList.hashCode())!=null) {
                adapterR.onNewDataAddedAddl(additionalStopsInfo.get(currentList.hashCode()));
            }
            adapterR.notifyDataSetChanged();
            localViews.put(currentList.hashCode(), view);
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
