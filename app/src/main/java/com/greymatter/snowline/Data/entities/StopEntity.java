package com.greymatter.snowline.Data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import static com.greymatter.snowline.app.Constants.*;

@Entity(tableName = DB_STOP_TABLE)
public class StopEntity {
    @PrimaryKey @ColumnInfo(name = DB_ID)
    public int id;

    @ColumnInfo(name = KEY)
    public int key;

    @ColumnInfo(name = DB_STOP_NUMBER)
    public String stopNumber;

    @ColumnInfo(name = DB_STOP_NAME)
    public String stopName;

    @ColumnInfo(name = DB_STOP_DIRECTION)
    public String direction;
}
