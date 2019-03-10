package ca.ualberta.ishelf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;


class RequestAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<Request> requestList = new ArrayList<Request>();
    private Context requestContext;

    public RequestAdapter(Context requestContext, ArrayList<Request> requestList) {
        this.requestList = requestList;
        this.requestContext = requestContext;
    }

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
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.request_book_item, parent, false);
            RequestViewHolder vh = new RequestViewHolder(v);
            return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
            RequestViewHolder requestHolder = (RequestViewHolder) holder;

            // Changing to fit my implementation of Request
            requestHolder.title.setText(requestList.get(position).getBookId().toString());
            requestHolder.userName.setText(requestList.get(position).getRequester());
            //requestHolder.requesterRatingBar.setRating(requestList.get(position).getRequester().getOverallRating());


            requestHolder.requestBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent((Activity) requestContext, BookProfileActivity.class);
//                    Book book = requestList.get(position);
//                    intent.putExtra("Book Data", book);
//                    //intent.putExtra("Check Data", true);
//                    Activity bookActivity = (Activity) requestContext;
//                    bookActivity.startActivity(intent);
                }
            });
        }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}
