package com.roy.buildings.food;

import com.roy.buildings.Building;
import com.roy.utils.Constants;
import org.engine.maths.Vector3f;
import org.engine.objects.GameObject;
import org.engine.shapes.Rectangle;
import org.engine.utils.Color;

public class Food extends Building {
    private boolean isAlive;
    private GameObject shape;
    public Food(int foodCount, Vector3f pos, Color color) {
        super("Food", Constants.FOOD_ID);
        this.shape = new Rectangle(foodCount, new Vector3f(pos), color);
        this.add(this.shape);
        this.foodCount = Constants.FOOD_VALUE *foodCount;
        this.isAlive = true;
    }

    public int decreaseFoodCount(int count){
        int realEaten = count;
        if(this.foodCount >= count)
            this.foodCount -= count;
        else {
            realEaten = this.foodCount;
            this.foodCount -= this.foodCount;
        }
        if(this.foodCount % Constants.FOOD_VALUE == 0) {
            this.resize();
        }
        if(this.foodCount <= Constants.FOOD_VALUE*10){
            this.isAlive = false;
        }
        return realEaten;
    }

    private void resize() {
        this.shape.setWidth(this.foodCount / (float)Constants.FOOD_VALUE);
        this.shape.setHeight(this.foodCount / (float)Constants.FOOD_VALUE);
        this.shape.resize();
    }

    public boolean isAlive() {
        return isAlive;
    }
}
