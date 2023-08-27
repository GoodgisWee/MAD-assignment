package my.edu.wheelio;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class BookingPage extends AppCompatActivity {

    private Spinner pickupSpinner, dropoffSpinner;
    private String pickUpPoint, dropOffPoint;
    private ArrayList<String[]> scheduleList, busList;
    private ImageButton searchBtn;
    private SQLiteAdapter mySQLiteAdapter;
    private TableLayout tableLayout1, tableLayout2;
    private TableRow headerTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page2);

        //database initialization
        mySQLiteAdapter = new SQLiteAdapter(this);

        //pickup point & dropoff point
        pickupSpinner = findViewById(R.id.pickUp);
        ArrayAdapter<CharSequence> pickupAdapter = ArrayAdapter.createFromResource(this, R.array.locations_items, android.R.layout.simple_spinner_item);
        pickupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickupSpinner.setAdapter(pickupAdapter);

        dropoffSpinner = findViewById(R.id.dropOff);
        ArrayAdapter<CharSequence> dropOffAdapter = ArrayAdapter.createFromResource(this, R.array.locations_items, android.R.layout.simple_spinner_item);
        dropOffAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropoffSpinner.setAdapter(dropOffAdapter);

        searchBtn = findViewById(R.id.searchBtn);


        pickupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    // The hint item is selected, handle it as needed (e.g., do nothing)
                } else {
                    // Handle the selected item
                    pickUpPoint = parentView.getItemAtPosition(position).toString();
                    Toast.makeText(getApplicationContext(), "Pick Up Point: " + pickUpPoint, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle when nothing is selected (optional)
            }
        });

        dropoffSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    // The hint item is selected, handle it as needed (e.g., do nothing)
                } else {
                    // Handle the selected item
                    dropOffPoint = parentView.getItemAtPosition(position).toString();
                    Toast.makeText(getApplicationContext(), "Drop Off Point: " + dropOffPoint, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle when nothing is selected (optional)
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.booking_schedule);
                showSchedule();
            }
        });
    }
    //function to show all schedule
    private void showSchedule(){
        //read data from db
        mySQLiteAdapter.openToRead();
        scheduleList = mySQLiteAdapter.readSchedule();
        busList = mySQLiteAdapter.readBus();

        tableLayout1 = findViewById(R.id.tableLayout1);
        tableLayout2 = findViewById(R.id.tableLayout2);
        String[] busArray = new String[busList.size()];
        String[] busStarting = new String[busList.size()];
        String[] busArrived = new String[busList.size()];

        //store all bus detail into array
        for(int i=0; i<busList.size(); i++){
            busArray[i] = busList.get(i)[0];
            busStarting[i] = busList.get(i)[4];
            busArrived[i] = busList.get(i)[5];
        }

        //show the record in table form seperate by each bus
        for(int i=0; i<scheduleList.size(); i++) {
            //Header Data
            String startPoint="", endPoint="";
            //header row and textview
            TableRow headerRow = new TableRow(this);
            headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView header = new TextView(BookingPage.this);
            header.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,1));
            header.setGravity(Gravity.CENTER);
            header.setTypeface(null, Typeface.BOLD);
            boolean isNewBus=false;
            //check is there another bus schedule
            if(i>0){
                if(!scheduleList.get(i)[5].equals(scheduleList.get(i-1)[5])){
                    isNewBus=true;
                }
            }
            if(i==0||isNewBus == true){
                for(int j=0; j<busList.size(); j++){
                    //get start and end Point
                    if(scheduleList.get(i)[5].equals(busList.get(j)[0])){
                        for(int k=0; k<6; k++){
                            header.setText(busList.get(j)[i]);
                            headerRow.addView(header);
                            if(scheduleList.get(i)[4].equals(busArray[0])){
                                tableLayout1.addView(headerRow);
                            } else if(scheduleList.get(i)[4].equals(busArray[1])){
                                tableLayout2.addView(headerRow);
                            }
                        }
                    }
                }
            }
            TableRow newRow = new TableRow(this);
            newRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            //Show all record in one row
            for (int j = 0; j < 6; j++) {
                //Record textview
                TextView scheduleTime = new TextView(BookingPage.this);
                scheduleTime.setLayoutParams(new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1
                ));
                scheduleTime.setText(scheduleList.get(i)[j]);
                scheduleTime.setGravity(Gravity.CENTER);
                newRow.addView(scheduleTime);
            }
            if(scheduleList.get(i)[4].equals(busArray[0])){
                tableLayout1.addView(newRow);
            } else if(scheduleList.get(i)[4].equals(busArray[1])){
                tableLayout2.addView(newRow);
            }
        }
    }
}