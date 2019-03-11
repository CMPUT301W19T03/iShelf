package ca.ualberta.ishelf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Bitmap> originalList = new ArrayList<Bitmap>();
    private Context galleryContext;
    private final int DELETE_IMAGE = 37;


    public GalleryAdapter(Context galleryContext, ArrayList<Bitmap> originalList) {
        this.originalList = originalList;
        this.galleryContext = galleryContext;
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

        imageHolder.image.setImageBitmap(originalList.get(position));
        imageHolder.image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                // convert imageView to a bitMap
                Bitmap bitMap = ((BitmapDrawable)imageHolder.image.getDrawable()).getBitmap();

                // send the bitmap to ViewImageActivity
                Bundle extras = new Bundle();
                extras.putParcelable("sent_image", bitMap);
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

//    //https://stackoverflow.com/questions/36513854/how-to-get-a-bitmap-from-vectordrawable
//    public Bitmap convertToBitMap(VectorDrawable drawable) {
//        try {
//            Bitmap bitmap;
//
//            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//
//            Canvas canvas = new Canvas(bitmap);
//            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//            drawable.draw(canvas);
//
//            return bitmap;
//        } catch (OutOfMemoryError e) {
//            // Handle the error
//            return null;
//        }
//    }

    public void updateList(ArrayList<Bitmap> list){
        originalList = list;
    }
}