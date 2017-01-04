package ahh.com.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import ahh.com.app.beans.User;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;

public class UserAuth extends AppCompatActivity {

    private TextView idp_tv;
    //    private Button loginButton;
    private Button signoutButton;
    private Button deletButton;
    private ImageView profileMv;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private boolean isLoggingOut = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");


        deletButton = (Button) findViewById(R.id.delete);
        signoutButton = (Button) findViewById(R.id.sign_out);
        idp_tv = (TextView) findViewById(R.id.textView);
        profileMv = (ImageView) findViewById(R.id.profileImg);
        deletButton.setOnClickListener(new DeleteUserAccount());
        signoutButton.setOnClickListener(new SignoutUser());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("UserAuth > onCreate", "onAuthStateChanged:signed_in:" + user.getUid());
                    showSnackbar("Signed in");
                } else if (user == null && isLoggingOut) {
                    // User is signed out
                    Log.d("UserAuth > onCreate", "onAuthStateChanged:signed_out");
                    showSnackbar("Signed_out");
                    cleareUserDara();


                }

            }


        };
        checkAuthentication();

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }

    private void cleareUserDara() {
        if (auth.getCurrentUser() == null) {
            getSharedPreferences(getString(R.string.sp_member_data), getApplicationContext().MODE_PRIVATE).edit().clear().commit();
            finish();
        }
    }

    private String checkNull(String value) {
        if (value != null) {
            return value;
        } else {
            return null;
        }
    }

    private Uri checkNull(Uri value) {
        if (value != null) {
            return value;
        } else {
            return null;
        }
    }

    private void checkAuthentication() {

        if (auth.getCurrentUser() != null) {
            // already signed in

//            Toast.makeText(getApplicationContext(), "already signed in", Toast.LENGTH_SHORT).show();
            setUserData(auth.getCurrentUser());
        } else {
            // not signed in

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .build(),
                    RC_SIGN_IN);

        }
    }


    private void setUserData(FirebaseUser currentUser) {
        if (currentUser != null) {
            String displayName = checkNull(currentUser.getDisplayName());
            String emailId = checkNull(currentUser.getEmail());
            Uri photoUrl = checkNull(currentUser.getPhotoUrl());
            String uid = checkNull(currentUser.getUid());
            //Loading image from url into imageView
            Glide.with(this)
                    .load(photoUrl)
                    .into(profileMv);

            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.sp_member_data), getApplicationContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.sp_member_imgurl), photoUrl + "");
            editor.putString(getString(R.string.sp_member_email), emailId);
            editor.putString(getString(R.string.sp_member_name), displayName);
            editor.commit();
            Log.d("checkAuthentication ", sharedPref.toString() + " , \n" + photoUrl + " , \n" + displayName + " , \n" + emailId);

            idp_tv.setText("Name : " + displayName + " \nemail : " + emailId + " \npotoUrl : " + photoUrl + " \nuid : " + uid);

        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // user is signed in!

            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);

            idp_tv.setText(idpResponse + "");
            if (auth.getCurrentUser() != null) {
                FirebaseUser currentUser = auth.getCurrentUser();
                setUserData(currentUser);
                addMember(currentUser.getDisplayName(), currentUser.getUid(), currentUser.getEmail());
            }

            return;
        }

        // Sign in canceled
        if (resultCode == RESULT_CANCELED) {
            showSnackbar("sign_in_cancelled");
            return;
        }

        // No network
        if (resultCode == ResultCodes.RESULT_NO_NETWORK) {
            showSnackbar("no_internet_connection");
            return;
        }


    }

    /**
     * Call to add new CoreMember data to firebase
     *
     * @param displayName
     * @param uid
     * @param emailId
     */
    private void addMember(String displayName, String uid, String emailId) {
        Log.e("AddMember", "Fn " + displayName + ", Ln " + uid + "eid " + emailId);


//        myRef.setValue("Hello, World!");
//        String key = myRef.push().getKey();
//        Log.e("AddMember", "key " + key);


        User user = new User(displayName, uid, emailId);
        myRef.child(uid).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                } else {
                    String userKey = databaseReference.getKey();
                    System.out.println("Data saved successfully." + "userKey " + userKey);


                }
            }
        });
    }

    protected void showSnackbar(String msg) {

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();


    }

    private class DeleteUserAccount implements View.OnClickListener {


        @Override
        public void onClick(View v) {
            final FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                final String userid = user.getUid();

                deleteUserdb(userid);
            }
        }


    }

    private void deleteUserdb(final String userid) {
        System.out.println("deleteUserdb userid=" + userid);
        DatabaseReference child = myRef.child(userid);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userid)) {
                    Log.e("hasChild", "Child available" + userid);
                } else {
                    Log.e("hasChild", "Child NOT available");
                    deleteUserAuthAcc();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


        myRef.child(userid).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("UserAuth", "Data deleted msg=" + databaseError.getMessage());

                } else {
                    String userKey = databaseReference.getKey();

                    Log.e("UserAuth", "Data deleteion FAILED." + "userKey " + userKey);
                    deleteUserAuthAcc();

                }
            }
        });
    }

    private void deleteUserAuthAcc() {

        auth.getCurrentUser().delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.e("UserAuth", "User account deleted.");
                            showSnackbar("Account deleted");


                            cleareUserDara();
                        } else {
                            Exception res = task.getException();
                            showSnackbar("Failed to delete your account!"+res);
                        }
                    }
                });
    }

    private class SignoutUser implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            auth.getInstance().signOut();
//            showSnackbar("Signout Successfull");
            isLoggingOut = true;
        }
    }

}
