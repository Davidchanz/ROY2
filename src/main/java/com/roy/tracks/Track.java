package com.roy.tracks;

import com.roy.utils.Constants;
import org.engine.maths.Vector3f;
import org.engine.objects.GameObject;
import org.engine.objects.ShapeObject;
import org.engine.shapes.Rectangle;
import org.engine.utils.Color;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Track extends ShapeObject {
    protected int type;
    protected GameObject shape;
    private int timeLine;
    private boolean isActive;
    private int range;
    private final int liveCapacity;
    private static final ArrayList<Track> tracks = new ArrayList<>();
    private static final Timer lifeTimer = new Timer(Constants.BUG_SPEED, Track::timerTick);
    static {
        lifeTimer.start();
    }

    private static void timerTick(ActionEvent actionEvent) {
        Arrays.stream(tracks.toArray(new Track[0])).toList().forEach(Track::decreaseTimeLine);
    }

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
        this.setVisible(false);
        tracks.add(this);
    }

    public void decreaseTimeLine(){
        this.timeLine--;
        if(this.timeLine <= 0){
            this.isActive = false;
            this.remove();
            tracks.remove(this);
        }
    }

    public void increaseTimeLine(){
       /* this.timeLine++;
        if(this.timeLine >= liveCapacity){
            this.timeLine = liveCapacity;
        }*/
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
