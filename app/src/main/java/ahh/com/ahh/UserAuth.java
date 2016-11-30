package ahh.com.ahh;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;

public class UserAuth extends AppCompatActivity {

    private TextView idp_tv;
    private Button loginButton;
    private Button signoutButton;
    private Button deletButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginButton = (Button) findViewById(R.id.loginButton);
        deletButton = (Button) findViewById(R.id.delete);
        signoutButton = (Button) findViewById(R.id.sign_out);
        idp_tv = (TextView) findViewById(R.id.textView);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                .build(),
                        RC_SIGN_IN);
            }
        });
        checkAuthentication();

//        signoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AuthUI.getInstance()
//                        .signOut()
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            public void onComplete(@NonNull Task<Void> task) {
//                                // user is now signed out
//
//                            }
//                        });
//            }
//        });


    }

    private void checkAuthentication() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            loginButton.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "already signed in", Toast.LENGTH_SHORT).show();
        } else {
            // not signed in
            loginButton.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "not signed in", Toast.LENGTH_SHORT).show();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // user is signed in!
            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
//            startActivity(new Intent(this, WelcomeBackActivity.class)
//                    .putExtra("my_token", idpResponse.getIdpToken()));
            idp_tv.setText(idpResponse + "");
//            finish();
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

        // User is not signed in. Maybe just wait for the user to press
        // "sign in" again, or show a message.
    }

    protected void showSnackbar(String msg) {
//       Snackbar snackbar = Snackbar
//               .make(getApplicationContext(), "Welcome to AndroidHive", 300);
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();


    }

}
