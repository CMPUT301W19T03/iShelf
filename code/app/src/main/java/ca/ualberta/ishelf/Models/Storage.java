package ca.ualberta.ishelf.Models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.v4.widget.CircularProgressDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.concurrent.CompletableFuture;
import ca.ualberta.ishelf.GlideApp;


public class Storage {
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public Storage() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public CompletableFuture<Void> addImage(String path, Uri image, Context context) {
        final CompletableFuture<Void> future = new CompletableFuture<>();

        StorageReference ref = storageReference.child(path);
        ref.putFile(image)
                .addOnSuccessListener(taskSnapshot -> {
                    upToast("Success", context);
                    future.complete(null);
                })
                .addOnFailureListener(error -> {
                    upToast("Failure", context);
                    future.complete(null);
                })
                .addOnProgressListener(taskSnapshot -> {
                    // stub
                });
        return future;
    }

    public void putImage(String path, ImageView imageView, Context context) {
        // https://stackoverflow.com/questions/35305875/progress-bar-while-loading-image-using-glide
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        StorageReference ref = storageReference.child(path);

        GlideApp.with(context)
                .load(ref)
                .placeholder(circularProgressDrawable)
                .into(imageView);
    }

    public CompletableFuture<Void> deleteImage(String path, Context context) {
        final CompletableFuture<Void> future = new CompletableFuture<>();

        StorageReference deleteRef = storageReference.child(path);

        deleteRef.delete()
                .addOnSuccessListener(aVoid -> {
                    upToast("Success", context);
                    future.complete(null);
                }).addOnFailureListener(exception -> {
                    upToast("Failure", context);
                    future.complete(null);
        });
        return future;
    }

    public void upToast(String string, Context context) {
        @SuppressLint("ShowToast")
        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 64);
        toast.show();
    }

}
