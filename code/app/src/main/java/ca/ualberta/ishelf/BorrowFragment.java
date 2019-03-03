package ca.ualberta.ishelf;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class BorrowFragment extends Fragment {
    private ArrayList<Book> bookList = new ArrayList<Book>();

    private RecyclerView bookRecyclerView;
    private RecyclerView.Adapter bookAdapter;
    private RecyclerView.LayoutManager bookLayoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_borrow, container, false);

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

        bookRecyclerView = (RecyclerView) view.findViewById((R.id.borrow_recycler));
        bookLayoutManager = new LinearLayoutManager(view.getContext());
        bookRecyclerView.setLayoutManager(bookLayoutManager);
        bookAdapter = new BorrowAdapter(view.getContext(), bookList);
        bookRecyclerView.setAdapter(bookAdapter);


        // creates a line between each individual record in the list
        // https://developer.android.com/reference/android/support/v7/widget/DividerItemDecoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(bookRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        bookRecyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

}
