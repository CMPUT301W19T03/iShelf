package ca.ualberta.ishelf.Models;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.CompletableFuture;

import ca.ualberta.ishelf.BookProfileActivity;
import ca.ualberta.ishelf.EditBookActivity;
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
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    future.complete(null);
                })
                .addOnFailureListener(error -> {
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                    future.complete(null);
                })
                .addOnProgressListener(taskSnapshot -> {
                    // stub
                });
        return future;
    }

    public void putImage(String path, ImageView imageView, Context context) {
        StorageReference ref = storageReference.child(path);

        GlideApp.with(context)
                .load(ref)
                .into(imageView);
    }

    public CompletableFuture<Void> deleteImage(String path, Context context) {
        final CompletableFuture<Void> future = new CompletableFuture<>();

        StorageReference deleteRef = storageReference.child(path);

        deleteRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    future.complete(null);
                }).addOnFailureListener(exception -> {
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                    future.complete(null);
                });
        return future;
    }
}
