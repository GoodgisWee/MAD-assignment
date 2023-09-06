package my.edu.utar.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import my.edu.utar.Database.SQLiteAdapter;
import my.edu.utar.R;

public class changePasswordPage extends AppCompatActivity {

    private EditText oldPasswordEditText, newPasswordEditText, confirmNewPasswordEditText;
    private Button changePasswordButton,goToProfileButton;
    private SQLiteAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");


        oldPasswordEditText = findViewById(R.id.old_password);
        newPasswordEditText = findViewById(R.id.new_password);
        confirmNewPasswordEditText = findViewById(R.id.confirm_new_password);
        changePasswordButton = findViewById(R.id.change_password_button);
        goToProfileButton = findViewById(R.id.goto_profile_button);

        changePasswordButton.setOnClickListener(view -> {
            String oldPassword = oldPasswordEditText.getText().toString();
            String newPassword = newPasswordEditText.getText().toString();
            String confirmNewPassword = confirmNewPasswordEditText.getText().toString();

            if (isValid(oldPassword, newPassword, confirmNewPassword)) {
                // Call method to change password in database, e.g., changePasswordInDb()

                db = new SQLiteAdapter(this);
                db.openToWrite();
                boolean result = db.updatePassword(oldPassword,newPassword);
                if (result) {
                    Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show();

/*                    // Show the "Go to Profile" button
                    goToProfileButton.setVisibility(View.VISIBLE);*/
                } else {
                    Toast.makeText(this, "Error: Old password may be incorrect.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Password change failed. Check your inputs.", Toast.LENGTH_SHORT).show();
            }
        });
        goToProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Jump to UserProfilePage
                Intent intent = new Intent(changePasswordPage.this, userProfilePage.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                finish();
            }
        });
    }



    private boolean isValid(String oldPassword, String newPassword, String confirmNewPassword) {
        // Here, validate the old password against the one in database,
        // check if newPassword and confirmNewPassword match,
        // and check if newPassword meets your password requirements.
        // For simplicity, let's just check if they are not empty and match.
        return !oldPassword.isEmpty() && newPassword.equals(confirmNewPassword);
    }


}

