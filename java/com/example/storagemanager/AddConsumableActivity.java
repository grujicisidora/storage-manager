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

public class AddConsumableActivity extends AppCompatActivity {

    TextInputLayout textInputConsumableName, textInputConsumableManufacturer, textInputConsumableModel, textInputConsumableCount;
    AutoCompleteTextView autoCompleteConsumableLocation, autoCompleteConsumableType;
    Button buttonAddNewConsumable;

    private DatabaseHelper databaseHelper;
    private int userID;

    private ArrayList<String> locations;
    private ArrayList<String> consumableTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_consumable);

        textInputConsumableName = findViewById(R.id.textInputConsumableName);
        textInputConsumableManufacturer = findViewById(R.id.textInputConsumableManufacturer);
        textInputConsumableModel = findViewById(R.id.textInputConsumableModel);
        textInputConsumableCount = findViewById(R.id.textInputConsumableCount);

        autoCompleteConsumableLocation = findViewById(R.id.autoCompleteConsumableLocation);
        autoCompleteConsumableType = findViewById(R.id.autoCompleteConsumableType);

        buttonAddNewConsumable = findViewById(R.id.buttonAddNewConsumable);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userID", -1);
        if(userID == -1){
            Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(AddConsumableActivity.this, ConsumableManagementActivity.class);
            intent1.putExtra("userID", userID);
            startActivity(intent1);
            finish();
        }

        databaseHelper = new DatabaseHelper(AddConsumableActivity.this);

        locations = new ArrayList<>();
        consumableTypes = new ArrayList<>();

        Cursor cursorLocations = databaseHelper.getAllLocations();
        Cursor cursorConsumableTypes = databaseHelper.getAllConsumableTypes();
        String location;
        String consumableType;
        while (cursorLocations.moveToNext()){
            location = cursorLocations.getString(1);
            locations.add(location);
        }
        while (cursorConsumableTypes.moveToNext()){
            consumableType = cursorConsumableTypes.getString(1);
            consumableTypes.add(consumableType);
        }

        ArrayAdapter adapterLocations = new ArrayAdapter(AddConsumableActivity.this, R.layout.dropdown_list_item, locations);
        ArrayAdapter adapterConsumableTypes = new ArrayAdapter(AddConsumableActivity.this, R.layout.dropdown_list_item, consumableTypes);
        autoCompleteConsumableLocation.setAdapter(adapterLocations);
        autoCompleteConsumableType.setAdapter(adapterConsumableTypes);

        buttonAddNewConsumable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String consumableName = textInputConsumableName.getEditText().getText().toString().trim();
                String consumableLocation = autoCompleteConsumableLocation.getText().toString().trim();
                String consumableType = autoCompleteConsumableType.getText().toString().trim();
                String consumableManufacturer = textInputConsumableManufacturer.getEditText().getText().toString().trim();
                String consumableModel = textInputConsumableModel.getEditText().getText().toString().trim();
                String consumableCount = textInputConsumableCount.getEditText().getText().toString().trim();

                if(consumableName.equals("") || consumableLocation.equals("") || consumableType.equals("") || consumableManufacturer.equals("")
                        || consumableModel.equals("") || consumableCount.equals("")){
                    Toast.makeText(AddConsumableActivity.this, "You need to fill out all of the fields.", Toast.LENGTH_SHORT).show();
                }
                else{
                    long res = databaseHelper.addConsumable(consumableName, consumableLocation, consumableType, consumableManufacturer, consumableModel, consumableCount);
                    if (res != -1){
                        Intent intent = new Intent(AddConsumableActivity.this, ConsumableManagementActivity.class);
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