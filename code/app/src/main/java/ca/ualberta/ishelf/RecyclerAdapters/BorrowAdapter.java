package ca.ualberta.ishelf.RecyclerAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ca.ualberta.ishelf.Models.Book;
import ca.ualberta.ishelf.BookProfileActivity;
import ca.ualberta.ishelf.Models.Database;
import ca.ualberta.ishelf.R;

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

    // FireBase stuff
    FirebaseStorage storage;
    StorageReference storageReference;

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

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

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
        public RatingBar ratingBar;
        public ImageView imageCover;

        public BorrowViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            owner = (TextView) view.findViewById(R.id.owner_borrow);
            description = (TextView) view.findViewById(R.id.description);
            borrowBody = (ConstraintLayout) view.findViewById(R.id.borrow_body);
            ratingBar = view.findViewById(R.id.ratingBar1);
            imageCover = (ImageView) view.findViewById(R.id.cover_borrow);
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

            setRating(borrowHolder, filterList.get(position).getId().toString());

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

            Book currentBook = originalList.get(position);
            ArrayList<String> images = currentBook.getGalleryImages();

            if (images.size() > 0) {

                String image = images.get(0);
                StorageReference ref = storageReference.child(image);
                final long ONE_MEGABYTE = 1024 * 1024;
                ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        borrowHolder.imageCover.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
    }


    /**
     * given a bookId and a list cell UI object, get the book object
     * from firebase and show the book rating in the list cell rating bar
     * @author rmnattas
     */
    private void setRating (final BorrowViewHolder borrowViewHolder, final String bookId){
        // connect to firebase
        final Database db = new Database(bookContext);
        final Firebase ref = db.connect(bookContext);

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

                    borrowViewHolder.ratingBar.setRating(book.getAvgRating());

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
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



