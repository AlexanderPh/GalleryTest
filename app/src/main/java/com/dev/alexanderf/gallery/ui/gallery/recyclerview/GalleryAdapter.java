package com.dev.alexanderf.gallery.ui.gallery.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.alexanderf.gallery.R;
import com.dev.alexanderf.gallery.model.GalleryItem;
import com.dev.alexanderf.gallery.ui.image_viewer.FullImageActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.Inflater;

/**
 * Created by AF.
 */
public class GalleryAdapter extends RecyclerView.Adapter {

    private Context context;
    private SortedList<GalleryItem> items;

    public GalleryAdapter(Context context) {
        this.context = context;
        items = new SortedList<>(GalleryItem.class, new SortedList.Callback<GalleryItem>() {
            @Override
            public int compare(GalleryItem o1, GalleryItem o2) {
                return 0;
            }

            @Override
            public void onChanged(int position, int count) {

            }

            @Override
            public boolean areContentsTheSame(GalleryItem oldItem, GalleryItem newItem) {
                return false;
            }

            @Override
            public boolean areItemsTheSame(GalleryItem item1, GalleryItem item2) {
                return false;
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ImageHolder imageHolder = (ImageHolder)holder;
        imageHolder.setImage(items.get(holder.getAdapterPosition()).getFile(), context);
        imageHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(FullImageActivity.createIntent(context, items.get(imageHolder.getAdapterPosition())));
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(ArrayList<GalleryItem> newItems){
        if (newItems != null){
            items.beginBatchedUpdates();
            items.addAll(newItems);
            items.endBatchedUpdates();
        }
    }
}
