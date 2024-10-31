package com.example.wastemanagement;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
public class GovtHomeActivity extends AppCompatActivity {

    private Button btnCalendar, btnFinePayment, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_home);

        btnCalendar = findViewById(R.id.btnCalendar);
        btnFinePayment = findViewById(R.id.btnFinePayment);
        btnLogout = findViewById(R.id.btnLogout);

        btnCalendar.setOnClickListener(v -> {
            Intent intent = new Intent(GovtHomeActivity.this, GovtCalendarActivity.class);
            startActivity(intent);
        });

        btnFinePayment.setOnClickListener(v -> {
            Intent intent = new Intent(GovtHomeActivity.this, GovtFinePaymentActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(GovtHomeActivity.this, MainActivity.class); // Assuming you have a login activity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Close current activity
        });
    }
}
