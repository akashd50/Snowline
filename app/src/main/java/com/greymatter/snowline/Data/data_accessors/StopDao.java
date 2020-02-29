package com.greymatter.snowline.Data.data_accessors;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.greymatter.snowline.Data.entities.StopEntity;
import java.util.List;

@Dao
public interface StopDao {
    @Query("SELECT * FROM STOPS_TABLE")
    List<StopEntity> getAll();

    @Query("SELECT * FROM STOPS_TABLE WHERE stop_number = :number LIMIT 1")
    StopEntity get(String number);

    @Query("SELECT * FROM STOPS_TABLE WHERE stop_number LIKE :number+'%'")
    Cursor getSimilar(String number);

    @Insert
    void insertAll(StopEntity... stops);

    @Delete
    void delete(StopEntity stop);
}
