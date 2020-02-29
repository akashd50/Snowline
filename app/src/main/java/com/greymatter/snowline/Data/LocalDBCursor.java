package com.greymatter.snowline.Data;

import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteQuery;
import com.greymatter.snowline.Objects.Stop;
import java.util.ArrayList;

public class LocalDBCursor extends SQLiteCursor {
    private ArrayList<Stop> stops;
    private int currIndex;
    public LocalDBCursor(SQLiteCursorDriver driver, String editTable, SQLiteQuery query){
        super(driver, editTable, query);

    }
}
