package ca.ualberta.ishelf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 *
 * @author Evan
 */
public class myBooksFragment extends Fragment {
    //    https://stackoverflow.com/questions/44777605/android-studio-how-to-add-filter-on-a-recyclerview-and-how-to-implement-it
    private static final String TAG = "xxxmyBooksActivityxxx";
    //String username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", "TestUsername");
    //vars
    private ArrayList<String> myBookNames = new ArrayList<>(); // the default, every book for a user will be in myBooks i think
    private ArrayList<String> myBookImage = new ArrayList<>();
    private ArrayList<String> borrowBooksName = new ArrayList<>();
    private ArrayList<String> borrowBooksImage = new ArrayList<>();
    private ArrayList<String> requestedBooksName = new ArrayList<>();
    private ArrayList<String> requestedBooksImage = new ArrayList<>();
    private  ArrayList<Book> myOwnedBooks = new ArrayList<>();
    private  ArrayList<Book> myBorrowBooks = new ArrayList<>();
    private  ArrayList<Book> myRequestedBooks = new ArrayList<>();
    private RatingBar ratingBar;
    private Spinner spinner; //drop-down filter: https://www.mkyong.com/android/android-spinner-drop-down-list-example/
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_book, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "OnCreate started");
        spinner = getActivity().findViewById(R.id.spinner);
        FloatingActionButton add = (FloatingActionButton) getActivity().findViewById(R.id.addButton); //sorry abdul
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook(v);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Filter(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
        myAdapter = new MyAdapter(myBookNames, myBookImage, myOwnedBooks, this.getContext()); //in the same order as the constructor in MyAdapter
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        initImage();
    }

    public void Filter(String filter){ //this is going to filter strings for now, but should work whenever we pass in book/user
        if(filter.equals("My Books")) {
            initRecyclerView(myBookNames, myBookImage, myOwnedBooks, this.getContext());
        }else if (filter.equals(("Lent out"))){
            initRecyclerView(borrowBooksName, borrowBooksImage,myBorrowBooks ,this.getContext()); // basically do nothing and go back to main page
        }else if (filter.equals("Requested")){
            initRecyclerView(requestedBooksName, requestedBooksImage,myRequestedBooks, this.getContext()); // basically do nothing and go back to main page

        }
        else if(filter.equals("Borrowed Books")){

        }
    }

    /*
     * ok wow addBook worked when it was an activity, im not sure what changed since i converted activity->fragment
     * illegalStateException
     */

    public void addBook(View view){ //so when you add a book, you immediately filter by status

        //im not sure if my books gets from user or book but i don't see a your borrowed books or requests

//        mImage.add("https://i.imgur.com/ZcLLrkY.jpg"); //wherever the book image comes from
//        mNames.add("Havana oh na na");




        Intent intent = new Intent(view.getContext(), EditBookActivity.class);
        intent.putExtra("Check Data",false);
        startActivityForResult(intent, 1);



//        Book book = new Book();
//        book.setName("50ShadesOfBlack");
//        ratingBar = getActivity().findViewById(R.id.ratingBar);
//        Rating rating = new Rating();
//        book.addRating(rating);
//        this.ratingBar.setRating(4); // this should work but does not work, idk why
//        myBookImage.add("https://m.media-amazon.com/images/M/MV5BMTQ3MTg3MzY4OV5BMl5BanBnXkFtZTgwNTI4MzM1NzE@._V1_UY1200_CR90,0,630,1200_AL_.jpg");
//        //font see any constructors for image in Book class yet
//        myBookNames.add(book.getName());
//        //myAdapter.updateData(); //this doesn't work for some reason
//        book.setStatus(0);
//        if (book.getStatus() == 0){ //available to borrow
//            borrowBooksImage.add("https://m.media-amazon.com/images/M/MV5BMTQ3MTg3MzY4OV5BMl5BanBnXkFtZTgwNTI4MzM1NzE@._V1_UY1200_CR90,0,630,1200_AL_.jpg");
//            borrowBooksName.add(book.getName());
//        }
//        if(book.getStatus() == 2){ //should be lent if there is such a status
//            requestedBooksImage.add("https://m.media-amazon.com/images/M/MV5BMTQ3MTg3MzY4OV5BMl5BanBnXkFtZTgwNTI4MzM1NzE@._V1_UY1200_CR90,0,630,1200_AL_.jpg");
//            requestedBooksName.add(book.getName());
//        }
//        initRecyclerView(myBookNames, myBookImage, this.getContext()); //there should be a way to update without initializing it again

    }

    private void initImage(){
        Log.d(TAG, "init works");

        Book book = new Book("Havana oh na-na", "Description", 1234L, "Year", "Genre", "author", false);

        myBookImage.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        myBookNames.add("Havana oh na-na");
        myOwnedBooks.add(book);



        Book book1 = new Book("Oh, but my heart is in Havana", "Description", 1234L, "Year", "Genre", "author", false);
        myBookImage.add("https://i.redd.it/j6myfqglup501.jpg");
        myBookNames.add("Oh, but my heart is in Havana");
        myOwnedBooks.add(book1);

        Book book2= new Book("There's somethin' 'bout his manners", "Description", 1234L, "Year", "Genre", "author", false);
        myBookImage.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        myBookNames.add("There's somethin' 'bout his manners");
        myOwnedBooks.add(book2);
        //myAdapter.updateData();   -- need to work on adding objects and not just strings, i just used strings to test - Evan

        initRecyclerView(myBookNames, myBookImage,myOwnedBooks, this.getContext());
    }
    /*
    getActivity() is bad practice but not sure how else to code it
     */

    private void initRecyclerView(ArrayList name, ArrayList image, ArrayList list, Context context){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
        MyAdapter adapter = new MyAdapter(name, image, list, this.getContext()); //in the same order as the constructor in MyAdapter
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
    private void enteredAlert(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setMessage((CharSequence) msg);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated catch block
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        myAdapter.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Book book = data.getParcelableExtra("Book Data");

                ratingBar = getActivity().findViewById(R.id.ratingBar);
                Rating rating = new Rating();
                book.addRating(rating);
                this.ratingBar.setRating(4); // this should work but does not work, idk why
                myOwnedBooks.add(book);
                myBookImage.add("https://m.media-amazon.com/images/M/MV5BMTQ3MTg3MzY4OV5BMl5BanBnXkFtZTgwNTI4MzM1NzE@._V1_UY1200_CR90,0,630,1200_AL_.jpg");
                //font see any constructors for image in Book class yet
                myBookNames.add(book.getName());
                //myAdapter.updateData(); //this doesn't work for some reason
                book.setStatus(0);
                if (book.getStatus() == 0){ //available to borrow
                    borrowBooksImage.add("https://m.media-amazon.com/images/M/MV5BMTQ3MTg3MzY4OV5BMl5BanBnXkFtZTgwNTI4MzM1NzE@._V1_UY1200_CR90,0,630,1200_AL_.jpg");
                    borrowBooksName.add(book.getName());
                }
                if(book.getStatus() == 2){ //should be lent if there is such a status
                    requestedBooksImage.add("https://m.media-amazon.com/images/M/MV5BMTQ3MTg3MzY4OV5BMl5BanBnXkFtZTgwNTI4MzM1NzE@._V1_UY1200_CR90,0,630,1200_AL_.jpg");
                    requestedBooksName.add(book.getName());
                }
                initRecyclerView(myBookNames, myBookImage,myOwnedBooks, this.getContext());
            }
        }

        if(requestCode == 1002){
            if(resultCode == RESULT_OK){
                boolean check = data.getBooleanExtra("Check", true);
                if(check) {


                    Book book = data.getParcelableExtra("Data");
                    int pos = data.getIntExtra("Pos", 0);

                    ratingBar = getActivity().findViewById(R.id.ratingBar);
                    Rating rating = new Rating();
                    book.addRating(rating);
                    this.ratingBar.setRating(4); // this should work but does not work, idk why
                    myOwnedBooks.set(pos, book);
                    myBookImage.set(pos, "https://m.media-amazon.com/images/M/MV5BMTQ3MTg3MzY4OV5BMl5BanBnXkFtZTgwNTI4MzM1NzE@._V1_UY1200_CR90,0,630,1200_AL_.jpg");
                    //font see any constructors for image in Book class yet
                    myBookNames.set(pos, book.getName());
                    //myAdapter.updateData(); //this doesn't work for some reason
                    book.setStatus(1);
                    if (book.getStatus() == 0) { //available to borrow
                        borrowBooksImage.set(pos,"https://m.media-amazon.com/images/M/MV5BMTQ3MTg3MzY4OV5BMl5BanBnXkFtZTgwNTI4MzM1NzE@._V1_UY1200_CR90,0,630,1200_AL_.jpg");
                        borrowBooksName.set(pos,book.getName());
                    }
                    if (book.getStatus() == 2) { //should be lent if there is such a status
                        requestedBooksImage.set(pos, "https://m.media-amazon.com/images/M/MV5BMTQ3MTg3MzY4OV5BMl5BanBnXkFtZTgwNTI4MzM1NzE@._V1_UY1200_CR90,0,630,1200_AL_.jpg");
                        requestedBooksName.set(pos, book.getName());
                    }
                    initRecyclerView(myBookNames, myBookImage, myOwnedBooks, this.getContext());
                }
                else{
                    int pos = data.getIntExtra("Pos", 1);
                    myOwnedBooks.remove(pos);
                    myBookNames.remove(pos);
                    myBookImage.remove(pos);
                    myAdapter.notifyDataSetChanged();

                    initRecyclerView(myBookNames, myBookImage, myOwnedBooks, this.getContext());

                }
            }
        }
    }
}