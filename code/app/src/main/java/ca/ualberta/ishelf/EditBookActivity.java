package ca.ualberta.ishelf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import ca.ualberta.ishelf.Models.Book;
import ca.ualberta.ishelf.Models.Database;
import ca.ualberta.ishelf.Models.User;
import ca.ualberta.ishelf.RecyclerAdapters.MyAdapter;
import ca.ualberta.ishelf.RecyclerAdapters.myBooksFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * EditBookActivity
 * Send in either:
 *                  key: "Book" - a Book object
 *
 * US 01.01.01
 * As an owner, I want to add a book in my books, each denoted with a clear, suitable description (at least title, author, and ISBN).
 * -the owner wants to be able to lend new books
 *
 * This activity allows the owner to add new books to his book list
 *
 * US 01.06.01
 * s an owner, I want to view and edit a book description in my books.
 *   if he enters it in by mistake or the book description is updated by the author
 *
 * this  also allows the user to
 * edit the book info
 *
 *
 * can only edit the book while it is in your possession - Evan
 *
 * @author : Mehrab
 */


public class EditBookActivity extends AppCompatActivity {

    private EditText TitleText;
    private EditText AuthorText;
    private EditText ISBNText;
    private EditText YearText;
    private EditText GenreText;
    private EditText DescriptionText;
    private ImageView CoverImage;

    private Button AddCover;
    private Button AddOther;

    private ArrayList<Book> Booklist = new ArrayList<Book>();
    private static final String FILENAME = "book1.sav";
    private Book passedBook = null;

    private final int SCAN_AND_GET_DESCRIPTION = 212;
    private final int GET_OTHER_BOOKS = 277;
    private final int PICK_IMAGE_FOR_GALLERYY = 36;


    private final OkHttpClient client = new OkHttpClient();

    private String URLcover = "";
    private ArrayList<String> galleryImageURLS = new ArrayList<String>();
    private int indexCover = -1;
    private boolean computation_done = false;
    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        //if its an edit book then true otherwise false
        Intent intent = getIntent();
        Boolean check = intent.getBooleanExtra("Check Data", false );
        TitleText = (EditText) findViewById(R.id.editTitle);
        AuthorText = (EditText) findViewById(R.id.editAuthor);
        ISBNText = (EditText) findViewById(R.id.editISBN);
        YearText = (EditText) findViewById(R.id.editYear);
        GenreText= (EditText) findViewById(R.id.editGenre);
        DescriptionText = (EditText) findViewById(R.id.editDes);
        CoverImage = (ImageView) findViewById(R.id.cover_image);
        AddCover = (Button) findViewById(R.id.add_cover_button);
        AddOther = (Button) findViewById(R.id.add_other_images_button);

        ISBNText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String isbn = s.toString();
                if (!isbn.equals("")) {
                    computation_done = true;
                    saveButton.setBackground(getDrawable(R.drawable.gradientbutton));
                }
                else {
                    computation_done = false;
                    saveButton.setBackground(getDrawable(R.drawable.roundedbuttongray));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        Button clearButton = (Button) findViewById(R.id.cancel);
        saveButton = (Button) findViewById(R.id.save);
        Button scanButton = (Button) findViewById(R.id.scan);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // convert imageView to a bitMap

                Intent intent = new Intent(EditBookActivity.this, ScanActivity.class);
                Bundle extras = new Bundle();
                extras.putString("task", "get_description");
                intent.putExtras(extras);
                startActivityForResult(intent, SCAN_AND_GET_DESCRIPTION);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();
                for (String path : galleryImageURLS) {
                    StorageReference deleteFile = storageReference.child(path);
                    deleteFile.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditBookActivity.this, "Previous Image Deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                finish();
            }
        });


        AddOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                extras.putString("check", "new");
                extras.putStringArrayList("sent_list", galleryImageURLS);
                Intent intent = new Intent(EditBookActivity.this, GalleryActivity.class);
                intent.putExtras(extras);
                startActivityForResult(intent, GET_OTHER_BOOKS);
            }
        });

        AddCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });




        loadFromFile();
//if its a book being edited set all text views to the preset data of the book object
        if(check){
            passedBook = intent.getParcelableExtra("Book Data");

            String title = passedBook.getName();
            String author = passedBook.getAuthor();
            String genre = passedBook.getGenre();
            String year = passedBook.getYear();
            String description = passedBook.getDescription();
            Long isbn = passedBook.getISBN();


            TitleText.setText(title);
            AuthorText.setText(author);
            GenreText.setText(genre);
            YearText.setText(year);
            DescriptionText.setText(description);
            ISBNText.setText(Long.toString(isbn));
        }




    }
