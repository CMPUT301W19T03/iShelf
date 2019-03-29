package ca.ualberta.ishelf.RecyclerAdapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import android.widget.Spinner;

import java.util.ArrayList;

import ca.ualberta.ishelf.Book;
import ca.ualberta.ishelf.Database;
import ca.ualberta.ishelf.R;
import ca.ualberta.ishelf.User;

import static android.support.constraint.Constraints.TAG;

/**
 * BorrowFragment
 * Send in either:
 *                  key: "Book" - a Book object
 * US 03.01.01
 * As a borrower, I want to specify a set of keywords, and search for all books that are not currently accepted or borrowed whose description contains all the keywords.
 * needs to be able to search by genre or keyword according to user tastes
 *
 *
 * US 03.02.01
 * As a borrower, I want search results to show each book not currently accepted or borrowed with its description, owner username, and status.
 * screen should show a list of all currently available books to browse from
 *
 * This shows all the books that you can borrow,
 * This activity loads all available dbooks from firebase
 *
 *
 * @author: Faisal
 */
public class BorrowFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final String link = "https://ishelf-bb4e7.firebaseio.com";
    private Firebase ref;
    private ArrayList<Book> bookList = new ArrayList<Book>();
    private User user = new User();
    private Book book = new Book();
    private RecyclerView bookRecyclerView;
    private BorrowAdapter bookAdapter;
    private RecyclerView.LayoutManager bookLayoutManager;
    private SwipeRefreshLayout borrowRefresh;
    private Spinner spinner;

    private String currentUsername = new String();
    private SearchView searchView;




    /**
     * onCreateView
     *
     * Initializes recyclerview and search for borrow books
     * @author: Faisal
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_borrow, container, false);

        bookRecyclerView = (RecyclerView) view.findViewById((R.id.borrow_recycler));
        bookLayoutManager = new LinearLayoutManager(view.getContext());
        bookRecyclerView.setLayoutManager(bookLayoutManager);
        bookAdapter = new BorrowAdapter(view.getContext(), bookList, bookList);
        bookRecyclerView.setAdapter(bookAdapter);
        spinner = view.findViewById(R.id.rating_sorter);



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



        // creates a line between each individual record in the list
        // https://developer.android.com/reference/android/support/v7/widget/DividerItemDecoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(bookRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        bookRecyclerView.addItemDecoration(dividerItemDecoration);

        getAvailableBooks();

        searchView = (SearchView) getActivity().findViewById(R.id.searchView1);

        borrowRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        borrowRefresh.setOnRefreshListener(this

        );

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                bookAdapter.getFilter().filter(text);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAvailableBooks();
    }

    public void Filter(String filter){
            if(filter.equals("1")) {
                ArrayList<Book> ratedBooks = new ArrayList<>(); // both owned and borrowed
                for (Book book : bookList){
                    if (book.getAvgRating()>=1){
                        ratedBooks.add(book);
                    }
                }
                bookAdapter.updateList(ratedBooks);
                bookAdapter.notifyDataSetChanged();
            } else if(filter.equals("2")) {
                ArrayList<Book> ratedBooks = new ArrayList<>(); // both owned and borrowed
                for (Book book : bookList){
                    if (book.getAvgRating()>=2){
                        ratedBooks.add(book);
                    }
                }
                bookAdapter.updateList(ratedBooks);
                bookAdapter.notifyDataSetChanged();
            } else if (filter.equals(("3"))){
                ArrayList<Book> ratedBooks = new ArrayList<>(); // both owned and borrowed
                for (Book book : bookList){
                    if (book.getAvgRating()>=3){
                        ratedBooks.add(book);
                    }
                }
                bookAdapter.updateList(ratedBooks);
                bookAdapter.notifyDataSetChanged();
            } else if(filter.equals("4")){
                ArrayList<Book> ratedBooks = new ArrayList<>(); // both owned and borrowed
                for (Book book : bookList){
                    if (book.getAvgRating()>=4){
                        ratedBooks.add(book);
                    }
                }
                bookAdapter.updateList(ratedBooks);
                bookAdapter.notifyDataSetChanged();
            }
            else if(filter.equals("5")){
                ArrayList<Book> ratedBooks = new ArrayList<>(); // both owned and borrowed
                for (Book book : bookList){
                    if (book.getAvgRating()>=5){
                        ratedBooks.add(book);
                    }
                }
                bookAdapter.updateList(ratedBooks);
                bookAdapter.notifyDataSetChanged();
            }
            else if(filter.equals("All")){
                bookAdapter.updateList(bookList);
                bookAdapter.notifyDataSetChanged();
            }
        }


    public void getUser(String username){
        if (username != null) {

            /**
             * Retrieve the user's info from the database (Firebase)
             */
            // Get user from the passed in username


            ref = new Firebase(link);

            // get reference to specific entry
            Firebase tempRef = ref.child("Users").child(username);
            // create a one time use listener to immediately access datasnapshot
            tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String jUser = dataSnapshot.getValue(String.class);
                    Log.d("jUser", jUser);
                    if (jUser != null) {

                        // Get user object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<User>() {
                        }.getType();
                        user = gson.fromJson(jUser, tokenType); // here is where we get the user object
                        // pass the retrieved User to updateUI to update the UI elements
                    } else {
                        Log.d("FBerror1", "User doesn't exist or string is empty");
                    }

                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    return;
                }
            });


        } else {
            Log.d(TAG, "onCreate: Username passed in is Null");
        }
    }

    /**
     * get available books from firebase
     * @author rmnattas
     */
    public void getAvailableBooks(){

        //connect to firebase
        Database db = new Database(getContext());
        Firebase fb = db.connect(getContext());
        Firebase childRef = fb.child("Books");


        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookList.clear();
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    String jBook = d.getValue(String.class);
                    //Log.d("jBook", jBook);
                    if (jBook != null) {
                        // Get book object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<Book>() {
                        }.getType();
                        book = gson.fromJson(jBook, tokenType); // here is where we get the user object
                        if (book.checkBorrowed() == false){
                            currentUsername = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

                            if(currentUsername==null){
                            }
                            else if(!currentUsername.equals(book.getOwner()))
                            {
                                if(book.getTransition()==0){
                                    bookList.add(book);
                                }

                            }

                        }
                    } else {
                        Log.d("FBerrorFragmentRequest", "User doesn't exist or string is empty");
                    }
                }
                bookAdapter.updateList(bookList);
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }

        });
    }

    /**
     * onRefresh
     *
     * Allows for pull to refresh (by calling to firebase)
     *
     * @author: Faisal
     */
    @Override
    public void onRefresh(){
        bookList.clear();
        getAvailableBooks();
        borrowRefresh.setRefreshing(false);
    }

}
