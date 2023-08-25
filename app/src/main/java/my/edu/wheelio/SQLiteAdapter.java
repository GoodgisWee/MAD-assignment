package my.edu.wheelio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SQLiteAdapter {

    //constant variable
    public static final String MYDATABASE_NAME = "WHEEL_IO_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String BOOKING_TABLE = "BOOKING_TABLE";
    public static final String BUS_TABLE = "BUS_TABLE";
    public static final String SCHEDULE_TABLE = "SCHEDULE_TABLE";
    public static final int MYDATABASE_VERSION = 1;

    //User table content
    public static final String USER_NAME = "userName";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_PASSWORD = "userPassword";
    public static final String USER_POINT = "userPoint";
    public static final String USER_CATEGORY = "userCategory";

    //bus table content
    public static final String BUS_PLATE_NO = "busPlateNo";
    public static final String BUS_CURRENT_STOP = "busCurrentStop";
    public static final String BUS_CURRENT_STOP_TIME = "busCurrentStopTime";
    public static final String BUS_STARTING_PLACE = "busStartingPlace";
    public static final String BUS_ENDING_PLACE = "busEndingPlace";

    //schedule table content
    public static final String SCHEDULE_TIME_STARTING = "timeStarting";
    public static final String SCHEDULE_TIME_ENDING = "timeEnding";
    public static final String SCHEDULE_TIME_BACKENDING = "timeBackEnding";
    public static final String SCHEDULE_SEAT_AVAILABLE = "seatAvailable";

    //booking table content
    public static final String BOOKING_DATE = "bookingDate";
    public static final String BOOKING_PICKUP = "bookingPickup";
    public static final String BOOKING_DROPOFF = "bookingDropoff";

//-----------------------------------------------------------------------------
    //SQL command to create all table
    private static final String SCRIPT_CREATE_USER_TABLE =
            "create table " + USER_TABLE
                    + " (userID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + USER_NAME + " text not null, "
                    + USER_EMAIL + " text not null, "
                    + USER_PASSWORD + " text not null, "
                    + USER_POINT + " INTEGER not null, "
                    + USER_CATEGORY + " text not null);";

    private static final String SCRIPT_CREATE_BUS_TABLE =
            "create table " + BUS_TABLE
                    + " (busID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + BUS_PLATE_NO + " text not null, "
                    + BUS_CURRENT_STOP + " text not null, "
                    + BUS_CURRENT_STOP_TIME + " text not null, "
                    + BUS_STARTING_PLACE + " text not null, "
                    + BUS_ENDING_PLACE + " text not null);";

    private static final String SCRIPT_CREATE_SCHEDULE_TABLE =
            "create table " + SCHEDULE_TABLE
                    + " (scheduleID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SCHEDULE_TIME_STARTING + " text not null, "
                    + SCHEDULE_TIME_ENDING + " text not null, "
                    + SCHEDULE_TIME_BACKENDING + " text not null, "
                    + SCHEDULE_SEAT_AVAILABLE + " INTEGER not null, "
                    + "busID INTEGER NOT NULL, "
                    + "FOREIGN KEY (busID) REFERENCES " + BUS_TABLE + "(busID));";

    private static final String SCRIPT_CREATE_BOOKING_TABLE =
            "create table " + BOOKING_TABLE
                    + " (bookingID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + BOOKING_DATE + " text not null, "
                    + BOOKING_PICKUP + " text not null, "
                    + BOOKING_DROPOFF + " text not null, "
                    + "userID INTEGER NOT NULL, "
                    + "busID INTEGER NOT NULL, "
                    + "FOREIGN KEY (userID) REFERENCES " + USER_TABLE + "(userID), "
                    + "FOREIGN KEY (busID) REFERENCES " + BUS_TABLE + "(busID));";

//-----------------------------------------------------------------------------
    //variables for db creation
    private Context context;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    //constructor for SQLiteAdapter
    public SQLiteAdapter(Context c)
    {
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

//-----------------------------------------------------------------------------
    //insert command for all table
    public long insertUserTable(String content1, String content2, String content3, int content4, String content5)
    {
        ContentValues contentValues = new ContentValues();
        //to write the content to the column of KEY_CONTENT
        contentValues.put(USER_NAME, content1);
        contentValues.put(USER_EMAIL, content2);
        contentValues.put(USER_PASSWORD, content3);
        contentValues.put(USER_POINT, content4);
        contentValues.put(USER_CATEGORY, content5);

        return sqLiteDatabase.insert(USER_TABLE, null, contentValues);
    }

    public long insertBusTable(String content1, String content2, String content3, String content4, String content5)
    {
        ContentValues contentValues = new ContentValues();
        //to write the content to the column of KEY_CONTENT
        contentValues.put(BUS_PLATE_NO, content1);
        contentValues.put(BUS_CURRENT_STOP, content2);
        contentValues.put(BUS_CURRENT_STOP_TIME, content3);
        contentValues.put(BUS_STARTING_PLACE, content4);
        contentValues.put(BUS_ENDING_PLACE, content5);

        return sqLiteDatabase.insert(BUS_TABLE, null, contentValues);
    }

    public long insertScheduleTable(String content1, String content2, String content3, int content4, int content5)
    {
        ContentValues contentValues = new ContentValues();
        //to write the content to the column of KEY_CONTENT
        contentValues.put(SCHEDULE_TIME_STARTING, content1);
        contentValues.put(SCHEDULE_TIME_ENDING, content2);
        contentValues.put(SCHEDULE_TIME_BACKENDING, content3);
        contentValues.put(SCHEDULE_SEAT_AVAILABLE, content4);
        contentValues.put("busID", content5);

        return sqLiteDatabase.insert(SCHEDULE_TABLE, null, contentValues);
    }
    public long insertBookingTable(String content1, String content2, String content3, int content4, int content5)
    {
        ContentValues contentValues = new ContentValues();
        //to write the content to the column of KEY_CONTENT
        contentValues.put(BOOKING_DATE, content1);
        contentValues.put(BOOKING_PICKUP, content2);
        contentValues.put(BOOKING_DROPOFF, content3);
        contentValues.put("userID", content4);
        contentValues.put("busID", content5);

        return sqLiteDatabase.insert(BOOKING_TABLE, null, contentValues);
    }

//------------------------------------------------------------------------------------------------
    //READ DATA
    public String readUser()
    {
        String [] columns = new String[] {USER_NAME, USER_EMAIL, USER_PASSWORD, USER_POINT, USER_CATEGORY}; 
        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(USER_TABLE, columns,
                null, null, null, null, null) ;

        String result = "";

        int index_CONTENT_1 = cursor.getColumnIndex(USER_NAME);
        int index_CONTENT_2 = cursor.getColumnIndex(USER_EMAIL);
        int index_CONTENT_3 = cursor.getColumnIndex(USER_PASSWORD);
        int index_CONTENT_4 = cursor.getColumnIndex(USER_POINT);
        int index_CONTENT_5 = cursor.getColumnIndex(USER_CATEGORY);

        //it will read all the data until finish
        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext())
        {
            result = result + cursor.getString(index_CONTENT_1) +";"
                    + cursor.getString(index_CONTENT_2) + ";"
                    + cursor.getString(index_CONTENT_3) + ";"
                    + cursor.getString(index_CONTENT_4) + ";"
                    + cursor.getString(index_CONTENT_5) + "\n";
        }

        return result;
    }

    public String readBus()
    {
        String [] columns = new String[] {BUS_PLATE_NO, BUS_CURRENT_STOP, BUS_CURRENT_STOP_TIME, BUS_STARTING_PLACE, BUS_ENDING_PLACE}; 
        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(BUS_TABLE, columns,
                null, null, null, null, null) ;

        String result = "";

        int index_CONTENT_1 = cursor.getColumnIndex(BUS_PLATE_NO);
        int index_CONTENT_2 = cursor.getColumnIndex(BUS_CURRENT_STOP);
        int index_CONTENT_3 = cursor.getColumnIndex(BUS_CURRENT_STOP_TIME);
        int index_CONTENT_4 = cursor.getColumnIndex(BUS_STARTING_PLACE);
        int index_CONTENT_5 = cursor.getColumnIndex(BUS_ENDING_PLACE);

        //it will read all the data until finish
        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext())
        {
            result = result + cursor.getString(index_CONTENT_1) +";"
                    + cursor.getString(index_CONTENT_2) + ";"
                    + cursor.getString(index_CONTENT_3) + ";"
                    + cursor.getString(index_CONTENT_4) + ";"
                    + cursor.getString(index_CONTENT_5) + "\n";
        }

        return result;
    }

    public String readSchedule()
    {
        String [] columns = new String[] {SCHEDULE_TIME_STARTING, SCHEDULE_TIME_ENDING, SCHEDULE_TIME_BACKENDING, SCHEDULE_SEAT_AVAILABLE, "busID"};
        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(SCHEDULE_TABLE, columns,
                null, null, null, null, null) ;

        String result = "";

        int index_CONTENT_1 = cursor.getColumnIndex(SCHEDULE_TIME_STARTING);
        int index_CONTENT_2 = cursor.getColumnIndex(SCHEDULE_TIME_ENDING);
        int index_CONTENT_3 = cursor.getColumnIndex(SCHEDULE_TIME_BACKENDING);
        int index_CONTENT_4 = cursor.getColumnIndex(SCHEDULE_SEAT_AVAILABLE);
        int index_CONTENT_5 = cursor.getColumnIndex("busID");

        //it will read all the data until finish
        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext())
        {
            result = result + cursor.getString(index_CONTENT_1) +";"
                    + cursor.getString(index_CONTENT_2) + ";"
                    + cursor.getString(index_CONTENT_3) + ";"
                    + cursor.getString(index_CONTENT_4) + ";"
                    + cursor.getString(index_CONTENT_5) + "\n";
        }

        return result;
    }

    public String readBooking()
    {
        String [] columns = new String[] {BOOKING_DATE, BOOKING_PICKUP, BOOKING_DROPOFF, "userID", "busID"};
        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(BOOKING_TABLE, columns,
                null, null, null, null, null) ;

        String result = "";

        int index_CONTENT_1 = cursor.getColumnIndex(BOOKING_DATE);
        int index_CONTENT_2 = cursor.getColumnIndex(BOOKING_PICKUP);
        int index_CONTENT_3 = cursor.getColumnIndex(BOOKING_DROPOFF);
        int index_CONTENT_4 = cursor.getColumnIndex("userID");
        int index_CONTENT_5 = cursor.getColumnIndex("busID");

        //it will read all the data until finish
        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext())
        {
            result = result + cursor.getString(index_CONTENT_1) +";"
                    + cursor.getString(index_CONTENT_2) + ";"
                    + cursor.getString(index_CONTENT_3) + ";"
                    + cursor.getString(index_CONTENT_4) + ";"
                    + cursor.getString(index_CONTENT_5) + "\n";
        }

        return result;
    }

