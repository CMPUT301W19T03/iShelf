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

/**
 * BorrowAdapter
 * Send in either:
 *
 * Allows for searching of firebase (description)
 *
 * @author : Faisal
 */
class BorrowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private ArrayList<Book> filterList = new ArrayList<Book>();
    private ArrayList<Book> originalList = new ArrayList<Book>();
    private Context bookContext;

    /**
     * Constructor
     *
     * @author : Faisal
     * @param filterList - after search
     * @param originalList - before search
     */
    public BorrowAdapter(Context bookContext, ArrayList<Book> filterList, ArrayList<Book> originalList) {
        this.filterList = filterList;
        this.originalList = originalList;
        this.bookContext = bookContext;
    }

    /**
     * BorrowViewHolder
     *
     * holds information about a book (name, description, user, rating)
     * and is clickable
     *
     * @author : Faisal
     *
     */
    public static class BorrowViewHolder extends RecyclerView.ViewHolder {
        public TextView owner;
        public TextView description;
        public TextView title;
        public ConstraintLayout borrowBody;

        public BorrowViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            owner = (TextView) view.findViewById(R.id.owner_borrow);
            description = (TextView) view.findViewById(R.id.description);
            borrowBody = (ConstraintLayout) view.findViewById(R.id.borrow_body);
        }
    }


    /**
     * onCreateViewHolder
     *
     * A regular ViewHolder
     *
     * @author : Faisal
     *
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.borrow_book_item, parent, false);
            BorrowViewHolder vh = new BorrowViewHolder(v);
            return vh;
    }


    /**
     * onBindViewHolder
     *
     * Allows for clicking on a book (takes you to Book Profile)
     * and connecting Book information to the TextViews.
     *
     * @author : Faisal
     *
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
            BorrowViewHolder borrowHolder = (BorrowViewHolder) holder;

            borrowHolder.title.setText(filterList.get(position).getName());
            borrowHolder.owner.setText(filterList.get(position).getOwner());
            borrowHolder.description.setText(filterList.get(position).getDescription());

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

    /**
     * getItemCount
     *
     * Gets the size of filterList (RecyclerView uses this for displaying the list)
     *
     * @author : Faisal
     *
     */
    @Override
    public int getItemCount() {
        return filterList.size();
    }


    /**
     * getFilter
     *
     * Based on this:
     * https://stackoverflow.com/questions/11619874/how-to-implement-getfilter-on-a-baseadapter
     * User: Omar Abdan
     *
     * https://stackoverflow.com/questions/30398247/how-to-filter-a-recyclerview-with-a-searchview
     * User: sagits
     *
     * Implements Filter
     *
     * @author : Faisal
     *
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
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
            if (item.getDescription().toLowerCase().contains(constraint)) {
                results.add(item);
            }
            if (item.getName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

    public void updateList(ArrayList<Book> list){
        originalList = list;
        filterList = list;
    }
}



