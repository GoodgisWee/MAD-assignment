package my.edu.utar.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import my.edu.utar.login.MainActivity;
import my.edu.utar.R;
import my.edu.utar.Database.SQLiteAdapter;

public class userProfilePage extends AppCompatActivity {
    private ImageView profilePictureImageView;
    private TextView uidTextView, nameTextView,emailTextView, pointsTextView, myProfileTextView, changePassTextView, aboutTextView, logoutTextView;
    private SQLiteAdapter mySQLiteAdapter;
    private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1002;
    private ImageButton homeBtn, profileBtn, bookingBtn;
    private ArrayList<String[]> userListByCondition;
    private Spinner busLocation;
    private Button updateButton;
    private String uid;
    private ArrayList<String[]> bus;
    private LinearLayout busLocationLayout;
    private ImageButton whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        //database initialization
        mySQLiteAdapter = new SQLiteAdapter(this);


        // Initialize UI elements
        profilePictureImageView = findViewById(R.id.userImage);
        try {
            String imagePath = "/data/user/0/my.edu.utar/files/" + uid + "_profile_image.png";
            File imageFile = new File(imagePath);

            if (imageFile.exists()) {
                // If the image file exists, set the image using its URI
                profilePictureImageView.setImageURI(Uri.parse(imagePath));
            } else {
                // If the image file doesn't exist, set the default profile image
                profilePictureImageView.setImageResource(R.drawable.profile);
            }
        } catch (Exception e) {
            // Handle any exceptions that may occur
            e.printStackTrace();
            profilePictureImageView.setImageResource(R.drawable.profile);
        }
        nameTextView = findViewById(R.id.name);
        uidTextView = findViewById(R.id.userid);
        pointsTextView = findViewById(R.id.point);
        emailTextView = findViewById(R.id.emailTextView);
        changePassTextView = findViewById(R.id.changePassword);
        aboutTextView = findViewById(R.id.about);
        logoutTextView = findViewById(R.id.logout);
        aboutTextView = findViewById(R.id.about);
        busLocation = findViewById(R.id.busLocation);
        updateButton = findViewById(R.id.updateButton);
        busLocationLayout = findViewById(R.id.busLocationLayout);
        whatsapp = findViewById(R.id.whatsapp);

        mySQLiteAdapter.openToWrite();
        userListByCondition = mySQLiteAdapter.readUserByCondition("userID", uid);
        bus = mySQLiteAdapter.readBusByCondition("userID", uid);
        mySQLiteAdapter.close();

        //output all user details
        if(userListByCondition.size()>0){
            nameTextView.setText(userListByCondition.get(0)[1]);
            uidTextView.setText(uid);
            pointsTextView.setText(userListByCondition.get(0)[4]);
            emailTextView.setText(userListByCondition.get(0)[2]);
        } else {
            Toast.makeText(this, "Error, user doesn't exist", Toast.LENGTH_SHORT).show();
        }

        //driver can update bus location
        if(userListByCondition.get(0)[5].equals("driver")){
            //update name
            nameTextView.setText(userListByCondition.get(0)[1]+" (driver)");
            //busLocation Spinner
            String[] locationItems = {bus.get(0)[4], bus.get(0)[5]};
            ArrayList<String> itemsWithoutPickUp = new ArrayList<>();
            for (String item : locationItems) {
                if (!item.equals(bus.get(0)[2])) {
                    itemsWithoutPickUp.add(item);
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    itemsWithoutPickUp
            );
            busLocation.setAdapter(adapter);
            int position = adapter.getPosition(bus.get(0)[2]);
            busLocation.setSelection(position);
            busLocationLayout.setVisibility(View.VISIBLE);
        }
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySQLiteAdapter.openToWrite();
                mySQLiteAdapter.updateBusLocation(bus.get(0)[0], busLocation.getSelectedItem().toString());
                mySQLiteAdapter.close();

                Toast.makeText(userProfilePage.this, "Successfully update the bus location",
                        Toast.LENGTH_SHORT).show();
            }
        });



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
                Intent intent = new Intent(userProfilePage.this, changePasswordPage.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        //whatapps API
        whatsapp.setOnClickListener(new View.OnClickListener() {
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

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(userProfilePage.this);
                builder.setTitle("Confirm Logout");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(userProfilePage.this, my.edu.utar.login.MainActivity.class);
                        mySQLiteAdapter.updateUserStatus(uid, "offline");
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog, do nothing
                        dialog.dismiss();
                    }
                });
                android.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //Bottom navigation bar
        homeBtn = findViewById(R.id.homeBtn);
        bookingBtn = findViewById(R.id.bookingBtn);
        profileBtn = findViewById(R.id.profileBtn);

        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userProfilePage.this, my.edu.utar.BookingHistory.MyTicketActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userProfilePage.this, my.edu.utar.BookingPage.BookingPage.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
            }
        });

    }

    //change user profile picture
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

            // Save the image to a file
            Bitmap imageBitmap = getBitmapFromUri(selectedImageUri);
            saveImageToFile(imageBitmap);
        } else if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profilePictureImageView.setImageBitmap(imageBitmap);

            // Save the image to a file
            saveImageToFile(imageBitmap);
        }
    }

    // Helper method to get a Bitmap from a URI
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to save a Bitmap to a file
    private void saveImageToFile(Bitmap bitmap) {
        File imageFile = new File(getFilesDir(), uid+"_profile_image.png"); // Change the filename and location as needed

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            // Now, you can update the user's profile with the imageFile.getAbsolutePath()
            // You'll use this path to load the image as a drawable when needed.
            String imagePath = imageFile.getAbsolutePath();
            updateProfileWithImagePath(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Example method to update the user's profile with the image path
    private void updateProfileWithImagePath(String imagePath) {
        // Create a Drawable from the image path
        Drawable drawable = Drawable.createFromPath(imagePath);

        // Check if drawable is not null (i.e., the image was successfully loaded)
        if (drawable != null) {
            // Set the drawable as the profile picture in an ImageView
            profilePictureImageView.setImageURI(Uri.parse(imagePath));
            // Optionally, you can store the imagePath in your preferences or database for future use.
            // For example, you might want to save it to persistently associate it with the user's profile.
            // SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
            // SharedPreferences.Editor editor = preferences.edit();
            // editor.putString("user_profile_image_path", imagePath);
            // editor.apply();
        } else {
            // Handle the case where the drawable could not be created from the image path.
            // You might want to show a default profile picture or an error message.
            Toast.makeText(userProfilePage.this, "Error when updating the photo.", Toast.LENGTH_SHORT).show();
            profilePictureImageView.setImageResource(R.drawable.profile);
        }
    }

}
