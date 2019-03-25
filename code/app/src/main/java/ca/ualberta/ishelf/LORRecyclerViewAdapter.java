/**
 * LORRecyclerViewAdapter
 * Version 1
 * March 10, 2019
 * @author : Randal Kimpinski
 */
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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
/**
* This is the adapter for the List of requests for the recycler view
 * @author : Randal Kimpinski
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
    private ArrayList<Integer> mStatus = new ArrayList<>();
    private Context mContext;

    /**
     * Assigns the arrays that we pass in to variables so that they can
     * later be assigned to their appropriate views
     * @param context the context that we create the recycler in
     * @param imageNames the names of the requesters
     * @param myRatings the value of the requester ratings
     * @param bookNames the name of the book they are requesting
     * @param myStatus the status of the book they are requesting
     * @author : Randal Kimpinski
     */
    public LORRecyclerViewAdapter(Context context, ArrayList<String> imageNames,
                              ArrayList<Float> myRatings, ArrayList<String> bookNames
                            , ArrayList<Integer> myStatus) {
        mContext = context;
        mImageNames = imageNames;
        bRatings = myRatings;
        mBookNames = bookNames;
        mStatus = myStatus;
    }

    /**
     * Inflates the view so that we can display our recyclerView
     * @param parent viewgroup parent object
     * @param i position
     * @return holder object
     * @author : Randal Kimpinski
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
     * @param holder viewholder
     * @param position position in view holder
     * @author : Randal Kimpinski
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int fposition = position;
        Log.d(TAG, "onBindViewHolder: called");
        // Set the contents of each recycler view item
        holder.testText.setText(mImageNames.get(position));
        holder.bookName.setText(mBookNames.get(position));
        holder.bRating.setRating(bRatings.get(position));
        //TODO set location button to visible or invisible
        if (mStatus.get(position) == 0) {
            // If the request hasn't been accepted, display the accept/decline buttons
            holder.locationButton.setVisibility(View.INVISIBLE);
            //holder.locationButton.setEnabled(false);
            holder.locationButton.setClickable(false);
            holder.acceptButton.setVisibility(View.VISIBLE);
            //holder.acceptButton.setEnabled(true);
            holder.acceptButton.setClickable(true);
            holder.declineButton.setVisibility(View.VISIBLE);
            //holder.declineButton.setEnabled(true);
            holder.declineButton.setClickable(true);
        } else {
            // If the request has been accepted, display location button
            holder.locationButton.setVisibility(View.INVISIBLE);
            //holder.locationButton.setEnabled(true);
            holder.locationButton.setClickable(false);
            holder.acceptButton.setVisibility(View.INVISIBLE);
            //holder.acceptButton.setEnabled(false);
            holder.acceptButton.setClickable(false);
            holder.declineButton.setVisibility(View.INVISIBLE);
            //holder.declineButton.setEnabled(false);
            holder.declineButton.setClickable(false);
        }

        /**
         * This is called when anywhere in the recyclerView item is pressed
         * @author : Randal Kimpinski
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
         * @author : Randal Kimpinski
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
         * @author : Randal Kimpinski
         */
        holder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick declineButton: clicked on: " + fposition);
                ((ListOfRequestsActivity) mContext).declineRequest(fposition);
            }
        });
        /**
         * This is called when the decline button is pressed
         * @author : Randal Kimpinski
         */
        holder.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick locationButton: clicked on: " + fposition);
                ((ListOfRequestsActivity) mContext).locationButton(fposition);
            }
        });
    }

    /**
     * How we decide how long the recyclerView is
     * @return int size of our array and so the number of recycler view items
     * @author : Randal Kimpinski
     */
    @Override
    public int getItemCount() {
        // necassary to actually display items
        return mImageNames.size();
    }

    /**
     * This viewholder is for each individual entry in our recycler view
     * Must contain all the appropriate view items
     * @author : Randal Kimpinski
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView testText;
        TextView bookName;
        Button acceptButton;
        Button declineButton;
        Button locationButton;
        RatingBar bRating;
        ConstraintLayout parentLayout;
        //RelativeLayout parentLayout;

        /**
         * sets the variables in our viewholder to the appropriate view items
         * @param itemView
         * @author : Randal Kimpinski
         */
        public ViewHolder(View itemView) {
            super(itemView);
            testText = itemView.findViewById(R.id.textView);
            bookName = itemView.findViewById(R.id.bookNameView);
            acceptButton = itemView.findViewById(R.id.AButton);
            declineButton = itemView.findViewById(R.id.DButton);
            locationButton = itemView.findViewById(R.id.locationButton);
            bRating = itemView.findViewById(R.id.ratingBar2);
            parentLayout = itemView.findViewById(R.id.LORParent);
            //declineButton.setTag(1, itemView);
            //acceptButton.setTag(2, itemView);
        }

    }

}



