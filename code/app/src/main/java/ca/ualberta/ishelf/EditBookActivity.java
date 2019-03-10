package ca.ualberta.ishelf;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

/**
 * ViewProfileActivity
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
 * @author: Mehrab
 */


public class EditBookActivity extends AppCompatActivity {

    private EditText TitleText;
    private EditText AuthorText;
    private EditText ISBNText;
    private EditText YearText;
    private EditText GenreText;
    private EditText DescriptionText;
    private ArrayList<Book> Booklist = new ArrayList<Book>();
    private static final String FILENAME = "book1.sav";
    private Book passedBook = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        //if its an edit book then true otherwise false
        Intent intent = getIntent();
        Boolean check = intent.getBooleanExtra("Check Data", false );
//assign all the edit texts to variables
        TitleText = (EditText) findViewById(R.id.editTitle);
        AuthorText = (EditText) findViewById(R.id.editAuthor);
        ISBNText = (EditText) findViewById(R.id.editISBN);
        YearText = (EditText) findViewById(R.id.editYear);
        GenreText= (EditText) findViewById(R.id.editGenre);
        DescriptionText = (EditText) findViewById(R.id.editDes);


        Button clearButton = (Button) findViewById(R.id.cancel);
        Button saveButton = (Button) findViewById(R.id.save);

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
        Long isbn = Long.parseLong(ISBNText.getText().toString());
        String year = YearText.getText().toString();

        String genre = GenreText.getText().toString();
        String description = DescriptionText.getText().toString();

        Book book = new Book(title, description, isbn, year, genre, author, false);

        // Get the signed in user's username from Shared Preferences
        String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
        book.setOwner(currentUsername);

        if (passedBook != null){
            book.setId(passedBook.getId());
        }

        Booklist.add(book);
        saveInFile();

        saveFirebase(book);


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
                    // book not in firebase => add book to Firebase
                    Log.d("Book", "Add book to firebase");
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

//load from json file
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



}

