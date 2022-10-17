package com.example.storagemanager;

public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private int isEnabled;
    private int consumableManagementRole;
    private int deviceManagementRole;

    public User(int id, String firstName, String lastName, String username, int isEnabled, int consumableManagementRole, int deviceManagementRole) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.isEnabled = isEnabled;
        this.consumableManagementRole = consumableManagementRole;
        this.deviceManagementRole = deviceManagementRole;
    }

    public User(int id, String firstName, String lastName, String username, String password, int isEnabled, int consumableManagementRole, int deviceManagementRole) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isEnabled = isEnabled;
        this.consumableManagementRole = consumableManagementRole;
        this.deviceManagementRole = deviceManagementRole;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getConsumableManagementRole() {
        return consumableManagementRole;
    }

    public void setConsumableManagementRole(int consumableManagementRole) {
        this.consumableManagementRole = consumableManagementRole;
    }

    public int getDeviceManagementRole() {
        return deviceManagementRole;
    }

    public void setDeviceManagementRole(int deviceManagementRole) {
        this.deviceManagementRole = deviceManagementRole;
    }
}
