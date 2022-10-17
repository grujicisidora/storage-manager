package com.example.storagemanager;

public class Device {

    private int id;
    private String name;
    private String manufacturer;
    private String model;
    private String serialNumber;
    private String inventoryNumber;
    private int deviceTypeID;
    private int locationID;

    public Device(int id, String name, String manufacturer, String model, String serialNumber, String inventoryNumber, int deviceTypeID, int locationID) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
        this.serialNumber = serialNumber;
        this.inventoryNumber = inventoryNumber;
        this.deviceTypeID = deviceTypeID;
        this.locationID = locationID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public int getDeviceTypeID() {
        return deviceTypeID;
    }

    public void setDeviceTypeID(int deviceTypeID) {
        this.deviceTypeID = deviceTypeID;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }
}
