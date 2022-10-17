package com.example.storagemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class ResetUserPasswordActivity extends AppCompatActivity {

    TextInputLayout textResetPassword, textConfirmResetPassword;
    Button buttonReset;

    DatabaseHelper databaseHelper;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_user_password);

        textResetPassword = findViewById(R.id.textResetPassword);
        textConfirmResetPassword = findViewById(R.id.textConfirmResetPassword);

        buttonReset = findViewById(R.id.buttonReset);

        databaseHelper = new DatabaseHelper(ResetUserPasswordActivity.this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        if(id == -1){
            Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(ResetUserPasswordActivity.this, UpdateUserActivity.class);
            startActivity(intent1);
            finish();
        }

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = textResetPassword.getEditText().getText().toString().trim();
                String confirmedPassword = textConfirmResetPassword.getEditText().getText().toString().trim();
                if(password.equals("")){
                    Toast.makeText(ResetUserPasswordActivity.this, "You need to enter a new password.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(password.equals(confirmedPassword)){
                    databaseHelper.resetUserPassword(id, password);
                    Intent intent2 = new Intent(ResetUserPasswordActivity.this, UpdateUserActivity.class);
                    intent2.putExtra("id", id);
                    startActivity(intent2);
                    finish();
                }
                else{
                    Toast.makeText(ResetUserPasswordActivity.this, "The passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(getParentActivityIntent().putExtra("id", id));
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}