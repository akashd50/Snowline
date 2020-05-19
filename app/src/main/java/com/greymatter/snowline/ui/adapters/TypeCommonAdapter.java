package com.greymatter.snowline.ui.adapters;

import java.util.ArrayList;

public interface TypeCommonAdapter {
    void onNewDataAdded(ArrayList list);
    void notifyDatasetChanged();
    void clear();
}
