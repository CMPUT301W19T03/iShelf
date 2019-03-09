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


public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter nAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Notification> notificationList = new ArrayList<Notification>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        createDummy();

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

    public  void getNotifications(){}



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
        Notification notification5 = new Notification(null,"Book Request Accepted", null);
        notificationList.add(notification5);

        Notification notification6 = new Notification(null,"Book Request Rejected", null);
        notificationList.add(notification6);


        Notification notification7 = new Notification(null,"Book Request Recieved", null);

        notificationList.add(notification7);
        Notification notification8 = new Notification(null,"Book Request Accepted", null);
        notificationList.add(notification8);

        Notification notification9 = new Notification(null,"Book Request Rejected", null);
        notificationList.add(notification9);


        Notification notification10 = new Notification(null,"Book Request Recieved", null);

        notificationList.add(notification10);
        Notification notification11 = new Notification(null,"Book Request Accepted", null);
        notificationList.add(notification11);
        Notification notification12 = new Notification(null,"Book Request Rejected", null);
        notificationList.add(notification12);



    }


}
