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
import java.util.Date;

/**
 * This is the adapter for the List of requests for the recycler view
 * @author : Randal Kimpinski
 */
public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.ViewHolder>{
    private static final String TAG = "RatingRecyclerViewAdapter";
    // Array holding the names of the books being requested
    private ArrayList<Rating> mRatings = new ArrayList<>();
    // potentially add new array list here
    private Context mContext;

    /**
     * Assigns the arrays that we pass in to variables so that they can
     * later be assigned to their appropriate views
     * @author : Randal Kimpinski
     */
    public RatingsAdapter(Context context, ArrayList<Rating> ratings) {
        mContext = context;
        mRatings = ratings;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
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
        holder.tvReviewerName.setText(mRatings.get(position).getReviewer());
        holder.tvDate.setText(mRatings.get(position).getDate().toString());
        holder.rbRating.setRating(mRatings.get(position).getRating());
        holder.tvTextComment.setText(mRatings.get(position).getComment());
    }

    /**
     * How we decide how long the recyclerView is
     * @return int size of our array and so the number of recycler view items
     * @author : Randal Kimpinski
     */
    @Override
    public int getItemCount() {
        // necassary to actually display items
        return mRatings.size();
    }

    /**
     * This viewholder is for each individual entry in our recycler view
     * Must contain all the appropriate view items
     * @author : Randal Kimpinski
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvReviewerName;
        TextView tvDate;
        RatingBar rbRating;
        TextView tvTextComment;

        /**
         * sets the variables in our viewholder to the appropriate view items
         * @param itemView
         * @author : Randal Kimpinski
         */
        public ViewHolder(View itemView) {
            super(itemView);
            tvReviewerName = itemView.findViewById(R.id.tvReviewerName);
            tvDate = itemView.findViewById(R.id.tvDate);
            rbRating = itemView.findViewById(R.id.rbRating);
            tvTextComment = itemView.findViewById(R.id.tvTextComment);
        }

    }

}



