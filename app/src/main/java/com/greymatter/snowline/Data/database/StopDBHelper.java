package com.greymatter.snowline.Data.database;

import android.database.Cursor;
import android.os.Handler;

import com.greymatter.snowline.Data.entities.AppDataEntity;
import com.greymatter.snowline.Data.entities.StopEntity;
import com.greymatter.snowline.app.Services;

import java.util.ArrayList;

import static com.greymatter.snowline.app.Constants.DEFAULT_USER;
import static com.greymatter.snowline.app.Constants.FAIL;
import static com.greymatter.snowline.app.Constants.SUCCESS;

public class StopDBHelper {
    private LocalDatabase database;
    public StopDBHelper(LocalDatabase dbref){
        database = dbref;
    }

    public void addStop(final StopEntity stopEntity, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    int nextId = Services.getAppDataDBHelper().getNextID(DEFAULT_USER);
                    stopEntity.id = nextId;

                    database.stopDao().insertAll(stopEntity);
                    handler.obtainMessage(SUCCESS).sendToTarget();
                }catch (Exception e){
                    handler.obtainMessage(FAIL).sendToTarget();
                }
            }
        }).start();
    }

    public void addStop(final StopEntity stopEntity){
        final Object lock = new Object();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    database.stopDao().insertAll(stopEntity);
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
                StopEntity stopEntity = database.stopDao().get(toFind);;
                if(stopEntity!=null) handler.obtainMessage(SUCCESS, stopEntity).sendToTarget();
                else  handler.obtainMessage(FAIL, stopEntity).sendToTarget();
            }
        }).start();
    }

    public boolean find(final String toFind){
        final Object lock = new Object();

        final StopEntity[] toReturn = new StopEntity[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    toReturn[0] = database.stopDao().get(toFind);
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

    public void getSimilar(final String toFind, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = database.stopDao().getSimilar(toFind);
                if(cursor!=null) handler.obtainMessage(SUCCESS,cursor).sendToTarget();
                else handler.obtainMessage(FAIL, cursor).sendToTarget();
            }
        }).start();
    }

    public void getAllStops(final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList toReturn = (ArrayList)database.stopDao().getAllToList();
                if(toReturn!=null) handler.obtainMessage(SUCCESS, toReturn).sendToTarget();
                else handler.obtainMessage(FAIL, toReturn).sendToTarget();
            }
        }).start();
    }

    public ArrayList<StopEntity> getAllStops(){
        final Object lock = new Object();
        final ArrayList<StopEntity> toReturn = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    toReturn.addAll(database.stopDao().getAllToList());
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
