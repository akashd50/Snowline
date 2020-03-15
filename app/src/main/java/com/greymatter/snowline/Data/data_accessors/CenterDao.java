package com.greymatter.snowline.Data.data_accessors;

import android.database.Cursor;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.greymatter.snowline.Data.entities.CenterEntity;
import java.util.List;

@Dao
public interface CenterDao {
    @Query("SELECT * FROM CENTER_TABLE")
    List<CenterEntity> getAllToList();

    @Query("SELECT * FROM CENTER_TABLE")
    Cursor getAllToCursor();

    @Query("SELECT * FROM CENTER_TABLE WHERE _id = :id LIMIT 1")
    CenterEntity get(int id);

    @Insert
    void insertAll(CenterEntity... center);

    @Delete
    void delete(CenterEntity center);
}
