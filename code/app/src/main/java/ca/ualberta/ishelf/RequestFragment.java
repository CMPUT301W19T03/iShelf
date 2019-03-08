package ca.ualberta.ishelf;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SharedMemory;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RequestFragment extends Fragment {
    private ArrayList<Request> requestList = new ArrayList<Request>();

    private RecyclerView requestRecyclerView;
    private RecyclerView.Adapter requestAdapter;
    private RecyclerView.LayoutManager requestLayoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        //createDummy();

        requestRecyclerView = (RecyclerView) view.findViewById((R.id.request_recycler));
        requestLayoutManager = new LinearLayoutManager(view.getContext());
        requestRecyclerView.setLayoutManager(requestLayoutManager);
        requestAdapter = new RequestAdapter(view.getContext(), requestList);
        requestRecyclerView.setAdapter(requestAdapter);

        // creates a line between each individual record in the list
        // https://developer.android.com/reference/android/support/v7/widget/DividerItemDecoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requestRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        requestRecyclerView.addItemDecoration(dividerItemDecoration);

        getRequests();
        
        return view;
    }

    /**
     * get list requests for the logged in user and update list
     */
    public void getRequests(){

        // get logged in username
        String username = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
        if (username == null){
            return;
        }

        //connect to firebase
        Database db = new Database(getContext());
        Firebase fb = db.connect(getContext());
        Firebase childRef = fb.child("Users").child(username);

        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jUser = dataSnapshot.getValue(String.class);
                Log.d("jUser", jUser);
                if (jUser != null) {
                    // Get user object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<User>(){}.getType();
                    User user = gson.fromJson(jUser, tokenType); // here is where we get the user object
                    requestList.addAll(user.getListofRequests());
                    requestAdapter.notifyDataSetChanged();
                } else {
                    Log.d("FBerrorFragmentRequest", "User doesn't exist or string is empty");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }

        });
    }

    public void createDummy() {
        Book testBook = new Book("Book 1", "Description", 1234L, "Year", "Genre", "author");
        User testRequester = new User("ppl", "1234567", "someone@Email.com");
        Rating rating = new Rating(3, "Okay");
        testRequester.addRating(rating);
        Request request1 = new Request(null, testRequester, testBook);
        requestList.add(request1);
    }


}
