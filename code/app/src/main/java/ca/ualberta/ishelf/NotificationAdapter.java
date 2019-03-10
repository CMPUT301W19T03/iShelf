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

//adapter for the notification recycler view
//works like all the other adapters in the project.
class NotificationAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<Notification> notificationList = new ArrayList<Notification>();


    public NotificationAdapter( ArrayList<Notification> notificationList) {
        this.notificationList = notificationList;

    }

    public static class NotificationViewHolder extends ViewHolder {
        public TextView noti;
        public TextView userName;
        public ConstraintLayout notificationBody;


        public NotificationViewHolder(View view) {
            super(view);
            noti = (TextView) view.findViewById(R.id.noti);

            notificationBody = (ConstraintLayout) view.findViewById(R.id.notification_body);
//            notificationerRatingBar = (RatingBar) view.findViewById(R.id.notificationerRatingBar);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        NotificationViewHolder vh = new NotificationViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        NotificationViewHolder notificationHolder = (NotificationViewHolder) holder;

        notificationHolder.noti.setText(notificationList.get(position).getText());

        //notificationHolder.notificationerRatingBar.setRating(notificationList.get(position).getNotificationer().getOverallRating());


        notificationHolder.notificationBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Intent intent = new Intent((Activity) notificationContext, BookProfileActivity.class);
//                    Book book = notificationList.get(position);
//                    intent.putExtra("Book Data", book);
//                    //intent.putExtra("Check Data", true);
//                    Activity bookActivity = (Activity) notificationContext;
//                    bookActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public void updateList(ArrayList<Notification> notificationList){
        this.notificationList = notificationList;
    }

}
