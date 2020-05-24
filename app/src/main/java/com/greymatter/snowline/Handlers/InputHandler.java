package com.greymatter.snowline.Handlers;

import android.os.Handler;
import android.util.Log;

import com.greymatter.snowline.app.Constants;

public class InputHandler {
    private long timeLastTextEntered, textActionThreshold;
    private Thread textCheckThread;
    private StringBuilder currentInput;
    private Handler actionHandler;
    private boolean exit;
    public InputHandler(long threshold, Handler handler) {
        this.textActionThreshold = threshold;
        this.actionHandler = handler;
        this.timeLastTextEntered = 0;
        this.currentInput = new StringBuilder();
        this.exit = false;
        this.initThread();
    }

    private void initThread() {
        textCheckThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!exit) {
                    synchronized (this) {
                        try {
                            wait(textActionThreshold);

                            if (timeLastTextEntered!=0 && System.currentTimeMillis() - timeLastTextEntered > textActionThreshold) {
                                Log.v("Running", "InputHandler, time greater");

                                actionHandler.obtainMessage(Constants.SUCCESS, currentInput.toString()).sendToTarget();

                                timeLastTextEntered = 0;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        textCheckThread.start();
    }

    public void update(String newText) {
       // synchronized (currentInput) {
            timeLastTextEntered = System.currentTimeMillis();
            currentInput.delete(0, currentInput.length());
            currentInput.append(newText);
            //currentInput.notifyAll();
      //  }
    }

    public void onExit() {
        exit = true;
    }
}
