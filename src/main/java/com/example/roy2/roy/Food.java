package com.example.roy2.roy;

import org.engine.maths.Vector3f;
import org.engine.objects.GameObject;
import org.engine.objects.ShapeObject;
import org.engine.shapes.Rectangle;
import org.engine.utils.Color;

public class Food extends Building{
    private boolean isAlive;
    private GameObject shape;
    public Food(int foodCount, Vector3f pos, Color color) {
        super("Food", 1);
        this.shape = new Rectangle(foodCount, new Vector3f(pos), color);
        this.add(this.shape);
        this.foodCount = 50*foodCount;
        this.isAlive = true;
    }

    public int decreaseFoodCount(int count){
        int realEaten = count;
        count = 0;//todo
        if(this.foodCount >= count)
            this.foodCount -= count;
        else {
            realEaten = this.foodCount;
            this.foodCount -= this.foodCount;
        }
        if(this.foodCount % 50 == 0) {
            this.resize();
        }
        if(this.foodCount <= 10){
            this.isAlive = false;
        }
        return realEaten;
    }

    private void resize() {
        /*this.shape.setWidth(this.foodCount / 50f);
        this.shape.setHeight(this.foodCount / 50f);
        this.shape.resize();*/
    }

    public boolean isAlive() {
        return isAlive;
    }
}
