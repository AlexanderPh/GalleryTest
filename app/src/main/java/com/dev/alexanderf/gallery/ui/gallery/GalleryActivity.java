package com.dev.alexanderf.gallery.ui.gallery;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dev.alexanderf.gallery.R;
import com.dev.alexanderf.gallery.model.GalleryItem;
import com.dev.alexanderf.gallery.ui.gallery.recyclerview.GalleryAdapter;
import com.dev.alexanderf.gallery.ui.gallery.utils.LoadHelper;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    private static final String RECYCLERVIEW_STATE_KEY = "recyclerview_state";
    private Parcelable recycleViewState;

    private RecyclerView recyclerView;
    private GalleryAdapter galleryAdapter;
    private GridLayoutManager gridLayoutManager;

    private LoadHelper loadHelper;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initUI();

        loadHelper = LoadHelper.getInstance();
        loadHelper.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(RECYCLERVIEW_STATE_KEY)){
            recycleViewState = savedInstanceState.getParcelable(RECYCLERVIEW_STATE_KEY);
        }

        loadHelper.loadItems(this);


    }

    private void initUI() {
        recyclerView = findViewById(R.id.recyclerview_gallery);
        gridLayoutManager = new GridLayoutManager(this, 3);
        galleryAdapter = new GalleryAdapter(this);
        recyclerView.setAdapter(galleryAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isFinishing()) {
                    return;
                }
                int firstVisiblePosition;
                if (dy > 0) {
                    firstVisiblePosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                    int fullItemCount = galleryAdapter.getItemCount();
                    if ((firstVisiblePosition) >= fullItemCount - 1) {
                        loadHelper.loadItems(GalleryActivity.this);
                    }
                }
            }
        });

    }

    public void loadItems(ArrayList<GalleryItem> galleryItems) {
        if (galleryItems != null){
            galleryAdapter.addItems(galleryItems);
            if (galleryItems.size() < LoadHelper.LOAD_LIMIT){
                loadHelper.setEndIsReached();
            }
            if (recycleViewState != null){
                recyclerView.getLayoutManager().onRestoreInstanceState(recycleViewState);
                recycleViewState = null;
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (loadHelper != null){
            loadHelper.onSaveInstanceState(outState);
        }
        if(recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if(layoutManager != null) {
                outState.putParcelable(RECYCLERVIEW_STATE_KEY, layoutManager.onSaveInstanceState());
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (loadHelper != null){
            loadHelper.onDestroy();
            loadHelper = null;
        }

        if (recyclerView != null) {
            recyclerView.setAdapter(null);
            recycleViewState = null;
        }
        galleryAdapter = null;
        super.onDestroy();
    }
}
