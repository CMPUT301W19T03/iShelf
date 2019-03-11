package ca.ualberta.ishelf;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
/*
THis is the adapter for the List of requests recycler view

 */

public class LORRecyclerViewAdapter extends RecyclerView.Adapter<LORRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "LORRecyclerViewAdapter";
    // Array holding request information (User wants to borrow this book)
    private ArrayList<String> mImageNames = new ArrayList<>();
    // Array holding the rating of the borrowers
    private ArrayList<Float> bRatings = new ArrayList<>();
    // Array holding the names of the books being requested
    private ArrayList<String> mBookNames = new ArrayList<>();
    // potentially add new array list here
    private Context mContext;

    public LORRecyclerViewAdapter(Context context, ArrayList<String> imageNames,
                                  ArrayList<Float> myRatings, ArrayList<String> bookNames) {
        mContext = context;
        mImageNames = imageNames;
        bRatings = myRatings;
        mBookNames = bookNames;
    }

    /**
     * Inflates the view so that we can display our recyclerView
     * @param parent
     * @param i
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_requests_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // Changes based on layouts and what we want them to look like

    /**
     * We define the functions that are called when the buttons are pressed in
     * our recycler view. We have the accept and decline button, aswell as a
     * rating bar and a textView
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int fposition = position;
        Log.d(TAG, "onBindViewHolder: called");
        // Set the contents of each recycler view item
        holder.testText.setText(mImageNames.get(position));
        holder.bookName.setText(mBookNames.get(position));
        holder.bRating.setRating(bRatings.get(position));


        /**
         * This is called when anywhere in the recyclerView item is pressed
         */
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mImageNames.get(fposition));
                Toast.makeText(mContext, mImageNames.get(fposition), Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * This is called when the accept button is pressed
         */
        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick acceptButton: clicked on: " + fposition);
                ((ListOfRequestsActivity) mContext).acceptRequest(fposition);
            }
        });
        /**
         * This is called when the decline button is pressed
         */
        holder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick declineButton: clicked on: " + fposition);
                ((ListOfRequestsActivity) mContext).declineRequest(fposition);
            }
        });
        // I think I need to add the button stuff here
    }

    /**
     * How we decide how long the recyclerView is
     * @return
     */
    @Override
    public int getItemCount() {
        // necassary to actually display items
        return mImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView testText;
        TextView bookName;
        Button acceptButton;
        Button declineButton;
        RatingBar bRating;
        ConstraintLayout parentLayout;
        //RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            testText = itemView.findViewById(R.id.textView);
            bookName = itemView.findViewById(R.id.bookNameView);
            acceptButton = itemView.findViewById(R.id.AButton);
            declineButton = itemView.findViewById(R.id.DButton);
            bRating = itemView.findViewById(R.id.ratingBar2);
            parentLayout = itemView.findViewById(R.id.LORParent);
            //declineButton.setTag(1, itemView);
            //acceptButton.setTag(2, itemView);
        }

    }

}



