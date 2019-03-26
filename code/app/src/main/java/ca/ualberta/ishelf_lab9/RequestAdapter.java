package ca.ualberta.ishelf_lab9;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * adapter for the RequestFragment recycler view
 */
class RequestAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final String TAG = "RequestAdapter";
    private ArrayList<Request> requestList;
    private ArrayList<Book> requestBooks;
    private Context requestContext;

    public RequestAdapter(Context requestContext) {
        this.requestList = new ArrayList<>();
        this.requestContext = requestContext;
        this.requestBooks = new ArrayList<>();
    }

//sets the objects in the recycler ite,
    public static class RequestViewHolder extends ViewHolder {
        public TextView title;
        public TextView userName;
        public TextView state;
        public TextView type;
        public TextView lblType;
        public ConstraintLayout requestBody;
        public RatingBar requesterRatingBar;


        public RequestViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title1);
            userName = view.findViewById(R.id.user_name);
            requestBody = view.findViewById(R.id.request_body);
            state = view.findViewById(R.id.request_state);
            type = view.findViewById(R.id.request_type);
            lblType = view.findViewById(R.id.lableType);
            requesterRatingBar = view.findViewById(R.id.requesterRatingBar);
        }
    }


    //attacheds the item layout to the recycler view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.request_book_item, parent, false);
            RequestViewHolder vh = new RequestViewHolder(v);
            return vh;
    }


    public void updateList(ArrayList<Request> requests, ArrayList<Book> books){
        this.requestList = requests;
        this.requestBooks = books;
        this.notifyDataSetChanged();
    }


    //deals with if an request is clicked
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Boolean canEdit = false;
        RequestViewHolder requestHolder = (RequestViewHolder) holder;

        Log.d(TAG, "onBindViewHolder");

        // Changing to fit my implementation of Request
        requestHolder.title.setText(requestBooks.get(position).getName());

        // set state TextView
        if (requestList.get(position).getStatus() == 1) {
            // accepted
            requestHolder.state.setText("Accepted");
            requestHolder.state.setTextColor(Color.rgb(50,205,50));  // green
        } else if (requestList.get(position).getStatus() == -1) {
            // declined
            requestHolder.state.setText("Declined");
            requestHolder.state.setTextColor(Color.RED);
        } else {
            // pending
            requestHolder.state.setText("Pending");
            requestHolder.state.setTextColor(requestHolder.type.getTextColors());
        }

        final String username = requestContext.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
        if (requestList.get(position).getRequester().equals(username)){
            // the request is made by the logged in user
            requestHolder.type.setText("Requested");
            requestHolder.userName.setText(requestList.get(position).getOwner());
            requestHolder.lblType.setText("Book owner:");
            setRating(requestHolder, requestList.get(position).getOwner());
//            requestHolder.requesterRatingBar.setRating(requestList.get(position).getOwner().getOverallRating());
        }else if (requestList.get(position).getOwner().equals(username)){
            // the request is received from other user
            requestHolder.type.setText("Received");
            requestHolder.userName.setText(requestList.get(position).getRequester());
            requestHolder.lblType.setText("Requested By:");

            // set showing edit/delete button in book profile
            if (requestBooks.get(position).getTransition() == 0){
                canEdit = true;
            }

            setRating(requestHolder, requestList.get(position).getRequester());
//            requestHolder.requesterRatingBar.setRating(requestList.get(position).getRequester().getOverallRating());
        }

        requestHolder.requestBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requestContext, BookProfileActivity.class);
                intent.putExtra("Book Data", requestBooks.get(position));
                requestContext.startActivity(intent);
            }
        });


    }

    /**
     * given a username and a list cell UI object, get the user object
     * from firebase and show the user rating in the list cell ratingBar
     * @author rmnattas
     */
    private void setRating (final RequestViewHolder requestViewHolder, final String username){
        // connect to firebase
        final Database db = new Database(requestContext);
        final Firebase ref = db.connect(requestContext);

        // get reference to specific entry
        Firebase tempRef = ref.child("Users").child(username);
        // create a one time use listener to immediately access datasnapshot
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jUser = dataSnapshot.getValue(String.class);
                if (jUser != null) {
                    // Get user object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<User>() {
                    }.getType();
                    User user = gson.fromJson(jUser, tokenType); // here is where we get the user object

                    requestViewHolder.requesterRatingBar.setRating(user.getOverallRating());

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount = " + Integer.toString(requestBooks.size()));
        return requestBooks.size();
    }

}
