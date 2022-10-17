package com.example.storagemanager;

public class Consumable {

    private int id;
    private String name;
    private String manufacturer;
    private String model;
    private int count;
    private int consumableTypeID;
    private int locationID;

    public Consumable(int id, String name, String manufacturer, String model, int count, int consumableTypeID, int locationID) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
        this.count = count;
        this.consumableTypeID = consumableTypeID;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getConsumableTypeID() {
        return consumableTypeID;
    }

    public void setConsumableTypeID(int consumableTypeID) {
        this.consumableTypeID = consumableTypeID;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }
}
