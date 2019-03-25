package ca.ualberta.ishelf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 *
 * US 01.04.01
 * As an owner, I want to view a list of all my books, and their descriptions, statuses, and current borrowers.
 * the owner wants to see their book library
 * US 01.05.01
 * As an owner, I want to view a list of all my books, filtered by status.
 * the owner wants to have a filter view of his books
 *
 * This activity shows the books you own, they can be filtered by status. You can click the book to get all of its details
 * view it, edit it and delete it. You can add new book to the list by pressing the floating add button
 *
 * @author Evan
 * @edited rmnattas
 *
 * -a lot of this code is going to change once i change myAdapter to match the one from borrow books, the tests will change too
 * -right now the way it's displayed will largely be changed in the next sprint - Evan
 */
public class myBooksFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    //    https://stackoverflow.com/questions/44777605/android-studio-how-to-add-filter-on-a-recyclerview-and-how-to-implement-it
    private static final String TAG = "MyBooksFragment";

    // no need for a bunch of list each has object,name,img lists @rmnattas
    private  ArrayList<Book> myOwnedBooks = new ArrayList<>();
    private  ArrayList<Book> myBorrowedBooks = new ArrayList<>();

    private RatingBar ratingBar;
    private Spinner spinner; //drop-down filter: https://www.mkyong.com/android/android-spinner-drop-down-list-example/
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private SwipeRefreshLayout myBooksRefresh;
    private SearchView searchView;

    /***********************************************************************************/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_book, container, false);
        // make  a list for recycler view
        ArrayList<Book> allMyBooks = new ArrayList<>(); // both owned and borrowed
        allMyBooks.addAll(myOwnedBooks);
        allMyBooks.addAll(myBorrowedBooks);

        // set recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        myAdapter = new MyAdapter(allMyBooks, this.getContext(), allMyBooks);
        recyclerView.setAdapter(myAdapter);
        spinner = view.findViewById(R.id.spinner);
        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.addButton); //sorry abdul
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
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        //getUserBooks();

        searchView = (SearchView) getActivity().findViewById(R.id.searchView1);

        myBooksRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh1);
        myBooksRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserBooks();
                myBooksRefresh.setRefreshing(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                myAdapter.getFilter().filter(text);
                return true;
            }
        });
        return view;
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Log.d(TAG, "OnCreate started");
//
//        // set recycler view events
//        spinner = getActivity().findViewById(R.id.spinner);
//        FloatingActionButton add = (FloatingActionButton) getActivity().findViewById(R.id.addButton); //sorry abdul
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addBook(v);
//            }
//        });
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedItem = parent.getItemAtPosition(position).toString();
//                Filter(selectedItem);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        // set recycler view
//        recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
//        // make  a list for recycler view
//        ArrayList<Book> allMyBooks = new ArrayList<>(); // both owned and borrowed
//        allMyBooks.addAll(myOwnedBooks);
//        allMyBooks.addAll(myBorrowedBooks);
//        myAdapter = new MyAdapter(allMyBooks, this.getContext(), allMyBooks);
//        recyclerView.setAdapter(myAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

