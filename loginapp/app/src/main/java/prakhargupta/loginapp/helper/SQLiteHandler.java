package prakhargupta.loginapp.helper;

/**
 * Created by Prakhar Gupta on 3/29/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "cms";

    // Login table name
    private static final String TABLE_USER = "users";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DBID = "dbid";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_DOB = "dob";
    private static final String KEY_EMAIL = "email";
//    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        System.out.println("00000000000000000000000000000000000000000000000000000000000000000000000000000");
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        System.out.println("111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DBID + " TEXT," + KEY_USERNAME + " TEXT," + KEY_DOB + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_CREATED_AT + " TEXT" + ")";
        Log.d("in sqlite handler: ", CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        System.out.println("222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String dbid, String username, String dob, String email, String created_at) {
        System.out.println("33333333333333333333333333333333333333333333333333333333333333333333333333333333");
        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
//        System.out.println("111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
//        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
//                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DBID + " TEXT," + KEY_USERNAME + " TEXT," + KEY_DOB + " TEXT,"
//                + KEY_EMAIL + " TEXT UNIQUE," + KEY_CREATED_AT + " TEXT" + ")";
//        Log.d("in sqlite handler: ", CREATE_LOGIN_TABLE);
//        db.execSQL(CREATE_LOGIN_TABLE);

        ContentValues values = new ContentValues();
        values.put(KEY_DBID, dbid); // Email
        values.put(KEY_USERNAME, username); // Name
        values.put(KEY_DOB, dob); // Email
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        System.out.println("44444444444444444444444444444444444444444444444444444444444444444444444444444");
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("dbid", cursor.getString(1));
            user.put("username", cursor.getString(2));
            user.put("dob", cursor.getString(3));
            user.put("email", cursor.getString(4));
            user.put("created_at", cursor.getString(5));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        System.out.println("555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555");
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}