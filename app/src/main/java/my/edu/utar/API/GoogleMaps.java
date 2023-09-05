
package my.edu.utar.API;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import my.edu.utar.R;
import my.edu.utar.SQLiteAdapter;

public class GoogleMaps extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener  {

    private GoogleMap mMap;
    private SQLiteAdapter mySQliteAdapter;
    private ArrayList<String[]> busList, scheduleList;
    private ImageButton zoomOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        mySQliteAdapter = new SQLiteAdapter(this);

        //zoom out function
        zoomOutButton = findViewById(R.id.zoomOutButton);
        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Animate the camera to the original position and zoom level
                LatLng originalLocation = new LatLng(4.3085, 101.1537); //kampar
                CameraPosition originalCameraPosition = new CameraPosition.Builder()
                        .target(originalLocation)
                        .zoom(14) // Adjust the original zoom level as needed
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(originalCameraPosition));

                // Hide the zoom out button again
                zoomOutButton.setVisibility(View.INVISIBLE);
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        //showing current location
        // Define the target location
        LatLng current = new LatLng(4.3085, 101.1537); //kampar

        // Set the initial camera position
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(current)
                .zoom(14) // You can adjust the zoom level
                .build();

        // Move the camera to the initial position
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // Load the custom marker image as a Bitmap
        Bitmap currentBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.current_location);
        // Resize the Bitmap to the desired dimensions
        Bitmap resizedCurrentBitmap = Bitmap.createScaledBitmap(currentBitmap, 100, 100, false);
        // Create a BitmapDescriptor from the resized Bitmap
        BitmapDescriptor resizedCurrentMarkerIcon = BitmapDescriptorFactory.fromBitmap(resizedCurrentBitmap);

        // Create a MarkerOptions object to put bus on map
        MarkerOptions currentMarkerOptions = new MarkerOptions()
                .position(current)  // Set the marker's position
                .icon(resizedCurrentMarkerIcon); // Set the custom marker image
        // Add the marker to the map
        Marker currentMarker = mMap.addMarker(currentMarkerOptions);
        currentMarker.setTag("current");

//----------------------------------------------------------------------------------
        //BUS LOCATION
        //read data from database
        mySQliteAdapter.openToRead();
        busList = mySQliteAdapter.readBusByCondition("busID", "1001");
        scheduleList = mySQliteAdapter.readScheduleByCondition("scheduleID", "1");

        //store all value in proper variable
        String busCurrent="", busCurrentTime="", scheduleTime="";
        if(busList.size()!=0 && scheduleList.size()!=0) {
            busCurrent = busList.get(0)[2];
            busCurrentTime = busList.get(0)[3];
            scheduleTime = scheduleList.get(0)[1];
        } else {
            Toast.makeText(GoogleMaps.this, "Error empty bus list", Toast.LENGTH_SHORT).show();
        }
        
        /*//Get current date and time
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = dateFormat.format(currentDate);
        String formattedTime = dateFormat.format(currentTime);

        Date targetDate;
        boolean future = false;
        try {
            targetDate = dateFormat.parse(busCurrentTime);
            int comparisonResult = dateFormat.parse(formattedDate).compareTo(targetDate);
            if (comparisonResult >= 0) {
                future = true;
            }
        } catch (ParseException e) {
            System.err.println("Error parsing the target date: " + e.getMessage());
        }*/
        //Get bus location and display
        if(busList.size()!=0){
            LatLng location;
            if(busCurrent.equals("Block N")){
                location = new LatLng(4.338782239269099, 101.13686989535465);
            } else if(busCurrent.equals("Block G")){
                location = new LatLng(4.3396009187838205, 101.1429483572651);
            }else if(busCurrent.equals("Block D")){
                location = new LatLng(4.337939446872019, 101.14425024898966);
            }else if(busCurrent.equals("WestLake")){
                location = new LatLng(4.329730234755697, 101.13627857155724);
            }else if(busCurrent.equals("Harvard")){
                location = new LatLng(4.33086221481433, 101.13234902558969);
            }else{
                location = new LatLng(4.32733805264933, 101.1356164630129);
            }

            // Load the custom marker image as a Bitmap
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bus);
            // Resize the Bitmap to the desired dimensions
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 100, 100, false);
            // Create a BitmapDescriptor from the resized Bitmap
            BitmapDescriptor resizedMarkerIcon = BitmapDescriptorFactory.fromBitmap(resizedBitmap);

            // Create a MarkerOptions object to put bus on map
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)  // Set the marker's position
                    .icon(resizedMarkerIcon); // Set the custom marker image
            // Add the marker to the map
            Marker busMarker = mMap.addMarker(markerOptions);
            busMarker.setTag("bus");
        } else {
            Toast.makeText(GoogleMaps.this, "Error bus ID", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Check if the clicked marker is the bus marker
        if (marker.getTag() != null) {
            // Animate the camera to the bus marker's position
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 18.0f)); // Adjust the zoom level as needed
            return true; // Return true to indicate that the click event has been handled
        }
        return false; // Return false to indicate that the click event has not been handled
    }

    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            // Handle location updates here
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            // Do something with the latitude and longitude
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Handle status changes if needed
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Called when the user enables the location provider (e.g., GPS)
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Called when the user disables the location provider
        }
    }

}