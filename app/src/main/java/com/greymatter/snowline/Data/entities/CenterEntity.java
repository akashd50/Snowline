package com.greymatter.snowline.Data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import static com.greymatter.snowline.app.Constants.*;

@Entity(tableName = DB_CENTER_TABLE)
public class CenterEntity {
    @PrimaryKey @ColumnInfo(name = DB_ID)
    public int id;

    @ColumnInfo(name = DB_STOP_NUMBER)
    public int stopNumber;

    @ColumnInfo(name = DB_ZONE)
    public int zone;

    @ColumnInfo(name = DB_UTMX)
    public int utmX;

    @ColumnInfo(name = DB_UTMY)
    public int utmY;

    @ColumnInfo(name = DB_LATITUDE)
    public int latitude;

    @ColumnInfo(name = DB_LONGITUDE)
    public int longitude;
}
