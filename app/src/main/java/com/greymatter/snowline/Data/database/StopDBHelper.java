package com.greymatter.snowline.Data.database;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.greymatter.snowline.Data.entities.StopEntity;

import java.util.ArrayList;

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

    public void getSimilar(final String toFind, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v("StopDBHelper#getSimilar()", "Running");
                Message completeMessage = handler.obtainMessage(0, database.stopDao().getSimilar(toFind));
                completeMessage.sendToTarget();

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
