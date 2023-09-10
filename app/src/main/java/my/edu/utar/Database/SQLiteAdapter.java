package my.edu.utar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SQLiteAdapter {

    //constant variable
    public static final String MYDATABASE_NAME = "WHEEL_IO_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String BOOKING_TABLE = "BOOKING_TABLE";
    public static final String BUS_TABLE = "BUS_TABLE";
    public static final String SCHEDULE_TABLE = "SCHEDULE_TABLE";
    public static final int MYDATABASE_VERSION = 12;

    //User table content
    public static final String USER_NAME = "userName";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_PASSWORD = "userPassword";
    public static final String USER_POINT = "userPoint";
    public static final String USER_CATEGORY = "userCategory";
    public static final String USER_STATUS = "userStatus";

    //bus table content
    public static final String BUS_PLATE_NO = "busPlateNo";
    public static final String BUS_CURRENT_STOP = "busCurrentStop";
    public static final String BUS_CURRENT_STOP_TIME = "busCurrentStopTime";
    public static final String BUS_STARTING_PLACE = "busStartingPlace";
    public static final String BUS_ENDING_PLACE = "busEndingPlace";

    //schedule table content
    public static final String SCHEDULE_TIME_STARTING = "timeStarting";
    public static final String SCHEDULE_TIME_ENDING = "timeEnding";
    public static final String SCHEDULE_DATE = "scheduleDate";
    public static final String SCHEDULE_SEAT_AVAILABLE = "seatAvailable";

    //booking table content
    public static final String BOOKING_DATE = "bookingDate";
    public static final String BOOKING_PICKUP = "bookingPickup";
    public static final String BOOKING_DROPOFF = "bookingDropoff";
    public static final String BOOKING_STATUS = "bookingStatus";
    public static final String BOOKING_NAME = "bookingName";

//-----------------------------------------------------------------------------

    //SQL command to create all table
    private static final String SCRIPT_CREATE_USER_TABLE =
            "create table if not exists " + USER_TABLE
                    + " (userID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + USER_NAME + " text not null, "
                    + USER_EMAIL + " text not null, "
                    + USER_PASSWORD + " text not null, "
                    + USER_POINT + " INTEGER not null, "
                    + USER_CATEGORY + " text not null, "
                    + USER_STATUS + " text not null);";

    private static final String SCRIPT_CREATE_BUS_TABLE =
            "create table if not exists " + BUS_TABLE
                    + " (busID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + BUS_PLATE_NO + " text not null, "
                    + BUS_CURRENT_STOP + " text not null, "
                    + BUS_CURRENT_STOP_TIME + " text not null, "
                    + BUS_STARTING_PLACE + " text not null, "
                    + BUS_ENDING_PLACE + " text not null, "
                    + "userID text not null);";

    private static final String SCRIPT_CREATE_SCHEDULE_TABLE =
            "create table if not exists " + SCHEDULE_TABLE
                    + " (scheduleID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SCHEDULE_TIME_STARTING + " text not null, "
                    + SCHEDULE_TIME_ENDING + " text not null, "
                    + SCHEDULE_DATE + " text not null, "
                    + SCHEDULE_SEAT_AVAILABLE + " INTEGER not null, "
                    + "busID INTEGER NOT NULL, "
                    + "FOREIGN KEY (busID) REFERENCES " + BUS_TABLE + "(busID));";

    private static final String SCRIPT_CREATE_BOOKING_TABLE =
            "create table if not exists " + BOOKING_TABLE
                    + " (bookingID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + BOOKING_DATE + " text not null, "
                    + BOOKING_PICKUP + " text not null, "
                    + BOOKING_DROPOFF + " text not null, "
                    + BOOKING_STATUS + " text not null, "
                    + BOOKING_NAME + " text not null, "
                    + "userID INTEGER NOT NULL, "
                    + "scheduleID INTEGER NOT NULL, "
                    + "FOREIGN KEY (userID) REFERENCES " + USER_TABLE + "(userID), "
                    + "FOREIGN KEY (scheduleID) REFERENCES " + SCHEDULE_TABLE + "(scheduleID));";


//----------------------------------------------------------------------------------------------

    //variables for db creation
    private Context context;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    //constructor for SQLiteAdapter
    public SQLiteAdapter(Context c) {
        context = c;
    }

    //open the database to write something
    public SQLiteAdapter openToWrite() throws android.database.SQLException
    {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
                MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase(); //writing mode

        return this;
    }

    //open the database to read something
    public SQLiteAdapter openToRead() throws android.database.SQLException
    {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
                MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase(); //reading mode

        return this;
    }

    //----------------------------------------------------------------------------------
    //INSERT INTO TABLE
    public long insertUserTable(String content1, String content2, String content3, int content4, String content5, String content6) {
        ContentValues contentValues = new ContentValues();
        //to write the content to the column of KEY_CONTENT
        contentValues.put(USER_NAME, content1);
        contentValues.put(USER_EMAIL, content2);
        contentValues.put(USER_PASSWORD, content3);
        contentValues.put(USER_POINT, content4);
        contentValues.put(USER_CATEGORY, content5);
        contentValues.put(USER_STATUS, content6);

        return sqLiteDatabase.insert(USER_TABLE, null, contentValues);
    }

    public long insertBusTable(String content1, String content2, String content3, String content4, String content5, String content6) {
        ContentValues contentValues = new ContentValues();
        //to write the content to the column of KEY_CONTENT
        contentValues.put(BUS_PLATE_NO, content1);
        contentValues.put(BUS_CURRENT_STOP, content2);
        contentValues.put(BUS_CURRENT_STOP_TIME, content3);
        contentValues.put(BUS_STARTING_PLACE, content4);
        contentValues.put(BUS_ENDING_PLACE, content5);
        contentValues.put("userID", content6);

        return sqLiteDatabase.insert(BUS_TABLE, null, contentValues);
    }

    public long insertScheduleTable(String content1, String content2, String content3, int content4, int content5) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCHEDULE_TIME_STARTING, content1);
        contentValues.put(SCHEDULE_TIME_ENDING, content2);
        contentValues.put(SCHEDULE_DATE, content3);
        contentValues.put(SCHEDULE_SEAT_AVAILABLE, content4);
        contentValues.put("busID", content5);
        return sqLiteDatabase.insert(SCHEDULE_TABLE, null, contentValues);
    }

    public boolean insertBookingTable(String content1, String content2, String content3, String content4, int content5, int content6, String content7)
    {
        ArrayList<String[]> scheduleList = readSchedule();
        ArrayList<String[]> userList = readUser();
        ContentValues contentValues = new ContentValues();
        for(int i=0; i<scheduleList.size(); i++){
            if(scheduleList.get(i)[0].equals(Integer.toString(content6))){
                for(int j=0; j<userList.size(); j++) {
                    if (userList.get(j)[0].equals(Integer.toString(content5))) {
                        //to write the content to the column of KEY_CONTENT
                        contentValues.put(BOOKING_DATE, content1);
                        contentValues.put(BOOKING_PICKUP, content2);
                        contentValues.put(BOOKING_DROPOFF, content3);
                        contentValues.put(BOOKING_STATUS, content4);
                        contentValues.put("userID", content5);
                        contentValues.put("scheduleID", content6);
                        contentValues.put(BOOKING_NAME, content7);
                        sqLiteDatabase.insert(BOOKING_TABLE, null, contentValues);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //------------------------------------------------------------------------------------------------
    //READ DATA
    public ArrayList<String[]> readUser() {
        String[] columns = new String[]{"userID", USER_NAME, USER_EMAIL, USER_PASSWORD, USER_POINT, USER_CATEGORY, USER_STATUS};
        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(USER_TABLE, columns,
                null, null, null, null, null);

        ArrayList<String[]> resultList = new ArrayList<>();

        int index_CONTENT = cursor.getColumnIndex("userID");
        int index_CONTENT_1 = cursor.getColumnIndex(USER_NAME);
        int index_CONTENT_2 = cursor.getColumnIndex(USER_EMAIL);
        int index_CONTENT_3 = cursor.getColumnIndex(USER_PASSWORD);
        int index_CONTENT_4 = cursor.getColumnIndex(USER_POINT);
        int index_CONTENT_5 = cursor.getColumnIndex(USER_CATEGORY);
        int index_CONTENT_6 = cursor.getColumnIndex(USER_STATUS);

        int count = 0;
        //it will read all the data until finish
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            if (count > 0) {
                String[] resultArray = new String[7];
                resultArray[0] = cursor.getString(index_CONTENT);
                resultArray[1] = cursor.getString(index_CONTENT_1);
                resultArray[2] = cursor.getString(index_CONTENT_2);
                resultArray[3] = cursor.getString(index_CONTENT_3);
                resultArray[4] = cursor.getString(index_CONTENT_4);
                resultArray[5] = cursor.getString(index_CONTENT_5);
                resultArray[6] = cursor.getString(index_CONTENT_6);
                resultList.add(resultArray);
            }
            count++;
        }
        cursor.close();
        return resultList;
    }

    public ArrayList<String[]> readUserByCondition(String condition, String conditionValue) {
        String[] columns = new String[]{"userID", USER_NAME, USER_EMAIL, USER_PASSWORD, USER_POINT, USER_CATEGORY, USER_STATUS};
        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(USER_TABLE, columns,
                condition + "=?", new String[]{conditionValue}, null, null,
                USER_NAME + " ASC");

        ArrayList<String[]> resultList = new ArrayList<>();

        int index_CONTENT = cursor.getColumnIndex("userID");
        int index_CONTENT_1 = cursor.getColumnIndex(USER_NAME);
        int index_CONTENT_2 = cursor.getColumnIndex(USER_EMAIL);
        int index_CONTENT_3 = cursor.getColumnIndex(USER_PASSWORD);
        int index_CONTENT_4 = cursor.getColumnIndex(USER_POINT);
        int index_CONTENT_5 = cursor.getColumnIndex(USER_CATEGORY);
        int index_CONTENT_6 = cursor.getColumnIndex(USER_STATUS);

        //it will read all the data until finish
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            String[] resultArray = new String[7];
            resultArray[0] = cursor.getString(index_CONTENT);
            resultArray[1] = cursor.getString(index_CONTENT_1);
            resultArray[2] = cursor.getString(index_CONTENT_2);
            resultArray[3] = cursor.getString(index_CONTENT_3);
            resultArray[4] = cursor.getString(index_CONTENT_4);
            resultArray[5] = cursor.getString(index_CONTENT_5);
            resultArray[6] = cursor.getString(index_CONTENT_6);
            resultList.add(resultArray);

        }
        cursor.close();
        return resultList;
    }

    public ArrayList<String[]> readBus() {
        String[] columns = new String[]{"busID", BUS_PLATE_NO, BUS_CURRENT_STOP, BUS_CURRENT_STOP_TIME, BUS_STARTING_PLACE, BUS_ENDING_PLACE, "userID"};
        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(BUS_TABLE, columns,
                null, null, null, null, null);

        ArrayList<String[]> resultList = new ArrayList<>();

        int index_CONTENT = cursor.getColumnIndex("busID");
        int index_CONTENT_1 = cursor.getColumnIndex(BUS_PLATE_NO);
        int index_CONTENT_2 = cursor.getColumnIndex(BUS_CURRENT_STOP);
        int index_CONTENT_3 = cursor.getColumnIndex(BUS_CURRENT_STOP_TIME);
        int index_CONTENT_4 = cursor.getColumnIndex(BUS_STARTING_PLACE);
        int index_CONTENT_5 = cursor.getColumnIndex(BUS_ENDING_PLACE);
        int index_CONTENT_6 = cursor.getColumnIndex("userID");

        int count = 0;
        //it will read all the data until finish
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            if (count > 0) {
                String[] resultArray = new String[7];
                resultArray[0] = cursor.getString(index_CONTENT);
                resultArray[1] = cursor.getString(index_CONTENT_1);
                resultArray[2] = cursor.getString(index_CONTENT_2);
                resultArray[3] = cursor.getString(index_CONTENT_3);
                resultArray[4] = cursor.getString(index_CONTENT_4);
                resultArray[5] = cursor.getString(index_CONTENT_5);
                resultArray[6] = cursor.getString(index_CONTENT_6);
                resultList.add(resultArray);
            }
            count++;
        }
        cursor.close();
        return resultList;
    }

    public ArrayList<String[]> readBusByCondition(String condition, String conditionValue) {
        String[] columns = new String[]{"busID", BUS_PLATE_NO, BUS_CURRENT_STOP, BUS_CURRENT_STOP_TIME, BUS_STARTING_PLACE, BUS_ENDING_PLACE, "userID"};
        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(BUS_TABLE, columns,
                condition + "=?", new String[]{conditionValue}, null, null,
                BUS_PLATE_NO + " ASC");

        ArrayList<String[]> resultList = new ArrayList<>();

        int index_CONTENT = cursor.getColumnIndex("busID");
        int index_CONTENT_1 = cursor.getColumnIndex(BUS_PLATE_NO);
        int index_CONTENT_2 = cursor.getColumnIndex(BUS_CURRENT_STOP);
        int index_CONTENT_3 = cursor.getColumnIndex(BUS_CURRENT_STOP_TIME);
        int index_CONTENT_4 = cursor.getColumnIndex(BUS_STARTING_PLACE);
        int index_CONTENT_5 = cursor.getColumnIndex(BUS_ENDING_PLACE);
        int index_CONTENT_6 = cursor.getColumnIndex("userID");

        //it will read all the data until finish
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            String[] resultArray = new String[7];
            resultArray[0] = cursor.getString(index_CONTENT);
            resultArray[1] = cursor.getString(index_CONTENT_1);
            resultArray[2] = cursor.getString(index_CONTENT_2);
            resultArray[3] = cursor.getString(index_CONTENT_3);
            resultArray[4] = cursor.getString(index_CONTENT_4);
            resultArray[5] = cursor.getString(index_CONTENT_5);
            resultArray[6] = cursor.getString(index_CONTENT_6);
            resultList.add(resultArray);

        }
        cursor.close();
        return resultList;
    }

    public ArrayList<String[]> readSchedule() {
        String[] columns = new String[]{"scheduleID", SCHEDULE_TIME_STARTING, SCHEDULE_TIME_ENDING, SCHEDULE_DATE, SCHEDULE_SEAT_AVAILABLE, "busID"};
        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(SCHEDULE_TABLE, columns,
                null, null, null, null, null);

        ArrayList<String[]> resultList = new ArrayList<>();

        int index_CONTENT = cursor.getColumnIndex("scheduleID");
        int index_CONTENT_1 = cursor.getColumnIndex(SCHEDULE_TIME_STARTING);
        int index_CONTENT_2 = cursor.getColumnIndex(SCHEDULE_TIME_ENDING);
        int index_CONTENT_3 = cursor.getColumnIndex(SCHEDULE_DATE);
        int index_CONTENT_4 = cursor.getColumnIndex(SCHEDULE_SEAT_AVAILABLE);
        int index_CONTENT_5 = cursor.getColumnIndex("busID");

        int count = 0;
        //it will read all the data until finish
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            if (count > 0) {
                String[] resultArray = new String[6];
                resultArray[0] = cursor.getString(index_CONTENT);
                resultArray[1] = cursor.getString(index_CONTENT_1);
                resultArray[2] = cursor.getString(index_CONTENT_2);
                resultArray[3] = cursor.getString(index_CONTENT_3);
                resultArray[4] = cursor.getString(index_CONTENT_4);
                resultArray[5] = cursor.getString(index_CONTENT_5);
                resultList.add(resultArray);
            }
            count++;
        }
        cursor.close();
        return resultList;
    }

    public ArrayList<String[]> readScheduleByCondition(String condition, String conditionValue) {
        String[] columns = new String[]{"scheduleID", SCHEDULE_TIME_STARTING, SCHEDULE_TIME_ENDING, SCHEDULE_DATE, SCHEDULE_SEAT_AVAILABLE, "busID"};
        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(SCHEDULE_TABLE, columns,
                condition + "=?", new String[]{conditionValue}, null, null,
                SCHEDULE_TIME_STARTING + " ASC");

        ArrayList<String[]> resultList = new ArrayList<>();

        int index_CONTENT = cursor.getColumnIndex("scheduleID");
        int index_CONTENT_1 = cursor.getColumnIndex(SCHEDULE_TIME_STARTING);
        int index_CONTENT_2 = cursor.getColumnIndex(SCHEDULE_TIME_ENDING);
        int index_CONTENT_3 = cursor.getColumnIndex(SCHEDULE_DATE);
        int index_CONTENT_4 = cursor.getColumnIndex(SCHEDULE_SEAT_AVAILABLE);
        int index_CONTENT_5 = cursor.getColumnIndex("busID");

        //it will read all the data until finish
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            String[] resultArray = new String[6];
            resultArray[0] = cursor.getString(index_CONTENT);
            resultArray[1] = cursor.getString(index_CONTENT_1);
            resultArray[2] = cursor.getString(index_CONTENT_2);
            resultArray[3] = cursor.getString(index_CONTENT_3);
            resultArray[4] = cursor.getString(index_CONTENT_4);
            resultArray[5] = cursor.getString(index_CONTENT_5);
            resultList.add(resultArray);
        }
        cursor.close();
        return resultList;
    }

    public ArrayList<String[]> readScheduleByTwoCondition(String condition, String conditionValue, String condition2, String conditionValue2) {
        String[] columns = new String[]{"scheduleID", SCHEDULE_TIME_STARTING, SCHEDULE_TIME_ENDING, SCHEDULE_DATE, SCHEDULE_SEAT_AVAILABLE, "busID"};
        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(SCHEDULE_TABLE, columns,
                condition + "=? AND "+ condition2 +"=?", new String[]{conditionValue, conditionValue2}, null, null,
                SCHEDULE_TIME_STARTING + " ASC");

        ArrayList<String[]> resultList = new ArrayList<>();

        int index_CONTENT = cursor.getColumnIndex("scheduleID");
        int index_CONTENT_1 = cursor.getColumnIndex(SCHEDULE_TIME_STARTING);
        int index_CONTENT_2 = cursor.getColumnIndex(SCHEDULE_TIME_ENDING);
        int index_CONTENT_3 = cursor.getColumnIndex(SCHEDULE_DATE);
        int index_CONTENT_4 = cursor.getColumnIndex(SCHEDULE_SEAT_AVAILABLE);
        int index_CONTENT_5 = cursor.getColumnIndex("busID");

        //it will read all the data until finish
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            String[] resultArray = new String[6];
            resultArray[0] = cursor.getString(index_CONTENT);
            resultArray[1] = cursor.getString(index_CONTENT_1);
            resultArray[2] = cursor.getString(index_CONTENT_2);
            resultArray[3] = cursor.getString(index_CONTENT_3);
            resultArray[4] = cursor.getString(index_CONTENT_4);
            resultArray[5] = cursor.getString(index_CONTENT_5);
            resultList.add(resultArray);
        }
        cursor.close();
        return resultList;
    }

    public ArrayList<String[]> readScheduleBySeat(String condition, int seat) {
        String[] columns = new String[]{"scheduleID", SCHEDULE_TIME_STARTING, SCHEDULE_TIME_ENDING, SCHEDULE_DATE, SCHEDULE_SEAT_AVAILABLE, "busID"};
        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(SCHEDULE_TABLE, columns,
                condition + "< "+seat, null, null, null,
                SCHEDULE_TIME_STARTING + " ASC");

        ArrayList<String[]> resultList = new ArrayList<>();

        int index_CONTENT = cursor.getColumnIndex("scheduleID");
        int index_CONTENT_1 = cursor.getColumnIndex(SCHEDULE_TIME_STARTING);
        int index_CONTENT_2 = cursor.getColumnIndex(SCHEDULE_TIME_ENDING);
        int index_CONTENT_3 = cursor.getColumnIndex(SCHEDULE_DATE);
        int index_CONTENT_4 = cursor.getColumnIndex(SCHEDULE_SEAT_AVAILABLE);
        int index_CONTENT_5 = cursor.getColumnIndex("busID");

        //it will read all the data until finish
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            String[] resultArray = new String[6];
            resultArray[0] = cursor.getString(index_CONTENT);
            resultArray[1] = cursor.getString(index_CONTENT_1);
            resultArray[2] = cursor.getString(index_CONTENT_2);
            resultArray[3] = cursor.getString(index_CONTENT_3);
            resultArray[4] = cursor.getString(index_CONTENT_4);
            resultArray[5] = cursor.getString(index_CONTENT_5);
            resultList.add(resultArray);
        }
        cursor.close();
        return resultList;
    }

    public ArrayList<String[]> readBooking() {
        String[] columns = new String[]{"bookingID", BOOKING_DATE, BOOKING_PICKUP, BOOKING_DROPOFF, BOOKING_STATUS, "userID", "scheduleID", BOOKING_NAME};
        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(BOOKING_TABLE, columns,
                null, null, null, null, null);

        ArrayList<String[]> resultList = new ArrayList<>();

        int index_CONTENT = cursor.getColumnIndex("bookingID");
        int index_CONTENT_1 = cursor.getColumnIndex(BOOKING_DATE);
        int index_CONTENT_2 = cursor.getColumnIndex(BOOKING_PICKUP);
        int index_CONTENT_3 = cursor.getColumnIndex(BOOKING_DROPOFF);
        int index_CONTENT_4 = cursor.getColumnIndex(BOOKING_STATUS);
        int index_CONTENT_5 = cursor.getColumnIndex("userID");
        int index_CONTENT_6 = cursor.getColumnIndex("scheduleID");
        int index_CONTENT_7 = cursor.getColumnIndex(BOOKING_NAME);

        int count = 0;
        //it will read all the data until finish
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            if (count > 0) {
                String[] resultArray = new String[8];
                resultArray[0] = cursor.getString(index_CONTENT);
                resultArray[1] = cursor.getString(index_CONTENT_1);
                resultArray[2] = cursor.getString(index_CONTENT_2);
                resultArray[3] = cursor.getString(index_CONTENT_3);
                resultArray[4] = cursor.getString(index_CONTENT_4);
                resultArray[5] = cursor.getString(index_CONTENT_5);
                resultArray[6] = cursor.getString(index_CONTENT_6);
                resultArray[7]=cursor.getString(index_CONTENT_7);
                resultList.add(resultArray);
            }
            count++;
        }
        cursor.close();
        return resultList;
    }

    public ArrayList<String[]> readBookingByCondition(String condition, String conditionValue) {
        String[] columns = new String[]{"bookingID", BOOKING_DATE, BOOKING_PICKUP, BOOKING_DROPOFF, BOOKING_STATUS, "userID", "scheduleID", BOOKING_NAME};
        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(BOOKING_TABLE, columns,
                condition + "=?", new String[]{conditionValue}, null, null,
                BOOKING_DATE + " ASC");

        ArrayList<String[]> resultList = new ArrayList<>();

        int index_CONTENT = cursor.getColumnIndex("bookingID");
        int index_CONTENT_1 = cursor.getColumnIndex(BOOKING_DATE);
        int index_CONTENT_2 = cursor.getColumnIndex(BOOKING_PICKUP);
        int index_CONTENT_3 = cursor.getColumnIndex(BOOKING_DROPOFF);
        int index_CONTENT_4 = cursor.getColumnIndex(BOOKING_STATUS);
        int index_CONTENT_5 = cursor.getColumnIndex("userID");
        int index_CONTENT_6 = cursor.getColumnIndex("scheduleID");
        int index_CONTENT_7 = cursor.getColumnIndex(BOOKING_NAME);

        //it will read all the data until finish
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            String[] resultArray = new String[8];
            resultArray[0] = cursor.getString(index_CONTENT);
            resultArray[1] = cursor.getString(index_CONTENT_1);
            resultArray[2] = cursor.getString(index_CONTENT_2);
            resultArray[3] = cursor.getString(index_CONTENT_3);
            resultArray[4] = cursor.getString(index_CONTENT_4);
            resultArray[5] = cursor.getString(index_CONTENT_5);
            resultArray[6] = cursor.getString(index_CONTENT_6);
            resultArray[7]=cursor.getString(index_CONTENT_7);
            resultList.add(resultArray);
        }
        cursor.close();
        return resultList;
    }

    public ArrayList<String[]> readBusPlateNos() {
        String[] columns = new String[]{BUS_PLATE_NO};

        // To locate the cursor
        Cursor cursor = sqLiteDatabase.query(BUS_TABLE, columns, null, null, null, null, null);

        ArrayList<String[]> resultList = new ArrayList<>();

        int indexID = cursor.getColumnIndex(BUS_PLATE_NO);

        // It will read all the data until finish
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String[] resultArray = new String[1];
            resultArray[0] = cursor.getString(indexID);
            resultList.add(resultArray);
        }

        cursor.close();

        return resultList;
    }

    public ArrayList<String[]> readScheduleDetails() {
        String[] columns = new String[]{ "scheduleID", SCHEDULE_TIME_STARTING, SCHEDULE_TIME_ENDING };

        // To locate the cursor
        Cursor cursor = sqLiteDatabase.query(SCHEDULE_TABLE, columns, null, null, null, null, null);

        ArrayList<String[]> resultList = new ArrayList<>();

        int indexID = cursor.getColumnIndex("scheduleID");
        int indexStartingTime = cursor.getColumnIndex(SCHEDULE_TIME_STARTING);
        int indexEndingTime = cursor.getColumnIndex(SCHEDULE_TIME_ENDING);

        // It will read all the data until finish
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String[] resultArray = new String[3];
            resultArray[0] = cursor.getString(indexID);
            resultArray[1] = cursor.getString(indexStartingTime);
            resultArray[2] = cursor.getString(indexEndingTime);
            resultList.add(resultArray);
        }

        cursor.close();

        return resultList;
    }


    //------------------------------------------------------------------------------------------------
    //UPDATE DB
    //update password
    public boolean updatePassword(String oldPassword, String newPassword) {
        SQLiteDatabase db = this.sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_PASSWORD, newPassword);

        // Define the WHERE clause to identify the row to update (assuming "ticket_id" is the primary key)
        String whereClause = "userPassword=?";
        String[] whereArgs = {oldPassword};

        // Perform the update operation
        int rowsAffected = db.update(USER_TABLE, values, whereClause, whereArgs);
        return rowsAffected >0;
    }

    // Update the schedule table based on certain conditions
    public boolean updateScheduleTableByInt(String updateColumn, int updateValues, String conditionColumn, String conditionValue) {
        ContentValues newValues = new ContentValues();
        newValues.put(updateColumn, updateValues);

        int rowsAffected = sqLiteDatabase.update(SCHEDULE_TABLE, newValues, conditionColumn + "=?", new String[]{conditionValue});
        return rowsAffected > 0;
    }

    public boolean updateScheduleTableByString(String updateColumn, String updateValues, String conditionColumn, String conditionValue) {
        ContentValues newValues = new ContentValues();
        newValues.put(updateColumn, updateValues);

        int rowsAffected = sqLiteDatabase.update(SCHEDULE_TABLE, newValues, conditionColumn + "=?", new String[]{conditionValue});
        return rowsAffected > 0;
    }

    public boolean updateUsername(String bookingName, String editedUsername) {
        SQLiteDatabase db = this.sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BOOKING_NAME, editedUsername);

        // Define the WHERE clause to identify the row to update (assuming "ticket_id" is the primary key)
        String whereClause = "bookingName=?";
        String[] whereArgs = {bookingName};

        // Perform the update operation
        int rowsAffected = db.update(BOOKING_TABLE, values, whereClause, whereArgs);
        return rowsAffected > 0;
    }
    public boolean updateUserPoint(String userID, int editedPoint) {
        SQLiteDatabase db = this.sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_POINT, editedPoint);

        // Define the WHERE clause to identify the row to update (assuming "ticket_id" is the primary key)
        String whereClause = "userID=?";
        String[] whereArgs = {userID};

        // Perform the update operation
        int rowsAffected = db.update(USER_TABLE, values, whereClause, whereArgs);
        return rowsAffected > 0;
    }

    public boolean updateUserStatus(String userID, String userStatus) {
        SQLiteDatabase db = this.sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_STATUS, userStatus);

        // Define the WHERE clause to identify the row to update (assuming "ticket_id" is the primary key)
        String whereClause = "userID=?";
        String[] whereArgs = {userID};

        // Perform the update operation
        int rowsAffected = db.update(USER_TABLE, values, whereClause, whereArgs);
        return rowsAffected >0;
    }

    public boolean updateBusLocation(String busID, String editedBusLocation) {
        SQLiteDatabase db = this.sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BUS_CURRENT_STOP, editedBusLocation);

        // Define the WHERE clause to identify the row to update (assuming "ticket_id" is the primary key)
        String whereClause = "busID=?";
        String[] whereArgs = {busID};

        // Perform the update operation
        int rowsAffected = db.update(BUS_TABLE, values, whereClause, whereArgs);
        return rowsAffected > 0;
    }
    //------------------------------------------------------------------------------------------------
    //check
    public boolean isEmailExists(String email) {
        String[] columns = {USER_EMAIL};
        String selection = USER_EMAIL + "=?";
        String[] selectionArgs = {email};

        Cursor cursor = sqLiteDatabase.query(USER_TABLE, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }

    //------------------------------------------------------------------------------------------------
    //delete specific booking table
    public boolean deleteBookingByCondition(String ticketId) {
        try {
            SQLiteDatabase db = this.sqLiteHelper.getWritableDatabase();
            int rowsDeleted = db.delete(BUS_TABLE, "ticketId" + "=?", new String[]{ticketId});
            db.close();

            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteScheduleByCondition(String scheduleDate) {
        try {
            SQLiteDatabase db = this.sqLiteHelper.getWritableDatabase();
            int rowsDeleted = db.delete(SCHEDULE_TABLE, "scheduleDate < ?", new String[]{scheduleDate});

            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    //delete all table
    public int deleteAll() {
        sqLiteDatabase.delete(BOOKING_TABLE, null, null);
        sqLiteDatabase.delete(SCHEDULE_TABLE, null, null);
        sqLiteDatabase.delete(USER_TABLE, null, null);
        sqLiteDatabase.delete(BUS_TABLE, null, null);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        ;
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + BOOKING_TABLE + "'");
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + SCHEDULE_TABLE + "'");
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + USER_TABLE + "'");
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + BUS_TABLE + "'");
        initializeTableSequence();
        return 1;
    }


    //------------------------------------------------------------------------------------------------
    //close the database
    public void close() {
        sqLiteHelper.close();
    }

    //------------------------------------------------------------------------------------------------
    //DB INITIALIZATION
    //SQLiteOpenHelper: A helper class to manage database creation and version management
    public class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(@Nullable Context context, @Nullable String name,
                            @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //create a table with column
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SCRIPT_CREATE_USER_TABLE);
            db.execSQL(SCRIPT_CREATE_BUS_TABLE);
            db.execSQL(SCRIPT_CREATE_SCHEDULE_TABLE);
            db.execSQL(SCRIPT_CREATE_BOOKING_TABLE);

            //make all table PK starting from desired value
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SCRIPT_CREATE_USER_TABLE);
            db.execSQL(SCRIPT_CREATE_BUS_TABLE);
            db.execSQL(SCRIPT_CREATE_SCHEDULE_TABLE);
            db.execSQL(SCRIPT_CREATE_BOOKING_TABLE);

            if (oldVersion < 12) {
                db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
                db.execSQL("DROP TABLE IF EXISTS " + BUS_TABLE);
                db.execSQL("DROP TABLE IF EXISTS " + SCHEDULE_TABLE);
                db.execSQL("DROP TABLE IF EXISTS " + BOOKING_TABLE);
                db.execSQL(SCRIPT_CREATE_USER_TABLE);
                db.execSQL(SCRIPT_CREATE_BUS_TABLE);
                db.execSQL(SCRIPT_CREATE_SCHEDULE_TABLE);
                db.execSQL(SCRIPT_CREATE_BOOKING_TABLE);

            }
        }
    }

    public void initializeTableSequence() {
        ContentValues values = new ContentValues();
        values.put("userID", 10000);
        values.put(USER_NAME, "DummyUser");
        values.put(USER_EMAIL, "dummy@gmail.com");
        values.put(USER_PASSWORD, "DummyUser");
        values.put(USER_POINT, 100);
        values.put(USER_CATEGORY, "student");
        values.put(USER_STATUS, "offline");
        sqLiteDatabase.insert(USER_TABLE, null, values);
        values.clear();

        values.put("busID", 1000);
        values.put(BUS_PLATE_NO, "DummyCarPlate");
        values.put(BUS_CURRENT_STOP, "DummyStop");
        values.put(BUS_CURRENT_STOP_TIME, "12:12:12");
        values.put(BUS_STARTING_PLACE, "DummyStartingPlace");
        values.put(BUS_ENDING_PLACE, "DummyEndingPlace");
        values.put("userID", 10000);
        sqLiteDatabase.insert(BUS_TABLE, null, values);
        values.clear();

        values.put("scheduleID", 0);
        values.put(SCHEDULE_TIME_STARTING, "12:12:12");
        values.put(SCHEDULE_TIME_ENDING, "12:12:12");
        values.put(SCHEDULE_DATE, "1212-12-12");
        values.put(SCHEDULE_SEAT_AVAILABLE, 0);
        values.put("busID", 1000);
        sqLiteDatabase.insert(SCHEDULE_TABLE, null, values);
        values.clear();

        values.put("bookingID", 100);
        values.put(BOOKING_DATE, "1212-12-12");
        values.put(BOOKING_PICKUP, "DummyPickup");
        values.put(BOOKING_DROPOFF, "DummyDropoff");
        values.put(BOOKING_STATUS, "none");
        values.put("userID", 10000);
        values.put("scheduleID", 0);
        values.put(BOOKING_NAME, "DummyName");
        sqLiteDatabase.insert(BOOKING_TABLE, null, values);
        values.clear();
    }
}
