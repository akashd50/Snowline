package com.greymatter.snowline.Data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.greymatter.snowline.Data.data_accessors.AppDataDao;
import com.greymatter.snowline.Data.data_accessors.CenterDao;
import com.greymatter.snowline.Data.data_accessors.StopDao;
import com.greymatter.snowline.Data.data_accessors.StreetDao;
import com.greymatter.snowline.Data.entities.AppDataEntity;
import com.greymatter.snowline.Data.entities.CenterEntity;
import com.greymatter.snowline.Data.entities.StopEntity;
import com.greymatter.snowline.Data.entities.StreetEntity;

@Database(entities = {StopEntity.class, StreetEntity.class, CenterEntity.class, AppDataEntity.class}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract StopDao stopDao();
    public abstract StreetDao streetDao();
    public abstract CenterDao centerDao();
    public abstract AppDataDao appDataDao();
}
