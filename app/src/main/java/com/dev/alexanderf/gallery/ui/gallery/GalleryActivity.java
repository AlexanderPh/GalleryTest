package com.dev.alexanderf.gallery.ui.gallery;

import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.dev.alexanderf.gallery.R;
import com.dev.alexanderf.gallery.model.GalleryItem;
import com.dev.alexanderf.gallery.ui.gallery.recyclerview.GalleryAdapter;
import com.dev.alexanderf.gallery.ui.gallery.utils.LoadHelper;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String RECYCLERVIEW_STATE_KEY = "recyclerview_state";
    private Parcelable recycleViewState;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private GalleryAdapter galleryAdapter;
    private GridLayoutManager gridLayoutManager;
    private FrameLayout errorview;

    private LoadHelper loadHelper;
    private boolean isLoading = false;
    private boolean fromBundle = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initUI();
        loadHelper = LoadHelper.getInstance();

        if (savedInstanceState != null && savedInstanceState.containsKey(RECYCLERVIEW_STATE_KEY)){
            recycleViewState = savedInstanceState.getParcelable(RECYCLERVIEW_STATE_KEY);
        }
        if (savedInstanceState != null){
            galleryAdapter.onRestoreInstanceState(savedInstanceState);
            fromBundle = true;
        }

        if (!fromBundle) {
            loadHelper.setPage(0);
            loadHelper.loadItems(this);
            isLoading = true;
        } else {
            showGalleryLayout();
        }
    }

    private void startLoad() {
        if (loadHelper.isEndIsReached()){
            return;
        }
        isLoading = true;
        galleryAdapter.setIsLoading();
        recyclerView.scrollToPosition(galleryAdapter.getItemCount() - 1);
        loadHelper.loadItems(this);
    }

    private void initUI() {
        recyclerView = findViewById(R.id.recyclerview_gallery);
        progressBar = findViewById(R.id.progress_bar);
        errorview = findViewById(R.id.error_view);
        Button tryAgainButton = findViewById(R.id.try_again);
        tryAgainButton.setOnClickListener(this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(this, 5);
        }else {
            gridLayoutManager = new GridLayoutManager(this, 3);
        }

        galleryAdapter = new GalleryAdapter(this);
        recyclerView.setAdapter(galleryAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        ViewPreloadSizeProvider viewPreloadSizeProvider = new ViewPreloadSizeProvider();
        RecyclerViewPreloader recyclerViewPreloader = new RecyclerViewPreloader(Glide.with(this), galleryAdapter, viewPreloadSizeProvider, 20);
        recyclerView.addOnScrollListener(recyclerViewPreloader);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return galleryAdapter.isHeader(position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isFinishing() || isLoading) {
                    return;
                }
                int firstVisiblePosition;
                if (dy > 0) {
                    firstVisiblePosition = gridLayoutManager.findLastVisibleItemPosition();
                    int fullItemCount = galleryAdapter.getItemCount();
                    if ((firstVisiblePosition) >= fullItemCount - 1) {
                        startLoad();

                    }
                }
            }
        });
    }

    public void loadItems(ArrayList<GalleryItem> galleryItems) {
        if (galleryItems == null){
            showErrorView();
            return;
        }
        if (galleryAdapter.getItemCount() == 0){
            showGalleryLayout();
        }
        galleryAdapter.setLoadingIsFinished();
        isLoading = false;
        galleryAdapter.addItems(galleryItems);
        if (galleryItems.size() < LoadHelper.LOAD_LIMIT){
            loadHelper.setEndIsReached();
        } else {
            loadHelper.upPage();
        }
        if (recycleViewState != null){
            recyclerView.getLayoutManager().onRestoreInstanceState(recycleViewState);
            recycleViewState = null;
        }
    }

    private void showErrorView() {
        errorview.setVisibility(View.VISIBLE);
    }

    private void showGalleryLayout() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if(layoutManager != null) {
                outState.putParcelable(RECYCLERVIEW_STATE_KEY, layoutManager.onSaveInstanceState());
            }
        }
        if (galleryAdapter != null){
            galleryAdapter.onSaveInstanceState(outState);
        }

    }

    @Override
    protected void onDestroy() {
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
            recycleViewState = null;
        }
        galleryAdapter = null;

        if (loadHelper != null){
            loadHelper.onDestroy();
            loadHelper = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.try_again:
                reload();
                break;
        }
    }

    private void reload() {
        if (loadHelper != null){
            errorview.setVisibility(View.GONE);
            loadHelper.loadItems(this);
        }
    }
}
