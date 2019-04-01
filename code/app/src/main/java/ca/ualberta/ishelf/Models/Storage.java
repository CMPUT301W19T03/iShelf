package ca.ualberta.ishelf.Models;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.CompletableFuture;

import ca.ualberta.ishelf.EditBookActivity;

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
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                        future.complete(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception error) {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        future.completeExceptionally(error);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });
        return future;
    }
}
