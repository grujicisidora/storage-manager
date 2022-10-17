package com.example.storagemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    TextInputLayout textInputUsername, textInputPassword;
    Button buttonLogin;

    DatabaseHelper databaseHelper;

    private int loginAttempts = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInputUsername = findViewById(R.id.textInputUsername);
        textInputPassword = findViewById(R.id.textInputPassword);

        buttonLogin = findViewById(R.id.buttonLogin);

        databaseHelper = new DatabaseHelper(MainActivity.this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = textInputUsername.getEditText().getText().toString().trim();
                String password = textInputPassword.getEditText().getText().toString().trim();
                if(username.equals("") == false && password.equals("") == false){
                    loginLogic(username, password);
                }
            }
        });
    }

    private void loginLogic(String username, String password){
        boolean conditionAdmin = databaseHelper.isAdmin(username, password);
        if(conditionAdmin){
            Toast.makeText(this, "You are now logged in as the administrator.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, AdminNavigationActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            if(databaseHelper.doesTheUserExist(username)){
                User user = databaseHelper.getUser(username);
                boolean conditionUser = databaseHelper.isUser(username, password);
                if(user.getIsEnabled() == 1 && conditionUser){
                    Toast.makeText(this, "You are now logged in as " + user.getFirstName() + " " + user.getLastName() + ".", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, UserNavigationActivity.class);
                    intent.putExtra("userID", user.getId());
                    startActivity(intent);
                    finish();
                }
                else if(user.getIsEnabled() == 1 && conditionUser == false){
                    loginAttempts--;
                    if(loginAttempts == 0) {
                        databaseHelper.updateEnableUser(user.getId(), 0);
                        Toast.makeText(this, "This user is now disabled due to too many wrong login attempts. For more information, " +
                                "please contact your administrator.", Toast.LENGTH_LONG).show();
                        textInputUsername.setEnabled(false);
                        textInputPassword.setEnabled(false);
                        buttonLogin.setEnabled(false);
                    }
                    else{
                        Toast.makeText(this, "Incorrect password.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "This user is disabled. For more information, please contact your administrator.", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                //Toast.makeText(this, "Incorrect username or password.", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "This user does not exist.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}