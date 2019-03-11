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

public class GalleryActivity extends AppCompatActivity {


    private int colomn_number = 2;
    private final int PICK_PHOTO_FOR_AVATAR = 36;
    private final int DELETE_IMAGE = 37;
    private ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();
    private Book passedBook;

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

    public void gotBook(Book book){
        ArrayList<String> stringList = book.getGalleryImages();
        for (String s : stringList){
            this.imageList.add(StringToBitMap(s));
        }
        this.passedBook = book;


        galleryAdapter.updateList(imageList);
        galleryAdapter.notifyDataSetChanged();

    }


    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

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

