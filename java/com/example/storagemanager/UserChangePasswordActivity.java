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

public class UserChangePasswordActivity extends AppCompatActivity {

    TextInputLayout textInputCurrentUserPassword, textInputNewUserPassword, textInputRepeatUserPassword;
    Button buttonChangePassword;

    DatabaseHelper databaseHelper;

    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_password);

        textInputCurrentUserPassword = findViewById(R.id.textInputCurrentUserPassword);
        textInputNewUserPassword = findViewById(R.id.textInputNewUserPassword);
        textInputRepeatUserPassword = findViewById(R.id.textInputRepeatUserPassword);

        buttonChangePassword = findViewById(R.id.buttonChangeUserPassword);

        databaseHelper = new DatabaseHelper(UserChangePasswordActivity.this);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userID", -1);
        if(userID == -1){
            Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(UserChangePasswordActivity.this, UserNavigationActivity.class);
            startActivity(intent1);
            finish();
        }

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPassword = textInputCurrentUserPassword.getEditText().getText().toString().trim();
                String newPassword = textInputNewUserPassword.getEditText().getText().toString().trim();
                String repeatPassword = textInputRepeatUserPassword.getEditText().getText().toString().trim();
                changePasswordLogic(currentPassword, newPassword, repeatPassword);
            }
        });
    }

    private void changePasswordLogic(String currentPassword, String newPassword, String repeatPassword){
        if(currentPassword.equals("") || newPassword.equals("") || repeatPassword.equals(""))
            Toast.makeText(this, "You have to fill out all of the fields.", Toast.LENGTH_SHORT).show();
        else{
            User user = databaseHelper.getUser(userID);
            if(currentPassword.equals(user.getPassword())){
                if(newPassword.equals(repeatPassword)){
                    if(newPassword.equals(currentPassword)){
                        Toast.makeText(this, "The new password cannot match the current password!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        databaseHelper.resetUserPassword(userID, newPassword);
                        Intent intent = new Intent(UserChangePasswordActivity.this, UserNavigationActivity.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent);
                        finish();
                    }
                }
                else{
                    Toast.makeText(this, "The new password and the repeat password must match!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "The current password you entered is not correct.", Toast.LENGTH_SHORT).show();
            }
        }
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