package com.greymatter.snowline.ui.adapters;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ListLineHolder extends RecyclerView.ViewHolder {
    private View lineView;
    public ListLineHolder(View v) {
        super(v);
        lineView = v;
    }

    public View getLineView(){
        return lineView;
    }
}