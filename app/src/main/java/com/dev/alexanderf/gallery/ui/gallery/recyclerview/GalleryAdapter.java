package com.dev.alexanderf.gallery.ui.gallery.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

    private static final String BUNDLE_KEY_ITEMS = "items_key";

    private Context context;
    private ArrayList<GalleryItem> items;
    private static final int DEFAULT_ITEM = 1;
    private static final int LOADING_ITEM = 0;


    public GalleryAdapter(Context context) {
        this.context = context;
        this.items = new  ArrayList<>();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == LOADING_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.gallery_item_load, parent, false);
            return new LoadHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.gallery_item_image, parent, false);
            return new ImageHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (items.get(position) != null){
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
        return items.get(position) == null ? LOADING_ITEM : DEFAULT_ITEM;
    }

    public void addItems(ArrayList<GalleryItem> newItems){ // Добавление нового сета картинок после загузки по скроллу
        int itemRange = items.size();
        if (newItems != null){
            items.addAll(newItems);
            notifyItemRangeInserted(itemRange, newItems.size());
        }
    }

    public boolean isHeader(int position) {
        return items.get(position) == null;
    }

    public void setIsLoading(){ //Добавленеи view с прогрессбаром на время загрузки по скроллу
        items.add(null);
        try {
            notifyItemInserted(items.size() - 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void setLoadingIsFinished(){ //Удаление view с прогрессбаром после загрузки по скроллу
        if (!items.isEmpty()) {
            try {
                items.remove(items.size() - 1);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            notifyItemRemoved(items.size());
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        if (outState != null){
            outState.putParcelableArrayList(BUNDLE_KEY_ITEMS, items);
        }

    }
    public void onRestoreInstanceState(Bundle onSavedInstanceSave){
        if (onSavedInstanceSave != null && onSavedInstanceSave.containsKey(BUNDLE_KEY_ITEMS)){
            items.addAll(onSavedInstanceSave.<GalleryItem>getParcelableArrayList(BUNDLE_KEY_ITEMS));
        }
    }
}
