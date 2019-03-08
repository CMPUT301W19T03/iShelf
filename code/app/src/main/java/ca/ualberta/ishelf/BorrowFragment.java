package ca.ualberta.ishelf;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.ArrayList;

public class BorrowFragment extends Fragment {
    private ArrayList<Book> bookList = new ArrayList<Book>();

    private RecyclerView bookRecyclerView;
    private RecyclerView.Adapter bookAdapter;
    private RecyclerView.LayoutManager bookLayoutManager;

    boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_borrow, container, false);

        bookRecyclerView = (RecyclerView) view.findViewById((R.id.borrow_recycler));
        bookLayoutManager = new LinearLayoutManager(view.getContext());
        bookRecyclerView.setLayoutManager(bookLayoutManager);
        bookAdapter = new BorrowAdapter(view.getContext(), bookList);
        bookRecyclerView.setAdapter(bookAdapter);
        initializeScrollListener();

        // creates a line between each individual record in the list
        // https://developer.android.com/reference/android/support/v7/widget/DividerItemDecoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(bookRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        bookRecyclerView.addItemDecoration(dividerItemDecoration);

        getAvailableBooks();

        return view;
    }


    private void initializeScrollListener() {
        bookRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == bookList.size() - 1) {
                        //bottom of list!
                        //loadMore(); No need it gets book from firebase in the background!
                        isLoading = true;
                    }
                }
            }
        });
    }

    /**
     * get available books from firebase
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
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }

        });
    }

    private void loadMore() {
        bookList.add(null);
        bookAdapter.notifyItemInserted(bookList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bookList.remove(bookList.size() - 1);
                int scrollPosition = bookList.size();
                bookAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    Book testBook8 = new Book("Book 8", "Description", 1234L, "Year", "Genre", "author");
                    bookList.add(testBook8);
                    currentSize++;
                }

                bookAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }



    void createDummy(){
        Book testBook = new Book("Book 1", "Description", 1234L, "Year", "Genre", "author");
        Book testBook2 = new Book("Book 2", "Description", 1234L, "Year", "Genre", "author");
        Book testBook3 = new Book("Book 3", "Description", 1234L, "Year", "Genre", "author");
        Book testBook4 = new Book("Book 4", "Description", 1234L, "Year", "Genre", "author");
        Book testBook5 = new Book("Book 5", "Description", 1234L, "Year", "Genre", "author");
        Book testBook6 = new Book("Book 6", "Description", 1234L, "Year", "Genre", "author");
        Book testBook7 = new Book("Book 7", "Description", 1234L, "Year", "Genre", "author");
        Book testBook8 = new Book("Book 8", "Description", 1234L, "Year", "Genre", "author");
        bookList.add(testBook);
        bookList.add(testBook2);
        bookList.add(testBook3);
        bookList.add(testBook4);
        bookList.add(testBook5);
        bookList.add(testBook6);
        bookList.add(testBook7);
        bookList.add(testBook8);
    }

}
