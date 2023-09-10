package my.edu.utar.login;

import android.content.Intent;
import android.os.Bundle;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import my.edu.utar.BookingPage.BookingPage;
import my.edu.utar.R;
import my.edu.utar.Database.SQLiteAdapter;

public class MainActivity extends AppCompatActivity {

    private SQLiteAdapter mySQLiteAdapter;
    private GoogleSignInClient mGoogleSignInClient;
    //create a status code (can be any unique integer)
    private static final int RC_SIGN_IN = 123;
    private EditText loginID, password;
    private  TextView signup;
    private Button loginButton;
    private String passwordStr, loginIDStr;
    private ArrayList<String[]> userList;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToWrite();
        Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();

//-----------------------------------------------------------------------------------------------------
        //GOOGLE LOGIN
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
        loginID = findViewById(R.id.loginID);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signup = findViewById(R.id.signUpText);

        String signupText = "Don't have a account yet? Register Here";

        SpannableString ss = new SpannableString(signupText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        };
        ss.setSpan(clickableSpan,26, 39, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signup.setText(ss);
        signup.setMovementMethod(LinkMovementMethod.getInstance());


        //Connect to database, then compare string from user input to the database
        // Scenario #1 : user click loginButton, write the details into the database

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginIDStr = loginID.getText().toString();
                passwordStr = password.getText().toString();
                if(passwordStr==null||passwordStr.equals("")){
                    Toast.makeText(MainActivity.this, "Error! Empty Password", Toast.LENGTH_SHORT).show();
                } else {
                    if(loginIDStr.contains("@")){
                        userList = mySQLiteAdapter.readUserByCondition("userEmail", loginIDStr);
                    } else if(loginIDStr==null||loginIDStr.equals("")){
                        Toast.makeText(MainActivity.this, "Error! Empty UserName", Toast.LENGTH_SHORT).show();
                    }else{
                        userList = mySQLiteAdapter.readUserByCondition("userName", loginIDStr);
                    }
                }

                if(userList.size() > 0){
                    Intent intent = new Intent(MainActivity.this, BookingPage.class);
                    uid = userList.get(0)[0];
                    mySQLiteAdapter.updateUserStatus(uid, "online");
                    intent.putExtra("uid",uid);
                    intent.putExtra("login","login");
                    mySQLiteAdapter.close();
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid login details. Please ensure you key in the right details", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            if (account != null) {
                // Sign-in was successful. You can use account.getIdToken() to get the ID token.
                // Now, you can start the next activity or perform any other necessary actions.

                // For example, you can start a new activity like this:
                Intent intent = new Intent(MainActivity.this, my.edu.utar.BookingPage.BookingPage.class);
                String displayName = account.getDisplayName();
                String email = account.getEmail();
                userList = mySQLiteAdapter.readUserByCondition("userName", displayName);
                if(userList.size()>0){
                    userList = mySQLiteAdapter.readUserByCondition("userName", loginIDStr);
                } else {
                    mySQLiteAdapter.insertUserTable(displayName, email, "root123", 100, "student", "online");
                    userList = mySQLiteAdapter.readUserByCondition("userName", displayName);
                }
                mySQLiteAdapter.close();
                uid = userList.get(0)[0];
                intent.putExtra("uid", uid);
                intent.putExtra("login", "login");
                startActivity(intent);
            }
        } catch (ApiException e) {
            // Sign-in failed. Handle the error.
            Log.e("#####DEBUG######", "Google Sign-In Failed: " + e.getMessage());
            Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
        }
    }

}