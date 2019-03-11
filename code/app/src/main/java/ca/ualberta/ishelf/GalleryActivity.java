package ca.ualberta.ishelf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {


    private int colomn_number = 2;
    private final int PICK_PHOTO_FOR_AVATAR = 36;
    private final int DELETE_IMAGE = 37;
    private ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();

    private RecyclerView galleryRecyclerView;
    private GalleryAdapter galleryAdapter;
    private RecyclerView.LayoutManager galleryLayoutManager;

    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // create recycler for images
        galleryRecyclerView = (RecyclerView) findViewById((R.id.gallery_recycler));
        galleryLayoutManager = new GridLayoutManager(this, colomn_number);
        galleryRecyclerView.setLayoutManager(galleryLayoutManager);
        galleryAdapter = new GalleryAdapter(this, imageList);
        galleryRecyclerView.setAdapter(galleryAdapter);

        // set up add button
        addButton = (Button) findViewById((R.id.add_image_button));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

    }


    // https://stackoverflow.com/questions/35028251/android-how-to-select-an-image-from-a-file-manager-after-clicking-on-a-button
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                Bitmap addedImage = BitmapFactory.decodeStream(inputStream);
                addedImage = Bitmap.createScaledBitmap(addedImage, 320, 320, true);
                imageList.add(addedImage);
                galleryAdapter.notifyDataSetChanged();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == DELETE_IMAGE && resultCode == Activity.RESULT_OK) {
            int position = data.getIntExtra("position", -1 );
            imageList.remove(position);
            galleryAdapter.notifyItemRemoved(position);
            galleryAdapter.notifyItemRangeChanged(position, imageList.size());
        }
    }

}

