package com.example.storagemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AddDeviceActivity extends AppCompatActivity {

    TextInputLayout textInputDeviceName, textInputDeviceManufacturer, textInputDeviceModel, textInputSerialNumber, textInputInventoryNumber;
    AutoCompleteTextView autoCompleteDeviceLocation, autoCompleteDeviceType;
    Button buttonAddNewDevice;

    private DatabaseHelper databaseHelper;
    private int userID;

    private ArrayList<String> locations;
    private ArrayList<String> deviceTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        textInputDeviceName = findViewById(R.id.textInputDeviceName);
        textInputDeviceManufacturer = findViewById(R.id.textInputDeviceManufacturer);
        textInputDeviceModel = findViewById(R.id.textInputDeviceModel);
        textInputSerialNumber = findViewById(R.id.textInputSerialNumber);
        textInputInventoryNumber = findViewById(R.id.textInputInventoryNumber);

        autoCompleteDeviceLocation = findViewById(R.id.autoCompleteDeviceLocation);
        autoCompleteDeviceType = findViewById(R.id.autoCompleteDeviceType);

        buttonAddNewDevice = findViewById(R.id.buttonAddNewDevice);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userID", -1);
        if(userID == -1){
            Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(AddDeviceActivity.this, DeviceManagementActivity.class);
            intent1.putExtra("userID", userID);
            startActivity(intent1);
            finish();
        }

        databaseHelper = new DatabaseHelper(AddDeviceActivity.this);

        locations = new ArrayList<>();
        deviceTypes = new ArrayList<>();

        Cursor cursorLocations = databaseHelper.getAllLocations();
        Cursor cursorDeviceTypes = databaseHelper.getAllDeviceTypes();
        String location;
        String deviceType;
        while (cursorLocations.moveToNext()){
            location = cursorLocations.getString(1);
            locations.add(location);
        }
        while (cursorDeviceTypes.moveToNext()){
            deviceType = cursorDeviceTypes.getString(1);
            deviceTypes.add(deviceType);
        }

        ArrayAdapter adapterLocations = new ArrayAdapter(AddDeviceActivity.this, R.layout.dropdown_list_item, locations);
        ArrayAdapter adapterDeviceTypes = new ArrayAdapter(AddDeviceActivity.this, R.layout.dropdown_list_item, deviceTypes);
        autoCompleteDeviceLocation.setAdapter(adapterLocations);
        autoCompleteDeviceType.setAdapter(adapterDeviceTypes);

        buttonAddNewDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deviceName = textInputDeviceName.getEditText().getText().toString().trim();
                String deviceLocation = autoCompleteDeviceLocation.getText().toString().trim();
                String deviceType = autoCompleteDeviceType.getText().toString().trim();
                String deviceManufacturer = textInputDeviceManufacturer.getEditText().getText().toString().trim();
                String deviceModel = textInputDeviceModel.getEditText().getText().toString().trim();
                String serialNumber = textInputSerialNumber.getEditText().getText().toString().trim();
                String inventoryNumber = textInputInventoryNumber.getEditText().getText().toString().trim();

                if(deviceName.equals("") || deviceLocation.equals("") || deviceType.equals("") || deviceManufacturer.equals("") || deviceModel.equals("")
                        || serialNumber.equals("") || inventoryNumber.equals("")){
                    Toast.makeText(AddDeviceActivity.this, "You need to fill out all of the fields.", Toast.LENGTH_SHORT).show();
                }
                else{
                    long res = databaseHelper.addDevice(deviceName, deviceLocation, deviceType, deviceManufacturer, deviceModel, serialNumber, inventoryNumber);
                    if (res != -1){
                        Intent intent = new Intent(AddDeviceActivity.this, DeviceManagementActivity.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(getParentActivityIntent().putExtra("userID", userID));
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}