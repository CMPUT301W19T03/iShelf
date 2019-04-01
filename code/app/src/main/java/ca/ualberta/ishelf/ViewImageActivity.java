package ca.ualberta.ishelf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ortiz.touchview.TouchImageView;

import java.util.concurrent.CompletableFuture;

import ca.ualberta.ishelf.Models.Storage;


/**
 * VIewImageActivity
 *
 * Shows an expanded view of the image
 * Allows for ther user to delete the image
 *
 * Implemented with TouchImageView for allow for dynamic image manipulation
 *
 * @author : Faisal
 */
public class ViewImageActivity extends AppCompatActivity {

    private Storage storage;
    private int position;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        storage = new Storage();

        Button deleteImageButton = findViewById(R.id.delete_image_button);
        Button setAsCoverButton = findViewById(R.id.set_as_cover_button);

        ImageView fullImage = (TouchImageView) findViewById(R.id.full_image);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            path = extras.getString("sent_image");
        }

        Boolean isOwner = extras.getBoolean("is_owner");

        if (!isOwner) {
            deleteImageButton.setVisibility(View.INVISIBLE);
            setAsCoverButton.setVisibility(View.INVISIBLE);
        }
        position = extras.getInt("position");

        storage.putImage(path, fullImage, ViewImageActivity.this);
    }

    public void deleteImageButton(View v){
        Bundle extras = new Bundle();
        extras.putInt("position", position);
        extras.putString("action", "delete");
        CompletableFuture<Void> future = storage.deleteImage(path, ViewImageActivity.this);
        Runnable runnable = () -> {
            Intent intent = new Intent(ViewImageActivity.this, GalleryActivity.class);
            intent.putExtras(extras);
            setResult(RESULT_OK, intent);
            finish();
        };
        future.thenRun(runnable);
    }

    public void setAsCoverButton(View v) {
        Bundle extras = new Bundle();
        extras.putInt("position", position);
        extras.putString("action", "setCover");
        Intent intent = new Intent(ViewImageActivity.this, GalleryActivity.class);
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        finish();
    }
}
