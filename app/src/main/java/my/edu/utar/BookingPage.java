package my.edu.utar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookingPage extends AppCompatActivity{

    private Spinner pickupSpinner, dropoffSpinner;
    private String pickUpPoint, dropOffPoint, dateStr, timeStr, paxStr;
    private ArrayList<String[]> scheduleListByCondition, busList, busListByCondition;
    private ImageButton searchBtn;
    private SQLiteAdapter mySQLiteAdapter;
    private TableLayout tableLayout1, tableLayout2;
    private TableRow headerTableLayout;
    private RecyclerView scheduleRecyclerView;
    private List<ScheduleItem> scheduleItemsList;
    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter;
    private EditText date, time, pax;
    private ImageButton homeBtn, bookingBtn, profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page2);

        pickupSpinner = findViewById(R.id.pickUp);
        dropoffSpinner = findViewById(R.id.dropOff);
        date = findViewById(R.id.editTextDate);
        time = findViewById(R.id.editTextTime);
        pax = findViewById(R.id.pax);
        searchBtn = findViewById(R.id.searchBtn);

        //Bar Navigation
        homeBtn = findViewById(R.id.homeBtn);
        bookingBtn = findViewById(R.id.bookingBtn);
        profileBtn = findViewById(R.id.profileBtn);

        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingPage.this, MyTicketActivity.class);
                startActivity(intent);
            }
        });

        //database initialization
        mySQLiteAdapter = new SQLiteAdapter(this);

        ///pickup point & dropoff point
        ArrayAdapter<CharSequence> pickupAdapter = ArrayAdapter.createFromResource(this, R.array.location_items, android.R.layout.simple_spinner_item);
        pickupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickupSpinner.setAdapter(pickupAdapter);
        pickupSpinner.setSelection(0);
        ArrayAdapter<CharSequence> dropOffAdapter = ArrayAdapter.createFromResource(this, R.array.location_items, android.R.layout.simple_spinner_item);
        dropOffAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropoffSpinner.setAdapter(dropOffAdapter);
        dropoffSpinner.setSelection(0);

        //user input validation for spinner class
        pickupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item from the pickupSpinner
                String selectedPickupItem = pickupSpinner.getSelectedItem().toString();

                // Create a new adapter for dropoffSpinner without the selected item
                ArrayAdapter<CharSequence> updatedDropOffAdapter = new ArrayAdapter<>(BookingPage.this, android.R.layout.simple_spinner_item);
                updatedDropOffAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                if (selectedPickupItem.equals("Block N") || selectedPickupItem.equals("Block D") || selectedPickupItem.equals("Block G")) {
                    updatedDropOffAdapter.add("Harvard");
                    updatedDropOffAdapter.add("WestLake");
                    updatedDropOffAdapter.add("Stanford");
                } else if (selectedPickupItem.equals("PickUp Point")) {
                    updatedDropOffAdapter.add("Choose PickUp Point First");
                } else {
                    updatedDropOffAdapter.add("Block N");
                    updatedDropOffAdapter.add("Block G");
                    updatedDropOffAdapter.add("Block D");
                }

                // Set the updated adapter to dropoffSpinner
                dropoffSpinner.setAdapter(updatedDropOffAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        //get the date
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookingPage.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                String formattedMonth, formattedDay;
                                if (monthOfYear < 10) {
                                    formattedMonth = String.format("%02d", monthOfYear + 1);
                                } else {
                                    formattedMonth = String.valueOf(monthOfYear);
                                }
                                if (dayOfMonth < 10) {
                                    formattedDay = String.format("%02d", dayOfMonth + 1);
                                } else {
                                    formattedDay = String.valueOf(dayOfMonth);
                                }
                                date.setText(formattedDay + "-"
                                        + formattedMonth + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        //get the time value
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int tHour = c.get(Calendar.HOUR); // current hour
                int tMinute = c.get(Calendar.MINUTE); // current minute
                int tSecond = c.get(Calendar.SECOND); // current second

                TimePickerDialog timePickerDialog = new TimePickerDialog(BookingPage.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                String formattedMinute, formattedHour, formattedSecond;
                                if (hour < 10) {
                                    formattedHour = String.format("%02d", hour);
                                } else {
                                    formattedHour = String.format("%02d", hour);
                                }
                                if (minute < 10) {
                                    formattedMinute = String.format("%02d", minute);
                                } else {
                                    formattedMinute = String.valueOf(minute);
                                }

                                if (hour < 12) {
                                    time.setText(formattedHour + ":" + formattedMinute + ":00 a.m.");
                                } else {
                                    time.setText(formattedHour + ":" + formattedMinute + ":00 p.m.");
                                }
                            }
                        }, tHour, tMinute, false);
                timePickerDialog.show();
            }
        });

        //get all result and pass to BookingPageModel
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickUpPoint = pickupSpinner.getSelectedItem().toString();
                dropOffPoint = dropoffSpinner.getSelectedItem().toString();
                dateStr = date.getText().toString();
                timeStr = time.getText().toString().replaceAll("(?i)[ ]?[ap]\\.[ ]?m\\.", "").trim();
                paxStr = pax.getText().toString();

                //input validation
                if (pickUpPoint.equals("PickUp Point") || pickUpPoint.equals("DropOff Point")) {
                    Toast.makeText(BookingPage.this, "Invalid PickUp / DropOff Point", Toast.LENGTH_SHORT).show();
                } else if(dateStr.equals("")) {
                    Toast.makeText(BookingPage.this, "DATE empty column", Toast.LENGTH_SHORT).show();
                } else if(timeStr.equals("")) {
                    Toast.makeText(BookingPage.this, "TIME empty column", Toast.LENGTH_SHORT).show();
                } else if(paxStr.equals("")) {
                    Toast.makeText(BookingPage.this, "PAX empty column", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(BookingPage.this, my.edu.utar.model.BookingPageModel.class);
                    intent.putExtra("pickUpPoint", pickUpPoint);
                    intent.putExtra("dropOffPoint", dropOffPoint);
                    intent.putExtra("dateStr", dateStr);
                    intent.putExtra("timeStr", timeStr);
                    intent.putExtra("paxStr", paxStr);
                    startActivity(intent);
                }
            }
        });
    }
}