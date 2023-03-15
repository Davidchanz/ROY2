package com.roy.bugs;

import com.roy.buildings.heap.BugHome;
import com.roy.tracks.Track;
import com.roy.utils.Constants;
import org.UnityMath.Vector2;
import org.engine.Scene;
import org.engine.maths.Vector3f;
import org.engine.objects.GameObject;
import org.engine.objects.ShapeObject;
import org.engine.shapes.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public abstract class Bug extends ShapeObject implements NPC{
    private int identity;
    protected Vector2 dir;

    protected int target;
    private BugHome bugHome;
    protected int rangeToHome;
    protected int freeWayCount;
    private final Random rnd;
    protected boolean lost;
    private final int smellRadius;
    protected int steps;
    private GameObject shape;
    private boolean isAlive;
    private float size;
    private Object memory;
    public Bug(int id, BugHome bugHome, float size, String texturePath){
        super("Bug", Constants.BUG_ID);
        this.size = size;
        this.rnd = new Random();
        this.shape = new Rectangle(size, new Vector3f(Scene.toGLDimension(bugHome.getPosition())), bugHome.getColor(), texturePath);
        this.add(this.shape);
        this.identity = id;
        this.bugHome = bugHome;
        this.smellRadius = Constants.BUG_SMELL_RADIUS;
        this.rangeToHome = 0;
        this.freeWayCount = 0;
        this.steps = 0;
        this.lost = true;
        this.isAlive = true;
        this.dir = new Vector2();
        switch (new Random().nextInt(4)){
            case 0 ->{//r
                this.dir.set(1,0);
                this.rotate(new Vector3f(0,0, 0), this.getPosition());
            }
            case 1 ->{//l
                this.dir.set(-1,0);
                this.rotate(new Vector3f(0,0, 180), this.getPosition());
            }
            case 2 ->{//u
                this.dir.set(0,-1);
                this.rotate(new Vector3f(0,0, -90), this.getPosition());
            }
            case 3 ->{//d
                this.dir.set(0,1);
                this.rotate(new Vector3f(0,0, 90), this.getPosition());
            }
        }
        this.findNewDir();
        this.setBuffered(false);
    }

    @Override
    public void move() {
        this.preMove();
        super.move(new Vector3f(dir.x, dir.y, 0));
        this.rotate();
        this.steps++;
        var whereI = this.bugHome.getMoveAction().apply(this);
        switch (whereI){
            case Constants.ANT_HEAP_ID ->{//found own home
                this.friendHome();
                if (this.steps % Constants.BUG_MARK_FREQUENCY == 0) {
                    this.mark();
                }
            }
            case Constants.FOOD_ID ->{//found food
                this.food();
                if (this.steps % Constants.BUG_MARK_FREQUENCY == 0) {
                    this.mark();
                }
            }
            case Constants.BORDER_RIGHT, Constants.BORDER_UP, Constants.BORDER_LEFT, Constants.BORDER_DOWN ->{//found border
                this.border(whereI);
            }

            case Constants.FREEWAY -> {//simple go
                this.freeWay();
                if (this.steps % Constants.BUG_MARK_FREQUENCY == 0) {
                    this.mark();
                }
            }

            case Constants.ALIEN_HOME ->{//found alien home
                this.alienHome();
                if (this.steps % Constants.BUG_MARK_FREQUENCY == 0) {
                    this.mark();
                }
            }
        }
        this.postMove();
    }

    @Override
    public Track smell() {
        ArrayList<Track> tracks = this.bugHome.getSmellAction().apply(this);
        if(tracks == null)
            return null;
        Track minTrack = null;
        for(var track: tracks){
            if (minTrack == null)
                minTrack = track;
            else if (minTrack.getRange() >= track.getRange())
                minTrack = track;
        }
        return minTrack;
    }

    @Override
    public void freeWay() {
        if(this.lost) {
            if (this.freeWayCount % Constants.BUG_TURN_STEPS == 0)
                this.findNewDir();
        }else {
            if(this.freeWayCount % Constants.BUG_LOST_STEPS == 0){
                this.lost = true;
                this.freeWayCount = 1;
            }
        }
        this.freeWayCount++;
    }

    @Override
    public void postMove() {
        if(this.lost && this.freeWayCount == Constants.BUG_LOST_STEPS*50){
            this.death();
        }
    }

    @Override
    public void preMove() {
        this.smell();
    }

    @Override
    public void border(int whereBorder) {
        if(this.lost) {
            this.move(new Vector3f(this.dir.x, this.dir.y, 0).neg());
            this.move(new Vector3f(this.dir.x, this.dir.y, 0).neg());
            this.move(new Vector3f(this.dir.x, this.dir.y, 0).neg());
            //this.turnFromBorder(whereBorder);
            this.turnAround();
            //this.findNewDir();
        }else {
            System.out.println("FUCK");
            //this.turnAround();
            this.move(new Vector3f(this.dir.x, this.dir.y, 0).neg());//TODO
            this.move(new Vector3f(this.dir.x, this.dir.y, 0).neg());
            this.move(new Vector3f(this.dir.x, this.dir.y, 0).neg());
            //this.turnFromBorder(whereBorder);
        }
        //this.mark();
    }

    private void turnFromBorder(int whereBorder) {
        var angle = this.dir.angleDeg();
        switch (whereBorder){
            case Constants.BORDER_RIGHT -> {
                this.move(new Vector3f(-1, 0, 0));
                if(angle == 0 || angle == 90)
                    this.turnAround();
                else if(angle < 90)
                    this.dir.rotateDeg(180 - angle);
                else
                    this.dir.rotateDeg(180 - angle + 360);
            }
            case Constants.BORDER_UP -> {
                this.move(new Vector3f(0, -1, 0));
                if(angle == 90  || angle == 180)
                    this.turnAround();
                else if(angle < 90)
                    this.dir.rotateDeg(180 + angle + angle);
                else
                    this.dir.rotateDeg(180 - angle + 180);
            }
            case Constants.BORDER_LEFT -> {
                this.move(new Vector3f(1, 0, 0));
                if(angle == 180  || angle == 270)
                    this.turnAround();
                else if(angle < 180)
                    this.dir.rotateDeg(-(angle - 180));
                else
                    this.dir.rotateDeg(180 - angle + 360);
            }
            case Constants.BORDER_DOWN -> {
                this.move(new Vector3f(0, 1, 0));
                if(angle == 270 || angle == 0)
                    this.turnAround();
                else if(angle < 270)
                    this.dir.rotateDeg(360 - angle + 180);
                else
                    this.dir.rotateDeg(360 - angle);
            }
        }
        this.rotate();
    }

    public int getSmellRadius() {
        return smellRadius;
    }

    public void findNewDir(){
        var degree = rnd.nextInt(Constants.BUG_TURN_DEGREE)-Constants.BUG_TURN_DEGREE/2f;
        this.dir.rotateDeg(degree);
    }

    public void rotate(){
        this.rotate(new Vector3f(0,0,-this.dir.angleDeg()), this.getPosition());
    }

    public void turnAround(){
        this.dir.rotate90(1).rotate90(1);
        this.rotate();
    }

    public void death() {
        this.setAlive(false);
        this.bugHome.hireBug(this);
        this.bugHome.getDeathAction().accept(this);
    }

    public int getTarget() {
        return target;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public BugHome getBugHome() {
        return bugHome;
    }

    public Object getMemory() {
        return memory;
    }

    public void setMemory(Object memory) {
        this.memory = memory;
    }
}
