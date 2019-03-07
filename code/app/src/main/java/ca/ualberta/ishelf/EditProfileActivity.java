package ca.ualberta.ishelf;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);

        // TODO: get the user from firebase
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
                    Type tokenType = new TypeToken<User>(){}.getType();
                    User user = gson.fromJson(jUser, tokenType); // here is where we get the user object

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

        /*
        // If User doesn't exist, or string is empty, return null
        Log.d("next", "this is where code jumps to next");
        Log.d("SizeAfter", String.valueOf(userList.size()));
        try {
            //Log.d("SizeLoop", String.valueOf(userList.size()));
            return userList.get(0);
        } catch (Exception e) {
            Log.d("FBerror", "No user exists in firebase with that username");
        }
        */

        /*
        // fill the fields with their current info
        editName.setText(user.getUsername());
        editEmail.setText(user.getEmail());
        editPhone.setText(user.getPhoneNum());
        */
    }

    public void saveButton(View v){
        // when the "Save" button gets hit
        String oldUsername = user.getUsername();

        String newUsername = editName.getText().toString();
        String newEmail = editEmail.getText().toString();
        String newPhone = editPhone.getText().toString();

        user.setUsername(newUsername);
        user.setEmail(newEmail);
        user.setPhoneNum(newPhone);

        // update username in UserPreferences
        SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).edit();
        //editor.putString("username", newUsername).apply();

        // update the user in the database
        Database database = new Database(this);
        //database.editUser(oldUsername, user);

        finish();
    }
}
