package com.forbitbd.myplayer;

import android.util.Log;

public class MyThread extends Thread {

    private boolean isrunning=false;
    private MinuteListener listener;

    public MyThread(MinuteListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        Log.d("TTTTTTT",Thread.currentThread().getName());

        while (isrunning){

            try {
                sleep(60000);
                listener.increment();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startThread(){
        isrunning= true;
        start();
    }

    public void stopThread(){
        isrunning = false;
    }


}
