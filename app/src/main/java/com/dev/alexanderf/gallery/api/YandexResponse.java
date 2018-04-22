package com.dev.alexanderf.gallery.api;

import com.dev.alexanderf.gallery.model.ResourceList;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AF.
 */
public class YandexResponse<T>{

    @SerializedName("_embedded")
    private final T response;
    private final int views_count;
    private final String resource_id;
    private final String media_type;
    private final String file;
    private final String type;
    private final String mime_type;
    private final String public_url;
    private final String path;
    private final String preview;
    private final String name;



    public YandexResponse( T response, int views_count, String resource_id, String media_type, String file, String type, String mime_type, String public_url, String path, String preview, String name) {
        this.response = response;
        this.views_count = views_count;
        this.resource_id = resource_id;
        this.media_type = media_type;
        this.file = file;
        this.type = type;
        this.mime_type = mime_type;
        this.public_url = public_url;
        this.path = path;
        this.preview = preview;
        this.name = name;
    }


    public T getResponse(){
        return response;
    }
}
