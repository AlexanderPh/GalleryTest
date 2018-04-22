package com.dev.alexanderf.gallery.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by AF.
 */
public class ResourceList {

    @SerializedName("items")
    private ArrayList<GalleryItem> data;
    private String public_key;
    private String sort;


    public ArrayList<GalleryItem> getData() {
        return data;
    }
}
