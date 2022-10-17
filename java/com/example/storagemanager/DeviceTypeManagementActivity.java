package com.example.storagemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DeviceTypeManagementActivity extends AppCompatActivity {

    private ListView listViewDeviceTypes;
    private FloatingActionButton buttonAddDeviceType, buttonDeleteAllDeviceTypes;

    private DatabaseHelper databaseHelper;

    private ArrayList<String> deviceTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_type_management);

        listViewDeviceTypes = findViewById(R.id.listViewDeviceTypes);
        buttonAddDeviceType = findViewById(R.id.buttonAddDeviceType);
        buttonDeleteAllDeviceTypes = findViewById(R.id.buttonDeleteAllDeviceTypes);

        databaseHelper = new DatabaseHelper(DeviceTypeManagementActivity.this);

        deviceTypes = new ArrayList<>();

        Cursor cursor = databaseHelper.getAllDeviceTypes();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "There are no added device types.", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursor.moveToNext()){
                deviceTypes.add(cursor.getString(1));
            }
            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceTypes);
            listViewDeviceTypes.setAdapter(adapter);
        }

        listViewDeviceTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String deviceTypeName = adapterView.getItemAtPosition(i).toString();
                int deviceTypeID = databaseHelper.getDeviceTypeID(deviceTypeName);
                if(deviceTypeID == -1){
                    Toast.makeText(DeviceTypeManagementActivity.this, "Error loading device type.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(DeviceTypeManagementActivity.this, UpdateDeviceTypeActivity.class);
                    intent.putExtra("id", deviceTypeID);
                    intent.putExtra("name", deviceTypeName);
                    startActivity(intent);
                }
            }
        });

        buttonAddDeviceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeviceTypeManagementActivity.this, AddDeviceTypeActivity.class);
                startActivity(intent);
            }
        });

        buttonDeleteAllDeviceTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.find_device_type_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.actionFindDeviceType);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search device types");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Cursor cursor = databaseHelper.searchDeviceTypes(s);
                ArrayList<String> filteredDeviceTypes = new ArrayList<>();
                ListAdapter filteredAdapter;
                while(cursor.moveToNext()){
                    String deviceType = cursor.getString(1);
                    filteredDeviceTypes.add(deviceType);
                }
                filteredAdapter = new ArrayAdapter<>(DeviceTypeManagementActivity.this, android.R.layout.simple_list_item_1, filteredDeviceTypes);
                listViewDeviceTypes.setAdapter(filteredAdapter);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all device types");
        builder.setMessage("By deleting all of the device types, you will delete associated devices. " +
                "Are you sure you want to delete all device types?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteAllDeviceTypes();
                finish();
                startActivity(getIntent());
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