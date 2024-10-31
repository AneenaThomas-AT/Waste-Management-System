package com.example.wastemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.AppCompatActivity;

public class UserHomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private String customerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        customerEmail = getIntent().getStringExtra("customerEmail");
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle menu item clicks here
                int id = item.getItemId();

                if (id == R.id.nav_calendar) {
                    // Handle Calendar action
                    // For example, open a new activity
                    startActivity(new Intent(UserHomeActivity.this, UserCalendarActivity.class));
                } else if (id == R.id.nav_fine_payment) {
                    Intent intent = new Intent(UserHomeActivity.this, UserFinePaymentActivity.class);
                    intent.putExtra("customerEmail", customerEmail);
                    startActivity(intent);

                } else if (id == R.id.nav_logout) {
                    // Handle Logout action
                    // For example, log the user out and return to login activity
                    Intent intent = new Intent(UserHomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Optional: close the current activity
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Update the header view with email ID
        View headerView = navigationView.getHeaderView(0);
        TextView emailTextView = headerView.findViewById(R.id.nav_header_email);
        emailTextView.setText(customerEmail);

        ImageView profileImageView = headerView.findViewById(R.id.nav_header_image);
        profileImageView.setImageResource(R.drawable.img); // Update with actual image if available
    }
}
