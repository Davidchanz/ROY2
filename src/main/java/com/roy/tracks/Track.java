package com.roy.tracks;

import com.roy.utils.Constants;
import org.engine.maths.Vector3f;
import org.engine.objects.GameObject;
import org.engine.objects.ShapeObject;
import org.engine.shapes.Rectangle;
import org.engine.utils.Color;

public abstract class Track extends ShapeObject {
    protected int type;
    protected GameObject shape;
    private int timeLine;
    private boolean isActive;
    private int range;
    private final int liveCapacity;

    public Track(int id, int range, Vector3f pos){
        //super(1f, new Vector3f(pos), Color.GREEN);
        //this.id = id;
        super("Track", id);
        this.range = range;
        this.shape = new Rectangle(1, new Vector3f(pos), Color.GREEN);
        this.add(shape);
        this.liveCapacity = Constants.TRACK_LIFE_CAPACITY;
        this.timeLine = liveCapacity;
        this.isActive = true;
    }

    public void decreaseTimeLine(){
        this.timeLine--;
        if(this.timeLine <= 0){
            this.isActive = false;
        }
    }

    public void increaseTimeLine(){
        this.timeLine++;
        if(this.timeLine >= liveCapacity){
            this.timeLine = liveCapacity;
        }
    }

    public int getType() {
        return type;
    }

    public int getRange() {
        return range;
    }


    public boolean isActive() {
        return isActive;
    }

}
