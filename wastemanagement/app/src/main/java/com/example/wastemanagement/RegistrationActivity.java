package com.example.wastemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPhoneNumber, etHouseNumber, etLayoutName, etPlace, etPincode, etPassword;
    private Button btnRegister;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dbHelper = new DBHelper(this);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etHouseNumber = findViewById(R.id.etHouseNumber);
        etLayoutName = findViewById(R.id.etLayoutName);
        etPlace = findViewById(R.id.etPlace);
        etPincode = findViewById(R.id.etPincode);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString();
            String email = etEmail.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();
            String houseNumber = etHouseNumber.getText().toString();
            String layoutName = etLayoutName.getText().toString();
            String place = etPlace.getText().toString();
            String pincode = etPincode.getText().toString();
            String password = etPassword.getText().toString();

            if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || houseNumber.isEmpty() || layoutName.isEmpty() || place.isEmpty() || pincode.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegistrationActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean isInserted = dbHelper.insertUser(fullName, email, phoneNumber, houseNumber, layoutName, place, pincode, password);
                if (isInserted) {
                    Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
