package com.dev.alexanderf.gallery.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AF.
 */
public class GalleryItem implements Parcelable {

    private String name;
    private String preview;
    private String file;
    private String created;
    private String modified;
    private String path;
    private String md5;
    private String type;
    private String mime_type;
    private String media_type;
    private long size;


    public GalleryItem() {

    }

    public GalleryItem(String name, String preview, String file, String created, String modified, String path, String md5, String type, String mime_type, String media_type, long size) {
        this.name = name;
        this.preview = preview;
        this.file = file;
        this.created = created;
        this.modified = modified;
        this.path = path;
        this.md5 = md5;
        this.type = type;
        this.mime_type = mime_type;
        this.media_type = media_type;
        this.size = size;
    }

    private GalleryItem(Parcel in) {
        name = in.readString();
        preview = in.readString();
        file = in.readString();
        created = in.readString();
        modified = in.readString();
        path = in.readString();
        md5 = in.readString();
        type = in.readString();
        mime_type = in.readString();
        media_type = in.readString();
        size = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(preview);
        dest.writeString(file);
        dest.writeString(created);
        dest.writeString(modified);
        dest.writeString(path);
        dest.writeString(md5);
        dest.writeString(type);
        dest.writeString(mime_type);
        dest.writeString(media_type);
        dest.writeLong(size);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GalleryItem> CREATOR = new Creator<GalleryItem>() {
        @Override
        public GalleryItem createFromParcel(Parcel in) {
            return new GalleryItem(in);
        }

        @Override
        public GalleryItem[] newArray(int size) {
            return new GalleryItem[size];
        }
    };


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public static Creator<GalleryItem> getCREATOR() {
        return CREATOR;
    }

}

