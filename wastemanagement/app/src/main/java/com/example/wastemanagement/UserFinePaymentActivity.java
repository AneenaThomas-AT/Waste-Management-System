package com.example.wastemanagement;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserFinePaymentActivity extends AppCompatActivity {

    private TableLayout fineDetailsTable;
    private Button payFineButton;
    private DBHelper dbHelper;
    private String customerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fine_payment);

        fineDetailsTable = findViewById(R.id.fineDetailsTable);
        payFineButton = findViewById(R.id.payFineButton);
        dbHelper = new DBHelper(this);

        customerEmail = getIntent().getStringExtra("customerEmail");
        Log.d("UserFinePaymentActivity", "Customer Email: " + customerEmail);

        if (customerEmail != null) {
            String[][] fineDetails = dbHelper.getFineDetails(customerEmail);
            Log.d("UserFinePaymentActivity", "Fine Details: " + fineDetails);
            populateFineDetails(fineDetails);
        } else {
            Toast.makeText(UserFinePaymentActivity.this, "No email provided", Toast.LENGTH_SHORT).show();
        }

        payFineButton.setOnClickListener(v -> {
            if (customerEmail != null) {
                new Thread(() -> {
                    boolean success = dbHelper.payFine(customerEmail);
                    runOnUiThread(() -> {
                        if (success) {
                            Toast.makeText(UserFinePaymentActivity.this, "Fine paid successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserFinePaymentActivity.this, "Failed to pay fine", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            } else {
                Toast.makeText(UserFinePaymentActivity.this, "No email provided", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateFineDetails(String[][] fineDetails) {
        for (String[] detail : fineDetails) {
            TableRow row = new TableRow(this);

            TextView dateView = new TextView(this);
            dateView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            dateView.setText(detail[0]);
            dateView.setPadding(8, 8, 8, 8);
            dateView.setGravity(Gravity.CENTER);
            row.addView(dateView);

            TextView reasonView = new TextView(this);
            reasonView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            reasonView.setText(detail[1]);
            reasonView.setPadding(8, 8, 8, 8);
            reasonView.setGravity(Gravity.CENTER);
            row.addView(reasonView);

            TextView amountView = new TextView(this);
            amountView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            amountView.setText(detail[2]);
            amountView.setPadding(8, 8, 8, 8);
            amountView.setGravity(Gravity.CENTER);
            row.addView(amountView);

            fineDetailsTable.addView(row);
        }
    }
}
