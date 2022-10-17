package com.example.storagemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class UpdateDeviceActivity extends AppCompatActivity {

    private TextView textDeviceID;
    private TextInputLayout textUpdateDeviceName, textUpdateDeviceManufacturer, textUpdateDeviceModel, textUpdateSerialNumber, textUpdateInventoryNumber;
    private AutoCompleteTextView autoCompleteUpdateDeviceLocation, autoCompleteUpdateDeviceType;

    private Button buttonUpdateDevice, buttonDeleteDevice;

    private DatabaseHelper databaseHelper;

    private ArrayList<String> locations;
    private ArrayList<String> deviceTypes;

    private int userID;
    private int id;

    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_device);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userID", -1);
        id = intent.getIntExtra("id", -1);
        if(userID == -1 || id == -1){
            Toast.makeText(this, "Error loading device.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(UpdateDeviceActivity.this, DeviceManagementActivity.class);
            intent1.putExtra("userID", userID);
            startActivity(intent1);
            finish();
        }
        textDeviceID = findViewById(R.id.textDeviceID);
        textUpdateDeviceName = findViewById(R.id.textUpdateDeviceName);
        textUpdateDeviceManufacturer = findViewById(R.id.textUpdateDeviceManufacturer);
        textUpdateDeviceModel = findViewById(R.id.textUpdateDeviceModel);
        textUpdateSerialNumber = findViewById(R.id.textUpdateSerialNumber);
        textUpdateInventoryNumber = findViewById(R.id.textUpdateInventoryNumber);

        autoCompleteUpdateDeviceLocation = findViewById(R.id.autoCompleteUpdateDeviceLocation);
        autoCompleteUpdateDeviceType = findViewById(R.id.autoCompleteUpdateDeviceType);

        buttonUpdateDevice = findViewById(R.id.buttonUpdateDevice);
        buttonDeleteDevice = findViewById(R.id.buttonDeleteDevice);

        textDeviceID.setText("Device ID: " + String.valueOf(id));

        databaseHelper = new DatabaseHelper(UpdateDeviceActivity.this);

        device = databaseHelper.getDevice(id);
        if(device == null){
            Intent intent2 = new Intent(UpdateDeviceActivity.this, DeviceManagementActivity.class);
            intent2.putExtra("userID", userID);
            startActivity(intent2);
            finish();
        }

        User user = databaseHelper.getUser(userID);
        int deviceManagementRole = user.getDeviceManagementRole();
        if(deviceManagementRole == 0){
            buttonUpdateDevice.setEnabled(false);
            buttonDeleteDevice.setEnabled(false);
            buttonUpdateDevice.setVisibility(View.GONE);
            buttonDeleteDevice.setVisibility(View.GONE);
        }
        else if(deviceManagementRole == 1){
            buttonUpdateDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String deviceName = textUpdateDeviceName.getEditText().getText().toString().trim();
                    String deviceLocation = autoCompleteUpdateDeviceLocation.getText().toString().trim();
                    String deviceType = autoCompleteUpdateDeviceType.getText().toString().trim();
                    String deviceManufacturer = textUpdateDeviceManufacturer.getEditText().getText().toString().trim();
                    String deviceModel = textUpdateDeviceModel.getEditText().getText().toString().trim();
                    String serialNumber = textUpdateSerialNumber.getEditText().getText().toString().trim();
                    String inventoryNumber = textUpdateInventoryNumber.getEditText().getText().toString().trim();

                    if(deviceName.equals("") || deviceLocation.equals("") || deviceType.equals("") || deviceManufacturer.equals("") || deviceModel.equals("") || serialNumber.equals("") || inventoryNumber.equals("")){
                        Toast.makeText(UpdateDeviceActivity.this, "You need to fill out all of the fields.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        int deviceLocationID = databaseHelper.getStorageRoomID(deviceLocation);
                        int deviceTypeID = databaseHelper.getDeviceTypeID(deviceType);
                        databaseHelper.updateDevice(id, deviceName, deviceManufacturer, deviceModel, serialNumber, inventoryNumber, deviceTypeID, deviceLocationID);
                        Toast.makeText(UpdateDeviceActivity.this, "You have successfully updated the device.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateDeviceActivity.this, DeviceManagementActivity.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent);
                        finish();
                    }
                }
            });

            buttonDeleteDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmDialog();
                }
            });
        }
        else{
            Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(UpdateDeviceActivity.this, DeviceManagementActivity.class);
            intent1.putExtra("userID", userID);
            startActivity(intent1);
            finish();
        }

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

        ArrayAdapter adapterLocations = new ArrayAdapter(UpdateDeviceActivity.this, R.layout.dropdown_list_item, locations);
        ArrayAdapter adapterDeviceTypes = new ArrayAdapter(UpdateDeviceActivity.this, R.layout.dropdown_list_item, deviceTypes);
        autoCompleteUpdateDeviceLocation.setAdapter(adapterLocations);
        autoCompleteUpdateDeviceType.setAdapter(adapterDeviceTypes);

        setEditTextContent(device);
    }

    private void setEditTextContent(Device device){
        textUpdateDeviceName.getEditText().setText(device.getName());
        autoCompleteUpdateDeviceLocation.setText(databaseHelper.getStorageRoomName(device.getLocationID()), false);
        autoCompleteUpdateDeviceType.setText(databaseHelper.getDeviceType(device.getDeviceTypeID()), false);
        textUpdateDeviceManufacturer.getEditText().setText(device.getManufacturer());
        textUpdateDeviceModel.getEditText().setText(device.getModel());
        textUpdateSerialNumber.getEditText().setText(device.getSerialNumber());
        textUpdateInventoryNumber.getEditText().setText(device.getInventoryNumber());
    }

    private void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete device");
        builder.setMessage("Are you sure you want to delete this device?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteDevice(id);
                Intent intent = new Intent(UpdateDeviceActivity.this, DeviceManagementActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
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