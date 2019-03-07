package ca.ualberta.ishelf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;





class BorrowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Book> bookList = new ArrayList<Book>();
    private Context bookContext;


    public BorrowAdapter(Context bookContext, ArrayList<Book> bookList) {
        this.bookList = bookList;
        this.bookContext = bookContext;
    }

    public static class BorrowViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ConstraintLayout borrowBody;

        public BorrowViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            borrowBody = (ConstraintLayout) view.findViewById(R.id.borrow_body);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.borrow_book_item, parent, false);
            BorrowViewHolder vh = new BorrowViewHolder(v);
            return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
            BorrowViewHolder borrowHolder = (BorrowViewHolder) holder;

            borrowHolder.title.setText(bookList.get(position).getName());

            borrowHolder.borrowBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent((Activity) bookContext, BookProfileActivity.class);
                    Book book = bookList.get(position);
                    intent.putExtra("Book Data", book);
                    //intent.putExtra("Check Data", true);
                    Activity bookActivity = (Activity) bookContext;
                    bookActivity.startActivity(intent);
                }
            });
        }

    @Override
    public int getItemCount() {
        return bookList.size();
    }


}
