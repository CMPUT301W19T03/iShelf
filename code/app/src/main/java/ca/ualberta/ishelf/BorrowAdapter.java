package ca.ualberta.ishelf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class BorrowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private ArrayList<Book> filterList = new ArrayList<Book>();
    private ArrayList<Book> originalList = new ArrayList<Book>();
    private Context bookContext;

    public BorrowAdapter(Context bookContext, ArrayList<Book> filterList, ArrayList<Book> originalList) {
        this.filterList = filterList;
        this.originalList = originalList;
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

            borrowHolder.title.setText(filterList.get(position).getName());

            borrowHolder.borrowBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent((Activity) bookContext, BookProfileActivity.class);
                    Book book = filterList.get(position);
                    intent.putExtra("Book Data", book);
                    Activity bookActivity = (Activity) bookContext;
                    bookActivity.startActivity(intent);
                }
            });
        }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterList = (ArrayList<Book>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Book> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = originalList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected List<Book> getFilteredResults(String constraint) {
        List<Book> results = new ArrayList<>();

        for (Book item : originalList) {
            if (item.getName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        Log.d("test","trst");
        return results;
    }

    public void updateList(ArrayList<Book> list){
        originalList = list;
        filterList = list;

    }

}



