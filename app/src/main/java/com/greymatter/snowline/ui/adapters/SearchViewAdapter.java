package com.greymatter.snowline.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cursoradapter.widget.CursorAdapter;

import com.greymatter.snowline.app.Constants;

public class SearchViewAdapter extends CursorAdapter {
    private Cursor localAdapterInstance;
    private boolean isDirty;

    public SearchViewAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public SearchViewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public Cursor getLocalAdapterInstance() {
        return localAdapterInstance;
    }

    public void setLocalAdapterInstance(Cursor localAdapterInstance) {
        this.localAdapterInstance = localAdapterInstance;
        isDirty = true;
    }

    public String getSuggestionText(int position){
        if(localAdapterInstance!=null){
            localAdapterInstance.moveToPosition(position);
            String stopNumber = localAdapterInstance.getString(localAdapterInstance.getColumnIndexOrThrow(Constants.DB_STOP_NUMBER));
            return stopNumber;
        }
        return null;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        if(isDirty){
            swapCursor(localAdapterInstance);
            isDirty = false;
        }

        View v = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(android.R.layout.simple_list_item_1, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        if(isDirty){
            swapCursor(localAdapterInstance);
            isDirty = false;
        }

        if(cursor!=null) {
            String stopNumber = cursor.getString(cursor.getColumnIndexOrThrow(Constants.DB_STOP_NUMBER));
            TextView t1 = (TextView) view;
            t1.setText(stopNumber);
        }
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        localAdapterInstance = newCursor;
        return super.swapCursor(newCursor);
    }
}
