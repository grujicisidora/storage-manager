package com.example.storagemanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class UserNavigationActivity extends AppCompatActivity {

    private Button buttonManageConsumables, buttonManageDevices;

    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_navigation);

        buttonManageConsumables = findViewById(R.id.buttonManageConsumables);
        buttonManageDevices = findViewById(R.id.buttonManageDevices);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userID", -1);
        if (userID == -1) {
            Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(UserNavigationActivity.this, MainActivity.class);
            startActivity(intent1);
            finish();
        }

        buttonManageConsumables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(UserNavigationActivity.this, ConsumableManagementActivity.class);
                intent2.putExtra("userID", userID);
                startActivity(intent2);
            }
        });

        buttonManageDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(UserNavigationActivity.this, DeviceManagementActivity.class);
                intent3.putExtra("userID", userID);
                startActivity(intent3);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemUserChangePassword:
                Intent intent1 = new Intent(UserNavigationActivity.this, UserChangePasswordActivity.class);
                intent1.putExtra("userID", userID);
                startActivity(intent1);
                finish();
                return true;
            case R.id.menuItemUserLogout:
                Intent intent2 = new Intent(UserNavigationActivity.this, MainActivity.class);
                startActivity(intent2);
                finish();
                return true;
            default:
                super.onOptionsItemSelected(item);
                return true;
        }
    }
}