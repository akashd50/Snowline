package com.greymatter.snowline.Data.database;

import android.database.Cursor;
import android.os.Handler;
import com.greymatter.snowline.Data.entities.StreetEntity;
import java.util.ArrayList;
import static com.greymatter.snowline.app.Constants.FAIL;
import static com.greymatter.snowline.app.Constants.SUCCESS;

public class StreetDBHelper {
    private LocalDatabase database;
    public StreetDBHelper(LocalDatabase dbref){
        database = dbref;
    }
    
    public void addNew(final StreetEntity streetEntity, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    database.streetDao().insertAll(streetEntity);
                    handler.obtainMessage(SUCCESS).sendToTarget();
                }catch (Exception e){
                    handler.obtainMessage(FAIL).sendToTarget();
                }
            }
        }).start();
    }
    
    public void addStop(final StreetEntity streetEntity){
        final Object lock = new Object();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    database.streetDao().insertAll(streetEntity);
                    lock.notifyAll();
                }
            }
        }).start();

        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void find(final String toFind, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                StreetEntity streetEntity = database.streetDao().get(toFind);;
                if(streetEntity!=null) handler.obtainMessage(SUCCESS, streetEntity).sendToTarget();
                else  handler.obtainMessage(FAIL, streetEntity).sendToTarget();
            }
        }).start();
    }

    public boolean find(final String toFind){
        final Object lock = new Object();

        final StreetEntity[] toReturn = new StreetEntity[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    toReturn[0] = database.streetDao().get(toFind);
                    lock.notifyAll();
                }
            }
        }).start();

        synchronized (lock){
            try {
                lock.wait();
                return !(toReturn[0]==null);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void getAllStreets(final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList toReturn = (ArrayList)database.streetDao().getAllToList();
                if(toReturn!=null) handler.obtainMessage(SUCCESS, toReturn).sendToTarget();
                else handler.obtainMessage(FAIL, toReturn).sendToTarget();
            }
        }).start();
    }

    public ArrayList<StreetEntity> getAllStreets(){
        final Object lock = new Object();
        final ArrayList<StreetEntity> toReturn = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    toReturn.addAll(database.streetDao().getAllToList());
                    lock.notifyAll();
                }
            }
        }).start();

        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return toReturn;
    }
}
