package com.mealsoffwheels.dronedelivery.activities;


public class Food {
    private int position;
    private String name;
    private boolean delete;
    private float price;

    public Food(int position, String name, float price) {
        this.position = position;
        this.name = name;
        delete = false;
        this.price = price;
    }

    public int getPosition() {
        return position;
    }

    public float getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
