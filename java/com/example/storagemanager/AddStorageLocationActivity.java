package com.example.storagemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class AddStorageLocationActivity extends AppCompatActivity {

    private TextInputLayout textInputStorageLocation;
    private Button buttonAddStorageRoom;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_storage_location);

        textInputStorageLocation = findViewById(R.id.textInputStorageLocation);
        buttonAddStorageRoom = findViewById(R.id.buttonAddNewStorageRoom);

        databaseHelper = new DatabaseHelper(AddStorageLocationActivity.this);

        buttonAddStorageRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String storageRoom = textInputStorageLocation.getEditText().getText().toString().trim();
                if(storageRoom.equals("")){
                    Toast.makeText(AddStorageLocationActivity.this, "You need to enter the storage room name.", Toast.LENGTH_SHORT).show();
                }
                else if (databaseHelper.doesLocationExist(storageRoom)){
                    Toast.makeText(AddStorageLocationActivity.this, "This storage room already exists.", Toast.LENGTH_SHORT).show();
                }
                else{
                    long res = databaseHelper.addStorageRoom(storageRoom);
                    if(res == -1){
                        Toast.makeText(AddStorageLocationActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AddStorageLocationActivity.this, "You have successfully added a new storage room.", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(AddStorageLocationActivity.this, StorageLocationManagementActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}