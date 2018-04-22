package com.dev.alexanderf.gallery.ui.image_viewer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dev.alexanderf.gallery.R;
import com.dev.alexanderf.gallery.model.GalleryItem;
import com.dev.alexanderf.gallery.utils.BitmapUtils;

/**
 * Created by AF.
 */
public class FullImageActivity extends AppCompatActivity implements View.OnClickListener, RequestListener<Drawable> {

    public static final String PARCELABLE_ITEM_KEY = "gallery_item";
    private static final int REQUEST_CODE = 101;


    private GestureImageView fullImage;
    private ProgressBar progressBar;
    private View toolbar;
    private View buttonSave;
    private View buttonBack;

    private GalleryItem galleryItem;
    private Drawable drawable = null;


    public static Intent createIntent(Context context, GalleryItem galleryItem) {
        Intent intent = new Intent(context, FullImageActivity.class);
        intent.putExtra(PARCELABLE_ITEM_KEY, galleryItem);
        return  intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getExtras() != null){
            if (getIntent().getExtras().containsKey(PARCELABLE_ITEM_KEY)){
                galleryItem = getIntent().getExtras().getParcelable(PARCELABLE_ITEM_KEY);
            }
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_full_image);

        initUI();
    }



    private void initUI() {
        fullImage = findViewById(R.id.fullImage);
        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.fireFullImageProgress);

        buttonSave = findViewById(R.id.button_save);
        buttonBack = findViewById(R.id.button_back);

        fullImage.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .skipMemoryCache(true);

        Glide.with(this)
                .load(galleryItem.getFile())
                .apply(options)
                .listener(this)
                .into(fullImage);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_save:
                onSaveButtonClick();
                break;
            case R.id.button_back:
                onBackPressed();
                break;
            case R.id.fullImage:
                showHideToolbar();
                break;
            default:
                break;
        }

    }

    private void showHideToolbar() {
        if (toolbar.getVisibility() == View.VISIBLE) {
            toolbar.setVisibility(View.GONE);
        } else {
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    private void onSaveButtonClick() {
        if(drawable != null) {
            if(checkWriteExternalPermission()) {
                saveDrawableToGallery();
            }else{
                requestWriteExternalPermission();
            }
        }
    }

    private void saveDrawableToGallery() {
        Bitmap bitmap = BitmapUtils.drawableToBitmap(drawable);
        BitmapUtils.saveBitmapToGallery(bitmap);
    //    Toast.makeText(this, R.string.title_image_saved, Toast.LENGTH_SHORT).show();
    }

    private boolean checkWriteExternalPermission()
    {
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void requestWriteExternalPermission(){
        String[] permission = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permission, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                saveDrawableToGallery();
            }
        }
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        if (!isFinishing()) {
            Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
        if (e != null) {
          e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        if (!isFinishing()) {
            progressBar.setVisibility(View.GONE);
            fullImage.setImageDrawable(resource);
            buttonSave.setVisibility(View.VISIBLE);
            drawable = resource;
        }
        return false;
    }
}
