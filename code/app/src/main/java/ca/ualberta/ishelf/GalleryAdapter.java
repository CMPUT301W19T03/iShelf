package ca.ualberta.ishelf;

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


/**
 * GalleryAdapter
 *
 * This is just a normal adapter, but for GalleryActivity (and associated recyclerView)
 *
 * @author : Faisal
 */
class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<String> originalList = new ArrayList<String>();
    private Context galleryContext;
    private final int DELETE_IMAGE = 37;

    // FireBase stuff
    FirebaseStorage storage;
    StorageReference storageReference;


    public GalleryAdapter(Context galleryContext, ArrayList<String> originalList) {
        this.originalList = originalList;
        this.galleryContext = galleryContext;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
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

        String test = originalList.get(position);
        StorageReference ref = storageReference.child(originalList.get(position));

        final long ONE_MEGABYTE = 1024 * 1024;
        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageHolder.image.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

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

    public void updateList(ArrayList<String> list){
        originalList = list;
    }
}