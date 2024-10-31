package com.example.wastemanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "waste_management.db";
    //private static final int DATABASE_VERSION = 1;
    private static final int DATABASE_VERSION = 2; // Increment the version number

    // Table and columns
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FULL_NAME = "fullName";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    private static final String COLUMN_HOUSE_NUMBER = "houseNumber";
    private static final String COLUMN_LAYOUT_NAME = "layoutName";
    private static final String COLUMN_PLACE = "place";
    private static final String COLUMN_PINCODE = "pincode";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FINE_DATE = "fineDate";
    private static final String TABLE_WASTE_DETAILS = "waste_details";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_DETAILS = "details";

    private static final String TABLE_FINES = "fines";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_REASON = "reason";

    // Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the users table
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FULL_NAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PHONE_NUMBER + " TEXT, "
                + COLUMN_HOUSE_NUMBER + " TEXT, "
                + COLUMN_LAYOUT_NAME + " TEXT, "
                + COLUMN_PLACE + " TEXT, "
                + COLUMN_PINCODE + " TEXT, "
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);

        // Create the waste details table
        String CREATE_WASTE_DETAILS_TABLE = "CREATE TABLE " + TABLE_WASTE_DETAILS + " ("
                + COLUMN_DATE + " TEXT PRIMARY KEY, "
                + COLUMN_DETAILS + " TEXT)";
        db.execSQL(CREATE_WASTE_DETAILS_TABLE);

        // Create the fines table
        // Check that the table is created with the correct schema
        String CREATE_FINES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FINES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_AMOUNT + " TEXT, "
                + COLUMN_REASON + " TEXT, "
                + COLUMN_FINE_DATE + " TEXT)";
        db.execSQL(CREATE_FINES_TABLE);}


        @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WASTE_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINES);
        onCreate(db);
    }

    // Method to insert a new user into the database
    public boolean insertUser(String fullName, String email, String phoneNumber, String houseNumber, String layoutName, String place, String pincode, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(COLUMN_HOUSE_NUMBER, houseNumber);
        values.put(COLUMN_LAYOUT_NAME, layoutName);
        values.put(COLUMN_PLACE, place);
        values.put(COLUMN_PINCODE, pincode);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Method to validate a user during login
    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    // Method to save waste details
    public boolean saveWasteDetails(String date, String details) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DETAILS, details);

        // Check if the date already exists in the database
        int rowsAffected = db.update(TABLE_WASTE_DETAILS, values, COLUMN_DATE + "=?", new String[]{date});

        // If no rows were affected, insert a new record
        if (rowsAffected == 0) {
            values.put(COLUMN_DATE, date);
            long result = db.insert(TABLE_WASTE_DETAILS, null, values);
            db.close();
            return result != -1;
        } else {
            db.close();
            return true;
        }
    }


    // Method to get waste details
    public String getWasteDetails(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_WASTE_DETAILS, new String[]{COLUMN_DETAILS}, COLUMN_DATE + "=?", new String[]{date}, null, null, null);
        if (cursor.moveToFirst()) {
            String details = cursor.getString(0);
            cursor.close();
            db.close();
            return details;
        } else {
            cursor.close();
            db.close();
            return "No details available";
        }
    }

    // Method to allocate a fine
    public boolean allocateFine(String email, String amount, String reason, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_REASON, reason);
        values.put(COLUMN_FINE_DATE, date);
        long result = db.insert(TABLE_FINES, null, values);
        db.close();
        return result != -1;
    }

    // Method to get fine details

    public String[][] getFineDetails(String email) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        final String TAG = "FineDetails";

        try {
            db = this.getReadableDatabase();
            Log.d(TAG, "Database opened successfully.");

            cursor = db.query(
                    TABLE_FINES,
                    new String[]{COLUMN_FINE_DATE, COLUMN_REASON, COLUMN_AMOUNT},
                    COLUMN_EMAIL + "=?",
                    new String[]{email},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                int count = cursor.getCount();
                String[][] fineDetails = new String[count][3]; // 3 columns: date, reason, amount
                int index = 0;

                do {
                    fineDetails[index][0] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FINE_DATE));
                    fineDetails[index][1] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REASON));
                    fineDetails[index][2] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
                    index++;
                } while (cursor.moveToNext());

                Log.d(TAG, "Fine details retrieved.");
                return fineDetails;
            } else {
                Log.d(TAG, "No fines found for email: " + email);
                return new String[0][0]; // Return empty array if no fines
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving fine details", e);
            return new String[0][0];
        } finally {
            if (cursor != null) {
                cursor.close();
                Log.d(TAG, "Cursor closed.");
            }
            if (db != null) {
                db.close();
                Log.d(TAG, "Database closed.");
            }
        }
    }





    // Method to pay a fine
    public boolean payFine(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_FINES, COLUMN_EMAIL + "=?", new String[]{email});
        db.close();
        return result > 0;
    }
}
