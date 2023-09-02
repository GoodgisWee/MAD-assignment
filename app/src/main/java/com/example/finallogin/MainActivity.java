package com.example.finallogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    //create a status code (can be any unique integer)
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Implementing Google Sign in logic
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        //Create an onClickListener to listen to the Google Sign in Button Clicked
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });


        //Handle regular sign in logic
        EditText loginEmail = findViewById(R.id.loginEmail);
        EditText password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);

        //Connect to database, then compare string from user input to the database
        // Scenario #1 : user click loginButton, write the details into the database
        // Scenario #2 : Before writing into database, check if there is same data in database
        // Scenario #3 : If no same data, write the data into database.
        // Scenario #4 : If HAVE same data, compare the user input password with the one available in database
        // (So that it can be a form of authentication)
        // Scenario #5 : Password MATCH, intent to another activity
        // Scenario #6 : Password DOES NOT MATCH, show Toast message "Invalid Password / Email!"
        // (Show toast message with password + email because we do not have the authentication method for email)
        // (But we can use toast message to inform user)
        // (If not toast message, can play with Visibility.GONE and Visibility.SHOW)


    }

    //Handle the sign in process
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // You've successfully signed in. You can use account.getIdToken() to get the ID token.
        } catch (ApiException e) {
            // Sign-in failed. Handle the error.
        }
    }

}