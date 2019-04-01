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
 * TODO_: Implement with TouchImageView for allow for dynamic image manipulation
 *
 * @author : Faisal
 */
public class ViewImageActivity extends AppCompatActivity {

    private ImageView fullImage;
    private Button deleteButton;
    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        storage = new Storage();

        fullImage = (TouchImageView) findViewById(R.id.full_image);
        Bundle extras = getIntent().getExtras();
        String path = extras.getString("sent_image");
        int position = extras.getInt("position");

        storage.putImage(path, fullImage, ViewImageActivity.this);

        // set up add button
        deleteButton = findViewById((R.id.delete_image_button));

        deleteButton.setOnClickListener(view -> {
            Bundle extras1 = new Bundle();
            extras1.putInt("position", position);
            CompletableFuture<Void> future = storage.deleteImage(path, ViewImageActivity.this);
            Runnable runnable = () -> {
                Intent intent = new Intent(view.getContext(), GalleryActivity.class);
                intent.putExtras(extras1);
                setResult(RESULT_OK, intent);
                finish();
            };
            future.thenRun(runnable);
        });

    }
}
