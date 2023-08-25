package my.edu.wheelio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SQLiteAdapter mySQLiteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView listContent = new TextView(this);
        listContent.setTextSize(24.0f);

        //let"s insert some data
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToWrite();

        mySQLiteAdapter.deleteAll();

        mySQLiteAdapter.insertUserTable("Ryan Li", "ryan123@gmail.com", "ryan123", 100, "student");
        mySQLiteAdapter.insertUserTable("Nicholas Ngiam", "nicholas123@gmail.com", "nicholas123", 100, "student");
        mySQLiteAdapter.insertUserTable("Wee Jeng Kai", "wee123@gmail.com", "wee123", 100, "driver");

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        Calendar currentTime = Calendar.getInstance();
        String formattedTime = currentTime.toString();
        currentTime.add(Calendar.HOUR_OF_DAY, 1);
        String formattedTime2 = currentTime.toString();
        currentTime.add(Calendar.HOUR_OF_DAY, 1);
        String formattedTime3 = currentTime.toString();
        
        mySQLiteAdapter.insertBusTable("ABC123", "Block N", formattedTime, "Block A", "WestLake");
        mySQLiteAdapter.insertBusTable("XYZ123", "Block H", formattedTime, "Block P", "Harvard");

        mySQLiteAdapter.insertScheduleTable(formattedTime, formattedTime2, formattedTime3, 30, 1001);
        mySQLiteAdapter.insertScheduleTable(formattedTime, formattedTime2, formattedTime3, 30, 1001);
        mySQLiteAdapter.insertScheduleTable(formattedTime, formattedTime2, formattedTime3, 50, 1002);
        mySQLiteAdapter.insertScheduleTable(formattedTime, formattedTime2, formattedTime3, 50, 1002);

        mySQLiteAdapter.insertBookingTable(formattedDate, "WestLake", "Block H", 10001, 1001);
        mySQLiteAdapter.insertBookingTable(formattedDate, "Harvard", "Block A", 10002, 1002);
        mySQLiteAdapter.insertBookingTable(formattedDate, "Block B", "Harvard", 10003, 1002);

        mySQLiteAdapter.close();

        listContent.setText("The data has been inserted!");

        mySQLiteAdapter.openToRead();
        String user = mySQLiteAdapter.readUser();
        String bus = mySQLiteAdapter.readBus();
        String schedule = mySQLiteAdapter.readSchedule();
        String booking = mySQLiteAdapter.readBooking();
        listContent.setText(user);
        listContent.setText(bus);
        listContent.setText(schedule);
        listContent.setText(booking);

        ll.addView(listContent);
        setContentView(ll);
    }
}