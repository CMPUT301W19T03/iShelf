package ca.ualberta.ishelf_lab9;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/*https://www.youtube.com/watch?v=Vyqz_-sJGFk
 */

/**
 *
 * THis is the adapter for the myBooks fragmesnt
 * TODO get actual images instead of the 3 temp ones
 * @author evan
 * @edited rmnattas
 *
 * mar 18 changed to match borrowfragment so i can search and filter - Evan
 */
public class MyBooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable{
    private static final String TAG = "MyBooksAdapter";

    private ArrayList<Book> booksList = new ArrayList<>();
    private ArrayList<Book> filterList = new ArrayList<>();
    private Context mContext;

    public MyBooksAdapter(ArrayList<Book> booksList, Context mContext, ArrayList<Book> filterList) {
        this.filterList = filterList;
        this.booksList = booksList;
        this.mContext = mContext;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView imageName;
        public TextView owner;
        public TextView description;
        public TextView title;
        public ConstraintLayout parentLayout;
        public RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            //image = itemView.findViewById(R.id.imageView2);
            //imageName = itemView.findViewById(R.id.image_name);
            parentLayout = (ConstraintLayout) itemView.findViewById(R.id.borrow_body); // just using the layout - Evan
            title = (TextView) itemView.findViewById(R.id.title);
            owner = (TextView) itemView.findViewById(R.id.owner_borrow);
            description = (TextView) itemView.findViewById(R.id.description);
            ratingBar = itemView.findViewById(R.id.ratingBar1);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this one inflates the view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.borrow_book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //changes based on layout
        Log.d(TAG, "onbindviewHolder: called");
        ViewHolder mholder = (ViewHolder) holder;

        // temp images
        ArrayList<String> tempImgs = new ArrayList<>();
        tempImgs.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        tempImgs.add("https://i.redd.it/j6myfqglup501.jpg");
        tempImgs.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        int randomImg = position%tempImgs.size();
        mholder.title.setText(filterList.get(position).getName());
        mholder.owner.setText(filterList.get(position).getOwner());
        mholder.description.setText(filterList.get(position).getDescription());
        setRating(mholder, filterList.get(position).getId().toString());
        //Glide.with(mContext).asBitmap().load(tempImgs.get(randomImg)).into(mholder.image);
        //mholder.imageName.setText(booksList.get(position).getName());
        mholder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( mContext, BookProfileActivity.class);
                Book book = booksList.get(position);
                intent.putExtra("Book Data", book);
                //intent.putExtra("Book ID", book.getId().toString());
                intent.putExtra("pos data", position);
                intent.putExtra("Button Visible", true);

                Activity bookActivity = (Activity) mContext;
                bookActivity.startActivity(intent);

            }
        });

    }

    /**
     * given a bookId and a list cell UI object, get the book object
     * from firebase and show the book rating in the list cell rating bar
     * @author rmnattas
     */
    private void setRating (final ViewHolder viewHolder, final String bookId){
        // connect to firebase
        final Database db = new Database(mContext);
        final Firebase ref = db.connect(mContext);

        // get reference to specific entry
        Firebase tempRef = ref.child("Books").child(bookId);
        // create a one time use listener to immediately access datasnapshot
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jBook = dataSnapshot.getValue(String.class);
                if (jBook != null) {
                    // Get user object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<Book>() {
                    }.getType();
                    Book book = gson.fromJson(jBook, tokenType); // here is where we get the user object

                    viewHolder.ratingBar.setRating(book.getAvgRating());

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
    }


    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyBooksAdapter", "onActivityResult");
    }

    @Override
    public int getItemCount() {
        // if 0, you would just get a blank screen
        return filterList.size();
    }

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
                    filteredResults = booksList;
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

        for (Book item : booksList) {
            if (item.getDescription().toLowerCase().contains(constraint)) {
                results.add(item);
            }
            if (item.getName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }
    /**
     * update the list of books in the adadpter
     * @param booksList new list of books
     * @author rmnattas
     * @edited Evan
     */
    public void updateList (ArrayList<Book> booksList){
        this.booksList = booksList;
        this.filterList = booksList;
    }

}