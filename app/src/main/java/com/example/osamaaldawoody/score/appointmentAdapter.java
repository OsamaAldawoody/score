package com.example.osamaaldawoody.score;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.osamaaldawoody.score.data.appointmentContract;

import java.util.Date;

public class appointmentAdapter extends CursorAdapter {
    public appointmentAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.activity_day_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView appointmentName = (TextView) view.findViewById(R.id.name_text);
        TextView appointmentTime = (TextView) view.findViewById(R.id.appointment_text_view);
        TextView appointmentDay = (TextView) view.findViewById(R.id.day_text_view);
        TextView appointmentDate = (TextView) view.findViewById(R.id.date_text_view);
        ImageView appointmentFixed = (ImageView) view.findViewById(R.id.fixed_appointment);

        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(appointmentContract.appointmentEntery.COLUMN_APPOINTMENT_NAME));
        String time = cursor.getString(cursor.getColumnIndexOrThrow(appointmentContract.appointmentEntery.COLUMN_APPOINTMENT_time));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(appointmentContract.appointmentEntery.COLUMN_APPOINTMENT_DATE));
        String day = cursor.getString(cursor.getColumnIndexOrThrow(appointmentContract.appointmentEntery.COLUMN_APPOINTMENT_DAY));
        int fixed = cursor.getInt(cursor.getColumnIndexOrThrow(appointmentContract.appointmentEntery.COLUMN_APPOINTMENT_FIXED));
        // Populate fields with extracted properties
        appointmentName.setText(name);


        appointmentDate.setText(date);


        appointmentDay.setText(day);


        appointmentTime.setText(time);

        if (fixed == 1){
            appointmentFixed.setVisibility(View.VISIBLE);
        }
        else{
            appointmentFixed.setVisibility(View.INVISIBLE);
        }
    }
}
