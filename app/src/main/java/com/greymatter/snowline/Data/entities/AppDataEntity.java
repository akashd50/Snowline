package com.greymatter.snowline.Data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import static com.greymatter.snowline.app.Constants.*;

@Entity(tableName = DB_APP_DATA_TABLE)
public class AppDataEntity {
    @PrimaryKey
    @ColumnInfo(name = DB_ID)
    public int user;

    @ColumnInfo(name = DB_APP_IDS)
    public int nextID;
}
