package ru.oksei.coworking.models;

import java.io.Serializable;

public class Workspace implements Serializable {
    private long id;
    private String name;
    private String category;
    private int capacity;
    private double pricePerHour;
    private String description;
    private String amenities;
    private boolean isAvailable;

    public Workspace(long id, String name, String category, int capacity, double pricePerHour, String description, String amenities, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.capacity = capacity;
        this.pricePerHour = pricePerHour;
        this.description = description;
        this.amenities = amenities;
        this.isAvailable = isAvailable;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getCapacity() { return capacity; }
    public double getPricePerHour() { return pricePerHour; }
    public String getDescription() { return description; }
    public String getAmenities() { return amenities; }
    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) { isAvailable = available; }
}
