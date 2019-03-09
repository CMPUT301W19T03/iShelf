package ca.ualberta.ishelf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * EditProfileActivity
 *
 * author: Jeremy
 */
public class EditProfileActivity extends AppCompatActivity {
    private EditText editName;
    private EditText editEmail;
    private EditText editPhone;
    private String username;
    private User user;
    private final String link = "https://ishelf-bb4e7.firebaseio.com";
    private Firebase ref;
    private boolean registering = false;
    private String TAG = "EditProfileActivity";
    private String oldUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);

        if (getIntent().hasExtra("Registering")) {
            registering = getIntent().getExtras().getBoolean("Registering");
        }

        if (registering){
            Log.d(TAG, "onCreate: Registering");
        } else {
            Log.d(TAG, "onCreate: Signed-in User, Retrieving from Firebase");
            username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

            // Retrive the user's info from Firebase
            Firebase.setAndroidContext(this);
            ref = new Firebase(link);

            // get reference to specific entry
            Firebase tempRef = ref.child("Users").child(username);
            final ArrayList<User> userList = new ArrayList<User>();
            // create a one time use listener to immediately access datasnapshot
            tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String jUser = dataSnapshot.getValue(String.class);
                    Log.d("jUser", jUser);
                    if (jUser != null) {
                        // Get user object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<User>() {
                        }.getType();
                        user = gson.fromJson(jUser, tokenType); // here is where we get the user object

                        // fill the fields with their current info
                        editName.setText(user.getUsername());
                        editEmail.setText(user.getEmail());
                        editPhone.setText(user.getPhoneNum());

                        Log.d("Confirm", user.getUsername());
                        userList.add(user);
                    } else {
                        Log.d("FBerror1", "User doesn't exist or string is empty");
                    }
                    Log.d("Size", String.valueOf(userList.size()));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    return;
                }
            });
        }

    }

    public void saveButton(View v){
        // when the "Save" button gets hit
        String newUsername = editName.getText().toString();
        String newEmail = editEmail.getText().toString();
        String newPhone = editPhone.getText().toString();

        if (registering){
            // Save the new username to Shared Preferences
            user = new User();
        } else {
            oldUsername = user.getUsername();
        }

        user.setUsername(newUsername);
        user.setEmail(newEmail);
        user.setPhoneNum(newPhone);

        SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).edit();
        editor.putString("username", newUsername).apply();

        Database database = new Database(this);
        if (registering){
            database.addUser(user);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            database.editUser(oldUsername, user);

            // add result for viewProfile
            Intent intent = new Intent();
            intent.putExtra("User", user);
            setResult(3, intent);
            finish();
        }
    }
}
