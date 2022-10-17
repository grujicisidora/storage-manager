package com.example.storagemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class UpdateConsumableTypeActivity extends AppCompatActivity {

    TextInputLayout textUpdateConsumableTypeName;
    TextView textConsumableTypeID;

    Button buttonUpdateConsumableType;
    Button buttonDeleteConsumableType;

    private DatabaseHelper databaseHelper;

    private int consumableTypeID;
    private String consumableTypeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_consumable_type);

        textUpdateConsumableTypeName = findViewById(R.id.textUpdateConsumableTypeName);
        textConsumableTypeID = findViewById(R.id.textConsumableTypeID);

        buttonUpdateConsumableType = findViewById(R.id.buttonUpdateConsumableType);
        buttonDeleteConsumableType = findViewById(R.id.buttonDeleteConsumableType);

        databaseHelper = new DatabaseHelper(UpdateConsumableTypeActivity.this);

        Intent intent = getIntent();
        consumableTypeID = intent.getIntExtra("id", -1);
        if(consumableTypeID == -1){
            Toast.makeText(this, "Error loading consumable type.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(UpdateConsumableTypeActivity.this, ConsumableTypeManagementActivity.class);
            startActivity(intent1);
            finish();
        }
        else{
            textConsumableTypeID.setText("Consumable type ID: " + String.valueOf(consumableTypeID));
            consumableTypeName = intent.getStringExtra("name");
            textUpdateConsumableTypeName.getEditText().setText(consumableTypeName);
        }

        buttonUpdateConsumableType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newConsumableTypeName = textUpdateConsumableTypeName.getEditText().getText().toString().trim();
                if(newConsumableTypeName.equals("")){
                    Toast.makeText(UpdateConsumableTypeActivity.this, "Please enter the new consumable type name.", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseHelper.updateConsumableType(consumableTypeID, newConsumableTypeName);
                }
            }
        });

        buttonDeleteConsumableType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete consumable type");
        builder.setMessage("By deleting this consumable type, you will delete all the associated consumables. Are you sure you want to delete this consumable type?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int res = databaseHelper.deleteConsumableType(consumableTypeID);
                if(res == 1){
                    Intent intent = new Intent(UpdateConsumableTypeActivity.this, ConsumableTypeManagementActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(UpdateConsumableTypeActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                }
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