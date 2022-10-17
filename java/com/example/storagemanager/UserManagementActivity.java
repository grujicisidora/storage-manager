package com.example.storagemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UserManagementActivity extends AppCompatActivity {

    ListView listViewUsers;
    FloatingActionButton buttonAddUser;
    DatabaseHelper databaseHelper;

    private ArrayList<User> users;

    private UsersCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        listViewUsers = findViewById(R.id.listViewUsers);
        buttonAddUser = findViewById(R.id.buttonAddUser);

        databaseHelper = new DatabaseHelper(UserManagementActivity.this);

        users = new ArrayList<>();
        Cursor cursorUsers = databaseHelper.readAllUsers();
        if(cursorUsers.getCount() == 0){
            Toast.makeText(this, "There are no user accounts created.", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursorUsers.moveToNext()){
                int id = cursorUsers.getInt(0);
                String firstName = cursorUsers.getString(1);
                String lastName = cursorUsers.getString(2);
                String username = cursorUsers.getString(3);
                int isEnabled = cursorUsers.getInt(5);
                int consumableManagementRole = cursorUsers.getInt(6);
                int deviceManagementRole = cursorUsers.getInt(7);
                User user = new User(id, firstName, lastName, username, isEnabled, consumableManagementRole, deviceManagementRole);
                users.add(user);
            }

            adapter = new UsersCustomAdapter(UserManagementActivity.this, R.layout.user_list_item, users);

            listViewUsers.setAdapter(adapter);
        }

        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserManagementActivity.this, AddUserActivity.class);
                startActivity(intent);
            }
        });

        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User intentUser = users.get(i);
                Intent intent = new Intent(UserManagementActivity.this, UpdateUserActivity.class);
                intent.putExtra("id", intentUser.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.find_user_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.actionFindUser);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search users");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Cursor cursor = databaseHelper.searchUser(s);
                ArrayList<User> filteredUsers = new ArrayList<>();
                UsersCustomAdapter filteredAdapter;
                while(cursor.moveToNext()){
                    int id = cursor.getInt(0);
                    String firstName = cursor.getString(1);
                    String lastName = cursor.getString(2);
                    String username = cursor.getString(3);
                    int isEnabled = cursor.getInt(5);
                    int consumableManagementRole = cursor.getInt(6);
                    int deviceManagementRole = cursor.getInt(7);
                    User user = new User(id, firstName, lastName, username, isEnabled, consumableManagementRole, deviceManagementRole);
                    filteredUsers.add(user);
                }
                filteredAdapter = new UsersCustomAdapter(UserManagementActivity.this, R.layout.user_list_item, filteredUsers);
                listViewUsers.setAdapter(filteredAdapter);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}