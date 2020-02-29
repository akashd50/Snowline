package com.greymatter.snowline.Data.database;

import android.database.Cursor;
import android.util.Log;

import com.greymatter.snowline.Data.entities.StopEntity;
import com.greymatter.snowline.Handlers.OnActionListener;

import java.util.ArrayList;
import java.util.List;

public class StopDBHelper {
    private LocalDatabase database;
    public StopDBHelper(LocalDatabase dbref){
        database = dbref;
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

    public boolean exists(final String toFind){
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

    public void getSimilar(final String toFind, final OnActionListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v("StopDBHelper#getSimilar()", "Running");
                listener.onAction(database.stopDao().getSimilar(toFind));
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
                    toReturn.addAll(database.stopDao().getAll());
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
