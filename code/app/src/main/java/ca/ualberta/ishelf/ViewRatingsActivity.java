package ca.ualberta.ishelf;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.UUID;

public class ViewRatingsActivity extends AppCompatActivity {
    // Tag for debugging
    private String TAG = "ViewRatingsActivity";
    // List of strings that are displayed in requests
    private ArrayList<String> mRatings = new ArrayList<>();
    // RecyclerView view and adapter
    public RecyclerView recyclerView;
    // RecylerView adapter
    RatingsAdapter adapter;
    // Holds the User of the user being viewed in this activity
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ratings);
        Log.d(TAG, "onCreate");


        // get the reference to RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.ratingsRecyclerView);
        // set a LinearLayoutManager with default orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        // Initialize the recyclerView
        initRecyclerView();
    }

    /**
     * Initialize the recycler view
     * This is when all the entries are set to their appropriate values
     * This method is called again later to simplify updating the data
     * @author : Randal Kimpinski
     */
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView:");
        adapter = new RatingsAdapter(this, mRatings);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