//when save button is clicked
    public void save(View v){

        String title = TitleText.getText().toString();

        String author = AuthorText.getText().toString();

        String isbn_string = ISBNText.getText().toString();

        Long isbn = null;
        if (!isbn_string.equals("")) {
            isbn = Long.parseLong(isbn_string);
        }

        String year = YearText.getText().toString();

        String genre = GenreText.getText().toString();
        String description = DescriptionText.getText().toString();
        if (passedBook == null){
            passedBook = new Book();
            String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
            passedBook.setOwner(currentUsername);
            passedBook.setHolder(currentUsername);
        }

//        Book book = new Book(title, description, isbn, year, genre, author, false);
        passedBook.setName(title);
        passedBook.setDescription(description);
        if (isbn != null) {
            passedBook.setISBN(isbn);
        }
        passedBook.setYear(year);
        passedBook.setGenre(genre);
        passedBook.setAuthor(author);
        passedBook.setGalleryImages(galleryImageURLS);
        passedBook.setIndexCover(indexCover);

        // Get the signed in user's username from Shared Preferences
        String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

        Booklist.add(passedBook);
        saveInFile();

        if (computation_done) {
            saveFirebase(passedBook);
        }


    }

    /**
     * Update book in firebase, if book not in firebase, add it.
     * @author rmnattas
     * @param book book to update/add
     */
    private void saveFirebase(final Book book){
        // connect to firebase
        final Database db = new Database(this);
        final Firebase ref = db.connect(this);

        Firebase tempRef = ref.child("Books");
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean found = false;  // true if book found in firebase

                // look for user in firebase
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    if (d.getKey().equals(book.getId().toString())){    // book found
                        Log.d("Book", "Book found");
                        found = true;
                        //edit the book in firebase
                        db.editBook(book);
                        break;
                    }
                }

                if (!found) {
                    String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
                    // book not in firebase => add book to Firebase
                    Log.d("Book", "Add book to firebase");
                    addBookToUser(currentUsername, book.getId());
                    db.addBook(book);
                }

                //check if new book or edited book, true if edited book
                Intent intent = getIntent();
                Boolean check = intent.getBooleanExtra("Check Data", false );

                //if edited book send it back to the my book fragment along with its
                // position data
                if(check){

                    int pos = intent.getIntExtra("Pos Data", 0);
                    Intent newINTent = new Intent(EditBookActivity.this, MyAdapter.class);


                    newINTent.putExtra("Data", book);
                    newINTent.putExtra("Pos", pos);
                    newINTent.putExtra("Check", true);

                    System.out.print("HElllllllloooooowewrwejrjoejiorwejrweijorij");

                    setResult(RESULT_OK,newINTent);
                    finish();


                }
                //else add it to myBooks
                else{
                    Intent newintent = new Intent(EditBookActivity.this, myBooksFragment.class);
                    newintent.putExtra("Book Data", book);
                    setResult(RESULT_OK, newintent);
                    finish();
                }


            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    /**
     * add the book to the user owned list and update firebase
     * @author rmnattas
     */
    public void addBookToUser(final String username, final UUID bookId){
        // get the user object from firebase
        final Database db = new Database(this);
        Firebase ref = db.connect(this);
        Firebase tempRef = ref.child("Users").child(username);

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jUser = dataSnapshot.getValue(String.class);
//                Log.d("jUser", jUser);
                if (jUser != null) {
                    // Get user object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<User>() {
                    }.getType();
                    User user = gson.fromJson(jUser, tokenType); // here is where we get the user object
                    user.addOwnedBook(bookId);
                    db.editUser(username, user);
                } else {
                    Log.d("myBookFrag", "11321");
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
    }


    private void loadFromFile() {

        try {

            FileReader in = new FileReader(new File(getFilesDir(),FILENAME));
            Gson gson = new Gson();
            Type listtype = new TypeToken<ArrayList<Book>>(){}.getType();
            Booklist = gson.fromJson(in, listtype);


        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

    }
//save to json file
    private void saveInFile() {
        try {
            FileWriter out = new FileWriter(new File(getFilesDir(),FILENAME));
            Gson gson = new Gson();

            gson.toJson(Booklist, out);

            out.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_AND_GET_DESCRIPTION && resultCode == Activity.RESULT_OK) {
            String ISBN = data.getStringExtra("ISBN");
            String description = data.getStringExtra("description");
            String year = data.getStringExtra("year");
            String title = data.getStringExtra("title");
            String author = data.getStringExtra("author");
            String genre = data.getStringExtra("genre");
            URLcover = data.getStringExtra("URL");
            getImageCover();
            DescriptionText.setText(description);
            ISBNText.setText(ISBN);
            TitleText.setText(title);
            YearText.setText(year);
            GenreText.setText(genre);
            AuthorText.setText(author);
        }

        if (requestCode == GET_OTHER_BOOKS && resultCode == Activity.RESULT_OK) {
            galleryImageURLS = data.getStringArrayListExtra("pathList");
        }

        if (requestCode == PICK_IMAGE_FOR_GALLERYY && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }

            Uri lastImagePath = data.getData();
            CoverImage.setImageURI(lastImagePath);
            String pathImage = "images1/" + UUID.randomUUID().toString();
            galleryImageURLS.add(pathImage);
            indexCover = galleryImageURLS.size() - 1;
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            // store in Storage
            StorageReference ref = storageReference.child(pathImage);
            ref.putFile(lastImagePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(EditBookActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditBookActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        }
    }

    public void getImageCover() {
        final Request request = new Request.Builder().url(URLcover).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //Handle the error
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    // https://stackoverflow.com/questions/40885860/how-to-save-bitmap-to-firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    String pathImage = "images1/" + UUID.randomUUID().toString();
                    galleryImageURLS.add(pathImage);
                    indexCover = galleryImageURLS.size() - 1;
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReference();
                    StorageReference ref = storageReference.child(pathImage);

                    UploadTask uploadTask = ref.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        }
                    });

                    // Remember to set the bitmap in the main thread.
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            CoverImage.setImageBitmap(bitmap);
                        }
                    });
                } else {
                    //Handle the error
                }
            }
        });
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_FOR_GALLERYY);
    }
}

