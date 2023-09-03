package my.edu.utar.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import my.edu.utar.BookingPage;
import my.edu.utar.MainActivity;
import my.edu.utar.MyTicketActivity;
import my.edu.utar.R;
import my.edu.utar.SQLiteAdapter;

public class userProfilePage extends AppCompatActivity {
    private ImageView profilePictureImageView;
    private TextView uidTextView, nameTextView,emailTextView, pointsTextView, myProfileTextView, changePassTextView, aboutTextView, logoutTextView;
    private SQLiteAdapter mySQLiteAdapter;
    private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1002;
    private ImageButton homeBtn, profileBtn, bookingBtn;
    private ArrayList<String[]> userListByCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");

        //database initialization
        mySQLiteAdapter = new SQLiteAdapter(this);


        // Initialize UI elements
        profilePictureImageView = findViewById(R.id.userImage);
        nameTextView = findViewById(R.id.name);
        uidTextView = findViewById(R.id.userid);
        pointsTextView = findViewById(R.id.point);
        emailTextView = findViewById(R.id.emailTextView);
        changePassTextView = findViewById(R.id.changePassword);
        aboutTextView = findViewById(R.id.about);
        logoutTextView = findViewById(R.id.logout);
        aboutTextView = findViewById(R.id.about);

        // Fetch user data from your backend or database
        // String userName = mySQLiteAdapter.USER_NAME;  // Replace with actual user data
        String userName = uid;

        mySQLiteAdapter.openToRead();
        userListByCondition = mySQLiteAdapter.readUserByCondition("userID", uid);
        if(userListByCondition.size()>0){
            uidTextView.setText(uid);
            nameTextView.setText(userListByCondition.get(0)[1]);
            pointsTextView.setText(userListByCondition.get(0)[4]);
            emailTextView.setText(userListByCondition.get(0)[2]);
        } else {
            Toast.makeText(this, "Error, user doesn't exist", Toast.LENGTH_SHORT).show();
        }

        //change profile picture
        profilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show dialog to choose between gallery or camera
                showImageChoiceDialog();
            }
        });

        //navigate to change password page
        changePassTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //whatapps API
        aboutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change to all ppl phone no
                String url = "https://api.whatsapp.com/send?text=https://api.whatsapp.com/send" +
                        "?phone=601117949618&text=Hi, I have a question for Wheel.IO regarding...";
                // Create an Intent with the ACTION_VIEW to open the URL
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(userProfilePage.this, "Error: Cannot open URL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Logout to login page
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userProfilePage.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });

        //Bottom navigation bar
        homeBtn = findViewById(R.id.homeBtn);
        bookingBtn = findViewById(R.id.bookingBtn);
        profileBtn = findViewById(R.id.profileBtn);

        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userProfilePage.this, MyTicketActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userProfilePage.this, BookingPage.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

    }

    private void showImageChoiceDialog() {
        CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, (dialog, which) -> {
            if (options[which].equals("Take Photo")) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE_IMAGE);
                }
            } else if (options[which].equals("Choose from Gallery")) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            } else if (options[which].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            profilePictureImageView.setImageURI(selectedImageUri);
        } else if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profilePictureImageView.setImageBitmap(imageBitmap);
        }
    }


}
