package com.example.osamaaldawoody.score.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.osamaaldawoody.score.data.appointmentContract.appointmentEntery;

public class appointmentDbHelper extends SQLiteOpenHelper {
    /** Name of the database file */
    public static final String DATABASE_NAME = "score.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;


    public appointmentDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + appointmentEntery.TABLE_NAME + " ("
                + appointmentEntery._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + appointmentEntery.COLUMN_APPOINTMENT_NAME + " TEXT NOT NULL, "
                + appointmentEntery.COLUMN_APPOINTMENT_DATE + " TEXT NOT NULL, "
                + appointmentEntery.COLUMN_APPOINTMENT_DAY + " TEXT NOT NULL, "
                + appointmentEntery.COLUMN_APPOINTMENT_DURATION + " INTEGER, "
                + appointmentEntery.COLUMN_APPOINTMENT_FIXED + " INTEGER, "
                + appointmentEntery.COLUMN_APPOINTMENT_time+ " TEXT NOT NULL );";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
