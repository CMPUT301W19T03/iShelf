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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);

        Database database = new Database();

        // TODO: get the user from firebase

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
        editor.putString("username", newUsername).apply();

        // update the user in the database
        Database database = new Database();
        database.editUser(oldUsername, user);

        finish();
    }
}
