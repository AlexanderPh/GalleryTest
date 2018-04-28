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
    private static final String BUNDLE_KEY_PAGE = "offset_key";
    private static final String BUNDLE_KEY_END = "end_key";
    public static final int LOAD_LIMIT  = 15;

    private static LoadHelper instance = null;

    private LoadExecutor executor;
    private LoadPublicResourcesTask task;

    private int page = 0;
    private boolean endIsReached = false;
    private boolean onRestoreLoad = false;

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

        if (onRestoreLoad) {
            task = new LoadPublicResourcesTask(activity, 0, page * LOAD_LIMIT);
            task.executeOnExecutor(executor);
            onRestoreLoad = false;
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

    public void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putInt(BUNDLE_KEY_PAGE, page);
            outState.putBoolean(BUNDLE_KEY_END, endIsReached);
        }
    }


    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(BUNDLE_KEY_PAGE)){
                page = savedInstanceState.getInt(BUNDLE_KEY_PAGE);
                onRestoreLoad = true;
            }

            if (savedInstanceState.containsKey(BUNDLE_KEY_END)){
                endIsReached = savedInstanceState.getBoolean(BUNDLE_KEY_END);
            }
        }
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
        endIsReached = false;
    }

}
