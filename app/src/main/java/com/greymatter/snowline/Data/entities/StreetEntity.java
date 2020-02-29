package com.greymatter.snowline.Data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import static com.greymatter.snowline.app.Constants.*;

@Entity(tableName = DB_STREET_TABLE)
public class StreetEntity {
    @PrimaryKey @ColumnInfo(name = DB_ID)
    public int id;

    @ColumnInfo(name = KEY)
    public int key;

    @ColumnInfo(name = DB_STREET_NAME)
    public String streetName;

    @ColumnInfo(name = DB_STREET_TYPE)
    public String streetType;
}
