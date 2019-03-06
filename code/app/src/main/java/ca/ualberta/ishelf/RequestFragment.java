package ca.ualberta.ishelf;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        createDummy();
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
        
        return view;
    }

    public void createDummy() {
        Book testBook = new Book("Book 1", "Description", 1234L, "Year", "Genre", "author");
        Request request1 = new Request(null, null, testBook);
        requestList.add(request1);
    }


}
