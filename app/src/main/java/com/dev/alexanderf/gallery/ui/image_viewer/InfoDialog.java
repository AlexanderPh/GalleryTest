package com.dev.alexanderf.gallery.ui.image_viewer;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dev.alexanderf.gallery.R;
import com.dev.alexanderf.gallery.model.GalleryItem;

/**
 * Created by AF.
 */
public class InfoDialog extends DialogFragment implements View.OnClickListener {

    private GalleryItem item;
    private Button closeButton;
    private TextView name;
    private TextView size;
    private TextView created;


    public static InfoDialog create (GalleryItem item){
        InfoDialog dialog = new InfoDialog();
        Bundle arguments = new Bundle();
        arguments.putParcelable(FullImageActivity.PARCELABLE_ITEM_KEY, item);
        dialog.setArguments(arguments);
        return dialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(FullImageActivity.PARCELABLE_ITEM_KEY)){
            item = getArguments().getParcelable(FullImageActivity.PARCELABLE_ITEM_KEY);
        } else {
            this.dismiss();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_info, container, false);
        initUI(view);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void initUI(View view) {
        closeButton = view.findViewById(R.id.close);
        closeButton.setOnClickListener(this);

        name = view.findViewById(R.id.name);
        size = view.findViewById(R.id.size);
        created = view.findViewById(R.id.created);

        if (item != null){
            setUpUi();
        }
    }

    private void setUpUi() {
        if (item.getName() != null){
            name.setText(item.getName());
        }
            size.setText(Long.toString(item.getSize()/1024)+ "Kb");

        if (item.getCreated() != null){
            created.setText(item.getCreated().substring(0,10));
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.close:
                this.dismiss();
                break;
        }
    }
}
