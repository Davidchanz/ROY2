package com.example.roy2.roy;

import org.UnityMath.Vector2;
import org.engine.Scene;
import org.engine.maths.Vector3f;
import org.engine.objects.GameObject;
import org.engine.objects.ShapeObject;
import org.engine.shapes.Rectangle;
import org.engine.utils.Color;

import java.util.ArrayList;
import java.util.Random;

public class Bug extends ShapeObject {
    private int identity;
    private Vector2 dir;
    private int type;
    private AntHeap antHeap;
    private int rangeToHome;
    private int rangeToFood;
    private final int foodCapacity;
    private int foodCount;
    private int freeWayCount;
    private boolean isFoundFood;
    private final Random rnd;
    private boolean lost;
    private final int smellRadius;
    private int steps;
    private GameObject shape;
    private boolean isAlive;
    public Bug(int id, AntHeap antHeap){
        super("Bug", 4);
        this.shape = new Rectangle(0.5f, new Vector3f(Scene.toGLDimension(antHeap.getPosition())), antHeap.getColor());
        this.add(this.shape);
        this.identity = id;
        this.type = 0;
        switch (new Random().nextInt(4)){
            case 0 ->{//r
                this.dir = new Vector2(1, 0);
            }
            case 1 ->{//l
                this.dir = new Vector2(-1, 0);
            }
            case 2 ->{//u
                this.dir = new Vector2(0, -1);
            }
            case 3 ->{//d
                this.dir = new Vector2(0, 1);
            }
        }
        this.rnd = new Random();
        this.antHeap = antHeap;
        this.foodCapacity = 5;
        this.rangeToFood = 0;
        this.rangeToHome = 0;
        this.findNewDir();
        this.freeWayCount = 0;
        this.lost = true;
        this.smellRadius = 10;
        this.steps = 0;
        this.isAlive = true;
    }

    public int getSmellRadius() {
        return smellRadius;
    }

    public void findNewDir(){
        this.dir.rotateDeg(rnd.nextInt(90)-45);
    }

    public void move() {
        if(!this.isAlive)//TODO
            return;
        this.smell();
        if (this.steps % 10 == 0) {
            this.mark();
        }
        super.move(new Vector3f(dir.x, dir.y, 0));
        this.steps++;
        switch (antHeap.getMoveAction().apply(this)){
            case 0 ->{//home
                if(this.type == 1) {
                    this.shape.setColor(this.antHeap.getColor());
                    this.freeWayCount = 1;
                    this.dir.rotate90(1).rotate90(1);
                    this.type = 0;
                    this.antHeap.addFood(this.foodCount);
                    this.foodCount = 0;
                    this.isFoundFood = false;
                    this.lost = false;
                    this.steps = 0;
                }
            }
            case 1 ->{//food
                if(this.type == 0) {
                    this.shape.setColor(Color.GREEN);
                    this.freeWayCount = 1;
                    this.isFoundFood = true;
                    this.dir.rotate90(1).rotate90(1);
                    this.type = 1;
                    this.lost = false;
                    this.steps = 0;
                }
            }
            case -2 ->{
                this.dir.rotate90(1).rotate90(1);
            }

            case -1 -> {
                if(this.lost) {
                    if (this.freeWayCount % 20 == 0)
                        this.findNewDir();
                }else {
                    if(this.freeWayCount % 50 == 0){
                        this.lost = true;
                        this.freeWayCount = 1;
                    }
                }
                this.freeWayCount++;
            }

            case 2 ->{
                if(this.type == 0) {
                    //this.shape.setColor(Color.MAGENTA);
                    this.freeWayCount = 1;
                    this.isFoundFood = true;
                    this.dir.rotate90(1).rotate90(1);
                    this.type = 1;
                    this.lost = false;
                    this.steps = 0;
                }
            }
        }
        if(!this.antHeap.isAlive()){
            this.death();
        }
    }

    public void mark(){
        Track track = null;
        switch (type){
            case 0 ->{//home
                track = new HomeTrack(this.antHeap.getIdentity(), this.rangeToHome, Scene.toGLDimension(this.getPosition()));
                this.rangeToHome++;
            }
            case 1 ->{//snack
                track = new SnackTrack(this.antHeap.getIdentity(), this.rangeToFood, Scene.toGLDimension(this.getPosition()));
                this.rangeToFood++;
            }
        }
        this.antHeap.getMarkAction().accept(track);
    }

    public void smell(){
        ArrayList<Track> tracks = antHeap.getSmellAction().apply(this);
        if(tracks == null)
            return;
        Track minTrack = null;
        for(var track: tracks){
            if (minTrack == null)
                minTrack = track;
            else if (minTrack.getRange() > track.getRange())
                minTrack = track;
        }
        if (minTrack == null)
            return;
        if(minTrack.getType() != this.antHeap.getIdentity())
            return;
        switch (this.type){
            case 1 ->{//home
                if(this.lost || minTrack.getRange() < this.rangeToHome) {
                    this.freeWayCount = 1;
                    this.lost = false;
                    this.dir = new Vector2(minTrack.getPosition().getX(), minTrack.getPosition().getY()).sub(new Vector2(this.getPosition().getX(), this.getPosition().getY())).nor();
                    this.rangeToHome = minTrack.getRange();
                }
            }
            case 0 ->{//snack
                if(this.lost || minTrack.getRange() < this.rangeToFood) {
                    this.freeWayCount = 1;
                    this.lost = false;
                    this.dir = new Vector2(minTrack.getPosition().getX(), minTrack.getPosition().getY()).sub(new Vector2(this.getPosition().getX(), this.getPosition().getY())).nor();
                    this.rangeToFood = minTrack.getRange();
                }
            }
        }
        //minTrack.decreaseTimeLine();
    }

    public int getFoodCapacity() {
        return foodCapacity;
    }

    public boolean isFoundFood() {
        return isFoundFood;
    }

    public void setFoundFood(boolean foundFood) {
        isFoundFood = foundFood;
    }

    public int getType() {
        return type;
    }

    public AntHeap getAntHeap() {
        return antHeap;
    }

    public void setAntHeap(AntHeap antHeap) {
        this.antHeap = antHeap;
        this.antHeap.addBug(this);
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public void still(AntHeap antHeap) {
        this.foodCount = antHeap.stilledFood(this.foodCapacity/2);
    }

    public void death() {
        this.isAlive = false;
        this.antHeap.hireBug(this);
        this.antHeap.getDeathAction().accept(this);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void eatFood(Food food) {
        this.foodCount = food.decreaseFoodCount(this.foodCapacity);
    }
}
