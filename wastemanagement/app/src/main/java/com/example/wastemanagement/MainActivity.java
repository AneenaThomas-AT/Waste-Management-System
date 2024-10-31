package com.example.wastemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnCustomer, btnGovAuthority, btnLogin;
    private EditText username, password;
    private TextView registerLink;
    private ImageView eyeIcon;
    private boolean isCustomerSelected = true;
    private boolean isPasswordVisible = false;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        btnCustomer = findViewById(R.id.btnCustomer);
        btnGovAuthority = findViewById(R.id.btnGovAuthority);
        btnLogin = findViewById(R.id.btnLogin);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        registerLink = findViewById(R.id.registerLink);
        eyeIcon = findViewById(R.id.eyeIcon);

        btnCustomer.setOnClickListener(v -> {
            btnCustomer.setBackgroundResource(R.drawable.btn_background_active);
            btnGovAuthority.setBackgroundResource(R.drawable.btn_background_inactive);
            registerLink.setVisibility(View.VISIBLE);
            isCustomerSelected = true;
        });

        btnGovAuthority.setOnClickListener(v -> {
            btnGovAuthority.setBackgroundResource(R.drawable.btn_background_active);
            btnCustomer.setBackgroundResource(R.drawable.btn_background_inactive);
            registerLink.setVisibility(View.GONE);
            isCustomerSelected = false;
        });

        btnLogin.setOnClickListener(v -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            } else if (isCustomerSelected && dbHelper.validateUser(user, pass)) {
                // Pass customer email to UserHomeActivity
                Intent intent = new Intent(MainActivity.this, UserHomeActivity.class);
                intent.putExtra("customerEmail", user);
                startActivity(intent);
            } else if (!isCustomerSelected && (user.equals("admin") && pass.equals("admin"))) {
                Intent intent = new Intent(MainActivity.this, GovtHomeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
eyeIcon.setOnClickListener(v -> {
        if (isPasswordVisible) {
            password.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            eyeIcon.setImageResource(R.drawable.img_2);
        } else {
            password.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            eyeIcon.setImageResource(R.drawable.img_1);
        }
        password.setSelection(password.getText().length()); // Move cursor to the end
        isPasswordVisible = !isPasswordVisible;
    });
}

    public void openRegistrationActivity(View view) {
        Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }
}
