package com.example.storagemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AddUserActivity extends AppCompatActivity {

    TextInputLayout textInputFirstName, textInputLastName, textInputUsersUsername, textInputUsersPassword, textConfirmUsersPassword;
    AutoCompleteTextView autoCompleteConsumableManagementRole, autoCompleteDeviceManagementRole;
    Button buttonAddNewUserAccount;

    DatabaseHelper databaseHelper;

    private ArrayList<String> roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        textInputFirstName = findViewById(R.id.textInputFirstName);
        textInputLastName = findViewById(R.id.textInputLastName);
        textInputUsersUsername = findViewById(R.id.textInputUsersUsername);
        textInputUsersPassword = findViewById(R.id.textInputUsersPassword);
        textConfirmUsersPassword = findViewById(R.id.textConfirmUsersPassword);

        autoCompleteConsumableManagementRole = findViewById(R.id.autoCompleteConsumableManagementRole);
        autoCompleteDeviceManagementRole = findViewById(R.id.autoCompleteDeviceManagementRole);

        buttonAddNewUserAccount = findViewById(R.id.buttonAddNewUserAccount);

        databaseHelper = new DatabaseHelper(AddUserActivity.this);

        roles = new ArrayList<>();
        roles.add("Read-only");
        roles.add("Administration");

        ArrayAdapter adapter = new ArrayAdapter(AddUserActivity.this, R.layout.dropdown_list_item, roles);
        autoCompleteConsumableManagementRole.setAdapter(adapter);
        autoCompleteDeviceManagementRole.setAdapter(adapter);

        buttonAddNewUserAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = textInputFirstName.getEditText().getText().toString().trim();
                String lastName = textInputLastName.getEditText().getText().toString().trim();
                String username = textInputUsersUsername.getEditText().getText().toString().trim();
                String password = textInputUsersPassword.getEditText().getText().toString().trim();
                String confirmedPassword = textConfirmUsersPassword.getEditText().getText().toString().trim();
                String consumableManagementRole = autoCompleteConsumableManagementRole.getText().toString().trim();
                String deviceManagementRole = autoCompleteDeviceManagementRole.getText().toString().trim();

                if(firstName.equals("") || lastName.equals("") || username.equals("") || password.equals("") || confirmedPassword.equals("")
                        || consumableManagementRole.equals("") || deviceManagementRole.equals("")){
                    Toast.makeText(AddUserActivity.this, "You need to fill out all of the fields.", Toast.LENGTH_SHORT).show();
                }
                else if(password.equals(confirmedPassword) == false){
                    Toast.makeText(AddUserActivity.this, "The passwords do not match.", Toast.LENGTH_SHORT).show();
                }
                else{
                    int consumableManagement = roles.indexOf(consumableManagementRole);
                    int deviceManagement = roles.indexOf(deviceManagementRole);
                    long res = databaseHelper.addUser(firstName, lastName, username, password, consumableManagement, deviceManagement);
                    if(res != -1) {
                        Toast.makeText(AddUserActivity.this, "You have created a new user.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddUserActivity.this, UserManagementActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
}