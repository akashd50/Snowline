package com.greymatter.snowline.Data.data_accessors;

import android.database.Cursor;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.greymatter.snowline.Data.entities.StreetEntity;
import java.util.List;

@Dao
public interface StreetDao {
    @Query("SELECT * FROM STREET_TABLE")
    List<StreetEntity> getAllToList();

    @Query("SELECT * FROM STREET_TABLE")
    Cursor getAllToCursor();

    @Query("SELECT * FROM STREET_TABLE WHERE street_name = :name LIMIT 1")
    StreetEntity get(String name);

    @Query("SELECT * FROM STREET_TABLE WHERE _id = :id LIMIT 1")
    StreetEntity get(int id);

    @Insert
    void insertAll(StreetEntity... street);

    @Delete
    void delete(StreetEntity street);
}
