package ca.ualberta.ishelf_lab9;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


/**
 * VIewImageActivity
 *
 * Shows an expanded view of the image
 * Allows for ther user to delete the image
 *
 * TODO_: Implement with TouchImageView for allow for dynamic image manipulation
 *
 * @author : Faisal
 */
public class ViewImageActivity extends AppCompatActivity {

    private ImageView fullImage;
    private Button deleteButton;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        fullImage = (ImageView) findViewById(R.id.full_image);
        Bundle extras = getIntent().getExtras();
        Bitmap bitMap = (Bitmap) extras.getParcelable("sent_image");
        position = extras.getInt("position");

        fullImage.setImageBitmap(bitMap);

        // set up add button
        deleteButton = (Button) findViewById((R.id.delete_image_button));

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                extras.putInt("position", position);
                Intent intent = new Intent(view.getContext(), GalleryActivity.class);
                intent.putExtras(extras);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
