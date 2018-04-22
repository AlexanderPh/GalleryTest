package com.dev.alexanderf.gallery.api;

import com.dev.alexanderf.gallery.model.GalleryItem;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by AF.
 */
public class PublicListResponse {

    @SerializedName("items")
    private ArrayList<GalleryItem> data;
    private String public_key;
    private String sort;


    public ArrayList<GalleryItem> getData() {
        return data;
    }
}
