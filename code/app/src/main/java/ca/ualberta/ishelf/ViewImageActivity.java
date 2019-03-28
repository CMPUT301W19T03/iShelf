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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


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
    private int position;

    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        fullImage = (ImageView) findViewById(R.id.full_image);
        Bundle extras = getIntent().getExtras();
        String URL = extras.getString("sent_image");
        position = extras.getInt("position");

        StorageReference ref = storageReference.child(URL);

        final long ONE_MEGABYTE = 1024 * 1024;
        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                fullImage.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        // set up add button
        deleteButton = (Button) findViewById((R.id.delete_image_button));

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                extras.putInt("position", position);
                StorageReference deleteRef = storageReference.child(URL);
                // Delete the file
                deleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                    }
                });

                Intent intent = new Intent(view.getContext(), GalleryActivity.class);
                intent.putExtras(extras);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
