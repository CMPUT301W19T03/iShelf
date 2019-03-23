package ca.ualberta.ishelf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

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


    private int colomn_number = 2;
    private final int PICK_IMAGE_FOR_GALLERYY = 36;
    private final int DELETE_IMAGE = 37;
    private ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();
    private Book passedBook;

    private RecyclerView galleryRecyclerView;
    private GalleryAdapter galleryAdapter;
    private RecyclerView.LayoutManager galleryLayoutManager;

    private Button addButton;

    /**
     * OnCreate
     *
     * Initializes recyclerView and button
     *
     * @author : Faisal
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // create recycler for images
        galleryRecyclerView = (RecyclerView) findViewById((R.id.gallery_recycler));
        galleryLayoutManager = new GridLayoutManager(this, colomn_number);
        galleryRecyclerView.setLayoutManager(galleryLayoutManager);
        galleryAdapter = new GalleryAdapter(this,imageList);
        galleryRecyclerView.setAdapter(galleryAdapter);

        Bundle extras = getIntent().getExtras();
        passedBook = extras.getParcelable("sent_book");
        getBook(passedBook.getId());


        // set up add button
        addButton = (Button) findViewById((R.id.add_image_button));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

    }


    /**
     * pickImage
     *
     * pick Image from Google folder
     *
     * Based on: Android: How to select an Image from a file manager after clicking on a Button?
     * https://stackoverflow.com/questions/35028251/android-how-to-select-an-image-from-a-file-manager-after-clicking-on-a-button
     * User: Ahsan Kamal
     *
     * And based on documentation:
     * https://developer.android.com/training/camera/photobasics
     * @author : Faisal (based on Ahsan Kamal)
     */
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_FOR_GALLERYY);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_FOR_GALLERYY && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                Bitmap addedImage = BitmapFactory.decodeStream(inputStream);
                // reduces the size of the Bitmap
                addedImage = Bitmap.createScaledBitmap(addedImage, 320, 320, true);
                imageList.add(addedImage);
                Database db = new Database(this);
                db.connect(this);
                passedBook.addImage(BitMapToString(addedImage));
                db.editBook(passedBook);
                galleryAdapter.notifyDataSetChanged();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == DELETE_IMAGE && resultCode == Activity.RESULT_OK) {
            int position = data.getIntExtra("position", -1 );
            Database db = new Database(this);
            db.connect(this);
            passedBook.removeImage(position);
            imageList.remove(position);
            db.editBook(passedBook);
            galleryAdapter.notifyItemRemoved(position);
            galleryAdapter.notifyItemRangeChanged(position, imageList.size());
        }
    }

    /**
     * getBook
     *
     * matches a book to get the list of images
     *
     * @author : Faisal, Abdul
     */
    public void getBook(UUID bookId){
        // Retrive the user's info from Firebase
        Database db = new Database(this);
        Firebase ref = db.connect(this);

        // get reference to specific entry
        Firebase tempRef = ref.child("Books").child(bookId.toString());
        //final ArrayList<User> userList = new ArrayList<User>();
        // create a one time use listener to immediately access datasnapshot
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jBook = dataSnapshot.getValue(String.class);
                Log.d("jUser", jBook);
                if (jBook != null) {
                    // Get user object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<Book>() {
                    }.getType();
                    Book book = gson.fromJson(jBook, tokenType);
                    gotBook(book);

                } else {
                    Log.d("FBerror1", "User doesn't exist or string is empty");
                }
                //Log.d("Size", String.valueOf(userList.size()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }

        });
    }

    /**
     * gotBook
     *
     * handles getting the list of images
     *
     * @author : Faisal
     */

    public void gotBook(Book book){
        ArrayList<String> stringList = book.getGalleryImages();
        for (String s : stringList){
            this.imageList.add(StringToBitMap(s));
        }
        this.passedBook = book;


        galleryAdapter.updateList(imageList);
        galleryAdapter.notifyDataSetChanged();

    }



    /**
     * BitMapToString
     *
     * How many ways to convert bitmap to string and vice-versa?
     * https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
     * User: sachin10
     *
     * Allows for conversion between Bitmap and String (Bitmap -> String)
     * @author : Faisal (copied from sachin10)
     */

    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /**
     * StringToBitMap
     *
     * How many ways to convert bitmap to string and vice-versa?
     * https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
     * User: sachin10
     *
     * Allows for conversion between Bitmap and String (String -> Bitmap)
     * @author : Faisal (copied from sachin10)
     */
    public static Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

}

