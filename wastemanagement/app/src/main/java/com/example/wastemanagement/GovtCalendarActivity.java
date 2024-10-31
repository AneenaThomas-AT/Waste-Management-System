package com.example.wastemanagement;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GovtCalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private EditText wasteDetails;
    private Button saveButton;
    private DBHelper dbHelper;
    private String selectedDate;  // Declare selectedDate as a class-level variable

    private Spinner wasteSpinner;
       ;  // Declare selectedDate as a class-level variable

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_govt_calendar);

            calendarView = findViewById(R.id.calendarView);
            wasteSpinner = findViewById(R.id.wasteSpinner);
            saveButton = findViewById(R.id.saveButton);
            dbHelper = new DBHelper(this);

            // Set up the spinner with waste types
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.waste_types, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            wasteSpinner.setAdapter(adapter);

            // Set the default selectedDate to today's date
            selectedDate = "";  // Initially empty; it will get updated when a date is selected

            // Listener for selecting a date on the calendar
            calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            });

            // Listener for saving waste details
            saveButton.setOnClickListener(v -> {
                String selectedWaste = wasteSpinner.getSelectedItem().toString();

                // Ensure a date is selected and waste details are entered
                if (selectedDate.isEmpty()) {
                    Toast.makeText(GovtCalendarActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();
                } else if (selectedWaste.isEmpty()) {
                    Toast.makeText(GovtCalendarActivity.this, "Please select waste type", Toast.LENGTH_SHORT).show();
                } else {
                    // Save waste details in the database
                    if (dbHelper.saveWasteDetails(selectedDate, selectedWaste)) {
                        Toast.makeText(GovtCalendarActivity.this, "Waste details saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GovtCalendarActivity.this, "Failed to save waste details", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
