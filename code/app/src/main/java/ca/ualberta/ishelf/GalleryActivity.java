package ca.ualberta.ishelf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.firebase.client.annotations.NotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import ca.ualberta.ishelf.Models.Book;
import ca.ualberta.ishelf.Models.Database;
import ca.ualberta.ishelf.Models.Storage;
import ca.ualberta.ishelf.RecyclerAdapters.GalleryAdapter;

/**
 * GalleryActivity
 *
 * Allows a user to see the associated images of a book
 * Allows for you to see an expanded view of the image if yyou click on the image
 * Allows for oyui to add a new image
 *
 * @author : Faisal
 */

public class GalleryActivity extends AppCompatActivity {

    private final int PICK_IMAGE_FOR_GALLERY = 36;
    private final int EDIT_PHOTO = 37;

    private ArrayList<String> imageList = new ArrayList<String>();
    private Book book;

    private RecyclerView galleryRecyclerView;
    private GalleryAdapter galleryAdapter;
    private RecyclerView.LayoutManager galleryLayoutManager;

    private Storage storage;
    private boolean hasBook;

    /**
     * OnCreate
     * <p>
     * Initializes recyclerView and button
     *
     * @author : Faisal
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // set up FireBase Storage
        storage = new Storage();


        Button addButton = (Button) findViewById((R.id.add_image_button));
        Bundle extras = getIntent().getExtras();
        if (extras != null) { hasBook = extras.getBoolean("has_book"); }

        if (hasBook) {
            book = Objects.requireNonNull(extras).getParcelable("sent_book");
            imageList = Objects.requireNonNull(book).getGalleryImages();
            String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
            boolean isOwner = (currentUsername != null && currentUsername.equals(book.getOwner()));
            if (!isOwner) {addButton.setVisibility(View.INVISIBLE);}
        }
        else {
            imageList = Objects.requireNonNull(extras).getStringArrayList("sent_list");
        }

        // create recycler for images
        galleryRecyclerView = (RecyclerView) findViewById((R.id.gallery_recycler));
        galleryLayoutManager = new GridLayoutManager(this, 3);
        galleryRecyclerView.setLayoutManager(galleryLayoutManager);
        galleryAdapter = new GalleryAdapter(this, imageList);
        galleryRecyclerView.setAdapter(galleryAdapter);
    }

    /**
     * pickImage
     * <p>
     * pick Image from Google folder
     * <p>
     * Based on: Android: How to select an Image from a file manager after clicking on a Button?
     * https://stackoverflow.com/questions/35028251/android-how-to-select-an-image-from-a-file-manager-after-clicking-on-a-button
     * User: Ahsan Kamal
     * <p>
     * And based on documentation:
     * https://developer.android.com/training/camera/photobasics
     *
     * @author : Faisal (based on Ahsan Kamal)
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_FOR_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }

            // get Uri and path (for FireBase Storage)
            Uri lastImagePath = data.getData();
            String pathImage = "images1/" + UUID.randomUUID().toString();

            // add path to Book in FireBase
            imageList.add(pathImage);
            if (hasBook) {
                Database db = new Database(this);
                db.connect(this);
                db.editBook(book);
            }

            // add image to Storage
            CompletableFuture<Void> future = storage.addImage(pathImage, lastImagePath, GalleryActivity.this);
            Runnable runnable = () -> galleryAdapter.notifyDataSetChanged();
            future.thenRun(runnable);
        }

        if (requestCode == EDIT_PHOTO && resultCode == Activity.RESULT_OK) {
            String action = data.getStringExtra("action");
            int position = data.getIntExtra("position", -1);
            if (action.equals("delete")) {
                imageList.remove(position);
                galleryAdapter.notifyItemRemoved(position);
                galleryAdapter.notifyItemRangeChanged(position, imageList.size());
            }
            else if (action.equals("setCover")) {
                String coverString = imageList.get(position);
                imageList.remove(position);
                imageList.add(0, coverString);
                galleryAdapter.notifyDataSetChanged();
            }
            if (book != null) {
                Database db = new Database(this);
                db.connect(this);
                db.editBook(book);
            }

        }
    }

    public void finishGalleryButton(View v){
        if (!hasBook) {
            Bundle extras = new Bundle();
            extras.putStringArrayList("pathList", imageList);
            Intent intent = new Intent(GalleryActivity.this, EditBookActivity.class);
            intent.putExtras(extras);
            setResult(RESULT_OK, intent);
            finish();
        }
        else {
            Bundle extras = new Bundle();
            ArrayList<String> newList = new ArrayList<>(imageList);
            extras.putStringArrayList("back_list", newList);
            Intent intent = new Intent(GalleryActivity.this, BookProfileActivity.class);
            intent.putExtras(extras);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void addImageButton(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_FOR_GALLERY);
    }
}

