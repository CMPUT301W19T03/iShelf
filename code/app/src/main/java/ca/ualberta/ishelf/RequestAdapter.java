package ca.ualberta.ishelf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
        public ConstraintLayout requestBody;


        public RequestViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title1);
            userName = (TextView) view.findViewById(R.id.user_name);
            requestBody = (ConstraintLayout) view.findViewById(R.id.request_body);
//            requesterRatingBar = (RatingBar) view.findViewById(R.id.requesterRatingBar);
        }
    }


    //attacheds the item layout to the recycler view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("j!!!Book2222", "test");
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.request_book_item, parent, false);
            RequestViewHolder vh = new RequestViewHolder(v);
            return vh;
    }


    public void updateList(ArrayList<Request> requests, ArrayList<Book> books){
        this.requestList = requests;
        this.requestBooks = books;
    }


    //deals with if an request is clicked
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
            RequestViewHolder requestHolder = (RequestViewHolder) holder;

        Log.d("j!!!Book", "test");

            // Changing to fit my implementation of Request
            requestHolder.title.setText(requestBooks.get(position).getName());
            requestHolder.userName.setText(requestList.get(position).getRequester());
            //requestHolder.requesterRatingBar.setRating(requestList.get(position).getRequester().getOverallRating());


//            requestHolder.requestBody.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent((Activity) requestContext, BookProfileActivity.class);
//                    Book book = requestList.get(position);
//                    intent.putExtra("Book Data", book);
//                    //intent.putExtra("Check Data", true);
//                    Activity bookActivity = (Activity) requestContext;
//                    bookActivity.startActivity(intent);
//                }
//            });
        }

    @Override
    public int getItemCount() {
        Log.d("j!!!Book3", Integer.toString(requestBooks.size()));
        return requestBooks.size();
    }
}
