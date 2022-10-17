package com.example.storagemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class UpdateUserActivity extends AppCompatActivity {

    TextInputLayout textUpdateFirstName, textUpdateLastName, textUpdateUsername;
    AutoCompleteTextView autoCompleteUpdateConsumableManagementRole, autoCompleteUpdateDeviceManagementRole;
    Switch switchEnableUser;
    Button buttonUpdateUser, buttonResetPassword, buttonDeleteUser;
    TextView textUserID;

    DatabaseHelper databaseHelper;

    private ArrayList<String> roles;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        textUpdateFirstName = findViewById(R.id.textUpdateFirstName);
        textUpdateLastName = findViewById(R.id.textUpdateLastName);
        textUpdateUsername = findViewById(R.id.textUpdateUsername);

        autoCompleteUpdateConsumableManagementRole = findViewById(R.id.autoCompleteUpdateConsumableManagementRole);
        autoCompleteUpdateDeviceManagementRole = findViewById(R.id.autoCompleteUpdateDeviceManagementRole);

        switchEnableUser = findViewById(R.id.switchEnableUser);

        buttonUpdateUser = findViewById(R.id.buttonUpdateUser);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);
        buttonDeleteUser = findViewById(R.id.buttonDeleteUser);

        textUserID = findViewById(R.id.textUserID);

        databaseHelper = new DatabaseHelper(UpdateUserActivity.this);

        roles = new ArrayList<>();
        roles.add("Read-only");
        roles.add("Administration");

        ArrayAdapter adapter = new ArrayAdapter(UpdateUserActivity.this, R.layout.dropdown_list_item, roles);
        autoCompleteUpdateConsumableManagementRole.setAdapter(adapter);
        autoCompleteUpdateDeviceManagementRole.setAdapter(adapter);

        Intent intent = getIntent();
        user = databaseHelper.getUser(intent.getIntExtra("id", -1));
        if(user == null){
            Toast.makeText(this, "Error loading user.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(UpdateUserActivity.this, UserManagementActivity.class);
            startActivity(intent1);
            finish();
        }
        else{
            textUserID.setText("User ID: " + String.valueOf(user.getId()));
            setEditTextContent(user);
        }

        switchEnableUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int isEnabled;
                if(b == true){
                    isEnabled = 1;
                    switchEnableUser.setText("The user is enabled");
                    Toast.makeText(UpdateUserActivity.this, "You have successfully enabled this user.", Toast.LENGTH_SHORT).show();
                }
                else{
                    isEnabled = 0;
                    switchEnableUser.setText("The user is disabled");
                    Toast.makeText(UpdateUserActivity.this, "You have successfully disabled this user.", Toast.LENGTH_SHORT).show();
                }
                user.setIsEnabled(isEnabled);
                databaseHelper.updateEnableUser(user.getId(), isEnabled);
            }
        });

        buttonUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = textUpdateFirstName.getEditText().getText().toString().trim();
                String lastName = textUpdateLastName.getEditText().getText().toString().trim();
                String username = textUpdateUsername.getEditText().getText().toString().trim();
                String consumableManagementRole = autoCompleteUpdateConsumableManagementRole.getText().toString().trim();
                String deviceManagementRole = autoCompleteUpdateDeviceManagementRole.getText().toString().trim();
                if(firstName.equals("") || lastName.equals("") || username.equals("") || consumableManagementRole.equals("") || deviceManagementRole.equals("")){
                    setEditTextContent(user);
                    Toast.makeText(UpdateUserActivity.this, "All of the fields have to be filled out!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(firstName.equals(user.getFirstName()) && lastName.equals(user.getLastName()) && username.equals(user.getUsername())
                        && consumableManagementRole.equals(roles.get(user.getConsumableManagementRole()))
                        && deviceManagementRole.equals(roles.get(user.getDeviceManagementRole())))
                    return;
                else{
                    int res = databaseHelper.updateUser(user.getId(), firstName, lastName, username, roles.indexOf(consumableManagementRole),
                            roles.indexOf(deviceManagementRole));
                    if(res == 1){
                        Toast.makeText(UpdateUserActivity.this, "You have successfully updated the user data.", Toast.LENGTH_SHORT).show();
                        user.setFirstName(firstName);
                        user.setLastName(lastName);
                        user.setUsername(username);
                        user.setConsumableManagementRole(roles.indexOf(consumableManagementRole));
                        user.setDeviceManagementRole(roles.indexOf(deviceManagementRole));
                    }
                    else{
                        setEditTextContent(user);
                        return;
                    }
                }
            }
        });

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateUserActivity.this, ResetUserPasswordActivity.class);
                intent.putExtra("id", user.getId());
                startActivity(intent);
            }
        });

        buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
    }

    private void setEditTextContent(User user){
        textUpdateFirstName.getEditText().setText(user.getFirstName());
        textUpdateLastName.getEditText().setText(user.getLastName());
        textUpdateUsername.getEditText().setText(user.getUsername());
        autoCompleteUpdateConsumableManagementRole.setText(roles.get(user.getConsumableManagementRole()), false);
        autoCompleteUpdateDeviceManagementRole.setText(roles.get(user.getDeviceManagementRole()), false);
        if(user.getIsEnabled() == 0){
            switchEnableUser.setChecked(false);
            switchEnableUser.setText("The user is disabled");
        }
        else if(user.getIsEnabled() == 1){
            switchEnableUser.setChecked(true);
            switchEnableUser.setText("The user is enabled");
        }
        else{
            Toast.makeText(this, "Error loading user.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateUserActivity.this, UserManagementActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete user");
        builder.setMessage("Are you sure you want to delete this user?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteUser(user.getId());
                Intent intent = new Intent(UpdateUserActivity.this, UserManagementActivity.class);
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
}