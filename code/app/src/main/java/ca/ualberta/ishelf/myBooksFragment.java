package ca.ualberta.ishelf;

import android.content.Context;
import android.content.DialogInterface;
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

/**
 *
 * @author Evan
 */
public class myBooksFragment extends Fragment {
    //    https://stackoverflow.com/questions/44777605/android-studio-how-to-add-filter-on-a-recyclerview-and-how-to-implement-it
    private static final String TAG = "xxxmyBooksActivityxxx";
    //String username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", "TestUsername");
    //vars
    private ArrayList<Book> myBooks = new ArrayList<>();
    private ArrayAdapter<Book> adapter;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImage = new ArrayList<>();
    private RatingBar ratingBar;
    private Spinner spinner; //drop-down filter: https://www.mkyong.com/android/android-spinner-drop-down-list-example/
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
                if(selectedItem.equals("My Books"))
                {
                    enteredAlert("yay!!");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        initImage();
    }

    public void filter(){

    }

    /*
     * ok wow addBook worked when it was an activity, im not sure what changed since i converted activity->fragment
     * illegalStateException
     */

    public void addBook(View view){
        //enteredAlert("this works");
        mImage.add("https://i.imgur.com/ZcLLrkY.jpg"); //wherever the book image comes from
        mNames.add("Havana oh na na");
        Book book = new Book();
        book.setName("50ShadesOfBlack");
        //ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar = getActivity().findViewById(R.id.ratingBar);
        Rating rating = new Rating();
        book.addRating(rating);
        this.ratingBar.setRating(4); // this should work but does not work, idk why
        mImage.add("https://m.media-amazon.com/images/M/MV5BMTQ3MTg3MzY4OV5BMl5BanBnXkFtZTgwNTI4MzM1NzE@._V1_UY1200_CR90,0,630,1200_AL_.jpg");
        mNames.add(book.getName());
        initRecyclerView(); //there should be a way to update without initializing it again
    }

    private void initImage(){
        Log.d(TAG, "init works");
        mImage.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
        mNames.add("Havana oh na na");

        mImage.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("half of my heart is in havana oh na na");

        mImage.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("He took me back to East Atlanta, na-na-na");

        mImage.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("Oh, but my heart is in Havana");

        mImage.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("There's somethin' 'bout his manners");

        initRecyclerView();
    }
    /*
    getActivity() is bad practice but not sure how else to code it
     */
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
        MyAdapter adapter = new MyAdapter(mNames, mImage, this.getContext()); //in the same order as the constructor in MyAdapter
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
}