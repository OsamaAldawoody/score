package com.example.osamaaldawoody.score.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class appointmentContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private appointmentContract(){
    }


    public static final String CONTENT_AUTHORITY = "com.example.osamaaldawoody.score";

    public static final Uri BASE_CONTENT_URL = Uri.parse("content://" +CONTENT_AUTHORITY);

    public static final String PATH_APPOINTMENT = "appointments";
    public static final class appointmentEntery implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URL , PATH_APPOINTMENT);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of Appointment.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_APPOINTMENT;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single Appointment.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_APPOINTMENT;
        /** Name of database table for pets */
        public final static String TABLE_NAME = "appointments";

        /**
         * Unique ID number for the pet (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the APPOINTMENT.
         *
         * Type: TEXT
         */
        public final static String COLUMN_APPOINTMENT_NAME ="name";

        /**
         * date of the APPOINTMENT.
         *
         * Type: TEXT
         */
        public final static String COLUMN_APPOINTMENT_DATE = "date";

        /**
         * day of the APPOINTMENT.
         *
         * Type: TEXT
         */
        public final static String COLUMN_APPOINTMENT_DAY = "day";

        /**
         * time of the APPOINTMENT.
         *
         * Type: TEXT
         */
        public final static String COLUMN_APPOINTMENT_time = "time";

        /**
         * Duration of Appointment.
         *
         * The only possible values are {@link #hour_1}, {@link #hour_1_5},{@link #hour_2}, {@link #hour_2_5},
         * or {@link #hour_3}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_APPOINTMENT_DURATION = "duration";
        /**
         * Is the appointment fixed.
         *
         * Type: boolean
         */

        public final static String COLUMN_APPOINTMENT_FIXED = "fixed";


        /**
         * Possible values for the durations of the appointment.
         */
        public static final int not_set = 0;
        public static final int hour_1 = 1;
        public static final int hour_1_5 = 2;
        public static final int hour_2 = 3;
        public static final int hour_2_5 = 4;
        public static final int hour_3 = 5;


        /**
         * Returns whether or not the given duration is {@link #hour_1}, {@link #hour_1_5},{@link #hour_2}, {@link #hour_2_5},
         * or {@link #hour_3}.
         */
        public static boolean isValidDuration(int duration) {
            if (duration == hour_1|| duration == hour_1_5
                    || duration == hour_2||duration == hour_2_5|| duration == hour_3 ) {
                return true;
            }
            return false;
        }
    }
}
