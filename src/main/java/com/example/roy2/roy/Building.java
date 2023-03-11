package com.example.roy2.roy;

import org.engine.objects.ShapeObject;

public abstract class Building extends ShapeObject {
    protected int foodCount;
    public Building(String name, int id) {
        super(name, id);
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }
}
