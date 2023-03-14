package com.map.borders;

import com.roy.utils.Constants;
import org.engine.objects.GameObject;
import org.engine.objects.ShapeObject;

public abstract class Border extends ShapeObject {
    protected GameObject shape;
    public Border(String name, int id){
        super(name, id);
        this.setSpriteSize(Constants.STONE_SIZE);
    }
}
