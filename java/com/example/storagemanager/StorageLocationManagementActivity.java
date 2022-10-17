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

public class StorageLocationManagementActivity extends AppCompatActivity {

    private ListView listViewLocations;
    private FloatingActionButton buttonAddStorageLocation, buttonDeleteAllStorageLocations;

    private DatabaseHelper databaseHelper;

    private ArrayList<String> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_location_management);

        listViewLocations = findViewById(R.id.listViewLocations);
        buttonAddStorageLocation = findViewById(R.id.buttonAddStorageLocation);
        buttonDeleteAllStorageLocations = findViewById(R.id.buttonDeleteAllStorageLocations);

        databaseHelper = new DatabaseHelper(StorageLocationManagementActivity.this);

        locations = new ArrayList<>();

        Cursor cursor = databaseHelper.getAllLocations();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "There are no added locations.", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursor.moveToNext()){
                locations.add(cursor.getString(1));
            }
            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locations);
            listViewLocations.setAdapter(adapter);
        }

        listViewLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String storageRoomName = adapterView.getItemAtPosition(i).toString();
                int storageRoomID = databaseHelper.getStorageRoomID(storageRoomName);
                if(storageRoomID == -1){
                    Toast.makeText(StorageLocationManagementActivity.this, "Error loading location.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(StorageLocationManagementActivity.this, UpdateStorageLocationActivity.class);
                    intent.putExtra("id", storageRoomID);
                    intent.putExtra("name", storageRoomName);
                    startActivity(intent);
                }
            }
        });

        buttonAddStorageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StorageLocationManagementActivity.this, AddStorageLocationActivity.class);
                startActivity(intent);
            }
        });

        buttonDeleteAllStorageLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.find_location_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.actionFindLocation);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search storage room location");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Cursor cursor = databaseHelper.searchLocation(s);
                ArrayList<String> filteredLocations = new ArrayList<>();
                ListAdapter filteredAdapter;
                while(cursor.moveToNext()){
                    String location = cursor.getString(1);
                    filteredLocations.add(location);
                }
                filteredAdapter = new ArrayAdapter<>(StorageLocationManagementActivity.this, android.R.layout.simple_list_item_1, filteredLocations);
                listViewLocations.setAdapter(filteredAdapter);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all storage locations");
        builder.setMessage("By deleting all of the storage locations, you will delete all the associated items. " +
                "Are you sure you want to delete all storage locations?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteAllStorageLocations();
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