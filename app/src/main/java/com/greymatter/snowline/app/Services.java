package com.greymatter.snowline.app;

import android.content.Context;
import androidx.room.Room;
import com.greymatter.snowline.Data.database.LocalDatabase;

public class Services {
    private static LocalDatabase localDatabase;
    public static LocalDatabase getDatabase(Context context){
         if(localDatabase==null) {
             localDatabase = Room.databaseBuilder(context,
                     LocalDatabase.class, "snowline").build();
         }
         return localDatabase;
    }
}
