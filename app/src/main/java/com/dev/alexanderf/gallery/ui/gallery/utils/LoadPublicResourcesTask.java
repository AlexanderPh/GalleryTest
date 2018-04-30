package com.dev.alexanderf.gallery.ui.gallery.utils;

import android.os.AsyncTask;

import com.dev.alexanderf.gallery.api.PublicListResponse;
import com.dev.alexanderf.gallery.api.YandexAPI;
import com.dev.alexanderf.gallery.api.YandexResponse;
import com.dev.alexanderf.gallery.model.GalleryItem;
import com.dev.alexanderf.gallery.ui.gallery.GalleryActivity;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by AF.
 */
class LoadPublicResourcesTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>> {

    private WeakReference<GalleryActivity> reference;
    private Integer offset = null;
    private Integer loadLimit;

    public LoadPublicResourcesTask(GalleryActivity activity, int offset, int loadLimit) {
        this.reference = new WeakReference<>(activity);
        this.loadLimit = loadLimit;
        if (offset != 0){
            this.offset = offset;
        }

    }

    @Override
    protected ArrayList<GalleryItem> doInBackground(Void... voids) {
        YandexAPI.API api = YandexAPI.getInstanceWithCache();
        Response<YandexResponse<PublicListResponse>> response = null;

        try {
            response = api.getItemList(YandexAPI.PUBLIC_KEY_URL, offset, loadLimit).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isCancelled()){
            return  null;
        }

        if (response != null && response.isSuccessful()){
            YandexResponse<PublicListResponse> yandexResponse = response.body();
            if (yandexResponse != null && yandexResponse.getResponse() != null && yandexResponse.getResponse().getData() != null){
                return yandexResponse.getResponse().getData();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<GalleryItem> galleryItems) {
        super.onPostExecute(galleryItems);
        if (reference.get() != null){
            reference.get().loadItems(galleryItems);
        }
    }
}
