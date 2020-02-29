package com.greymatter.snowline.Data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.greymatter.snowline.app.Constants.DB_ID;
import static com.greymatter.snowline.app.Constants.DB_STOP_TABLE;
import static com.greymatter.snowline.app.Constants.DB_STREET_NAME;
import static com.greymatter.snowline.app.Constants.DB_STREET_TABLE;
import static com.greymatter.snowline.app.Constants.DB_STREET_TYPE;

@Entity(tableName = DB_STREET_TABLE)
public class StreetEntity {
    @ColumnInfo(name = DB_STREET_NAME)
    public String streetName;

    @ColumnInfo(name = DB_STREET_TYPE)
    public String streetType;
}
