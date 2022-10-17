package com.example.storagemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DeviceManagementActivity extends AppCompatActivity {

    ListView listViewDevices;
    FloatingActionButton buttonAddDevice;
    DatabaseHelper databaseHelper;

    private ArrayList<Device> devices;

    private DevicesCustomAdapter adapter;

    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_management);

        listViewDevices = findViewById(R.id.listViewDevices);
        buttonAddDevice = findViewById(R.id.buttonAddDevice);

        databaseHelper = new DatabaseHelper(DeviceManagementActivity.this);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userID", -1);
        if(userID == -1){
            Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(DeviceManagementActivity.this, MainActivity.class);
            startActivity(intent1);
            finish();
        }

        User user = databaseHelper.getUser(userID);
        int deviceManagementRole = user.getDeviceManagementRole();
        if(deviceManagementRole == 0){
            buttonAddDevice.setEnabled(false);
            buttonAddDevice.setVisibility(View.GONE);
        }
        else if(deviceManagementRole == 1){
            buttonAddDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DeviceManagementActivity.this, AddDeviceActivity.class);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                }
            });
        }
        else{
            Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(DeviceManagementActivity.this, UserNavigationActivity.class);
            intent1.putExtra("userID", userID);
            startActivity(intent1);
            finish();
        }

        devices = new ArrayList<>();
        Cursor cursorDevices = databaseHelper.readAllDevices();
        if(cursorDevices.getCount() == 0){
            Toast.makeText(this, "There are no devices created.", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursorDevices.moveToNext()){
                int id = cursorDevices.getInt(0);
                String name = cursorDevices.getString(1);
                String manufacturer = cursorDevices.getString(2);
                String model = cursorDevices.getString(3);
                String serialNumber = cursorDevices.getString(4);
                String inventoryNumber = cursorDevices.getString(5);
                int deviceTypeID = cursorDevices.getInt(6);
                int locationID = cursorDevices.getInt(7);
                Device device = new Device(id, name, manufacturer, model, serialNumber, inventoryNumber, deviceTypeID, locationID);
                devices.add(device);
            }

            adapter = new DevicesCustomAdapter(DeviceManagementActivity.this, R.layout.device_list_item, devices);

            listViewDevices.setAdapter(adapter);
        }

        listViewDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Device intentDevice = devices.get(i);
                Intent intent = new Intent(DeviceManagementActivity.this, UpdateDeviceActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("id", intentDevice.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.find_device_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.actionFindDevice);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search devices");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Cursor cursor = databaseHelper.searchDevice(s);
                ArrayList<Device> filteredDevices = new ArrayList<>();
                DevicesCustomAdapter filteredAdapter;
                while(cursor.moveToNext()){
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String manufacturer = cursor.getString(2);
                    String model = cursor.getString(3);
                    String serialNumber = cursor.getString(4);
                    String inventoryNumber = cursor.getString(5);
                    int deviceTypeID = cursor.getInt(6);
                    int locationID = cursor.getInt(7);
                    Device device = new Device(id, name, manufacturer, model, serialNumber, inventoryNumber, deviceTypeID, locationID);
                    filteredDevices.add(device);
                }
                filteredAdapter = new DevicesCustomAdapter(DeviceManagementActivity.this, R.layout.device_list_item, filteredDevices);
                listViewDevices.setAdapter(filteredAdapter);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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