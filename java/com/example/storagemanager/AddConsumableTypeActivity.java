package com.example.storagemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class AddConsumableTypeActivity extends AppCompatActivity {

    private TextInputLayout textInputConsumableType;
    private Button buttonAddConsumableType;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_consumable_type);

        textInputConsumableType = findViewById(R.id.textInputConsumableType);
        buttonAddConsumableType = findViewById(R.id.buttonAddNewConsumableType);

        databaseHelper = new DatabaseHelper(AddConsumableTypeActivity.this);

        buttonAddConsumableType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String consumableType = textInputConsumableType.getEditText().getText().toString().trim();
                if(consumableType.equals("")){
                    Toast.makeText(AddConsumableTypeActivity.this, "You need to enter the consumable type name.", Toast.LENGTH_SHORT).show();
                }
                else if (databaseHelper.doesConsumableTypeExist(consumableType)){
                    Toast.makeText(AddConsumableTypeActivity.this, "This consumable type already exists.", Toast.LENGTH_SHORT).show();
                }
                else{
                    long res = databaseHelper.addConsumableType(consumableType);
                    if(res == -1){
                        Toast.makeText(AddConsumableTypeActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AddConsumableTypeActivity.this, "You have successfully added a new consumable type.", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(AddConsumableTypeActivity.this, ConsumableTypeManagementActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}