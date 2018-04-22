package com.dev.alexanderf.gallery.ui.gallery.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dev.alexanderf.gallery.R;

/**
 * Created by AF.
 */
public class ImageHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public View mainLayout;


    public ImageHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.item_image);
        mainLayout = itemView.findViewById(R.id.item_layout);
    }


    public void setImage(String url, Context context){
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }
}
