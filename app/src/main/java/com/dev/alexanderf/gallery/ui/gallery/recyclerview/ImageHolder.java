package com.dev.alexanderf.gallery.ui.gallery.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dev.alexanderf.gallery.R;

/**
 * Created by AF.
 */
class ImageHolder extends RecyclerView.ViewHolder {
    public final SquareImageView imageView;
    public final View mainLayout;

    ImageHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.item_image);
        mainLayout = itemView.findViewById(R.id.item_layout);
    }
}
