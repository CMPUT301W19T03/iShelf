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

import java.net.URI;
import java.util.ArrayList;
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

    // int
    private final int PICK_IMAGE_FOR_GALLERYY = 36;
    private final int DELETE_IMAGE = 37;
    private int colomn_number = 2;


    private ArrayList<String> imageList = new ArrayList<String>();

    private Book book;

    private RecyclerView galleryRecyclerView;
    private GalleryAdapter galleryAdapter;
    private RecyclerView.LayoutManager galleryLayoutManager;

    private Button addButton;
    private Button doneButton;

    private String check;

    private Storage storage;

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




        // set up add button

        addButton = (Button) findViewById((R.id.add_image_button));
        // get ID for book
        Bundle extras = getIntent().getExtras();
        check = extras.getString("check");
        if (check.equals("has_book")) {
            book = extras.getParcelable("sent_book");
            imageList = book.getGalleryImages();
            String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
            Boolean isOwner = (currentUsername.equals(book.getOwner()));
            if(!isOwner) {
                addButton.setVisibility(View.INVISIBLE);
            }
        }
        else {
            imageList = extras.getStringArrayList("sent_list");
        }

        // create recycler for images
        galleryRecyclerView = (RecyclerView) findViewById((R.id.gallery_recycler));
        galleryLayoutManager = new GridLayoutManager(this, colomn_number);
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
        if (requestCode == PICK_IMAGE_FOR_GALLERYY && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri lastImagePath = data.getData();
            String pathImage = "images1/" + UUID.randomUUID().toString();

            // add path to Book in FireBase
            imageList.add(pathImage);
            if (book != null) {
                Database db = new Database(this);
                db.connect(this);
                db.editBook(book);
            }

            CompletableFuture<Void> future = storage.addImage(pathImage, lastImagePath, GalleryActivity.this);
            Runnable runnable = () -> galleryAdapter.notifyDataSetChanged();
            future.thenRun(runnable);
        }


        if (requestCode == DELETE_IMAGE && resultCode == Activity.RESULT_OK) {
            int position = data.getIntExtra("position", -1 );
            imageList.remove(position);
            if (book != null) {
                Database db = new Database(this);
                db.connect(this);
                db.editBook(book);
            }
            galleryAdapter.notifyItemRemoved(position);
            galleryAdapter.notifyItemRangeChanged(position, imageList.size());
        }
    }

    public void updateImages(){
        galleryAdapter.updateList(imageList);
        galleryAdapter.notifyDataSetChanged();
    }

    public void finishGalleryButton(View v){
        if (!check.equals("has_book")) {
            Bundle extras = new Bundle();
            extras.putStringArrayList("pathList", imageList);
            Intent intent = new Intent(GalleryActivity.this, EditBookActivity.class);
            intent.putExtras(extras);
            setResult(RESULT_OK, intent);
            finish();
        }
        else {
            finish();
        }
    }

    public void addImageButton() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_FOR_GALLERYY);
    }

}