//    }

    @Override
    public void onResume() {
        super.onResume();

        // clear myBooks listView
        ArrayList<Book> cleanList = new ArrayList<>();
        myAdapter.updateList(cleanList);
        myAdapter.notifyDataSetChanged();

        // update list of books
        getUserBooks();
    }

    /**
     * Get the logged in user books from firebase
     * @author rmnattas
     */
    private void getUserBooks(){

        // get logged in user username
        final String currentUsername = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

        // get the user object from firebase
        Firebase ref = new Database(getContext()).connect(getContext());
        Firebase tempRef = ref.child("Users").child(currentUsername);
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
                    //get owned and borrowed books
                    getBooks(user.getOwnedBooks(), user.getBorrowedBooks());
                } else {
                    Log.d(TAG, "11321");
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });

    }


    /**
     * given a list of books ids get the book objects
     * from firebase and add them to owned or borrowed
     * books list accordingly
     * @author rmnattas
     * @param ownedBooksIds list of owned books ids
     * @param borrowedBooksIds list of borrowed books ids
     */
    private void getBooks(final ArrayList<UUID> ownedBooksIds, final ArrayList<UUID> borrowedBooksIds){
        // get logged in user username
        final String currentUsername = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

        Firebase ref = new Database(getContext()).connect(getContext());
        Firebase childRef = ref.child("Books");
        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // clear previous lists
                myOwnedBooks.clear();
                myBorrowedBooks.clear();
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    String jBook = d.getValue(String.class);
                    //Log.d("jBook", jBook);
                    if (jBook != null) {
                        // Get book object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<Book>() {}.getType();
                        Book book = gson.fromJson(jBook, tokenType); // here is where we get the user object
                        if (ownedBooksIds.contains(book.getId()) || book.getOwner().equals(currentUsername)){
                            myOwnedBooks.add(book);
                        } else if (borrowedBooksIds.contains(book.getId())){
                            myBorrowedBooks.add(book);
                        }
                    } else {
                        Log.d("FBerrorFragmentRequest", "User doesn't exist or string is empty");
                    }
                }

                // add owned and borrowed books to one list
                ArrayList<Book> allMyBooks = new ArrayList<>(); // both owned and borrowed
                allMyBooks.addAll(myOwnedBooks);
                allMyBooks.addAll(myBorrowedBooks);

                myAdapter.updateList(allMyBooks);
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }

        });
    }

    /**
     * filter books by {all, own, lent-out, borrowed}
     * @param filter filter name
     * @author Evan
     * @edited rmnattas
     */
    public void Filter(String filter){ //this is going to filter strings for now, but should work whenever we pass in book/user
        if(filter.equals("All")) {
            // add owned and borrowed books to one list
            ArrayList<Book> allMyBooks = new ArrayList<>(); // both owned and borrowed
            allMyBooks.addAll(myOwnedBooks);
            allMyBooks.addAll(myBorrowedBooks);
            myAdapter.updateList(allMyBooks);
            myAdapter.notifyDataSetChanged();
        } else if(filter.equals("My Books")) {
            myAdapter.updateList(myOwnedBooks);
            myAdapter.notifyDataSetChanged();
        } else if (filter.equals(("Lent out"))){
            ArrayList<Book> lentoutBooks = new ArrayList<>(); // both owned and borrowed
            for (Book book : myOwnedBooks){
                if (book.checkBorrowed()){
                    lentoutBooks.add(book);
                }
            }
            myAdapter.updateList(lentoutBooks);
            myAdapter.notifyDataSetChanged();
        } else if(filter.equals("Borrowed Books")){
            myAdapter.updateList(myBorrowedBooks);
            myAdapter.notifyDataSetChanged();
        }
    }

    /**
     * called when add book is clicked
     * @param view v
     * @author Evan
     */
    public void addBook(View view){ //so when you add a book, you immediately filter by status
        Intent intent = new Intent(view.getContext(), EditBookActivity.class);
        intent.putExtra("Check Data",false);
        startActivityForResult(intent, 1);
    }


    /**
     * called when get back from an activity with result
     * @param requestCode
     * @param resultCode
     * @param data
     * @author Evan/Mehrab
     * @edited rmnattas
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        myAdapter.onActivityResult(requestCode, resultCode, data);

        /** Adding new book */
        if(requestCode == 1){

//            Book book = data.getParcelableExtra("Book Data");

            // clear myBooks listView
            ArrayList<Book> cleanList = new ArrayList<>();
            myAdapter.updateList(cleanList);
            myAdapter.notifyDataSetChanged();
            // get updated my books from firebase
            getUserBooks();

            if(resultCode == RESULT_OK){ }
        }

        /** editing or deleting a book */
        if(requestCode == 1002){

            // clear myBooks listView
            ArrayList<Book> cleanList = new ArrayList<>();
            myAdapter.updateList(cleanList);
            myAdapter.notifyDataSetChanged();
            // get updated my books from firebase
            getUserBooks();

            if(resultCode == RESULT_OK){ }
        }
    }

    @Override
    public void onRefresh() {
        getUserBooks();
        myBooksRefresh.setRefreshing(false);
    }
}