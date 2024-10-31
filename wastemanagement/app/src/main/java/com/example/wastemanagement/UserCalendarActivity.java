package com.example.wastemanagement;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


public class UserCalendarActivity extends AppCompatActivity {

        private CalendarView calendarView;
        private TextView wasteDetailsView;
        private DBHelper dbHelper;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_calendar);

            calendarView = findViewById(R.id.calendarView);
            wasteDetailsView = findViewById(R.id.wasteDetailsTextView);
            dbHelper = new DBHelper(this);

            // Listener for selecting a date on the calendar
            calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;

                // Fetch waste details for the selected date
                String wasteDetails = dbHelper.getWasteDetails(selectedDate);

                // Check if waste details exist
                if (wasteDetails.equals("No details available")) {
                    Toast.makeText(UserCalendarActivity.this, "No waste collection details for this date", Toast.LENGTH_SHORT).show();
                }

                // Set the fetched details to the TextView
                wasteDetailsView.setText(wasteDetails);
            });

        }
    }
