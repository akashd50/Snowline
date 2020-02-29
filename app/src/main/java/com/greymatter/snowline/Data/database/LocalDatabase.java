package com.greymatter.snowline.Data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.greymatter.snowline.Data.data_accessors.StopDao;
import com.greymatter.snowline.Data.entities.StopEntity;

@Database(entities = {StopEntity.class}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract StopDao stopDao();
}
