package ca.ualberta.ishelf;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/*https://www.youtube.com/watch?v=Vyqz_-sJGFk
 */

/**
 *
 * THis is the adapter for the myBooks fragmesnt
 * TODO get actual images instead of the 3 temp ones
 * @author evan
 * @edited rmnattas
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static final String TAG = "MyAdapter";

//    private ArrayList<String> mImageName = new ArrayList<>();
//    private ArrayList<String> mImages = new ArrayList<>();

    // one list that has the book objects no need
    // for separate lists for names and images @rmnattas
    private  ArrayList<Book> booksList;

    private Context mContext;

//    public MyAdapter(ArrayList<String> mImageName, ArrayList<String> mImages,ArrayList<Book> mbookList, Context mContext) {
//        this.mImageName = mImageName;
//        this.mImages = mImages;

    public MyAdapter(ArrayList<Book> booksList, Context mContext) {
        this.booksList = booksList;
        this.mContext = mContext;
    }

    /**
     * update the list of books in the adadpter
     * @param booksList new list of books
     * @author rmnattas
     */
    public void updateList (ArrayList<Book> booksList){
        this.booksList = booksList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this one inflates the view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //changes based on layout
        Log.d(TAG, "onbindviewHolder: called");

        // temp images
        ArrayList<String> tempImgs = new ArrayList<>();
        tempImgs.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        tempImgs.add("https://i.redd.it/j6myfqglup501.jpg");
        tempImgs.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        int randomImg = position%tempImgs.size();
        /**
         * bind the stuff
         */
        mholder.title.setText(filterList.get(position).getName());

        Glide.with(mContext).asBitmap().load(tempImgs.get(randomImg)).into(holder.image);
        holder.imageName.setText(booksList.get(position).getName());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( mContext, BookProfileActivity.class);
                Book book = booksList.get(position);
                intent.putExtra("Book Data", book);
                intent.putExtra("pos data", position);
                intent.putExtra("Button Visible", true);

                ((Activity) mContext).startActivityForResult(intent,1002);

            }
        });


    }


    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyAdapter", "onActivityResult");
    }

    @Override
    public int getItemCount() {
        // if 0, you would just get a blank screen
        return booksList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView imageName;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
