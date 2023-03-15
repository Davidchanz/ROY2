package com.roy.tracks;

import com.roy.utils.Constants;
import org.UnityMath.Vector2;
import org.engine.maths.Vector3f;
import org.engine.objects.GameObject;
import org.engine.objects.ShapeObject;
import org.engine.shapes.Rectangle;
import org.engine.utils.Color;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Track extends Rectangle {
    protected int type;
    //protected GameObject shape;
    private int timeLine;
    private boolean isActive;
    private int range;
    private final int liveCapacity = Constants.TRACK_LIFE_CAPACITY;
    private static final List<Track> tracks = Collections.synchronizedList(new ArrayList<>());
    private static final Timer lifeTimer = new Timer(Constants.BUG_SPEED, Track::timerTick);
    static {
        lifeTimer.start();
    }

    private static void timerTick(ActionEvent actionEvent) {
        Arrays.stream(tracks.toArray(new Track[0])).toList().forEach(Track::decreaseTimeLine);
    }

    public Track(int id, int range, Vector3f pos, Color color){
        super(1f, new Vector3f(pos), color);
        //this.setId(); = id;
        //super("Track", id);
        this.range = range;
        //this.shape = new Rectangle(1, new Vector3f(pos), Color.GREEN);
        //this.add(shape);
        this.timeLine = liveCapacity;
        this.isActive = true;
        //this.setVisible(false);
        tracks.add(this);
    }

    public void decreaseTimeLine(){
        //this.timeLine--;
        if(this.timeLine <= 0){
            tracks.remove(this);
            this.isActive = false;
            /*if(this.getParent() != null) */this.getParent().remove(this);//remove();
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
