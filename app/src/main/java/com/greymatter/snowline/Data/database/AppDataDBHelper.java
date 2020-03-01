package com.greymatter.snowline.Data.database;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.greymatter.snowline.Data.entities.AppDataEntity;
import com.greymatter.snowline.Data.entities.StopEntity;

import java.util.ArrayList;

import static com.greymatter.snowline.app.Constants.FAIL;
import static com.greymatter.snowline.app.Constants.SUCCESS;

public class AppDataDBHelper {
    private LocalDatabase database;
    public AppDataDBHelper(LocalDatabase dbref){
        database = dbref;
    }

    public void addNew(final AppDataEntity appDataEntity, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    database.appDataDao().insertAll(appDataEntity);
                    if(handler!=null) handler.obtainMessage(SUCCESS).sendToTarget();
                }catch (Exception e){
                    if(handler!=null) handler.obtainMessage(FAIL).sendToTarget();
                }
            }
        }).start();
    }

    public void addNew(final AppDataEntity appDataEntity){
        final Object lock = new Object();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    database.appDataDao().insertAll(appDataEntity);
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
                AppDataEntity appDataEntity = database.appDataDao().get(toFind);
                if(appDataEntity!=null && handler!=null) handler.obtainMessage(SUCCESS, appDataEntity).sendToTarget();
                else if(handler!=null)  handler.obtainMessage(FAIL, appDataEntity).sendToTarget();
            }
        }).start();
    }

    public AppDataEntity find(final String toFind){
        final Object lock = new Object();

        final AppDataEntity[] toReturn = new AppDataEntity[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    toReturn[0] = database.appDataDao().get(toFind);
                    lock.notifyAll();
                }
            }
        }).start();

        synchronized (lock){
            try {
                lock.wait();
                return toReturn[0];

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void getNextID(final String user, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    AppDataEntity appDataEntity = database.appDataDao().get(user);
                    int nextId = appDataEntity.nextID;
                    appDataEntity.nextID++;

                    update(appDataEntity, null);

                    if(handler!=null) handler.obtainMessage(SUCCESS,nextId).sendToTarget();
                }catch (Exception e){
                    if(handler!=null) handler.obtainMessage(FAIL).sendToTarget();
                }
            }
        }).start();
    }

    public int getNextID(final String user){
        final Object lock = new Object();
        final int[] toReturn = new int[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    try {
                        AppDataEntity appDataEntity = database.appDataDao().get(user);
                        toReturn[0] = appDataEntity.nextID;
                        appDataEntity.nextID++;
                        update(appDataEntity, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        synchronized (lock){
            try {
                lock.wait();
                return toReturn[0];
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    public void update(final AppDataEntity appDataEntity, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    database.appDataDao().update(appDataEntity);
                    if(handler!=null) handler.obtainMessage(SUCCESS).sendToTarget();
                }catch (Exception e){
                    if(handler!=null) handler.obtainMessage(FAIL).sendToTarget();
                }
            }
        }).start();
    }
}
