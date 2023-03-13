package com.roy.bugs;

import com.roy.buildings.food.Food;
import com.roy.buildings.heap.AntHeap;
import com.roy.tracks.HomeTrack;
import com.roy.tracks.FoodTrack;
import com.roy.tracks.Track;
import com.roy.utils.Constants;
import org.UnityMath.Vector2;
import org.engine.Scene;

public class AntWorker extends Bug {
    private int rangeToFood;
    private final int foodCapacity;
    private int foodCount;
    private boolean isFoundFood;
    private AntHeap antHeap;
    public AntWorker(int id, AntHeap antHeap){
        super(id, antHeap, Constants.ANT_WORKER_SIZE, Constants.ANT_WORKER);
        this.antHeap = antHeap;
        this.foodCapacity = Constants.ANT_WORKER_FOOD_CAPACITY;
        this.rangeToFood = 0;
        this.isFoundFood = false;
        this.foodCount = 0;
        this.target = Constants.FOOD_ID;
    }

    @Override
    public void border() {
        super.border();
    }

    @Override
    public void preMove() {
        super.preMove();
    }

    @Override
    public void postMove() {
        super.postMove();
    }

    @Override
    public void freeWay() {
        super.freeWay();
    }

    @Override
    public void friendHome() {
        if(this.target == Constants.ANT_HEAP_ID) {//have food
            //this.shape.setColor(this.antHeap.getColor());
            this.deliveryFood();
            this.freeWayCount = 1;
            this.turnAround();
            this.isFoundFood = false;
            this.lost = false;
            this.steps = 0;
            this.target = Constants.FOOD_ID;
        }
    }

    @Override
    public void friendBud() {

    }

    @Override
    public void alienHome() {
        if(!this.isFoundFood) {//have not food, still food
            this.still((AntHeap) this.getMemory());
            this.freeWayCount = 1;
            this.isFoundFood = true;
            this.turnAround();
            this.target = 1;
            this.lost = false;
            this.steps = 0;
        }
    }

    @Override
    public void alienBug() {

    }

    @Override
    public void food() {
        if(this.target == Constants.FOOD_ID) {//have not food
            //this.shape.setColor(Color.GREEN);
            this.eatFood((Food)this.getMemory());
            this.freeWayCount = 1;
            this.isFoundFood = true;
            this.turnAround();
            this.lost = false;
            this.steps = 0;
            this.target = Constants.ANT_HEAP_ID;
        }
    }

    @Override
    public void mark(){
        Track track = null;
        switch (this.target){
            case Constants.FOOD_ID ->{//looking for food
                track = new HomeTrack(this.antHeap.getIdentity(), this.rangeToHome, Scene.toGLDimension(this.getPosition()));
                this.rangeToHome++;
            }
            case Constants.ANT_HEAP_ID ->{//looking for home
                track = new FoodTrack(this.antHeap.getIdentity(), this.rangeToFood, Scene.toGLDimension(this.getPosition()));
                this.rangeToFood++;
            }
        }
        this.antHeap.getMarkAction().accept(track);
    }

    @Override
    public Track smell(){
        Track minTrack = super.smell();
        if (minTrack == null)
            return null;
        if(minTrack.getType() != this.antHeap.getIdentity())
            return null;
        switch (this.target){
            case Constants.ANT_HEAP_ID ->{//looking for shortest way to home
                if(this.lost || minTrack.getRange() < this.rangeToHome) {
                    this.freeWayCount = 1;
                    this.lost = false;
                    this.dir = new Vector2(minTrack.getPosition().getX(), minTrack.getPosition().getY()).sub(new Vector2(this.getPosition().getX(), this.getPosition().getY())).nor();
                    this.rotate();
                    this.rangeToHome = minTrack.getRange();
                }
            }
            case Constants.FOOD_ID ->{//looking for shortest way food
                if(this.lost || minTrack.getRange() < this.rangeToFood) {
                    this.freeWayCount = 1;
                    this.lost = false;
                    this.dir = new Vector2(minTrack.getPosition().getX(), minTrack.getPosition().getY()).sub(new Vector2(this.getPosition().getX(), this.getPosition().getY())).nor();
                    this.rotate();
                    this.rangeToFood = minTrack.getRange();
                }
            }
        }
        minTrack.increaseTimeLine();
        return minTrack;
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

    public AntHeap getAntHeap() {
        return antHeap;
    }

    public void setAntHeap(AntHeap antHeap) {
        this.antHeap = antHeap;
        this.antHeap.addWorker(this);
    }

    public void still(AntHeap antHeap) {
        this.foodCount = antHeap.stilledFood(this.foodCapacity/2);
    }

    public void deliveryFood(){
        this.antHeap.addFood(this.foodCount);
        this.foodCount = 0;
    }

    public void eatFood(Food food) {
        this.foodCount = food.decreaseFoodCount(this.foodCapacity);
    }
}
