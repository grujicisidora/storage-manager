package com.example.storagemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class UpdateConsumableActivity extends AppCompatActivity {

    private TextView textConsumableID;
    private TextInputLayout textUpdateConsumableName, textUpdateConsumableManufacturer, textUpdateConsumableModel, textUpdateConsumableCount;
    private AutoCompleteTextView autoCompleteUpdateConsumableLocation, autoCompleteUpdateConsumableType;

    private LinearLayout linearLayout;

    private Button buttonUpdateConsumable, buttonDeleteConsumable;

    private ImageButton imageButtonIncrementConsumableCount, imageButtonDecrementConsumableCount;

    private DatabaseHelper databaseHelper;

    private ArrayList<String> locations;
    private ArrayList<String> consumableTypes;

    private int userID;
    private int id;

    private Consumable consumable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_consumable);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userID", -1);
        id = intent.getIntExtra("id", -1);
        if(userID == -1 || id == -1){
            Toast.makeText(this, "Error loading consumable.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(UpdateConsumableActivity.this, ConsumableManagementActivity.class);
            intent1.putExtra("userID", userID);
            startActivity(intent1);
            finish();
        }
        textConsumableID = findViewById(R.id.textConsumableID);
        textUpdateConsumableName = findViewById(R.id.textUpdateConsumableName);
        textUpdateConsumableManufacturer = findViewById(R.id.textUpdateConsumableManufacturer);
        textUpdateConsumableModel = findViewById(R.id.textUpdateConsumableModel);
        textUpdateConsumableCount = findViewById(R.id.textUpdateConsumableCount);

        autoCompleteUpdateConsumableLocation = findViewById(R.id.autoCompleteUpdateConsumableLocation);
        autoCompleteUpdateConsumableType = findViewById(R.id.autoCompleteUpdateConsumableType);

        linearLayout = findViewById(R.id.linearLayout);

        buttonUpdateConsumable = findViewById(R.id.buttonUpdateConsumable);
        buttonDeleteConsumable = findViewById(R.id.buttonDeleteConsumable);

        imageButtonIncrementConsumableCount = findViewById(R.id.imageButtonIncrementConsumableCount);
        imageButtonDecrementConsumableCount = findViewById(R.id.imageButtonDecrementConsumableCount);

        textConsumableID.setText("Consumable ID: " + String.valueOf(id));

        databaseHelper = new DatabaseHelper(UpdateConsumableActivity.this);

        consumable = databaseHelper.getConsumable(id);
        if(consumable == null){
            Intent intent2 = new Intent(UpdateConsumableActivity.this, ConsumableManagementActivity.class);
            intent2.putExtra("userID", userID);
            startActivity(intent2);
            finish();
        }

        User user = databaseHelper.getUser(userID);
        int consumableManagementRole = user.getConsumableManagementRole();
        if(consumableManagementRole == 0){
            buttonUpdateConsumable.setEnabled(false);
            buttonDeleteConsumable.setEnabled(false);
            imageButtonIncrementConsumableCount.setEnabled(false);
            imageButtonDecrementConsumableCount.setEnabled(false);
            buttonUpdateConsumable.setVisibility(View.GONE);
            buttonDeleteConsumable.setVisibility(View.GONE);
            imageButtonIncrementConsumableCount.setVisibility(View.GONE);
            imageButtonDecrementConsumableCount.setVisibility(View.GONE);
            ((LinearLayout.LayoutParams) linearLayout.getLayoutParams()).gravity = Gravity.CENTER;
        }
        else if(consumableManagementRole == 1){
            imageButtonDecrementConsumableCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int consumableCount = consumable.getCount();
                    if(consumableCount > 0){
                        consumableCount--;
                        textUpdateConsumableCount.getEditText().setText(String.valueOf(consumableCount));
                        consumable.setCount(consumableCount);
                        databaseHelper.updateConsumableCount(id, consumableCount);
                    }
                }
            });

            imageButtonIncrementConsumableCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int consumableCount = consumable.getCount();
                    consumableCount++;
                    textUpdateConsumableCount.getEditText().setText(String.valueOf(consumableCount));
                    consumable.setCount(consumableCount);
                    databaseHelper.updateConsumableCount(id, consumableCount);
                }
            });

            buttonUpdateConsumable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String consumableName = textUpdateConsumableName.getEditText().getText().toString().trim();
                    String consumableLocation = autoCompleteUpdateConsumableLocation.getText().toString().trim();
                    String consumableType = autoCompleteUpdateConsumableType.getText().toString().trim();
                    String consumableManufacturer = textUpdateConsumableManufacturer.getEditText().getText().toString().trim();
                    String consumableModel = textUpdateConsumableModel.getEditText().getText().toString().trim();
                    String consumableCount = textUpdateConsumableCount.getEditText().getText().toString().trim();

                    if(consumableName.equals("") || consumableLocation.equals("") || consumableType.equals("") || consumableManufacturer.equals("") ||
                            consumableModel.equals("") || consumableCount.equals("")){
                        Toast.makeText(UpdateConsumableActivity.this, "You need to fill out all of the fields.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        int consumableLocationID = databaseHelper.getStorageRoomID(consumableLocation);
                        int consumableTypeID = databaseHelper.getConsumableTypeID(consumableType);
                        int consumableNumber = Integer.parseInt(consumableCount);
                        databaseHelper.updateConsumable(id, consumableName, consumableManufacturer, consumableModel, consumableNumber, consumableTypeID,
                                consumableLocationID);
                        Toast.makeText(UpdateConsumableActivity.this, "You have successfully updated the consumable.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateConsumableActivity.this, ConsumableManagementActivity.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent);
                        finish();
                    }
                }
            });

            buttonDeleteConsumable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmDialog();
                }
            });
        }
        else{
            Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(UpdateConsumableActivity.this, ConsumableManagementActivity.class);
            intent1.putExtra("userID", userID);
            startActivity(intent1);
            finish();
        }

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

        ArrayAdapter adapterLocations = new ArrayAdapter(UpdateConsumableActivity.this, R.layout.dropdown_list_item, locations);
        ArrayAdapter adapterConsumableTypes = new ArrayAdapter(UpdateConsumableActivity.this, R.layout.dropdown_list_item, consumableTypes);
        autoCompleteUpdateConsumableLocation.setAdapter(adapterLocations);
        autoCompleteUpdateConsumableType.setAdapter(adapterConsumableTypes);

        setEditTextContent(consumable);

    }

    private void setEditTextContent(Consumable consumable){
        textUpdateConsumableName.getEditText().setText(consumable.getName());
        autoCompleteUpdateConsumableLocation.setText(databaseHelper.getStorageRoomName(consumable.getLocationID()), false);
        autoCompleteUpdateConsumableType.setText(databaseHelper.getConsumableType(consumable.getConsumableTypeID()), false);
        textUpdateConsumableManufacturer.getEditText().setText(consumable.getManufacturer());
        textUpdateConsumableModel.getEditText().setText(consumable.getModel());
        textUpdateConsumableCount.getEditText().setText(String.valueOf(consumable.getCount()));
    }

    private void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete consumable");
        builder.setMessage("Are you sure you want to delete this consumable?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteConsumable(id);
                Intent intent = new Intent(UpdateConsumableActivity.this, ConsumableManagementActivity.class);
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