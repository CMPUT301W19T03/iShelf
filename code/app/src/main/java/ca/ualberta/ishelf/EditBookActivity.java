package ca.ualberta.ishelf;

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

public class EditBookActivity extends AppCompatActivity {

    private EditText TitleText;
    private EditText AuthorText;
    private EditText ISBNText;
    private EditText YearText;
    private EditText GenreText;
    private EditText DescriptionText;
    private ArrayList<Book> Booklist = new ArrayList<Book>();
    private static final String FILENAME = "book1.sav";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);


        Intent intent = getIntent();
        Boolean check = intent.getBooleanExtra("Check Data", false );

        TitleText = (EditText) findViewById(R.id.editTitle);
        AuthorText = (EditText) findViewById(R.id.editAuthor);
        ISBNText = (EditText) findViewById(R.id.editISBN);
        YearText = (EditText) findViewById(R.id.editYear);
        GenreText= (EditText) findViewById(R.id.editGenre);
        DescriptionText = (EditText) findViewById(R.id.editDes);


        Button clearButton = (Button) findViewById(R.id.cancel);
        Button saveButton = (Button) findViewById(R.id.save);

        loadFromFile();

        if(check){
            Book data = intent.getParcelableExtra("Book Data");

            String title = data.getName();
            String author = data.getAuthor();
            String genre = data.getGenre();
            String year = data.getYear();
            String description = data.getDescription();
            Long isbn = data.getISBN();


            TitleText.setText(title);
            AuthorText.setText(author);
            GenreText.setText(genre);
            YearText.setText(year);
            DescriptionText.setText(description);
            ISBNText.setText(Long.toString(isbn));
        }




    }

    public void save(View v){

        String title = TitleText.getText().toString();

        String author = AuthorText.getText().toString();
        Long isbn = Long.parseLong(ISBNText.getText().toString());
        String year = YearText.getText().toString();

        String genre = GenreText.getText().toString();
        String description = DescriptionText.getText().toString();

        Book book = new Book(title, description, isbn, year, genre, author);


        Booklist.add(book);
        saveInFile();

        saveFirebase(book);


    }

    /**
     * Update book in firebase, if book not in firebase, add it.
     * @param book book to update/add
     */
    private void saveFirebase(final Book book){
        // connect to firebase
        final Firebase ref = new Database(this).connect(this);

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
                        // Book found, update it in firebase
                        Firebase userchild = ref.child("Books").child(d.getKey());
                        // set book object JSON
                        book.setId(UUID.fromString(d.getKey()));
                        Gson gson = new Gson();
                        final String jBook = gson.toJson(book);
                        // save the new Book object to firebase
                        userchild.setValue(jBook);
                        break;
                    }
                }

                if (!found) {
                    // book not in firebase => add book to Firebase
                    Log.d("Book", "Add book to firebase");
                    Firebase userchild = ref.child("Books").child(book.getId().toString());
                    // set book object JSON
                    Gson gson = new Gson();
                    final String jBook = gson.toJson(book);
                    // save the new Book object to firebase
                    userchild.setValue(jBook);
                }


                Intent intent = getIntent();
                Boolean check = intent.getBooleanExtra("Check Data", false );


                if(check){

                    int pos = intent.getIntExtra("Pos Data", 0);
                    Intent newINTent = new Intent(EditBookActivity.this, myBooksFragment.class);


                    newINTent.putExtra("Book Data", book);
                    newINTent.putExtra("Pos Data", pos);
                    newINTent.putExtra("Check Data", true);

                    System.out.print("HElllllllloooooowewrwejrjoejiorwejrweijorij");

                    setResult(RESULT_OK,newINTent);
                    finish();


                }
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


    private void loadFromFile() {

        try {

            FileReader in = new FileReader(new File(getFilesDir(),FILENAME));
            Gson gson = new Gson();
            Type listtype = new TypeToken<ArrayList<Book>>(){}.getType();
            Booklist = gson.fromJson(in, listtype);


        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

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

