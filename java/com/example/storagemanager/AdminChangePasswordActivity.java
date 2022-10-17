package com.example.storagemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class AdminChangePasswordActivity extends AppCompatActivity {

    TextInputLayout textInputCurrentAdminPassword, textInputNewAdminPassword, textInputRepeatAdminPassword;
    Button buttonChangePassword;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_change_password);

        textInputCurrentAdminPassword = findViewById(R.id.textInputCurrentAdminPassword);
        textInputNewAdminPassword = findViewById(R.id.textInputNewAdminPassword);
        textInputRepeatAdminPassword = findViewById(R.id.textInputRepeatAdminPassword);

        buttonChangePassword = findViewById(R.id.buttonChangeAdminPassword);

        databaseHelper = new DatabaseHelper(AdminChangePasswordActivity.this);

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPassword = textInputCurrentAdminPassword.getEditText().getText().toString().trim();
                String newPassword = textInputNewAdminPassword.getEditText().getText().toString().trim();
                String repeatPassword = textInputRepeatAdminPassword.getEditText().getText().toString().trim();
                changeAdminPasswordLogic(currentPassword, newPassword, repeatPassword);
            }
        });

    }

    private void changeAdminPasswordLogic(String currentPassword, String newPassword, String repeatPassword){
        if(currentPassword.equals("") || newPassword.equals("") || repeatPassword.equals(""))
            Toast.makeText(this, "You have to fill out all of the fields.", Toast.LENGTH_SHORT).show();
        else{
            Cursor adminCursor = databaseHelper.getAdminAccount();
            while (adminCursor.moveToNext()){
                String readAdminPassword = adminCursor.getString(2);
                if(currentPassword.equals(readAdminPassword)){
                    if(newPassword.equals(repeatPassword)){
                        if(newPassword.equals(currentPassword)){
                            Toast.makeText(this, "The new password cannot match the current password!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            databaseHelper.updateAdminPassword(newPassword);
                            Intent intent = new Intent(AdminChangePasswordActivity.this, AdminNavigationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else{
                        Toast.makeText(this, "The new password and the repeat password must match!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "The current password you entered is not correct.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(getParentActivityIntent());
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}