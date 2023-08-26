package my.edu.wheelio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        Date oneHourLater = calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        Date twoHourLater = calendar.getTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTimeStr = timeFormat.format(currentTime);
        String oneHourLaterStr = timeFormat.format(oneHourLater);
        String twoHourLaterStr = timeFormat.format(twoHourLater);

        //let"s insert some data
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToWrite();

        mySQLiteAdapter.deleteAll();

        mySQLiteAdapter.insertUserTable("Ryan Li", "ryan123@gmail.com", "ryan123", 100, "student");
        mySQLiteAdapter.insertUserTable("Nicholas Ngiam", "nicholas123@gmail.com", "nicholas123", 100, "student");
        mySQLiteAdapter.insertUserTable("Wee Jeng Kai", "wee123@gmail.com", "wee123", 100, "driver");

        mySQLiteAdapter.insertBusTable("ABC123", "Block N", currentTimeStr, "Block A", "WestLake");
        mySQLiteAdapter.insertBusTable("XYZ123", "Block H", currentTimeStr, "Block P", "Harvard");

        mySQLiteAdapter.insertScheduleTable(currentTimeStr, oneHourLaterStr, twoHourLaterStr, 30, 1001);
        mySQLiteAdapter.insertScheduleTable(currentTimeStr, oneHourLaterStr, twoHourLaterStr, 30, 1001);
        mySQLiteAdapter.insertScheduleTable(currentTimeStr, oneHourLaterStr, twoHourLaterStr, 30, 1001);
        mySQLiteAdapter.insertScheduleTable(currentTimeStr, oneHourLaterStr, twoHourLaterStr, 50, 1002);
        mySQLiteAdapter.insertScheduleTable(currentTimeStr, oneHourLaterStr, twoHourLaterStr, 50, 1002);
        mySQLiteAdapter.insertScheduleTable(currentTimeStr, oneHourLaterStr, twoHourLaterStr, 50, 1002);

        mySQLiteAdapter.insertBookingTable(formattedDate, "WestLake", "Block H", 10001, 1);
        mySQLiteAdapter.insertBookingTable(formattedDate, "Harvard", "Block A", 10002, 3);
        mySQLiteAdapter.insertBookingTable(formattedDate, "Block B", "Harvard", 10003, 4);

        mySQLiteAdapter.close();

        //listContent.setText("The data has been inserted!");

        mySQLiteAdapter.openToRead();
        ArrayList<String[]> userList = mySQLiteAdapter.readUser();
        ArrayList<String[]> busList = mySQLiteAdapter.readBus();
        ArrayList<String[]> scheduleList = mySQLiteAdapter.readSchedule();
        ArrayList<String[]> bookingList = mySQLiteAdapter.readBooking();

        for(int i=1; i<userList.size(); i++) {
            for(int j=0; j<userList.get(i).length; j++){
                listContent.setText(userList.get(i)[j]);
            }
        }
        for(int i=1; i<busList.size(); i++) {
            for(int j=0; j<busList.get(i).length; j++){
                listContent.setText(busList.get(i)[j]);
            }
        }
        for(int i=1; i<scheduleList.size(); i++) {
            for(int j=0; j<scheduleList.get(i).length; j++){
                listContent.setText(scheduleList.get(i)[j]);
            }
        }
        for(int i=1; i<bookingList.size(); i++) {
            for (int j = 0; j < bookingList.get(i).length; j++) {
                listContent.setText(bookingList.get(i)[j]);
            }
        }

        ll.addView(listContent);
        setContentView(ll);
    }
}