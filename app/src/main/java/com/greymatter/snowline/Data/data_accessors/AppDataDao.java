package com.greymatter.snowline.Data.data_accessors;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.greymatter.snowline.Data.entities.AppDataEntity;

@Dao
public interface AppDataDao {
    @Query("SELECT * FROM APP_DATA_TABLE WHERE _id = :user LIMIT 1")
    AppDataEntity get(String user);

    @Update
    void update(AppDataEntity appDataEntity);

    @Insert
    void insertAll(AppDataEntity... appDataEntities);

    @Delete
    void delete(AppDataEntity appDataEntity);
}
