package com.example.wastemanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;

public class GovtFinePaymentActivity extends AppCompatActivity {

    private EditText customerEmail, fineAmount, fineReason, fineDate;
    private Button allocateFineButton;
    private DBHelper dbHelper;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_fine_payment);

        customerEmail = findViewById(R.id.customerEmail);
        fineAmount = findViewById(R.id.fineAmount);
        fineReason = findViewById(R.id.fineReason);
        fineDate = findViewById(R.id.fineDate);
        allocateFineButton = findViewById(R.id.allocateFineButton);
        dbHelper = new DBHelper(this);
        calendar = Calendar.getInstance();

        // Listener for fine date
        fineDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(GovtFinePaymentActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        fineDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Listener for allocating a fine
        allocateFineButton.setOnClickListener(v -> {
            String email = customerEmail.getText().toString();
            String amount = fineAmount.getText().toString();
            String reason = fineReason.getText().toString();
            String date = fineDate.getText().toString();

            // Ensure all fields are filled
            if (email.isEmpty() || amount.isEmpty() || reason.isEmpty() || date.isEmpty()) {
                Toast.makeText(GovtFinePaymentActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Allocate fine in the database
                if (dbHelper.allocateFine(email, amount, reason, date)) {
                    Toast.makeText(GovtFinePaymentActivity.this, "Fine allocated successfully", Toast.LENGTH_SHORT).show();
                    customerEmail.setText("");  // Clear the input fields
                    fineAmount.setText("");
                    fineReason.setText("");
                    fineDate.setText("");
                } else {
                    Toast.makeText(GovtFinePaymentActivity.this, "Failed to allocate fine", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
