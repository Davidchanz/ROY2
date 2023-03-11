package com.example.roy2.roy;

import org.engine.maths.Vector3f;
import org.engine.objects.GameObject;
import org.engine.objects.ShapeObject;
import org.engine.shapes.Rectangle;
import org.engine.utils.Color;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class AntHeap extends Building{
    private Consumer<Bug> createBug;
    private Consumer<Track> markAction;
    private Function<Bug, ArrayList<Track>> smellAction;
    private Function<Bug, Integer> moveAction;
    private Consumer<ShapeObject> deathAction;
    private int identity;
    private int bugCount;
    private GameObject shape;
    private boolean isAlive;
    private ArrayList<Bug> bugs;

    public AntHeap(int id, int foodCount, Vector3f pos, Color color, Consumer<Bug> createBug, Consumer<Track> markAction, Function<Bug, ArrayList<Track>> smellAction, Function<Bug, Integer> moveAction, Consumer<ShapeObject> death) {
        super("AntHeap", 0);
        this.identity = id;
        this.createBug = createBug;
        this.markAction = markAction;
        this.smellAction = smellAction;
        this.moveAction = moveAction;
        this.deathAction = death;
        this.shape = new Rectangle(foodCount, new Vector3f(pos), color);
        this.foodCount = foodCount * 50;
        this.add(this.shape);
        this.bugCount = 0;
        this.isAlive = true;
        this.bugs = new ArrayList<>();
    }

    public Consumer<Track> getMarkAction() {
        return markAction;
    }

    public Function<Bug, ArrayList<Track>> getSmellAction() {
        return smellAction;
    }

    public Function<Bug, Integer> getMoveAction() {
        return moveAction;
    }

    public Color getColor(){
        return this.body.get(0).getColor();
    }

    public Consumer<ShapeObject> getDeathAction() {
        return deathAction;
    }

    public Bug addBug(){
        Bug bug = new Bug(bugCount++, this);
        this.createBug.accept(bug);
        this.bugs.add(bug);
        return bug;
    }

    public void addFood(int foodCount){
        /*this.foodCount += foodCount;//todo
        if(this.foodCount % 50 == 0) {
            this.resize();
            for(int i = 0; i < 5; i++)
                this.addBug();
        }
        if(this.foodCount % 10 == 0) {
            this.addBug();
        }*/
    }

    public int stilledFood(int count){
        int realStilled = count;
        /*count = 0;//todo
        if(this.foodCount >= count)
            this.foodCount -= count;
        else {
            realStilled = this.foodCount;
            this.foodCount -= this.foodCount;
        }
        if(this.foodCount % 50 == 0) {
            this.resize();
        }
        if(this.foodCount <= 500) {
            this.isAlive = false;
            this.death();
        }*/
        return realStilled;
    }

    private void resize() {
       /* this.shape.setWidth(this.foodCount / 50f);
        this.shape.setHeight(this.foodCount / 50f);
        this.shape.resize();*/
    }

    public Bug addBug(Bug bug){
        bug.setIdentity(bugCount++);
        this.bugs.add(bug);
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

    public void hireBug(Bug bug) {
        this.bugs.remove(bug);
    }

    public int getBugCount() {
        return bugs.size();
    }

    public int getFoodCount() {
        return foodCount;
    }
}
