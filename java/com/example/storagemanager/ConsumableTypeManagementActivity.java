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

public class ConsumableTypeManagementActivity extends AppCompatActivity {

    private ListView listViewConsumableTypes;
    private FloatingActionButton buttonAddConsumableType, buttonDeleteAllConsumableTypes;

    private DatabaseHelper databaseHelper;

    private ArrayList<String> consumableTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumable_type_management);

        listViewConsumableTypes = findViewById(R.id.listViewConsumableTypes);
        buttonAddConsumableType = findViewById(R.id.buttonAddConsumableType);
        buttonDeleteAllConsumableTypes = findViewById(R.id.buttonDeleteAllConsumableTypes);

        databaseHelper = new DatabaseHelper(ConsumableTypeManagementActivity.this);

        consumableTypes = new ArrayList<>();

        Cursor cursor = databaseHelper.getAllConsumableTypes();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "There are no added consumable types.", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursor.moveToNext()){
                consumableTypes.add(cursor.getString(1));
            }
            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, consumableTypes);
            listViewConsumableTypes.setAdapter(adapter);
        }

         listViewConsumableTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String consumableTypeName = adapterView.getItemAtPosition(i).toString();
                int consumableTypeID = databaseHelper.getConsumableTypeID(consumableTypeName);
                if(consumableTypeID == -1){
                    Toast.makeText(ConsumableTypeManagementActivity.this, "Error loading consumable type.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(ConsumableTypeManagementActivity.this, UpdateConsumableTypeActivity.class);
                    intent.putExtra("id", consumableTypeID);
                    intent.putExtra("name", consumableTypeName);
                    startActivity(intent);
                }
            }
         });

        buttonAddConsumableType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsumableTypeManagementActivity.this, AddConsumableTypeActivity.class);
                startActivity(intent);
            }
        });

        buttonDeleteAllConsumableTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.find_consumable_type_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.actionFindConsumableType);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search consumable types");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Cursor cursor = databaseHelper.searchConsumableTypes(s);
                ArrayList<String> filteredConsumableTypes = new ArrayList<>();
                ListAdapter filteredAdapter;
                while(cursor.moveToNext()){
                    String consumableType = cursor.getString(1);
                    filteredConsumableTypes.add(consumableType);
                }
                filteredAdapter = new ArrayAdapter<>(ConsumableTypeManagementActivity.this, android.R.layout.simple_list_item_1, filteredConsumableTypes);
                listViewConsumableTypes.setAdapter(filteredAdapter);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all consumable types");
        builder.setMessage("By deleting all of the consumable types, you will delete associated consumables. " +
                "Are you sure you want to delete all consumable types?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteAllConsumableTypes();
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