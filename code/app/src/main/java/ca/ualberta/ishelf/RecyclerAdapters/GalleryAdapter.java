package ca.ualberta.ishelf.RecyclerAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import ca.ualberta.ishelf.GlideApp;
import ca.ualberta.ishelf.Models.Storage;
import ca.ualberta.ishelf.R;
import ca.ualberta.ishelf.ViewImageActivity;


/**
 * GalleryAdapter
 *
 * This is just a normal adapter, but for GalleryActivity (and associated recyclerView)
 *
 * @author : Faisal
 */
public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<String> originalList = new ArrayList<String>();
    private Context galleryContext;
    private final int DELETE_IMAGE = 37;
    private Storage storage;


    public GalleryAdapter(Context galleryContext, ArrayList<String> originalList) {
        this.originalList = originalList;
        this.galleryContext = galleryContext;
        storage = new Storage();
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public GalleryViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image_gallery);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_item, parent, false);
        GalleryViewHolder vh = new GalleryViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final GalleryViewHolder imageHolder = (GalleryViewHolder) holder;
        storage.putImage(originalList.get(position), imageHolder.image, galleryContext);

        imageHolder.image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                // convert imageView to a bitMap
                // send the bitmap to ViewImageActivity
                Bundle extras = new Bundle();
                extras.putString("sent_image", originalList.get(position));
                extras.putInt("position", position);
                Intent intent = new Intent((Activity) galleryContext, ViewImageActivity.class);
                intent.putExtras(extras);
                Activity galleryActivity = (Activity) galleryContext;
                galleryActivity.startActivityForResult(intent, DELETE_IMAGE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return originalList.size();
    }
}