//------------------------------------------------------------------------------------------------
    //close the database
    public void close()
    {
        sqLiteHelper.close();
    }

    //DELETE TABLE
    public int deleteAll()
    {
        sqLiteDatabase.delete(USER_TABLE,null, null);
        sqLiteDatabase.delete(BOOKING_TABLE,null, null);
        sqLiteDatabase.delete(BUS_TABLE,null, null);
        sqLiteDatabase.delete(SCHEDULE_TABLE,null, null);
        return 1;
    }

//------------------------------------------------------------------------------------------------
    //SQLiteOpenHelper: A helper class to manage database creation and version management
    public class SQLiteHelper extends SQLiteOpenHelper
    {

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
            ContentValues values = new ContentValues();
            values.put("userID", 10000);
            values.put(USER_NAME, "DummyUser");
            values.put(USER_EMAIL, "dummy@gmail.com");
            values.put(USER_PASSWORD, "DummyUser");
            values.put(USER_POINT, 100);
            values.put(USER_CATEGORY, "student");
            db.insert(USER_TABLE, null, values);
            values.clear();

            values.put("busID", 1000);
            values.put(BUS_PLATE_NO, "DummyCarPlate");
            values.put(BUS_CURRENT_STOP, "DummyStop");
            values.put(BUS_CURRENT_STOP_TIME, "12:12:12");
            values.put(BUS_STARTING_PLACE, "DummyStartingPlace");
            values.put(BUS_ENDING_PLACE, "DummyEndingPlace");
            db.insert(BUS_TABLE, null, values);
            values.clear();

            values.put("bookingID", 100);
            values.put(BOOKING_DATE, "1212-12-12");
            values.put(BOOKING_PICKUP, "DummyPickup");
            values.put(BOOKING_DROPOFF, "DummyDropoff");
            values.put("userID", 10000);
            values.put("busID", 1000);
            db.insert(BOOKING_TABLE, null, values);
            values.clear();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SCRIPT_CREATE_USER_TABLE);
            db.execSQL(SCRIPT_CREATE_BOOKING_TABLE);
            db.execSQL(SCRIPT_CREATE_BUS_TABLE);
            db.execSQL(SCRIPT_CREATE_SCHEDULE_TABLE);
        }
    }
}
