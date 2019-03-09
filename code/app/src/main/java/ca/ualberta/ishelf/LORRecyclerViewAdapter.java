package ca.ualberta.ishelf;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LORRecyclerViewAdapter extends RecyclerView.Adapter<LORRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "LORRecyclerViewAdapter";
    private ArrayList<String> mImageNames = new ArrayList<>();
    // potentially add new array list here
    private Context mContext;

    public LORRecyclerViewAdapter(Context context, ArrayList<String> imageNames) {
        mImageNames = imageNames;
        mContext = context;
    }



    // Inflates the view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_requests_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // Changes based on layouts and what we want them to look like
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int fposition = position;
        Log.d(TAG, "onBindViewHolder: called");
        holder.testText.setText(mImageNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mImageNames.get(fposition));
                Toast.makeText(mContext, mImageNames.get(fposition), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        // necassary to actually display items
        return mImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView testText;
        ConstraintLayout parentLayout;
        //RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            testText = itemView.findViewById(R.id.textView);
            parentLayout = itemView.findViewById(R.id.LORParent);

        }

    }

}



