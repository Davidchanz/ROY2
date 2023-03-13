package com.roy.buildings.heap;

import com.roy.bugs.AntWorker;
import com.roy.bugs.Bug;
import com.roy.buildings.Building;
import com.roy.tracks.Track;
import com.roy.utils.Constants;
import org.engine.maths.Vector3f;
import org.engine.objects.GameObject;
import org.engine.objects.ShapeObject;
import org.engine.shapes.Rectangle;
import org.engine.utils.Color;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class AntHeap extends Building implements BugHome{
    private Consumer<Bug> createBug;
    private Consumer<Track> markAction;
    private Function<Bug, ArrayList<Track>> smellAction;
    private Function<Bug, Integer> moveAction;
    private Consumer<ShapeObject> deathAction;
    private int identity;
    private int bugCount;
    private GameObject shape;
    private boolean isAlive;
    //private ArrayList<Bug> bugs;
    private Color color;

    public AntHeap(int id, int foodCount, Vector3f pos, Color color, Consumer<Bug> createBug, Consumer<Track> markAction, Function<Bug, ArrayList<Track>> smellAction, Function<Bug, Integer> moveAction, Consumer<ShapeObject> death) {
        super("AntHeap", Constants.ANT_HEAP_ID);
        this.identity = id;
        this.createBug = createBug;
        this.markAction = markAction;
        this.smellAction = smellAction;
        this.moveAction = moveAction;
        this.deathAction = death;
        this.shape = new Rectangle(foodCount, new Vector3f(pos), color, "src/main/resources/com/roy/ant_heap.png");
        this.foodCount = foodCount * Constants.FOOD_VALUE;
        this.add(this.shape);
        this.bugCount = 0;
        this.isAlive = true;
        //this.bugs = new ArrayList<>();
        this.color = color;
    }

    @Override
    public Consumer<Track> getMarkAction() {
        return markAction;
    }

    @Override
    public Function<Bug, ArrayList<Track>> getSmellAction() {
        return smellAction;
    }

    @Override
    public Function<Bug, Integer> getMoveAction() {
        return moveAction;
    }

    @Override
    public Color getColor(){
        return this.color;
    }

    @Override
    public Consumer<ShapeObject> getDeathAction() {
        return deathAction;
    }

    public Bug addWorker(){
        AntWorker bug = new AntWorker(bugCount++, this);
        this.createBug.accept(bug);
        //this.bugs.add(bug);
        return bug;
    }

    public void addFood(int foodCount){
        /*this.foodCount += foodCount;//todo
        if(this.foodCount % Constants.FOOD_VALUE == 0) {
            this.resize();
            this.addBug();
        }*/
    }

    public int stilledFood(int count){
        int realStilled = count;
        /*if(this.foodCount >= count)
            this.foodCount -= count;
        else {
            realStilled = this.foodCount;
            this.foodCount -= this.foodCount;
        }
        if(this.foodCount % Constants.FOOD_VALUE == 0) {
            this.resize();
        }
        if(this.foodCount <= Constants.FOOD_VALUE*10) {
            this.isAlive = false;
            this.death();
        }*/
        return realStilled;
    }

    private void resize() {
        this.shape.setWidth(this.foodCount / (float)Constants.FOOD_VALUE);
        this.shape.setHeight(this.foodCount / (float)Constants.FOOD_VALUE);
        this.shape.resize();
    }

    public Bug addWorker(AntWorker bug){
        bug.setIdentity(bugCount++);
        //this.bugs.add(bug);
        return bug;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void death(){
        this.deathAction.accept(this);
    }

    public int getIdentity() {
        return identity;
    }

    @Override
    public void hireBug(Bug bug) {
        //this.bugs.remove(bug);
    }

    public int getBugCount() {
        return bugCount;
    }

    public int getFoodCount() {
        return foodCount;
    }
}
