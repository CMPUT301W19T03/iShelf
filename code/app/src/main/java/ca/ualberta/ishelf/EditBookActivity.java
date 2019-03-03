package ca.ualberta.ishelf;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class EditBookActivity extends AppCompatActivity {

    private EditText TitleText;
    private EditText AuthorText;
    private EditText ISBNText;
    private EditText YearText;
    private EditText GenreText;
    private EditText DescriptionText;
    private ArrayList<Book> Booklist = new ArrayList<Book>();
    private static final String FILENAME = "book1.sav";
    private final String link = "https://ishelf-bb4e7.firebaseio.com";
    private Firebase ref = new Firebase(link);

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
            Book data = intent.getParcelableExtra("Data");

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

        // Save book changes to Firebase
        saveInCloud(book);

        Intent intent = new Intent(EditBookActivity.this, BookProfileActivity.class);

        intent.putExtra("Book Data", book);



        startActivity(intent);
        finish();





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

    private void saveInCloud(Book book) {
        // Delete old version of book from firebase
        // get reference to specific entry
        Firebase tempRef = ref.child("Books").child(book.getId().toString());
        // create a one time use listener to immediately access datasnapshot
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // Delete our entry
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });

        // Add new version of book to firebase
        Firebase bookchild = ref.child("Books").child(book.getId().toString());
        // Convert to Gson
        Gson gson = new Gson();
        String jBook = gson.toJson(book);
        // Save to firebase
        bookchild.setValue(jBook);
    }

}

