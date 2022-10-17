package com.example.storagemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AdminNavigationActivity extends AppCompatActivity {

    private Button buttonUsersAdministration, buttonDropdownListsAdministration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_navigation);

        buttonUsersAdministration = findViewById(R.id.buttonUsersAdministration);
        buttonDropdownListsAdministration = findViewById(R.id.buttonDropdownListsAdministration);

        buttonUsersAdministration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminNavigationActivity.this, UserManagementActivity.class);
                startActivity(intent);
            }
        });

        buttonDropdownListsAdministration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminNavigationActivity.this, DropdownListsManagementActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItemAdminChangePassword:
                Intent intent1 = new Intent(AdminNavigationActivity.this, AdminChangePasswordActivity.class);
                startActivity(intent1);
                finish();
                return true;
            case R.id.menuItemAdminLogout:
                Intent intent2 = new Intent(AdminNavigationActivity.this, MainActivity.class);
                startActivity(intent2);
                finish();
                return true;
            default:
                super.onOptionsItemSelected(item);
                return true;
        }
    }
}