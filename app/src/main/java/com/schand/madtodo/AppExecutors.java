package com.schand.madtodo;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;

public class AppExecutors {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor dbIO;
    private final Executor mainThread;
    private final Executor uiIO;

    private AppExecutors(Executor dbIO, Executor uiIO, Executor mainThread) {
        this.dbIO = dbIO;
        this.uiIO = uiIO;
        this.mainThread = mainThread;
    }

    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor dbIO() {
        return dbIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor uiIO() {
        return uiIO;
    }


    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
