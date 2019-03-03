package ca.ualberta.ishelf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BookProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_profile);

        Intent intent = getIntent();
        Book data = intent.getParcelableExtra("Book Data");




        String title = data.getName();
        String author = data.getAuthor();
        String genre = data.getGenre();
        String year = data.getYear();
        String description = data.getDescription();
        Long isbn = data.getISBN();

        TextView textView = findViewById(R.id.Title);
        textView.setText(title);

        TextView textView1 = findViewById(R.id.Author);
        textView1.setText(author);

        TextView textView2 = findViewById(R.id.genre);
        textView2.setText(genre);

        TextView textView3 = findViewById(R.id.Year);
        textView3.setText(year);

        TextView textView4 = findViewById(R.id.des);
        textView4.setText(description);

        TextView textView5 = findViewById(R.id.ISBN);
        textView5.setText(Long.toString(isbn));

        TextView textView6 = findViewById(R.id.status);
        textView6.setText("AVAILABLE");

        Button editButton = (Button) findViewById(R.id.edit);





    }


    public void edit(View v){

        Intent intent = getIntent();
        Book data = intent.getParcelableExtra("Book Data");

        Intent newINTent = new Intent(BookProfileActivity.this, EditBookActivity.class);


        newINTent.putExtra("Data", data);
        newINTent.putExtra("Check Data", true);



        startActivity(newINTent);
        finish();

    }
}
