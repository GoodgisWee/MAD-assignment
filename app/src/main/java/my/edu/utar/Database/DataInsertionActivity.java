package my.edu.utar.Database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import my.edu.utar.BookingPage.BookingPage;
import my.edu.utar.R;

public class DataInsertionActivity extends AppCompatActivity {

    private SQLiteAdapter mySQLiteAdapter;
    ArrayList<String[]> userList, busList, scheduleList, bookingList, scheduleListExist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySQLiteAdapter = new SQLiteAdapter(this);


        try{
            mySQLiteAdapter.openToWrite();
            //mySQLiteAdapter.deleteAll();
            //if no user then enter data into it
            userList = mySQLiteAdapter.readUser();

            if(userList.size()==0){
                mySQLiteAdapter.initializeTableSequence();
                mySQLiteAdapter.insertUserTable("Goodgis123", "wee123@gmail.com", "goodgis123", 100, "driver", "offline");
                mySQLiteAdapter.insertUserTable("Kuanchee123", "kuanchee123@gmail.com", "kuanchee123", 100, "driver", "offline");
                mySQLiteAdapter.insertUserTable("Jielun123", "jielun123@gmail.com", "jielun123", 100, "driver", "offline");

                mySQLiteAdapter.insertUserTable("Ryan123", "ryan123@gmail.com", "ryan123", 100, "user", "offline");
                mySQLiteAdapter.insertUserTable("Nicholas123", "nicholas123@gmail.com", "nicholas123", 100, "user", "offline");
                mySQLiteAdapter.insertUserTable("Wee123", "wee123@gmail.com", "wee123", 100, "user", "offline");
                mySQLiteAdapter.insertUserTable("Ahmad123", "ahmad123@gmail.com", "ahmad123", 100, "user", "offline");
            }


            //------------------INSERT BUS TABLE-------------------------
            //if no bus then insert data into it

            busList = mySQLiteAdapter.readBus();
            if(busList.size()==0){
                //bus to WestLake
                mySQLiteAdapter.insertBusTable("BTW 123", "Block G", "13:00:00", "Block N", "WestLake", "10001");
                mySQLiteAdapter.insertBusTable("BTW 123", "Block G", "13:00:00", "WestLake", "Block N", "10001");
                mySQLiteAdapter.insertBusTable("BTW 123", "Block G", "13:00:00", "Block G", "WestLake", "10001");
                mySQLiteAdapter.insertBusTable("BTW 123", "Block G", "13:00:00", "WestLake", "Block G", "10001");
                mySQLiteAdapter.insertBusTable("BTW 123", "Block G", "13:00:00", "Block D", "WestLake", "10001");
                mySQLiteAdapter.insertBusTable("BTW 123", "Block G", "13:00:00", "WestLake", "Block D", "10001");
                //bus to Harvard
                mySQLiteAdapter.insertBusTable("BTH 123", "Block N", "13:00:00", "Block N", "Harvard", "10002");
                mySQLiteAdapter.insertBusTable("BTH 123", "Block N", "13:00:00", "Harvard", "Block N", "10002");
                mySQLiteAdapter.insertBusTable("BTH 123", "Block N", "13:00:00", "Block G", "Harvard", "10002");
                mySQLiteAdapter.insertBusTable("BTH 123", "Block N", "13:00:00", "Harvard", "Block G", "10002");
                mySQLiteAdapter.insertBusTable("BTH 123", "Block N", "13:00:00", "Block D", "Harvard", "10002");
                mySQLiteAdapter.insertBusTable("BTH 123", "Block N", "13:00:00", "Harvard", "Block D", "10002");
                //bus to Stanford
                mySQLiteAdapter.insertBusTable("BTS 123", "Block D", "13:00:00", "Block N", "Stanford", "10003");
                mySQLiteAdapter.insertBusTable("BTS 123", "Block D", "13:00:00", "Stanford", "Block N", "10003");
                mySQLiteAdapter.insertBusTable("BTS 123", "Block D", "13:00:00", "Block G", "Stanford", "10003");
                mySQLiteAdapter.insertBusTable("BTS 123", "Block D", "13:00:00", "Stanford", "Block G", "10003");
                mySQLiteAdapter.insertBusTable("BTS 123", "Block D", "13:00:00", "Block D", "Stanford", "10003");
                mySQLiteAdapter.insertBusTable("BTS 123", "Block D", "13:00:00", "Stanford", "Block D", "10003");
            }

            //------------------------------INSERT SCHEDULE TABLE----------------
            //Get Date
            TimeZone malaysiaTimeZone = TimeZone.getTimeZone("Asia/Kuala_Lumpur");
            Calendar calendar = Calendar.getInstance(malaysiaTimeZone);
            SimpleDateFormat dateStrFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            dateStrFormat.setTimeZone(malaysiaTimeZone);
            String currentDateStr = dateStrFormat.format(calendar.getTime());
            /*scheduleListExist= mySQLiteAdapter.readScheduleBySeat("seatAvailable", 30);
            String[] dateExist = new String[scheduleList.size()];
            String[] startTimeExist = new String[scheduleList.size()];
            String[] busIdExist = new String[scheduleList.size()];
            for(int i=0; i<scheduleList.size(); i++){
                    dateExist[i]=scheduleList.get(i)[3];
                startTimeExist[i]=scheduleList.get(i)[1];
                busIdExist[i]=scheduleList.get(i)[5];
            }*/
            mySQLiteAdapter.deleteScheduleByCondition(currentDateStr);

            //update for 5 days schedule
            for(int i=0;i<5;i++){
                if(i==0){
                    calendar.add(Calendar.DAY_OF_YEAR, 0);
                } else {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }
                Date date = calendar.getTime();
                String dateStr = dateStrFormat.format(date);
                scheduleList= mySQLiteAdapter.readScheduleByCondition("scheduleDate", dateStr);
                if(scheduleList.size()==0){
                    //add new schedule
                    String startTime = "09:00:00";
                    for(int j=0; j<11; j++){ //7 period bus driver short rest for 20 minute
                        for (int k = 0; k < 6; k++) { //6 bus drive each period
                            mySQLiteAdapter.insertScheduleTable(startTime, addMinutesToTime(startTime, 30), dateStr, 30, 1001 + k);
                            mySQLiteAdapter.insertScheduleTable(startTime, addMinutesToTime(startTime, 30), dateStr, 30, 1007 + k);
                            mySQLiteAdapter.insertScheduleTable(startTime, addMinutesToTime(startTime, 30), dateStr, 30, 1013 + k);
                            startTime = addMinutesToTime(startTime, 5);
                        }
                        startTime = addMinutesToTime(startTime, 20);
                    }
                }
            }

            //------------------INSERT BOOKING TABLE-------------------------
            //if no booking then insert booking record
            if(bookingList.size()==0){

                mySQLiteAdapter.insertBookingTable("2022-07-17", "Harvard", "Block G", "past", 10004, 22, "Young Lai Sien");
                mySQLiteAdapter.insertBookingTable("2022-07-17", "WestLake", "Block D", "past", 10004, 17, "Man Rock");
                mySQLiteAdapter.insertBookingTable("2022-07-18", "Harvard", "Block G", "past", 10004, 22, "Young Lai Sien");
                mySQLiteAdapter.insertBookingTable("2022-07-17", "Harvard", "Block N", "past", 10005, 32, "Wee Jeng Kai");
                mySQLiteAdapter.insertBookingTable("2022-07-20", "Harvard", "Block G", "past", 10005, 1, "Wee Jeng Kai");
                mySQLiteAdapter.insertBookingTable("2022-07-14", "Stanford", "Block D", "past", 10006, 42, "Andrew");
                mySQLiteAdapter.insertBookingTable("2022-07-17", "Harvard", "Block N", "past", 10007, 19, "Jabita");
            }

        } catch(Exception e){
            e.printStackTrace();
        } finally {
            mySQLiteAdapter.close();
            boolean userLoginAlready = false;
            String uid = "";
            if(userList != null && userList.size() > 0){
                for(int i=0; i<userList.size(); i++){
                    if(userList.get(i)[6].equals("online")){
                        userLoginAlready = true;
                        uid = userList.get(i)[0];
                        break;
                    }
                }
            }

            if(userLoginAlready == true){
                Intent intent = new Intent(DataInsertionActivity.this, my.edu.utar.BookingPage.BookingPage.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(DataInsertionActivity.this, my.edu.utar.login.MainActivity.class);
                startActivity(intent);
                finish();
            }

        }
        //------------------INSERT USER TABLE-------------------------


    }

    // Function to add minutes to a time string
    public String addMinutesToTime(String time, int minutesToAdd) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(time));
            calendar.add(Calendar.MINUTE, minutesToAdd);
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return time; // Return the original time if there's an error
        }
    }
}