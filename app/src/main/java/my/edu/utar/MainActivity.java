package my.edu.utar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

        //get currentTime
        Locale malaysiaLocale = new Locale("ms", "MY");
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", malaysiaLocale);
        String formattedDate = dateFormat.format(currentDate);

        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        Date oneHourLater = calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        Date twoHourLater = calendar.getTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:00", malaysiaLocale);
        String startingTimeStr = timeFormat.format(currentTime);
        String arrivedTimeStr = timeFormat.format(oneHourLater);

        ll.addView(listContent);
        setContentView(ll);

        //let's insert some data
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToWrite();

        mySQLiteAdapter.deleteAll();


        mySQLiteAdapter.insertUserTable("Ryan Li", "ryan123@gmail.com", "ryan123", 100, "student");
        mySQLiteAdapter.insertUserTable("Nicholas Ngiam", "nicholas123@gmail.com", "nicholas123", 100, "student");
        mySQLiteAdapter.insertUserTable("Wee Jeng Kai", "wee123@gmail.com", "wee123", 100, "driver");

        //bus to WestLake
        mySQLiteAdapter.insertBusTable("BTW 123", "Block G", "13:00:00", "Block N", "WestLake");
        mySQLiteAdapter.insertBusTable("BTW 123", "Block G", "13:00:00", "Block G", "WestLake");
        mySQLiteAdapter.insertBusTable("BTW 123", "Block G", "13:00:00", "Block D", "WestLake");
        mySQLiteAdapter.insertBusTable("BTW 123", "Block G", "13:00:00", "WestLake", "Block N");
        mySQLiteAdapter.insertBusTable("BTW 123", "Block G", "13:00:00", "WestLake", "Block G");
        mySQLiteAdapter.insertBusTable("BTW 123", "Block G", "13:00:00", "WestLake", "Block D");
        //bus to Harvard
        mySQLiteAdapter.insertBusTable("BTH 123", "Block G", "13:00:00", "Block N", "Harvard");
        mySQLiteAdapter.insertBusTable("BTH 123", "Block G", "13:00:00", "Block G", "Harvard");
        mySQLiteAdapter.insertBusTable("BTH 123", "Block G", "13:00:00", "Block D", "Harvard");
        mySQLiteAdapter.insertBusTable("BTH 123", "Block G", "13:00:00", "Harvard", "Block N");
        mySQLiteAdapter.insertBusTable("BTH 123", "Block G", "13:00:00", "Harvard", "Block G");
        mySQLiteAdapter.insertBusTable("BTH 123", "Block G", "13:00:00", "Harvard", "Block D");
        //bus to Stanford
        mySQLiteAdapter.insertBusTable("BTS 123", "Block G", "13:00:00", "Block N", "Stanford");
        mySQLiteAdapter.insertBusTable("BTS 123", "Block G", "13:00:00", "Block G", "Stanford");
        mySQLiteAdapter.insertBusTable("BTS 123", "Block G", "13:00:00", "Block D", "Stanford");
        mySQLiteAdapter.insertBusTable("BTS 123", "Block G", "13:00:00", "Stanford", "Block N");
        mySQLiteAdapter.insertBusTable("BTS 123", "Block G", "13:00:00", "Stanford", "Block G");
        mySQLiteAdapter.insertBusTable("BTS 123", "Block G", "13:00:00", "Stanford", "Block D");

        //WestLake Bus Schedule
        mySQLiteAdapter.insertScheduleTable("09:00:00", "09:30:00", "30-08-2023", 30, 1001);
        mySQLiteAdapter.insertScheduleTable("09:05:00", "09:35:00", "30-08-2023", 30, 1002);
        mySQLiteAdapter.insertScheduleTable("09:10:00", "09:40:00", "30-08-2023", 30, 1003);
        mySQLiteAdapter.insertScheduleTable("09:30:00", "10:00:00", "30-08-2023", 30, 1004);
        mySQLiteAdapter.insertScheduleTable("09:35:00", "10:05:00", "30-08-2023", 30, 1005);
        mySQLiteAdapter.insertScheduleTable("09:40:00", "10:10:00", "30-08-2023", 30, 1006);

        mySQLiteAdapter.insertScheduleTable("10:30:00", "11:00:00", "30-08-2023", 30, 1001);
        mySQLiteAdapter.insertScheduleTable("10:35:00", "11:05:00", "30-08-2023", 30, 1002);
        mySQLiteAdapter.insertScheduleTable("10:40:00", "11:10:00", "30-08-2023", 30, 1003);
        mySQLiteAdapter.insertScheduleTable("11:00:00", "11:30:00", "30-08-2023", 30, 1004);
        mySQLiteAdapter.insertScheduleTable("11:05:00", "11:35:00", "30-08-2023", 30, 1005);
        mySQLiteAdapter.insertScheduleTable("11:10:00", "11:40:00", "30-08-2023", 30, 1006);

        mySQLiteAdapter.insertScheduleTable("12:00:00", "12:30:00", "30-08-2023", 30, 1001);
        mySQLiteAdapter.insertScheduleTable("12:05:00", "12:35:00", "30-08-2023", 30, 1002);
        mySQLiteAdapter.insertScheduleTable("12:10:00", "12:40:00", "30-08-2023", 30, 1003);
        mySQLiteAdapter.insertScheduleTable("12:30:00", "13:00:00", "30-08-2023", 30, 1004);
        mySQLiteAdapter.insertScheduleTable("12:35:00", "13:05:00", "30-08-2023", 30, 1005);
        mySQLiteAdapter.insertScheduleTable("12:40:00", "13:10:00", "30-08-2023", 30, 1006);

        mySQLiteAdapter.insertScheduleTable("14:00:00", "14:30:00", "30-08-2023", 30, 1001);
        mySQLiteAdapter.insertScheduleTable("14:05:00", "14:35:00", "30-08-2023", 30, 1002);
        mySQLiteAdapter.insertScheduleTable("14:10:00", "14:40:00", "30-08-2023", 30, 1003);
        mySQLiteAdapter.insertScheduleTable("14:30:00", "15:00:00", "30-08-2023", 30, 1004);
        mySQLiteAdapter.insertScheduleTable("14:35:00", "15:05:00", "30-08-2023", 30, 1005);
        mySQLiteAdapter.insertScheduleTable("14:40:00", "15:10:00", "30-08-2023", 30, 1006);

        mySQLiteAdapter.insertScheduleTable("16:00:00", "16:30:00", "30-08-2023", 30, 1001);
        mySQLiteAdapter.insertScheduleTable("16:05:00", "16:35:00", "30-08-2023", 30, 1002);
        mySQLiteAdapter.insertScheduleTable("16:10:00", "16:40:00", "30-08-2023", 30, 1003);
        mySQLiteAdapter.insertScheduleTable("16:30:00", "17:00:00", "30-08-2023", 30, 1004);
        mySQLiteAdapter.insertScheduleTable("16:35:00", "17:05:00", "30-08-2023", 30, 1005);
        mySQLiteAdapter.insertScheduleTable("16:40:00", "17:10:00", "30-08-2023", 30, 1006);

        mySQLiteAdapter.insertScheduleTable("17:30:00", "17:40:00", "30-08-2023", 30, 1001);
        mySQLiteAdapter.insertScheduleTable("17:35:00", "17:45:00", "30-08-2023", 30, 1002);
        mySQLiteAdapter.insertScheduleTable("17:40:00", "17:50:00", "30-08-2023", 30, 1003);
        mySQLiteAdapter.insertScheduleTable("18:00:00", "18:10:00", "30-08-2023", 30, 1004);
        mySQLiteAdapter.insertScheduleTable("18:05:00", "18:15:00", "30-08-2023", 30, 1005);
        mySQLiteAdapter.insertScheduleTable("18:10:00", "18:20:00", "30-08-2023", 30, 1006);

        //Harvard Bus Schedule
        mySQLiteAdapter.insertScheduleTable("09:00:00", "09:30:00", "30-08-2023", 30, 1007);
        mySQLiteAdapter.insertScheduleTable("09:05:00", "09:35:00", "30-08-2023", 30, 1008);
        mySQLiteAdapter.insertScheduleTable("09:10:00", "09:40:00", "30-08-2023", 30, 1009);
        mySQLiteAdapter.insertScheduleTable("09:30:00", "10:00:00", "30-08-2023", 30, 1010);
        mySQLiteAdapter.insertScheduleTable("09:35:00", "10:05:00", "30-08-2023", 30, 1011);
        mySQLiteAdapter.insertScheduleTable("09:40:00", "10:10:00", "30-08-2023", 30, 1012);

        mySQLiteAdapter.insertScheduleTable("10:30:00", "11:00:00", "30-08-2023", 30, 1007); 
        mySQLiteAdapter.insertScheduleTable("10:35:00", "11:05:00", "30-08-2023", 30, 1008); 
        mySQLiteAdapter.insertScheduleTable("10:40:00", "11:10:00", "30-08-2023", 30, 1009); 
        mySQLiteAdapter.insertScheduleTable("11:00:00", "11:30:00", "30-08-2023", 30, 1010); 
        mySQLiteAdapter.insertScheduleTable("11:05:00", "11:35:00", "30-08-2023", 30, 1011); 
        mySQLiteAdapter.insertScheduleTable("11:10:00", "11:40:00", "30-08-2023", 30, 1012);

        mySQLiteAdapter.insertScheduleTable("12:00:00", "12:30:00", "30-08-2023", 30, 1007);
        mySQLiteAdapter.insertScheduleTable("12:05:00", "12:35:00", "30-08-2023", 30, 1008);
        mySQLiteAdapter.insertScheduleTable("12:10:00", "12:40:00", "30-08-2023", 30, 1009);
        mySQLiteAdapter.insertScheduleTable("12:30:00", "13:00:00", "30-08-2023", 30, 1010);
        mySQLiteAdapter.insertScheduleTable("12:35:00", "13:05:00", "30-08-2023", 30, 1011);
        mySQLiteAdapter.insertScheduleTable("12:40:00", "13:10:00", "30-08-2023", 30, 1012);

        mySQLiteAdapter.insertScheduleTable("14:00:00", "14:30:00", "30-08-2023", 30, 1007);
        mySQLiteAdapter.insertScheduleTable("14:05:00", "14:35:00", "30-08-2023", 30, 1008);
        mySQLiteAdapter.insertScheduleTable("14:10:00", "14:40:00", "30-08-2023", 30, 1009);
        mySQLiteAdapter.insertScheduleTable("14:30:00", "15:00:00", "30-08-2023", 30, 1010);
        mySQLiteAdapter.insertScheduleTable("14:35:00", "15:05:00", "30-08-2023", 30, 1011);
        mySQLiteAdapter.insertScheduleTable("14:40:00", "15:10:00", "30-08-2023", 30, 1012);

        mySQLiteAdapter.insertScheduleTable("16:00:00", "16:30:00", "30-08-2023", 30, 1007);
        mySQLiteAdapter.insertScheduleTable("16:05:00", "16:35:00", "30-08-2023", 30, 1008);
        mySQLiteAdapter.insertScheduleTable("16:10:00", "16:40:00", "30-08-2023", 30, 1009);
        mySQLiteAdapter.insertScheduleTable("16:30:00", "17:00:00", "30-08-2023", 30, 1010);
        mySQLiteAdapter.insertScheduleTable("16:35:00", "17:05:00", "30-08-2023", 30, 1011);
        mySQLiteAdapter.insertScheduleTable("16:40:00", "17:10:00", "30-08-2023", 30, 1012);

        mySQLiteAdapter.insertScheduleTable("17:30:00", "17:40:00", "30-08-2023", 30, 1007);
        mySQLiteAdapter.insertScheduleTable("17:35:00", "17:45:00", "30-08-2023", 30, 1008);
        mySQLiteAdapter.insertScheduleTable("17:40:00", "17:50:00", "30-08-2023", 30, 1009);
        mySQLiteAdapter.insertScheduleTable("18:00:00", "18:10:00", "30-08-2023", 30, 1010);
        mySQLiteAdapter.insertScheduleTable("18:05:00", "18:15:00", "30-08-2023", 30, 1011);
        mySQLiteAdapter.insertScheduleTable("18:10:00", "18:20:00", "30-08-2023", 30, 1012);

        //Stanford Bus Schedule
        mySQLiteAdapter.insertScheduleTable("12:00:00", "12:30:00", "30-08-2023", 30, 1013);
        mySQLiteAdapter.insertScheduleTable("12:05:00", "12:35:00", "30-08-2023", 30, 1014);
        mySQLiteAdapter.insertScheduleTable("12:10:00", "12:40:00", "30-08-2023", 30, 1015);
        mySQLiteAdapter.insertScheduleTable("12:30:00", "13:00:00", "30-08-2023", 30, 1016);
        mySQLiteAdapter.insertScheduleTable("12:35:00", "13:05:00", "30-08-2023", 30, 1017);
        mySQLiteAdapter.insertScheduleTable("12:40:00", "13:10:00", "30-08-2023", 30, 1018);

        mySQLiteAdapter.insertScheduleTable("14:00:00", "14:30:00", "30-08-2023", 30, 1013);
        mySQLiteAdapter.insertScheduleTable("14:05:00", "14:35:00", "30-08-2023", 30, 1014);
        mySQLiteAdapter.insertScheduleTable("14:10:00", "14:40:00", "30-08-2023", 30, 1015);
        mySQLiteAdapter.insertScheduleTable("14:30:00", "15:00:00", "30-08-2023", 30, 1016);
        mySQLiteAdapter.insertScheduleTable("14:35:00", "15:05:00", "30-08-2023", 30, 1017);
        mySQLiteAdapter.insertScheduleTable("14:40:00", "15:10:00", "30-08-2023", 30, 1018);

        mySQLiteAdapter.insertScheduleTable("16:00:00", "16:30:00", "30-08-2023", 30, 1013);
        mySQLiteAdapter.insertScheduleTable("16:05:00", "16:35:00", "30-08-2023", 30, 1014);
        mySQLiteAdapter.insertScheduleTable("16:10:00", "16:40:00", "30-08-2023", 30, 1015);
        mySQLiteAdapter.insertScheduleTable("16:30:00", "17:00:00", "30-08-2023", 30, 1016);
        mySQLiteAdapter.insertScheduleTable("16:35:00", "17:05:00", "30-08-2023", 30, 1017);
        mySQLiteAdapter.insertScheduleTable("16:40:00", "17:10:00", "30-08-2023", 30, 1018);

        mySQLiteAdapter.insertScheduleTable("17:30:00", "17:40:00", "30-08-2023", 30, 1013);
        mySQLiteAdapter.insertScheduleTable("17:35:00", "17:45:00", "30-08-2023", 30, 1014);
        mySQLiteAdapter.insertScheduleTable("17:40:00", "17:50:00", "30-08-2023", 30, 1015);
        mySQLiteAdapter.insertScheduleTable("18:00:00", "18:10:00", "30-08-2023", 30, 1016);
        mySQLiteAdapter.insertScheduleTable("18:05:00", "18:15:00", "30-08-2023", 30, 1017);
        mySQLiteAdapter.insertScheduleTable("18:10:00", "18:20:00", "30-08-2023", 30, 1018);

        mySQLiteAdapter.insertBookingTable(formattedDate, "Harvard", "Block G", "past", 10002, 22);
        mySQLiteAdapter.insertBookingTable(formattedDate, "Harvard", "Block N", "current", 10003, 23);

        mySQLiteAdapter.close();

        listContent.setText("The data has been inserted!");

        mySQLiteAdapter.openToRead();
        ArrayList<String[]> userList = mySQLiteAdapter.readUser();
        ArrayList<String[]> busList = mySQLiteAdapter.readBus();
        ArrayList<String[]> bookingList = mySQLiteAdapter.readBooking();
        ArrayList<String[]> scheduleList = mySQLiteAdapter.readSchedule();

        Intent intent = new Intent(this, BookingPage.class);
        startActivity(intent);
        /*//read the data from the table
        mySQLiteAdapter.openToRead();
        //String contentRead = mySQLiteAdapter.queueAll();
        String contentRead = mySQLiteAdapter.queueAll_Three();

        listContent.setText(contentRead);*/
    }
}