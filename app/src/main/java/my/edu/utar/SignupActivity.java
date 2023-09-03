package my.edu.utar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SignupActivity extends AppCompatActivity {

    private SQLiteAdapter mySQLiteAdapter;

    private static final int RC_SIGN_IN = 123;
    private EditText email, username, password1, password2;
    private Button registerButton;
    private String password1Str, password2Str, emailStr, usernameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mySQLiteAdapter = new SQLiteAdapter(this);

        email = findViewById(R.id.emailText);
        username = findViewById(R.id.username);
        password1 = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        registerButton = findViewById(R.id.registerButton);

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

        registerButton.setOnClickListener(view -> {
            String enteredEmail = email.getText().toString();
            String enteredName = username.getText().toString();
            String enteredPassword1 = password1.getText().toString();
            String enteredPassword2 = password2.getText().toString();

            if (enteredEmail == null || enteredEmail.isEmpty() || !isValidEmail(enteredEmail)) {
                // Show a toast message for invalid email
                Toast.makeText(this, "Invalid Email, Please try again.", Toast.LENGTH_SHORT).show();
            } else if (enteredName == null || enteredName.isEmpty() || !isValidName(enteredName)) {
                // Show a toast message for invalid name
                Toast.makeText(this, "Invalid Name, Please enter FULL name.", Toast.LENGTH_SHORT).show();
            } else if (enteredPassword1 == null || enteredPassword1.isEmpty() || enteredPassword2 == null || enteredPassword2.isEmpty() || !doPasswordsMatch(enteredPassword1, enteredPassword2)) {
                // Show a toast message for password mismatch or empty password fields
                Toast.makeText(this, "Password MisMatch or Empty, Please Check again.", Toast.LENGTH_SHORT).show();
            } else {
                // All input is valid, proceed with registration
                insertUser(enteredName, enteredEmail, enteredPassword1);
            }
        });




    }

    private void insertUser(String name, String email, String password) {
        mySQLiteAdapter.openToRead();

        // Check if the email already exists in the database
        if (mySQLiteAdapter.isEmailExists(email)) {
            mySQLiteAdapter.close();
            // Show a toast message indicating that the email is already registered
            Toast.makeText(this, "Email already exists. Please use a different email.", Toast.LENGTH_SHORT).show();
        } else {
            mySQLiteAdapter.close();
            mySQLiteAdapter.openToWrite();
            long result = mySQLiteAdapter.insertUserTable(name, email, password, 0, "Member");
            mySQLiteAdapter.close();
            if (result > 0) {
                // Registration successful
                Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                // You can also add additional logic here
                // For example, navigate to the main activity
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                // Registration failed, handle the error
                // Display an error message or toast
                Toast.makeText(this, "Registration Error", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }


    private boolean isValidName(String name) {
        String namePattern = "^[a-zA-Z ]+$";
        return name.matches(namePattern);
    }

    private boolean doPasswordsMatch(String password1, String password2) {
        return password1.equals(password2);
    }


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