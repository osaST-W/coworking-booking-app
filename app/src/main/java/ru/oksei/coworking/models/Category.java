package ru.oksei.coworking.models;

public class Category {
    private long id;
    private String name;
    private String description;

    public Category(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return name;
    }
}
