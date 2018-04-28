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

/**
 * Created by AF.
 */
public class GalleryAdapter extends RecyclerView.Adapter {

    private Context context;
    private SortedList<GalleryItem> items;
    private static final int DEFAULT_ITEM = 1;
    private static final int LOADING_ITEM = 0;
    private GalleryItem loadingItem;

    private int loadingViewPosition;


    public GalleryAdapter(Context context) {
        this.context = context;
        this.loadingItem = new GalleryItem(GalleryItem.ItemType.loadingType);

        items = new SortedList<>(GalleryItem.class, new SortedList.Callback<GalleryItem>() {
            @Override
            public int compare(GalleryItem o1, GalleryItem o2) {
                return Integer.compare(o2.getItemType().ordinal(), o1.getItemType().ordinal());
            }

            @Override
            public void onChanged(int position, int count) {

            }

            @Override
            public boolean areContentsTheSame(GalleryItem oldItem, GalleryItem newItem) {
                items.remove(oldItem);
                return true;
            }

            @Override
            public boolean areItemsTheSame(GalleryItem item1, GalleryItem item2) {
                if (item1.getItemType() == GalleryItem.ItemType.loadingType){
                    items.remove(item2);
                    return false;
                }
                return false;
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
               notifyItemRemoved(position);
              //  notifyItemRangeRemoved(position, count);
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
        View view;
        if (viewType == DEFAULT_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.gallery_item_image, parent, false);
            return new ImageHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.gallery_item_load, parent, false);
            return new LoadHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (items.get(position).getItemType() == GalleryItem.ItemType.defaultType){
            final ImageHolder imageHolder = (ImageHolder) holder;
            imageHolder.setImage(items.get(holder.getAdapterPosition()).getFile(), context);
            imageHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(FullImageActivity.createIntent(context, items.get(imageHolder.getAdapterPosition())));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType() == GalleryItem.ItemType.loadingType ? LOADING_ITEM : DEFAULT_ITEM;
    }

    public void addItems(ArrayList<GalleryItem> newItems){
        if (newItems != null){
            items.beginBatchedUpdates();
            items.addAll(newItems);
            items.endBatchedUpdates();
        }
    }

    public boolean isHeader(int position) {
        return items.get(position).getItemType() == GalleryItem.ItemType.loadingType;
    }

    public void addLoadView() {

        if (items.size() == 0) {
            loadingViewPosition = items.size();
        } else {
            loadingViewPosition = items.size()-1;
        }
        items.beginBatchedUpdates();
        items.add(loadingItem);
        items.endBatchedUpdates();


    }

    public void removeLoadView(){
            items.beginBatchedUpdates();
            items.removeItemAt(loadingViewPosition);
            items.endBatchedUpdates();

    }

    public boolean isNullItem(int position) {

        return items.get(position) == null;
    }
}
