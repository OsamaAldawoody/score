package com.example.osamaaldawoody.score;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.osamaaldawoody.score.data.appointmentContract.appointmentEntery;
import com.example.osamaaldawoody.score.data.appointmentDbHelper;

public class editAppointment extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    //get the spinner from the xml.
    Spinner dropdown;

    Button buttonTime;

    Button buttonDate;

    TextView textTime;

    TextView textDate;

    EditText editName;

    TextView textDay;

    CheckBox checkBox;

    int fixed = 0;
    /** Content URI for the existing pet (null if it's a new pet) */
    private Uri mCurrentPetUri;
    /** Boolean flag that keeps track of whether the pet has been edited (true) or not (false) */
    private boolean mAppointmentChanged = false;
    /**
     *
     */
    int mDuration = appointmentEntery.hour_1;

    /** Identifier for the appointment data loader */
    private static final int EXISTING_APPOINTMENT_LOADER = 0;

    appointmentDbHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_appointment);

        Intent  i = getIntent();

        mCurrentPetUri = i .getData();


        if (mCurrentPetUri == null){
            this.setTitle(R.string.add_appointment);
            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a pet that hasn't been created yet.)
            invalidateOptionsMenu();
        }
        else {
            this.setTitle(R.string.edit_appointment);
            // Initialize a loader to read the appointment data from the database
            // and display the current values in the editors
            getSupportLoaderManager().initLoader(EXISTING_APPOINTMENT_LOADER,null,this);
        }

        mDbHelper = new appointmentDbHelper(this);

        buttonTime = findViewById(R.id.setTimeButton);
        buttonDate = findViewById(R.id.setDateButton);
        textTime = findViewById(R.id.time_text_view);
        textDate = findViewById(R.id.date_enter_text_view);
        editName = findViewById(R.id.edit_name);
        textDay = findViewById(R.id.day_of_week_txt);
        checkBox = findViewById(R.id.fixed_check_box);


        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize a new time picker dialog fragment
                android.support.v4.app.DialogFragment dFragment = new TimePickerFragment();

                // Show the time picker dialog fragment
                dFragment.show(getSupportFragmentManager(),"Time Picker");
            }
        });

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize a new Date picker dialog fragment
                android.support.v4.app.DialogFragment dFragment = new DatePickerFragment();

                // Show the time picker dialog fragment
                dFragment.show(getSupportFragmentManager(),"Date Picker");
            }
        });

        textTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize a new time picker dialog fragment
                android.support.v4.app.DialogFragment dFragment = new TimePickerFragment();

                // Show the time picker dialog fragment
                dFragment.show(getSupportFragmentManager(),"Time Picker");
            }
        });

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize a new Date picker dialog fragment
                android.support.v4.app.DialogFragment dFragment = new DatePickerFragment();

                // Show the time picker dialog fragment
                dFragment.show(getSupportFragmentManager(),"Date Picker");
            }
        });

        setupSpinner();

        editName.setOnTouchListener(mTouchListener);
        textDate.setOnTouchListener(mTouchListener);
        textDay.setOnTouchListener(mTouchListener);
        checkBox.setOnTouchListener(mTouchListener);
        dropdown.setOnTouchListener(mTouchListener);
    }
    /**
     * Get user input from editor and save pet into database.
     */
    private void saveAppointment() {

        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = editName.getText().toString().trim();
        String dateString = textDate.getText().toString().trim();
        String dayString = textDay.getText().toString().trim();
        String timeString = textTime.getText().toString().trim();

        if (checkBox.isChecked()){
            fixed = 1;
        }else{
            fixed = 0;
        }

        //  check if all the fields in the editor are blank
        if (
            TextUtils.isEmpty(nameString) || TextUtils.isEmpty(dayString) ||
            TextUtils.isEmpty(dateString) || mDuration == appointmentEntery.not_set) {
            Toast.makeText(this, "The Appointment Not Set ! , All Fields Required", Toast.LENGTH_LONG).show();
            // Since no fields were modified, we can return early without creating or edit appointment.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }


        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(appointmentEntery.COLUMN_APPOINTMENT_NAME, nameString);
        values.put(appointmentEntery.COLUMN_APPOINTMENT_DATE, dateString);
        values.put(appointmentEntery.COLUMN_APPOINTMENT_time, timeString);
        values.put(appointmentEntery.COLUMN_APPOINTMENT_DURATION, mDuration);
        values.put(appointmentEntery.COLUMN_APPOINTMENT_DAY,dayString);
        values.put(appointmentEntery.COLUMN_APPOINTMENT_FIXED,fixed);



        // Determine if this is a new or existing appointment by checking if mCurrentPetUri is null or not
        if (mCurrentPetUri == null) {
            // This is a NEW pet, so insert a new pet into the provider,
            // returning the content URI for the new pet.

                Uri newUri = getContentResolver().insert(appointmentEntery.CONTENT_URI, values);

                // Show a toast message depending on whether or not the insertion was successful.
                if (newUri == null) {
                    // If the new content URI is null, then there was an error with insertion.
                    Toast.makeText(this, getString(R.string.editor_insert_appointment_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the insertion was successful and we can display a toast.
                    Toast.makeText(this, getString(R.string.editor_insert_appointment_successful),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
            int rowsAffected = getContentResolver().update(mCurrentPetUri, values, null, null);

            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Setup the dropdown spinner that allows the user to select the duration of the appointment.
     */
    private void setupSpinner() {

        //get the spinner from the xml.
        dropdown = findViewById(R.id.spinner1);
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter durationSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        durationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        dropdown.setAdapter(durationSpinnerAdapter);

        // Set the integer mSelected to the constant values
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.duration_notSet))) {
                        mDuration = appointmentEntery.not_set;
                    } else if (selection.equals(getString(R.string.one_1))) {
                        mDuration = appointmentEntery.hour_1;
                    } else if (selection.equals(getString(R.string.half_past_1))) {
                        mDuration = appointmentEntery.hour_1_5;
                    } else if (selection.equals(getString(R.string.two_hour))) {
                        mDuration = appointmentEntery.hour_2;
                    } else if (selection.equals(getString(R.string.half_past_2))) {
                        mDuration = appointmentEntery.hour_2_5;
                    } else {
                        mDuration = appointmentEntery.hour_3;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mDuration = appointmentEntery.not_set;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.save:
                saveAppointment();
                finish();
                break;
            case R.id.deleteAppointment:
                showDeleteConfirmationDialog();
                break;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the appointment hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mAppointmentChanged) {
                    NavUtils.navigateUpFromSameTask(editAppointment.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(editAppointment.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mAppointmentChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        // Since the editor shows all appointments attributes, define a projection that contains
        // all columns from the appointment table table
        String[] projection = {
                appointmentEntery._ID,
                appointmentEntery.COLUMN_APPOINTMENT_NAME,
                appointmentEntery.COLUMN_APPOINTMENT_DATE,
                appointmentEntery.COLUMN_APPOINTMENT_DAY,
                appointmentEntery.COLUMN_APPOINTMENT_time,
                appointmentEntery.COLUMN_APPOINTMENT_DURATION,
                appointmentEntery.COLUMN_APPOINTMENT_FIXED};

        // This loader will execute the ContentProvider's query method on a background thread
        return new android.support.v4.content.CursorLoader(this,   // Parent activity context
                mCurrentPetUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
// Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(appointmentEntery.COLUMN_APPOINTMENT_NAME);
            int timeColumnIndex = cursor.getColumnIndex(appointmentEntery.COLUMN_APPOINTMENT_time);
            int dayColumnIndex = cursor.getColumnIndex(appointmentEntery.COLUMN_APPOINTMENT_DAY);
            int dateColumnIndex = cursor.getColumnIndex(appointmentEntery.COLUMN_APPOINTMENT_DATE);
            int durationColumnIndex = cursor.getColumnIndex(appointmentEntery.COLUMN_APPOINTMENT_DURATION);
            int fixedColumnIndex = cursor.getColumnIndex(appointmentEntery.COLUMN_APPOINTMENT_FIXED);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            Toast.makeText(this, String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();
            String time = cursor.getString(timeColumnIndex);
            String day = cursor.getString(dayColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            int duration = cursor.getInt(durationColumnIndex);
            int fixed = cursor.getInt(fixedColumnIndex);

            // Update the views on the screen with the values from the database
            editName.setText(name);
            textDate.setText(date);
            textDay.setText(day);
            textTime.setText(time);
            if (fixed == 1) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            // Duration is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options (0 is not set, 1 is 1 hour, 2 is 1.5 hour , 3 is 2, 4 is 2.5 hour, 5 is 3 hour).
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (duration) {
                case appointmentEntery.hour_1:
                    dropdown.setSelection(1);
                    break;
                case appointmentEntery.hour_1_5:
                    dropdown.setSelection(2);
                    break;
                case appointmentEntery.hour_2:
                    dropdown.setSelection(3);
                    break;
                case appointmentEntery.hour_2_5:
                    dropdown.setSelection(4);
                    break;
                case appointmentEntery.hour_3:
                    dropdown.setSelection(5);
                    break;
                default:
                    dropdown.setSelection(0);
                    break;
            }
        }
    }
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
// If the loader is invalidated, clear out all the data from the input fields.
        editName.setText("");
        textTime.setText("");
        textDay.setText("");
        dropdown.setSelection(0); // Select "not set" time
        textDate.setText("");
        checkBox.setChecked(false);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    // OnTouchListener that listens for any user touches on a View, implying that they are modifying
    // the view, and we change the mPetHasChanged boolean to true.

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mAppointmentChanged = true;
            return false;
        }
    };

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentPetUri == null) {
            MenuItem menuItem = menu.findItem(R.id.deleteAppointment);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deletePet();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deletePet() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentPetUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentPetUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        // Close the activity
        finish();
    }
}
