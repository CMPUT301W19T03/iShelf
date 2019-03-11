package ca.ualberta.ishelf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.app.SearchManager;
import android.util.Log;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
    private ArrayList<Book> bookList = new ArrayList<Book>();

    private RecyclerView bookRecyclerView;
    private BorrowAdapter bookAdapter;
    private RecyclerView.LayoutManager bookLayoutManager;
    private SwipeRefreshLayout borrowRefresh;


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

        // creates a line between each individual record in the list
        // https://developer.android.com/reference/android/support/v7/widget/DividerItemDecoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(bookRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        bookRecyclerView.addItemDecoration(dividerItemDecoration);

        getAvailableBooks();

        searchView = (SearchView) getActivity().findViewById(R.id.searchView1);

        borrowRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        borrowRefresh.setOnRefreshListener(this);

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
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    String jBook = d.getValue(String.class);
                    //Log.d("jBook", jBook);
                    if (jBook != null) {
                        // Get book object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<Book>() {
                        }.getType();
                        Book book = gson.fromJson(jBook, tokenType); // here is where we get the user object
                        if (book.checkBorrowed() == false){
                            bookList.add(book);
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
