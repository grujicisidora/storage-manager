package com.example.storagemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class UpdateDeviceTypeActivity extends AppCompatActivity {

    TextInputLayout textUpdateDeviceTypeName;
    TextView textDeviceTypeID;

    Button buttonUpdateDeviceType;
    Button buttonDeleteDeviceType;

    private DatabaseHelper databaseHelper;

    private int deviceTypeID;
    private String deviceTypeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_device_type);

        textUpdateDeviceTypeName = findViewById(R.id.textUpdateDeviceTypeName);
        textDeviceTypeID = findViewById(R.id.textDeviceTypeID);

        buttonUpdateDeviceType = findViewById(R.id.buttonUpdateDeviceType);
        buttonDeleteDeviceType = findViewById(R.id.buttonDeleteDeviceType);

        databaseHelper = new DatabaseHelper(UpdateDeviceTypeActivity.this);

        Intent intent = getIntent();
        deviceTypeID = intent.getIntExtra("id", -1);
        if(deviceTypeID == -1){
            Toast.makeText(this, "Error loading device type.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(UpdateDeviceTypeActivity.this, DeviceTypeManagementActivity.class);
            startActivity(intent1);
            finish();
        }
        else{
            textDeviceTypeID.setText("Device type ID: " + String.valueOf(deviceTypeID));
            deviceTypeName = intent.getStringExtra("name");
            textUpdateDeviceTypeName.getEditText().setText(deviceTypeName);
        }

        buttonUpdateDeviceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newDeviceTypeName = textUpdateDeviceTypeName.getEditText().getText().toString().trim();
                if(newDeviceTypeName.equals("")){
                    Toast.makeText(UpdateDeviceTypeActivity.this, "Please enter the new device type name.", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseHelper.updateDeviceType(deviceTypeID, newDeviceTypeName);
                }
            }
        });

        buttonDeleteDeviceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete device type");
        builder.setMessage("By deleting this device type, you will delete all associated devices. Are you sure you want to delete this device type?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int res = databaseHelper.deleteDeviceType(deviceTypeID);
                if(res == 1){
                    Intent intent = new Intent(UpdateDeviceTypeActivity.this, DeviceTypeManagementActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(UpdateDeviceTypeActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}