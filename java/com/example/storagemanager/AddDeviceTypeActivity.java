package com.example.storagemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class AddDeviceTypeActivity extends AppCompatActivity {

    private TextInputLayout textInputDeviceType;
    private Button buttonAddDeviceType;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_type);

        textInputDeviceType = findViewById(R.id.textInputDeviceType);
        buttonAddDeviceType = findViewById(R.id.buttonAddNewDeviceType);

        databaseHelper = new DatabaseHelper(AddDeviceTypeActivity.this);

        buttonAddDeviceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deviceType = textInputDeviceType.getEditText().getText().toString().trim();
                if(deviceType.equals("")){
                    Toast.makeText(AddDeviceTypeActivity.this, "You need to enter the device type name.", Toast.LENGTH_SHORT).show();
                }
                else if (databaseHelper.doesDeviceTypeExist(deviceType)){
                    Toast.makeText(AddDeviceTypeActivity.this, "This device type already exists.", Toast.LENGTH_SHORT).show();
                }
                else{
                    long res = databaseHelper.addDeviceType(deviceType);
                    if(res == -1){
                        Toast.makeText(AddDeviceTypeActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AddDeviceTypeActivity.this, "You have successfully added a new device type.", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(AddDeviceTypeActivity.this, DeviceTypeManagementActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}