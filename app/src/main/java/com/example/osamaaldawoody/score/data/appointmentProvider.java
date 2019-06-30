package com.example.osamaaldawoody.score.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.osamaaldawoody.score.data.appointmentContract.appointmentEntery;

public class appointmentProvider extends ContentProvider {
    /** Tag for the log messages */
    public static final String LOG_TAG = appointmentProvider.class.getSimpleName();

    appointmentDbHelper appointDbHelper;
    /** URI matcher code for the content URI for the pets table */
    private static final int APPOINTMENTS = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int APPOINTMENTS_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        sUriMatcher.addURI(appointmentContract.CONTENT_AUTHORITY,appointmentContract.PATH_APPOINTMENT,APPOINTMENTS);

        sUriMatcher.addURI(appointmentContract.CONTENT_AUTHORITY,appointmentContract.PATH_APPOINTMENT + "/#",APPOINTMENTS_ID);
    }
    @Override
    public boolean onCreate() {
        //  Create and initialize a appointmentDbHelper object to gain access to the pets database.
        appointDbHelper = new appointmentDbHelper(getContext());
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,String sortOrder) {
         SQLiteDatabase db = appointDbHelper.getReadableDatabase();
         Cursor cursor ;
        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case APPOINTMENTS:
                cursor = db.query(appointmentEntery.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
                break;
            case APPOINTMENTS_ID:

                selection = appointmentEntery._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };


                cursor = db.query(appointmentEntery.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case APPOINTMENTS:
                return insertAppointment(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertAppointment(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(appointmentEntery.COLUMN_APPOINTMENT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("appointment requires a name");
        }

        // Check that the time is valid
        Integer duration = values.getAsInteger(appointmentEntery.COLUMN_APPOINTMENT_DURATION);
        if (duration == null || !appointmentEntery.isValidDuration(duration)) {
            throw new IllegalArgumentException("appointment requires valid duration");
        }

        String time = values.getAsString(appointmentEntery.COLUMN_APPOINTMENT_time);
        if (time == null) {
            throw new IllegalArgumentException("appointment requires valid weight");
        }

        String date = values.getAsString(appointmentEntery.COLUMN_APPOINTMENT_DATE);
        if (date == null) {
            throw new IllegalArgumentException("appointment requires valid weight");
        }
        SQLiteDatabase db = appointDbHelper.getWritableDatabase();
        // Insert the new appointment with the given values
        long id = db.insert(appointmentEntery.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case APPOINTMENTS:
                return updateAppointment(appointmentEntery.CONTENT_URI, contentValues, selection, selectionArgs);
            case APPOINTMENTS_ID:
                selection = appointmentEntery._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateAppointment(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

        private int updateAppointment(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

            // If the {@link AppointmentEntry#COLUMN_APPOINTMENT_NAME} key is present,
            // check that the name value is not null.
            if (values.containsKey(appointmentEntery.COLUMN_APPOINTMENT_NAME)) {
                String name = values.getAsString(appointmentEntery.COLUMN_APPOINTMENT_NAME);
                if (name == null) {
                    throw new IllegalArgumentException("appointment requires a name");
                }
            }


            // check that the duration value is valid.
            if (values.containsKey(appointmentEntery.COLUMN_APPOINTMENT_DURATION)) {
                Integer duration = values.getAsInteger(appointmentEntery.COLUMN_APPOINTMENT_DURATION);
                if (duration == null || !appointmentEntery.isValidDuration(duration)) {
                    throw new IllegalArgumentException("appointment requires valid duration");
                }
            }

            // check that the time value is valid.
            if (values.containsKey(appointmentEntery.COLUMN_APPOINTMENT_time)) {
                String time = values.getAsString(appointmentEntery.COLUMN_APPOINTMENT_time);
                if (time == null) {
                    throw new IllegalArgumentException("appointment requires valid time");
                }
            }

            // check that the day value is valid.
            if (values.containsKey(appointmentEntery.COLUMN_APPOINTMENT_DAY)) {
                String day = values.getAsString(appointmentEntery.COLUMN_APPOINTMENT_DAY);
                if (day == null) {
                    throw new IllegalArgumentException("appointment requires valid day");
                }
            }

            // check that the date value is valid.
            if (values.containsKey(appointmentEntery.COLUMN_APPOINTMENT_DATE)) {

                String date = values.getAsString(appointmentEntery.COLUMN_APPOINTMENT_DATE);
                if (date == null) {
                    throw new IllegalArgumentException("appointment requires valid date");
                }
            }
            // No need to check the breed, any value is valid (including null).

            // If there are no values to update, then don't try to update the database
            if (values.size() == 0) {
                return 0;
            }

            // Otherwise, get writeable database to update the data
            SQLiteDatabase database = appointDbHelper.getWritableDatabase();

            // Returns the number of database rows affected by the update statement
             int rowsUpdated = database.update(appointmentEntery.TABLE_NAME, values, selection, selectionArgs);

            // If 1 or more rows were updated, then notify all listeners that the data at the
            // given URI has changed
            if (rowsUpdated != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return rowsUpdated;
        }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = appointDbHelper.getWritableDatabase();
        int rowDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case APPOINTMENTS:
                // Delete all rows that match the selection and selection args
                rowDeleted = database.delete(appointmentEntery.TABLE_NAME, selection, selectionArgs);
                if (rowDeleted != 0) {
                    // delete automatically from user interface
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowDeleted;
                case APPOINTMENTS_ID:
                // Delete a single row given by the ID in the URI
                selection = appointmentEntery._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowDeleted = database.delete(appointmentEntery.TABLE_NAME, selection, selectionArgs);
                    if (rowDeleted != 0 ) {
                        // delete automatically from user interface
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                    return rowDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case APPOINTMENTS:
                return appointmentEntery.CONTENT_LIST_TYPE;
            case APPOINTMENTS_ID:
                return appointmentEntery.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
