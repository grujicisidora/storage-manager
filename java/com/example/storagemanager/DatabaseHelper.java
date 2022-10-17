package com.example.storagemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    //context
    private Context context;

    //database and table names
    private static final String DATABASE_NAME = "storage_manager.db";
    private static final String TABLE_NAME_ADMIN = "admin_data";
    private static final String TABLE_NAME_USERS = "user_data";
    private static final String TABLE_NAME_DEVICE_TYPES = "device_types";
    private static final String TABLE_NAME_CONSUMABLE_TYPES = "consumable_types";
    private static final String TABLE_NAME_LOCATIONS = "storage_locations";
    private static final String TABLE_NAME_DEVICES = "devices";
    private static final String TABLE_NAME_CONSUMABLES = "consumables";

    //admin table - columns
    private static final String ADMIN_COL1 = "admin_id";
    private static final String ADMIN_COL2 = "admin_username";
    private static final String ADMIN_COL3 = "admin_password";

    //users table - columns
    private static final String USERS_COL1 = "user_id";
    private static final String USERS_COL2 = "first_name";
    private static final String USERS_COL3 = "last_name";
    private static final String USERS_COL4 = "user_username";
    private static final String USERS_COL5 = "user_password";
    private static final String USERS_COL6 = "user_enabled";
    private static final String USERS_COL7 = "consumable_management_privileges";
    private static final String USERS_COL8 = "device_management_privileges";


    //locations table - columns
    private static final String LOCATIONS_COL1 = "location_id";
    private static final String LOCATIONS_COL2 = "storage_room_name";

    //consumable types table - columns
    private static final String CONSUMABLE_TYPES_COL1 = "consumable_type_id";
    private static final String CONSUMABLE_TYPES_COL2 = "consumable_type_name";

    //device types table - columns
    private static final String DEVICE_TYPES_COL1 = "device_type_id";
    private static final String DEVICE_TYPES_COL2 = "device_type_name";

    //consumables table - columns
    private static final String CONSUMABLES_COL1 = "consumable_id";
    private static final String CONSUMABLES_COL2 = "consumable_name";
    private static final String CONSUMABLES_COL3 = "consumable_manufacturer";
    private static final String CONSUMABLES_COL4 = "consumable_model";
    private static final String CONSUMABLES_COL5 = "consumable_count";
    private static final String CONSUMABLES_COL6 = "consumable_type_id";
    private static final String CONSUMABLES_COL7 = "location_id";

    //devices table - columns
    private static final String DEVICES_COL1 = "device_id";
    private static final String DEVICES_COL2 = "device_name";
    private static final String DEVICES_COL3 = "device_manufacturer";
    private static final String DEVICES_COL4 = "device_model";
    private static final String DEVICES_COL5 = "serial_number";
    private static final String DEVICES_COL6 = "inventory_number";
    private static final String DEVICES_COL7 = "device_type_id";
    private static final String DEVICES_COL8 = "location_id";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query1 = "CREATE TABLE " + TABLE_NAME_ADMIN + " (" +
                ADMIN_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ADMIN_COL2 + " TEXT, " +
                ADMIN_COL3 + " TEXT);";

        String query2 = "CREATE TABLE " + TABLE_NAME_USERS + " (" +
                USERS_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERS_COL2 + " TEXT, " +
                USERS_COL3 + " TEXT, " +
                USERS_COL4 + " TEXT, " +
                USERS_COL5 + " TEXT, " +
                USERS_COL6 + " INTEGER, " +
                USERS_COL7 + " INTEGER, " +
                USERS_COL8 + " INTEGER);";

        String query3 = "CREATE TABLE " + TABLE_NAME_LOCATIONS + " (" +
                LOCATIONS_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LOCATIONS_COL2 + " TEXT);";

        String query4 = "CREATE TABLE " + TABLE_NAME_CONSUMABLE_TYPES + " (" +
                CONSUMABLE_TYPES_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CONSUMABLE_TYPES_COL2 + " TEXT);";

        String query5 = "CREATE TABLE " + TABLE_NAME_DEVICE_TYPES + " (" +
                DEVICE_TYPES_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DEVICE_TYPES_COL2 + " TEXT);";

        String query6 = "CREATE TABLE " + TABLE_NAME_CONSUMABLES + " (" +
                CONSUMABLES_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CONSUMABLES_COL2 + " TEXT, " +
                CONSUMABLES_COL3 + " TEXT, " +
                CONSUMABLES_COL4 + " TEXT, " +
                CONSUMABLES_COL5 + " INTEGER, " +
                CONSUMABLES_COL6 + " INTEGER, " +
                CONSUMABLES_COL7 + " INTEGER, " +
                "FOREIGN KEY (" + CONSUMABLES_COL6 + ") REFERENCES " + TABLE_NAME_CONSUMABLE_TYPES + "(" + CONSUMABLE_TYPES_COL1 + ")" +
                " ON UPDATE CASCADE ON DELETE CASCADE , " +
                "FOREIGN KEY (" + CONSUMABLES_COL7 + ") REFERENCES " + TABLE_NAME_LOCATIONS + "(" + LOCATIONS_COL1 + ")" +
                " ON UPDATE CASCADE ON DELETE CASCADE" + ");";

        String query7 = "CREATE TABLE " + TABLE_NAME_DEVICES + " (" +
                DEVICES_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DEVICES_COL2 + " TEXT, " +
                DEVICES_COL3 + " TEXT, " +
                DEVICES_COL4 + " TEXT, " +
                DEVICES_COL5 + " TEXT, " +
                DEVICES_COL6 + " TEXT, " +
                DEVICES_COL7 + " INTEGER, " +
                DEVICES_COL8 + " INTEGER, " +
                "FOREIGN KEY (" + DEVICES_COL7 + ") REFERENCES " + TABLE_NAME_DEVICE_TYPES + "(" + DEVICE_TYPES_COL1 + ")" +
                " ON UPDATE CASCADE ON DELETE CASCADE , " +
                "FOREIGN KEY (" + DEVICES_COL8 + ") REFERENCES " + TABLE_NAME_LOCATIONS + "(" + LOCATIONS_COL1 + ")" +
                " ON UPDATE CASCADE ON DELETE CASCADE" + ");";

        sqLiteDatabase.execSQL(query1);
        adminInitializeAccount(sqLiteDatabase);
        sqLiteDatabase.execSQL(query2);
        sqLiteDatabase.execSQL(query3);
        sqLiteDatabase.execSQL(query4);
        sqLiteDatabase.execSQL(query5);
        sqLiteDatabase.execSQL(query6);
        sqLiteDatabase.execSQL(query7);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query1 = "DROP TABLE IF EXISTS " + TABLE_NAME_ADMIN;
        sqLiteDatabase.execSQL(query1);
        String query2 = "DROP TABLE IF EXISTS " + TABLE_NAME_USERS;
        sqLiteDatabase.execSQL(query2);
        String query3 = "DROP TABLE IF EXISTS " + TABLE_NAME_LOCATIONS;
        sqLiteDatabase.execSQL(query3);
        String query4 = "DROP TABLE IF EXISTS " + TABLE_NAME_CONSUMABLE_TYPES;
        sqLiteDatabase.execSQL(query4);
        String query5 = "DROP TABLE IF EXISTS " + TABLE_NAME_DEVICE_TYPES;
        sqLiteDatabase.execSQL(query5);
        String query6 = "DROP TABLE IF EXISTS " + TABLE_NAME_CONSUMABLES;
        sqLiteDatabase.execSQL(query6);
        String query7 = "DROP TABLE IF EXISTS " + TABLE_NAME_DEVICES;
        sqLiteDatabase.execSQL(query7);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON");
    }

    private void adminInitializeAccount(SQLiteDatabase db){
        String query = "INSERT INTO " + TABLE_NAME_ADMIN + " (" + ADMIN_COL2 + ", " + ADMIN_COL3 + ") VALUES ('admin', 'admin');";
        db.execSQL(query);
    }

    public boolean isAdmin(String username, String password){
        boolean res;
        Cursor cursor = getAdminAccount();
        String adminUsername = "";
        String adminPassword = "";
        while(cursor.moveToNext()){
            adminUsername = cursor.getString(1);
            adminPassword = cursor.getString(2);
        }
        if(adminUsername.equals(username) && adminPassword.equals(password))
            res = true;
        else
            res = false;
        return res;
    }

    public Cursor getAdminAccount(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_ADMIN;
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public void updateAdminPassword(String newPassword){
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_ADMIN + " SET " + ADMIN_COL3 + " = '" + newPassword + "' WHERE " +
                ADMIN_COL1 + " = 1;";
        db.execSQL(query);
    }

    public Cursor readAllUsers(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_USERS;
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public boolean doesTheUserExist(String username){
        boolean result = true;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_USERS + " WHERE " + USERS_COL4 + " = '" + username + "';";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() == 0)
            result = false;
        return result;
    }

    private int isUsernameChanged(int id, String readUsername){
        int res;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_USERS + " WHERE " + USERS_COL1 + " = " + String.valueOf(id) + ";";
        Cursor cursor = db.rawQuery(query, null);
        String username = "";
        while (cursor.moveToNext()){
            username = cursor.getString(3);
        }
        if(username.equals(""))
            res = -1;
        else if(username.equals(readUsername))
            res = 0;
        else
            res = 1;
        return res;
    }

    private String getUserPassword(String username){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_USERS + " WHERE " + USERS_COL4 + " = '" + username +"';";
        Cursor cursor = db.rawQuery(query, null);
        String password = "";
        while (cursor.moveToNext()){
            password = cursor.getString(4);
        }
        return password;
     }

    public long addUser(String firstName, String lastName, String username, String password, int consumableManagementRole, int deviceManagementRole){
        long res;
        if(doesTheUserExist(username)){
            Toast.makeText(context, "The user with this username already exists.", Toast.LENGTH_SHORT).show();
            res = -1;
        }
        else{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(USERS_COL2, firstName);
            cv.put(USERS_COL3, lastName);
            cv.put(USERS_COL4, username);
            cv.put(USERS_COL5, password);
            cv.put(USERS_COL6, 1); //pri kreiranju user-a automatski stavljamo da je nalog enable-ovan
            cv.put(USERS_COL7, consumableManagementRole);
            cv.put(USERS_COL8, deviceManagementRole);
            res = db.insert(TABLE_NAME_USERS, null, cv);
        }
        return res;
    }

    public void updateEnableUser(int id, int isEnabled){
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_USERS + " SET " + USERS_COL6 + " = " + String.valueOf(isEnabled) +
                " WHERE " + USERS_COL1 + " = " + String.valueOf(id) + ";";
        db.execSQL(query);
    }

    public int updateUser(int id, String firstName, String lastName, String username, int consumableManagementRole, int deviceManagementRole){
        int res = 1;
        int condition = isUsernameChanged(id, username);
        if (condition == -1){
            Toast.makeText(context, "Error updating user.", Toast.LENGTH_SHORT).show();
            res = -1;
        }
        else if(doesTheUserExist(username) && condition == 1){
            Toast.makeText(context, "The user with this username already exists.", Toast.LENGTH_SHORT).show();
            res = -1;
        }
        else{
            SQLiteDatabase db = getWritableDatabase();
            String query = "UPDATE " + TABLE_NAME_USERS + " SET " + USERS_COL2 + " = '" + firstName + "', " +
                    USERS_COL3 + " = '" + lastName + "', " +
                    USERS_COL4 + " = '" + username + "', " +
                    USERS_COL7 + " = " + String.valueOf(consumableManagementRole) + ", " +
                    USERS_COL8 + " = " + String.valueOf(deviceManagementRole) +
                    " WHERE " + USERS_COL1 + " = " + String.valueOf(id) + ";";
            db.execSQL(query);
        }
        return res;
    }

    public void resetUserPassword(int id, String newPassword){
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_USERS + " SET " + USERS_COL5 + " = '" + newPassword + "' WHERE " + USERS_COL1 + " = " + String.valueOf(id) + ";";
        db.execSQL(query);
        Toast.makeText(context, "You have updated the password successfully.", Toast.LENGTH_SHORT).show();
    }

    public User getUser(int id){
        User user;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_USERS + " WHERE " + USERS_COL1 + " = " + String.valueOf(id) + ";";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() == 0){
            Toast.makeText(context, "Error loading user.", Toast.LENGTH_SHORT).show();
            return null;
        }
        else{
            String firstName = "";
            String lastName = "";
            String username = "";
            String password = "";
            int isEnabled = 0;
            int consumableManagementRole = 0;
            int deviceManagementRole = 0;
            while (cursor.moveToNext()){
                firstName = cursor.getString(1);
                lastName = cursor.getString(2);
                username = cursor.getString(3);
                password = cursor.getString(4);
                isEnabled = cursor.getInt(5);
                consumableManagementRole = cursor.getInt(6);
                deviceManagementRole = cursor.getInt(7);
            }
            user = new User(id, firstName, lastName, username, password, isEnabled, consumableManagementRole, deviceManagementRole);
            return user;
        }
    }

    public User getUser(String findUsername){
        User user;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_USERS + " WHERE " + USERS_COL4 + " = '" + findUsername + "';";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() == 0){
            Toast.makeText(context, "Error loading user.", Toast.LENGTH_SHORT).show();
            return null;
        }
        else{
            int id = -1;
            String firstName = "";
            String lastName = "";
            String username = "";
            String password = "";
            int isEnabled = -1;
            int consumableManagementRole = 0;
            int deviceManagementRole = 0;
            while (cursor.moveToNext()){
                id = cursor.getInt(0);
                firstName = cursor.getString(1);
                lastName = cursor.getString(2);
                username = cursor.getString(3);
                password = cursor.getString(4);
                isEnabled = cursor.getInt(5);
                consumableManagementRole = cursor.getInt(6);
                deviceManagementRole = cursor.getInt(7);
            }
            user = new User(id, firstName, lastName, username, password, isEnabled, consumableManagementRole, deviceManagementRole);
            return user;
        }
    }

    public void deleteUser(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_USERS + " WHERE " + USERS_COL1 + " = " + String.valueOf(id) + ";";
        db.execSQL(query);
        Toast.makeText(context, "You have successfully deleted this user.", Toast.LENGTH_SHORT).show();
    }

    public Cursor searchUser(String s){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_USERS + " WHERE " + USERS_COL2 + " LIKE '%" + s + "%' OR " +
                USERS_COL3 + " LIKE '%" + s + "%' OR " +
                USERS_COL4 + " LIKE '%" + s + "%';";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public boolean isUser(String username, String password){
        boolean res = doesTheUserExist(username);
        if(res){
            String userPassword = getUserPassword(username);
            if(password.equals(userPassword))
                res = true;
            else
                res = false;
        }
        return res;
    }

    public Cursor getAllLocations(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_LOCATIONS;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public boolean doesLocationExist(String storageRoom){
        boolean res;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_LOCATIONS + " WHERE " + LOCATIONS_COL2 + " = '" + storageRoom + "';";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() == 0)
            res = false;
        else
            res = true;
        return res;
    }

    public long addStorageRoom(String storageRoom){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LOCATIONS_COL2, storageRoom);
        long res = db.insert(TABLE_NAME_LOCATIONS, null, cv);
        return res;
    }

    public int getStorageRoomID(String name){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_LOCATIONS + " WHERE " + LOCATIONS_COL2 + " = '" + name + "';";
        Cursor cursor = db.rawQuery(query, null);
        int res = -1;
        while (cursor.moveToNext()){
            res = cursor.getInt(0);
        }
        return res;
    }

    public String getStorageRoomName(int id){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_LOCATIONS + " WHERE " + LOCATIONS_COL1 + " = " + String.valueOf(id) + ";";
        Cursor cursor = db.rawQuery(query, null);
        String res = "";
        while (cursor.moveToNext()){
            res = cursor.getString(1);
        }
        return res;
    }

    public void updateStorageLocation(int storageRoomID, String newStorageRoomName){
        if(doesLocationExist(newStorageRoomName)){
            if(getStorageRoomID(newStorageRoomName) != storageRoomID){
                Toast.makeText(context, "This storage room location already exists.", Toast.LENGTH_SHORT).show();
            }
            else
                return;
        }
        else{
            SQLiteDatabase db = getWritableDatabase();
            String query = "UPDATE " + TABLE_NAME_LOCATIONS + " SET " + LOCATIONS_COL2 + " = '" + newStorageRoomName + "' WHERE "
                    + LOCATIONS_COL1 + " = " + String.valueOf(storageRoomID) + ";";
            db.execSQL(query);
            Toast.makeText(context, "You have successfully updated the storage room name.", Toast.LENGTH_SHORT).show();
        }
    }

    public int deleteStorageLocation(int storageRoomID){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_LOCATIONS + " WHERE " + LOCATIONS_COL1 + " = " + String.valueOf(storageRoomID) + ";";
        db.execSQL(query);
        Toast.makeText(context, "You have successfully deleted the storage location.", Toast.LENGTH_SHORT).show();
        return 1;
     }

     public Cursor searchLocation(String s){
         SQLiteDatabase db = getReadableDatabase();
         String query = "SELECT * FROM " + TABLE_NAME_LOCATIONS + " WHERE " + LOCATIONS_COL2 + " LIKE '%" + s + "%';";
         Cursor cursor = db.rawQuery(query, null);
         return cursor;
     }

     public void deleteAllStorageLocations(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_LOCATIONS;
        db.execSQL(query);
     }

    public boolean doesConsumableTypeExist(String consumableType){
        boolean res;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_CONSUMABLE_TYPES + " WHERE " + CONSUMABLE_TYPES_COL2 + " = '" + consumableType + "';";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() == 0)
            res = false;
        else
            res = true;
        return res;
    }

    public Cursor getAllConsumableTypes(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_CONSUMABLE_TYPES;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public int getConsumableTypeID(String name){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_CONSUMABLE_TYPES + " WHERE " + CONSUMABLE_TYPES_COL2 + " = '" + name + "';";
        Cursor cursor = db.rawQuery(query, null);
        int res = -1;
        while (cursor.moveToNext()){
            res = cursor.getInt(0);
        }
        return res;
    }

    public String getConsumableType(int id){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_CONSUMABLE_TYPES + " WHERE " + CONSUMABLE_TYPES_COL1 + " = " + String.valueOf(id) + ";";
        Cursor cursor = db.rawQuery(query, null);
        String value = "";
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                value = cursor.getString(1);
            }
        }
        return value;
    }

    public void deleteAllConsumableTypes(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_CONSUMABLE_TYPES;
        db.execSQL(query);
    }

    public Cursor searchConsumableTypes(String s){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_CONSUMABLE_TYPES + " WHERE " + CONSUMABLE_TYPES_COL2 + " LIKE '%" + s + "%';";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public void updateConsumableType(int consumableTypeID, String newConsumableTypeName){
        if(doesConsumableTypeExist(newConsumableTypeName)){
            if(getConsumableTypeID(newConsumableTypeName) != consumableTypeID){
                Toast.makeText(context, "This consumable type already exists.", Toast.LENGTH_SHORT).show();
            }
            else
                return;
        }
        else{
            SQLiteDatabase db = getWritableDatabase();
            String query = "UPDATE " + TABLE_NAME_CONSUMABLE_TYPES + " SET " + CONSUMABLE_TYPES_COL2 + " = '" + newConsumableTypeName + "' WHERE "
                    + CONSUMABLE_TYPES_COL1 + " = " + String.valueOf(consumableTypeID) + ";";
            db.execSQL(query);
            Toast.makeText(context, "You have successfully updated the consumable type.", Toast.LENGTH_SHORT).show();
        }
    }

    public int deleteConsumableType(int consumableTypeID){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_CONSUMABLE_TYPES + " WHERE " + CONSUMABLE_TYPES_COL1 + " = " + String.valueOf(consumableTypeID) + ";";
        db.execSQL(query);
        Toast.makeText(context, "You have successfully deleted the consumable type.", Toast.LENGTH_SHORT).show();
        return 1;
    }

    public long addConsumableType(String consumableType){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CONSUMABLE_TYPES_COL2, consumableType);
        long res = db.insert(TABLE_NAME_CONSUMABLE_TYPES, null, cv);
        return res;
    }

    public Cursor getAllDeviceTypes(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_DEVICE_TYPES;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public String getDeviceType(int id){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_DEVICE_TYPES + " WHERE " + DEVICE_TYPES_COL1 + " = " + String.valueOf(id) + ";";
        Cursor cursor = db.rawQuery(query, null);
        String value = "";
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                value = cursor.getString(1);
            }
        }
        return value;
    }

    public int getDeviceTypeID(String name){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_DEVICE_TYPES + " WHERE " + DEVICE_TYPES_COL2 + " = '" + name + "';";
        Cursor cursor = db.rawQuery(query, null);
        int res = -1;
        while (cursor.moveToNext()){
            res = cursor.getInt(0);
        }
        return res;
    }

    public boolean doesDeviceTypeExist(String deviceType){
        boolean res;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_DEVICE_TYPES + " WHERE " + DEVICE_TYPES_COL2 + " = '" + deviceType + "';";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() == 0)
            res = false;
        else
            res = true;
        return res;
    }

    public void deleteAllDeviceTypes(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_DEVICE_TYPES;
        db.execSQL(query);
    }

    public Cursor searchDeviceTypes(String s){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_DEVICE_TYPES + " WHERE " + DEVICE_TYPES_COL2 + " LIKE '%" + s + "%';";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public void updateDeviceType(int deviceTypeID, String newDeviceTypeName){
        if(doesDeviceTypeExist(newDeviceTypeName)){
            if(getDeviceTypeID(newDeviceTypeName) != deviceTypeID){
                Toast.makeText(context, "This device type already exists.", Toast.LENGTH_SHORT).show();
            }
            else
                return;
        }
        else{
            SQLiteDatabase db = getWritableDatabase();
            String query = "UPDATE " + TABLE_NAME_DEVICE_TYPES + " SET " + DEVICE_TYPES_COL2 + " = '" + newDeviceTypeName + "' WHERE "
                    + DEVICE_TYPES_COL1 + " = " + String.valueOf(deviceTypeID) + ";";
            db.execSQL(query);
            Toast.makeText(context, "You have successfully updated the device type.", Toast.LENGTH_SHORT).show();
        }
    }

    public int deleteDeviceType(int deviceTypeID){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_DEVICE_TYPES + " WHERE " + DEVICE_TYPES_COL1 + " = " + String.valueOf(deviceTypeID) + ";";
        db.execSQL(query);
        Toast.makeText(context, "You have successfully deleted the device type.", Toast.LENGTH_SHORT).show();
        return 1;
    }

    public long addDeviceType(String deviceType){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DEVICE_TYPES_COL2, deviceType);
        long res = db.insert(TABLE_NAME_DEVICE_TYPES, null, cv);
        return res;
    }

    public Cursor readAllConsumables(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_CONSUMABLES;
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public Cursor searchConsumable(String s){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM ((" + TABLE_NAME_CONSUMABLES +
                " INNER JOIN " + TABLE_NAME_LOCATIONS + " ON " + TABLE_NAME_CONSUMABLES + "." + CONSUMABLES_COL7 + " = " + TABLE_NAME_LOCATIONS + "." +
                LOCATIONS_COL1 + ") INNER JOIN " + TABLE_NAME_CONSUMABLE_TYPES + " ON " + TABLE_NAME_CONSUMABLES + "." + CONSUMABLES_COL6 + " = "
                + TABLE_NAME_CONSUMABLE_TYPES + "." + CONSUMABLE_TYPES_COL1 +
                ") WHERE " + TABLE_NAME_CONSUMABLES + "." + CONSUMABLES_COL2 + " LIKE '%" + s + "%' OR " +
                TABLE_NAME_CONSUMABLES + "." + CONSUMABLES_COL3 + " LIKE '%" + s + "%' OR " +
                TABLE_NAME_CONSUMABLES + "." + CONSUMABLES_COL4 + " LIKE '%" + s + "%' OR " +
                TABLE_NAME_CONSUMABLE_TYPES + "." + CONSUMABLE_TYPES_COL2 + " LIKE '%" + s + "%' OR " +
                TABLE_NAME_LOCATIONS + "." + LOCATIONS_COL2 + " LIKE '%" + s + "%';";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public long addConsumable(String name, String location, String type, String manufacturer, String model, String count){
        int locationID = getStorageRoomID(location);
        int consumableTypeID = getConsumableTypeID(type);
        int consumableCount = Integer.parseInt(count);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CONSUMABLES_COL2, name);
        cv.put(CONSUMABLES_COL3, manufacturer);
        cv.put(CONSUMABLES_COL4, model);
        cv.put(CONSUMABLES_COL5, consumableCount);
        cv.put(CONSUMABLES_COL6, consumableTypeID);
        cv.put(CONSUMABLES_COL7, locationID);
        long res = db.insert(TABLE_NAME_CONSUMABLES, null, cv);
        if(res == -1){
            Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "You have successfully added a new consumable", Toast.LENGTH_SHORT).show();
        }
        return res;
    }

    public Consumable getConsumable(int id){
        Consumable consumable;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_CONSUMABLES + " WHERE " + CONSUMABLES_COL1 + " = " + String.valueOf(id) + ";";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() == 0){
            Toast.makeText(context, "Error loading consumable.", Toast.LENGTH_SHORT).show();
            return null;
        }
        else{
            String name = "";
            String manufacturer = "";
            String model = "";
            int count = -1;
            int consumableTypeID = -1;
            int locationID = -1;
            while (cursor.moveToNext()){
                name = cursor.getString(1);
                manufacturer = cursor.getString(2);
                model = cursor.getString(3);
                count = cursor.getInt(4);
                consumableTypeID = cursor.getInt(5);
                locationID = cursor.getInt(6);
            }
            if(count == -1 || consumableTypeID == -1 || locationID == -1){
                Toast.makeText(context, "Error loading consumable.", Toast.LENGTH_SHORT).show();
                return null;
            }
            else{
                consumable = new Consumable(id, name, manufacturer, model, count, consumableTypeID, locationID);
                return consumable;
            }
        }
    }

    public void updateConsumableCount(int consumableID, int consumableCount){
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_CONSUMABLES + " SET " + CONSUMABLES_COL5 + " = " + String.valueOf(consumableCount) + " WHERE " +
                CONSUMABLES_COL1 + " = " + String.valueOf(consumableID) + ";";
        db.execSQL(query);
    }

    public void updateConsumable(int id, String name, String manufacturer, String model, int count, int consumableTypeID, int locationID){
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_CONSUMABLES + " SET " + CONSUMABLES_COL2 + " = '" + name + "', " +
                CONSUMABLES_COL3 + " = '" + manufacturer + "', " +
                CONSUMABLES_COL4 + " = '" + model + "', " +
                CONSUMABLES_COL5 + " = " + String.valueOf(count) + ", " +
                CONSUMABLES_COL6 + " = " + String.valueOf(consumableTypeID) + ", " +
                CONSUMABLES_COL7 + " = " + String.valueOf(locationID) +
                " WHERE " + CONSUMABLES_COL1 + " = " + String.valueOf(id) + ";";
        db.execSQL(query);
    }

    public void deleteConsumable(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_CONSUMABLES + " WHERE " + CONSUMABLES_COL1 + " = " + String.valueOf(id) + ";";
        db.execSQL(query);
        Toast.makeText(context, "You have successfully deleted this consumable.", Toast.LENGTH_SHORT).show();
    }

    public Cursor readAllDevices(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_DEVICES;
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public long addDevice(String name, String location, String type, String manufacturer, String model, String serialNumber, String inventoryNumber){
        int locationID = getStorageRoomID(location);
        int deviceTypeID = getDeviceTypeID(type);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DEVICES_COL2, name);
        cv.put(DEVICES_COL3, manufacturer);
        cv.put(DEVICES_COL4, model);
        cv.put(DEVICES_COL5, serialNumber);
        cv.put(DEVICES_COL6, inventoryNumber);
        cv.put(DEVICES_COL7, deviceTypeID);
        cv.put(DEVICES_COL8, locationID);
        long res = db.insert(TABLE_NAME_DEVICES, null, cv);
        if(res == -1){
            Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "You have successfully added a new device.", Toast.LENGTH_SHORT).show();
        }
        return res;
    }

    public Device getDevice(int id){
        Device device;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_DEVICES + " WHERE " + DEVICES_COL1 + " = " + String.valueOf(id) + ";";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() == 0){
            Toast.makeText(context, "Error loading device.", Toast.LENGTH_SHORT).show();
            return null;
        }
        else{
            String name = "";
            String manufacturer = "";
            String model = "";
            String serialNumber = "";
            String inventoryNumber = "";
            int deviceTypeID = -1;
            int locationID = -1;
            while (cursor.moveToNext()){
                name = cursor.getString(1);
                manufacturer = cursor.getString(2);
                model = cursor.getString(3);
                serialNumber = cursor.getString(4);
                inventoryNumber = cursor.getString(5);
                deviceTypeID = cursor.getInt(6);
                locationID = cursor.getInt(7);
            }
            if(deviceTypeID == -1 || locationID == -1){
                Toast.makeText(context, "Error loading device.", Toast.LENGTH_SHORT).show();
                return null;
            }
            else{
                device = new Device(id, name, manufacturer, model, serialNumber, inventoryNumber, deviceTypeID, locationID);
                return device;
            }
        }
    }

    public void updateDevice(int id, String name, String manufacturer, String model, String serialNumber, String inventoryNumber, int deviceTypeID, int locationID){
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_DEVICES + " SET " + DEVICES_COL2 + " = '" + name + "', " +
                DEVICES_COL3 + " = '" + manufacturer + "', " +
                DEVICES_COL4 + " = '" + model + "', " +
                DEVICES_COL5 + " = '" + serialNumber + "', " +
                DEVICES_COL6 + " = '" + inventoryNumber + "', " +
                DEVICES_COL7 + " = " + String.valueOf(deviceTypeID) + ", " +
                DEVICES_COL8 + " = " + String.valueOf(locationID) +
                " WHERE " + DEVICES_COL1 + " = " + String.valueOf(id) + ";";
        db.execSQL(query);
    }

    public void deleteDevice(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_DEVICES + " WHERE " + DEVICES_COL1 + " = " + String.valueOf(id) + ";";
        db.execSQL(query);
        Toast.makeText(context, "You have successfully deleted this device.", Toast.LENGTH_SHORT).show();
    }

    public Cursor searchDevice(String s){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM ((" + TABLE_NAME_DEVICES +
                " INNER JOIN " + TABLE_NAME_LOCATIONS + " ON " + TABLE_NAME_DEVICES + "." + DEVICES_COL8 + " = " + TABLE_NAME_LOCATIONS + "." + LOCATIONS_COL1 +
                ") INNER JOIN " + TABLE_NAME_DEVICE_TYPES + " ON " + TABLE_NAME_DEVICES + "." + DEVICES_COL7 + " = " + TABLE_NAME_DEVICE_TYPES + "."
                + DEVICE_TYPES_COL1 + ") WHERE " + TABLE_NAME_DEVICES + "." + DEVICES_COL2 + " LIKE '%" + s + "%' OR " +
                TABLE_NAME_DEVICES + "." + DEVICES_COL3 + " LIKE '%" + s + "%' OR " +
                TABLE_NAME_DEVICES + "." + DEVICES_COL4 + " LIKE '%" + s + "%' OR " +
                TABLE_NAME_DEVICES + "." + DEVICES_COL5 + " LIKE '%" + s + "%' OR " +
                TABLE_NAME_DEVICES + "." + DEVICES_COL6 + " LIKE '%" + s + "%' OR " +
                TABLE_NAME_DEVICE_TYPES + "." + DEVICE_TYPES_COL2 + " LIKE '%" + s + "%' OR " +
                TABLE_NAME_LOCATIONS + "." + LOCATIONS_COL2 + " LIKE '%" + s + "%';";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

}
