package com.danny.projectt.utils;

import android.os.Handler;
import android.os.HandlerThread;

public class WorkerThread extends HandlerThread {

    public Handler handler;

    public WorkerThread(String name) {

        super(name);
    }

    public Handler getHandler() {

        return handler;
    }

    public void postTask(Runnable task) {

        handler.post(task);
    }

    public void prepareHandler() {

        handler = new Handler(getLooper());

    }
}
