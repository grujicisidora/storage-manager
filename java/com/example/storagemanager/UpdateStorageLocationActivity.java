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

public class UpdateStorageLocationActivity extends AppCompatActivity {

    TextInputLayout textUpdateStorageRoomName;
    TextView textStorageRoomID;

    Button buttonUpdateStorageRoom;
    Button buttonDeleteStorageRoom;

    private DatabaseHelper databaseHelper;

    private int storageRoomID;
    private String storageRoomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_storage_location);

        textUpdateStorageRoomName = findViewById(R.id.textUpdateStorageRoomName);
        textStorageRoomID = findViewById(R.id.textStorageRoomID);

        buttonUpdateStorageRoom = findViewById(R.id.buttonUpdateStorageRoom);
        buttonDeleteStorageRoom = findViewById(R.id.buttonDeleteStorageRoom);

        databaseHelper = new DatabaseHelper(UpdateStorageLocationActivity.this);

        Intent intent = getIntent();
        storageRoomID = intent.getIntExtra("id", -1);
        if(storageRoomID == -1){
            Toast.makeText(this, "Error loading location.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(UpdateStorageLocationActivity.this, StorageLocationManagementActivity.class);
            startActivity(intent1);
            finish();
        }
        else{
            textStorageRoomID.setText("Storage room ID: " + String.valueOf(storageRoomID));
            storageRoomName = intent.getStringExtra("name");
            textUpdateStorageRoomName.getEditText().setText(storageRoomName);
        }

        buttonUpdateStorageRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newStorageRoomName = textUpdateStorageRoomName.getEditText().getText().toString().trim();
                if(newStorageRoomName.equals("")){
                    Toast.makeText(UpdateStorageLocationActivity.this, "Please enter the new storage room name.", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseHelper.updateStorageLocation(storageRoomID, newStorageRoomName);
                }
            }
        });

        buttonDeleteStorageRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete storage location");
        builder.setMessage("By deleting this storage location, you will delete all associated items. Are you sure you want to delete this storage location?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int res = databaseHelper.deleteStorageLocation(storageRoomID);
                if(res == 1){
                    Intent intent = new Intent(UpdateStorageLocationActivity.this, StorageLocationManagementActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(UpdateStorageLocationActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
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