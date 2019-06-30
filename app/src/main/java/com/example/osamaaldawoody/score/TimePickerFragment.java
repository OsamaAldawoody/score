package com.example.osamaaldawoody.score;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.app.DialogFragment;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.TimePicker;

public class TimePickerFragment extends android.support.v4.app.DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // Get a Calendar instance
        final Calendar calendar = Calendar.getInstance();
        // Get the current hour and minute
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        /*
            Creates a new time picker dialog with the specified theme.

                TimePickerDialog(Context context, int themeResId,
                    TimePickerDialog.OnTimeSetListener listener,
                    int hourOfDay, int minute, boolean is24HourView)
         */

        // TimePickerDialog Theme : THEME_HOLO_LIGHT
        TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT,this,hour,minute,false);

        // Return the TimePickerDialog
        return tpd;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        // Set a variable to hold the current time AM PM Status
        // Initially we set the variable value to AM
        String status = "AM";

        if(hourOfDay > 11)
        {
            // If the hour is greater than or equal to 12
            // Then the current AM PM status is PM
            status = "PM";
        }

        // Initialize a new variable to hold 12 hour format hour value
        int hour_of_12_hour_format;

        if(hourOfDay > 11){

            // If the hour is greater than or equal to 12
            // Then we subtract 12 from the hour to make it 12 hour format time
            hour_of_12_hour_format = hourOfDay - 12;
        }
        else {
            hour_of_12_hour_format = hourOfDay;
        }

        // Get the calling activity TextView reference
        TextView tv = (TextView) getActivity().findViewById(R.id.time_text_view);
        // Display the 12 hour format time in app interface
        tv.setText(hour_of_12_hour_format + " : " + minute + " : " + status);
    }
}
