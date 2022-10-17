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

public class ConsumableManagementActivity extends AppCompatActivity {

    ListView listViewConsumables;
    FloatingActionButton buttonAddConsumable;
    DatabaseHelper databaseHelper;

    private ArrayList<Consumable> consumables;

    private ConsumablesCustomAdapter adapter;

    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumable_management);

        listViewConsumables = findViewById(R.id.listViewConsumables);
        buttonAddConsumable = findViewById(R.id.buttonAddConsumable);

        databaseHelper = new DatabaseHelper(ConsumableManagementActivity.this);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userID", -1);
        if(userID == -1){
            Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(ConsumableManagementActivity.this, MainActivity.class);
            startActivity(intent1);
            finish();
        }

        User user = databaseHelper.getUser(userID);
        int consumableManagementRole = user.getConsumableManagementRole();
        if(consumableManagementRole == 0){
            buttonAddConsumable.setEnabled(false);
            buttonAddConsumable.setVisibility(View.GONE);
        }
        else if(consumableManagementRole == 1){
            buttonAddConsumable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ConsumableManagementActivity.this, AddConsumableActivity.class);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                }
            });
        }
        else{
            Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(ConsumableManagementActivity.this, UserNavigationActivity.class);
            intent1.putExtra("userID", userID);
            startActivity(intent1);
            finish();
        }

        consumables = new ArrayList<>();
        Cursor cursorConsumables = databaseHelper.readAllConsumables();
        if(cursorConsumables.getCount() == 0){
            Toast.makeText(this, "There are no consumables created.", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursorConsumables.moveToNext()){
                int id = cursorConsumables.getInt(0);
                String name = cursorConsumables.getString(1);
                String manufacturer = cursorConsumables.getString(2);
                String model = cursorConsumables.getString(3);
                int count = cursorConsumables.getInt(4);
                int consumableTypeID = cursorConsumables.getInt(5);
                int locationID = cursorConsumables.getInt(6);
                Consumable consumable = new Consumable(id, name, manufacturer, model, count, consumableTypeID, locationID);
                consumables.add(consumable);
            }

            adapter = new ConsumablesCustomAdapter(ConsumableManagementActivity.this, R.layout.consumable_list_item, consumables);

            listViewConsumables.setAdapter(adapter);
        }

        listViewConsumables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Consumable intentConsumable = consumables.get(i);
                Intent intent = new Intent(ConsumableManagementActivity.this, UpdateConsumableActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("id", intentConsumable.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.find_consumable_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.actionFindConsumable);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search consumables");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Cursor cursor = databaseHelper.searchConsumable(s);
                ArrayList<Consumable> filteredConsumables = new ArrayList<>();
                ConsumablesCustomAdapter filteredAdapter;
                while(cursor.moveToNext()){
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String manufacturer = cursor.getString(2);
                    String model = cursor.getString(3);
                    int count = cursor.getInt(4);
                    int consumableTypeID = cursor.getInt(5);
                    int locationID = cursor.getInt(6);
                    Consumable consumable = new Consumable(id, name, manufacturer, model, count, consumableTypeID, locationID);
                    filteredConsumables.add(consumable);
                }
                filteredAdapter = new ConsumablesCustomAdapter(ConsumableManagementActivity.this, R.layout.consumable_list_item, filteredConsumables);
                listViewConsumables.setAdapter(filteredAdapter);
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