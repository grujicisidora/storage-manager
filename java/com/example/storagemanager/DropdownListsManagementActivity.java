package com.example.storagemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DropdownListsManagementActivity extends AppCompatActivity {

    private Button buttonStorageLocationManagement, buttonConsumableTypeManagement, buttonDeviceTypeManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropdown_lists_management);

        buttonStorageLocationManagement = findViewById(R.id.buttonStorageLocationManagement);
        buttonConsumableTypeManagement = findViewById(R.id.buttonConsumableTypeManagement);
        buttonDeviceTypeManagement = findViewById(R.id.buttonDeviceTypeManagement);

        buttonStorageLocationManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DropdownListsManagementActivity.this, StorageLocationManagementActivity.class);
                startActivity(intent);
            }
        });

        buttonConsumableTypeManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DropdownListsManagementActivity.this, ConsumableTypeManagementActivity.class);
                startActivity(intent);
            }
        });

        buttonDeviceTypeManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DropdownListsManagementActivity.this, DeviceTypeManagementActivity.class);
                startActivity(intent);
            }
        });
    }
}