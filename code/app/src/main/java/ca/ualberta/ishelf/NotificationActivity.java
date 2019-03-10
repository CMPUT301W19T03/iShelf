package ca.ualberta.ishelf;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.app.SearchManager;
import android.util.Log;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
 * Notifications Activity
 * US 04.03.01
 *- As an owner, I want to be notified of a request.
 * This Activity notifiesso owner can reply in a timely manner of a request
 *
 * US 05.03.01
 * As a borrower, I want to be notified of an accepted request.
 * the borrower wants to know when his request of borrowing got accepted
 *
 * This activity notifies the borrower if his request has been accepted or not
 *
 * TODO get only the logged in user notifications from firebase
 * @author mehrab
 */
public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotificationAdapter nAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Notification> notificationList = new ArrayList<Notification>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // createDummy();

        recyclerView = (RecyclerView) findViewById(R.id.notification_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nAdapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(nAdapter);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                LinearLayoutManager.VERTICAL);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);


        getNotifications();

    }

    /**
     * get notifications from firebase
     * @author rmnattas
     */
    public  void getNotifications(){
        //connect to firebase
        Database db = new Database(this);
        Firebase fb = db.connect(this);
        Firebase childRef = fb.child("Notifications");

        // get logged in username
        final String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    String jNotification = d.getValue(String.class);
                    if (jNotification != null) {
                        // Get notification object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<Notification>() {
                        }.getType();
                        Notification notification = gson.fromJson(jNotification, tokenType); // here is where we get the user object
                        // if (notification.getUserName().equals(currentUsername)){
                            notificationList.add(notification);
                        // }
                    } else {
                        Log.d("NotificationActivity", "Notification ERROR");
                    }
                }
                nAdapter.updateList(notificationList);
                nAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }

        });
    }



    public void createDummy() {
        Notification notification = new Notification(null,"Welcome to iShelf", null);
        notificationList.add(notification);
        Notification notification1 = new Notification(null,"Book Request Recieved", null);

        notificationList.add(notification1);
        Notification notification2 = new Notification(null,"Book Request Accepted", null);
        notificationList.add(notification2);

        Notification notification3 = new Notification(null,"Book Request Rejected", null);
        notificationList.add(notification3);


        Notification notification4 = new Notification(null,"Book Request Recieved", null);

        notificationList.add(notification4);


    }


}
