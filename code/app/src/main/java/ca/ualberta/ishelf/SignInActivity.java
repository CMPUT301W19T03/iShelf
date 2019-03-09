package ca.ualberta.ishelf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * SignInActivity
 *
 * TODO: (low-priority) add a rudimentary password function
 * @author rmnattas
 */
public class SignInActivity extends AppCompatActivity {

//    private FirebaseAuth mAuth;
//    private GoogleSignInClient mGoogleSignInClient;
//    private static final String TAG = "GoogleActivity";
//    private static final int RC_SIGN_IN = 9001;

    EditText txtusername;
    Button btnsingin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Get UI elements
        txtusername = findViewById(R.id.txtusername);
        btnsingin = findViewById(R.id.btnsignin);

    }

    /**
     * Called when sign in button is clicked
     * Confirms the sign in and call signedIn to complete
     * the necessary steps in UserPreferences and Firebase
     * @param view
     */
    public void singinClicked(View view){
        // For now, only use the entered username without a password
        signedIn(txtusername.getText().toString());
    }

    /**
     * registerClicked
     * Called when the Register button is clicked
     * Takes the new user to the EditProfileActivity
     * for them to register for a new account
     * @author Jeremy
     * @param view
     */
    public void registerClicked(View view){
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("Registering", true);
        startActivity(intent);
    }

    /**
     * Called when user successfully sign in
     * Sets UserPreferences and check if user in Firebase database.
     * If not, adds their User object to Firebase.
     * @param username the username for the logged-in user
     */
    private void signedIn(final String username){
        // connect to firebase
        final Database db = new Database(this);
        final Firebase ref = db.connect(this);

        Firebase tempRef = ref.child("Users");
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean found = false;  // true if user found in firebase

                // look for user in firebase
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    if (d.getKey().equals(username)){    // user found
                        Log.d("User", "User found");

                        // update username in UserPreferences
                        SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).edit();
                        editor.putString("username", username).apply();

                        found = true;

                        // go to previous activity
                        SignInActivity.super.onBackPressed();
                    }
                }

                if (!found) {
                    // user not in firebase => new user, add user to Firebase
                    Log.d("User", "User not in firebase");
                    // user not found, ask user to register
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "User Not Found\nPlease Register",
                            Toast.LENGTH_LONG);
                    toast.show();
                }

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_in);
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("1075824538974-sk0elkp53ff9vtlnm4rtm5nd1bpkliv0.apps.googleusercontent.com")
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        FirebaseApp.initializeApp(this);
//        mAuth = FirebaseAuth.getInstance();
//
//        SignInButton googleButton = findViewById(R.id.sign_in_button);
//        googleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("SignIn","Button Clicked");
//                mGoogleSignInClient.signOut();
//                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//                startActivityForResult(signInIntent, RC_SIGN_IN);
//            }
//        });
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                Log.d("LoggedIn", "ActivityResult");
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//                Toast.makeText(SignInActivity.this, "Google SignIn Error", Toast.LENGTH_LONG).show();
//                // TODO
//            }
//        }
//    }
//
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
//
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//
//                            String username = user.getEmail().substring(0, user.getEmail().indexOf('@'));
//                            Log.d("LoggedIn", username);
//                            SignedIn(username);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(SignInActivity.this, "Failed to SignIn", Toast.LENGTH_LONG).show();
//                            // TODO
//                        }
//                    }
//                });
//    }
//
//

}

