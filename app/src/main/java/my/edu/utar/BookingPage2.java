package my.edu.utar;

import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class BookingPage2 extends AppCompatActivity {

    private Spinner pickupSpinner, dropoffSpinner;
    private String pickUpPoint, dropOffPoint, Date;
    private ArrayList<String[]> scheduleList, busList;
    private ImageButton searchBtn;
    private SQLiteAdapter mySQLiteAdapter;
    private TableLayout tableLayout1, tableLayout2;
    private TableRow headerTableLayout;
    private RecyclerView scheduleRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_booking_page2);

        pickupSpinner = findViewById(R.id.pickUp);
        dropoffSpinner = findViewById(R.id.dropOff);
        EditText date = findViewById(R.id.editTextDate);
        searchBtn = findViewById(R.id.searchBtn);

        //database initialization
        mySQLiteAdapter = new SQLiteAdapter(this);

        //pickup point & dropoff point
        ArrayAdapter<CharSequence> pickupAdapter = ArrayAdapter.createFromResource(this, R.array.locations_items, android.R.layout.simple_spinner_item);
        pickupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickupSpinner.setAdapter(pickupAdapter);
        ArrayAdapter<CharSequence> dropOffAdapter = ArrayAdapter.createFromResource(this, R.array.locations_items, android.R.layout.simple_spinner_item);
        dropOffAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropoffSpinner.setAdapter(dropOffAdapter);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                // set day of month , month and year value in the edit text
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookingPage2.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date.setText(dayOfMonth + ":"
                                        + (monthOfYear + 1) + ":" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.booking_schedule);
                pickUpPoint = pickupSpinner.getSelectedItem().toString();
                dropOffPoint = dropoffSpinner.getSelectedItem().toString();
                Date = date.getText().toString();
                showSchedule();
            }
        });
    }
    //function to show all schedule
    private void showSchedule() {
        //read data from db
        mySQLiteAdapter.openToRead();
        busList = mySQLiteAdapter.readBus();
        String busID = "";
        boolean noBus = true;

        //check if there bus available
        for (int i = 0; i < busList.size(); i++) {
            if (pickUpPoint.equals(busList.get(i)[4])) {
                busID = busList.get(i)[0];
                scheduleList = mySQLiteAdapter.readScheduleByBus(busID);
                noBus = false;
                break;
            }
        }

        tableLayout1 = findViewById(R.id.tableLayout1);
        tableLayout2 = findViewById(R.id.tableLayout2);
        String[] headerTitle = {"Time depart", "Time Arrived", "Date", "Seat Available", "Bus ID"};

        if (noBus == false) {
            TableRow headerRow = new TableRow(this);
            headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView header = new TextView(BookingPage2.this);
            header.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            header.setGravity(Gravity.CENTER);
            header.setTypeface(null, Typeface.BOLD);
            header.setText(pickUpPoint + "   ---->   " + dropOffPoint);
            headerRow.addView(header);
            tableLayout1.addView(headerRow);
            for (int i = 0; i < headerTitle.length; i++){
                TextView headerTextView = new TextView(BookingPage2.this);
                headerTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                headerTextView.setGravity(Gravity.CENTER);
                headerTextView.setText(headerTitle[i]);
            }
            for (int i = 0; i < scheduleList.size(); i++) {
                TableRow newRow = new TableRow(this);
                newRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                for (int j = 1; j < 6; j++) {
                    TextView scheduleTime = new TextView(BookingPage2.this);
                    scheduleTime.setLayoutParams(new TableRow.LayoutParams(
                            0,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1
                    ));
                    scheduleTime.setText(scheduleList.get(i)[j]);
                    scheduleTime.setGravity(Gravity.CENTER);
                    newRow.addView(scheduleTime);
                }
                tableLayout1.addView(newRow);
            }
        } else {
            Toast.makeText(this, "there is no bus from " + pickUpPoint + " to " + dropOffPoint,
                    Toast.LENGTH_SHORT).show();
        }*/
    }
}