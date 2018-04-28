package com.dev.alexanderf.gallery.ui.gallery.utils;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.dev.alexanderf.gallery.ui.gallery.GalleryActivity;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by AF.
 */
public class LoadHelper {

    public static final int LOAD_LIMIT  = 15;

    private static LoadHelper instance = null;

    private LoadExecutor executor;
    private LoadPublicResourcesTask task;

    private int page = 0;
    private boolean endIsReached = false;

    private LoadHelper() {
        executor = new LoadExecutor();

    }

    public static LoadHelper getInstance(){
        if (instance == null){
            instance = new LoadHelper();
        }
        return instance;
    }

    public void loadItems(GalleryActivity activity){
        if (activity == null || activity.isFinishing()){
            return;
        }
        if (task != null){
            task.cancel(true);
        }
        if (!endIsReached){
            int offset = page * LOAD_LIMIT;
            task = new LoadPublicResourcesTask(activity, offset, LOAD_LIMIT);
            task.executeOnExecutor(executor);
            page++;
        } else {
            Toast.makeText(activity, "End is reached", Toast.LENGTH_SHORT).show();
        }


    }

    public void setEndIsReached() {
        endIsReached = true;
    }

    public boolean isEndIsReached() {
        return endIsReached;
    }



    private static class LoadExecutor extends ThreadPoolExecutor {
        private LoadExecutor() {
            super(1, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    return new Thread(r, "Load Executor");
                }
            });
            allowCoreThreadTimeOut(true);
        }
    }


    public void onDestroy(){
        if (task != null){
            task.cancel(true);
        }
        task = null;
    }

}
