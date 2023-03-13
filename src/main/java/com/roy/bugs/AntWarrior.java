package com.roy.bugs;

import com.roy.buildings.heap.AntHeap;
import com.roy.tracks.HomeTrack;
import com.roy.tracks.FoodTrack;
import com.roy.tracks.Track;
import org.UnityMath.Vector2;
import org.engine.Scene;

public class AntWarrior extends Bug{
    private AntHeap antHeap;
    private int damage;
    public AntWarrior(int id, AntHeap bugHome, float size, String texturePath) {
        super(id, bugHome, size, texturePath);
        this.antHeap = bugHome;
        this.damage = 10;
    }

    @Override
    public void mark() {
        /*Track track = null;
        switch (this.target){
            case 0 ->{//looking for food
                track = new HomeTrack(this.antHeap.getIdentity(), this.rangeToHome, Scene.toGLDimension(this.getPosition()));
                this.rangeToHome++;
            }
            case 1 ->{//looking for home
                track = new FoodTrack(this.antHeap.getIdentity(), this.rangeToFood, Scene.toGLDimension(this.getPosition()));
                this.rangeToFood++;
            }
        }
        this.antHeap.getMarkAction().accept(track);*/
    }

    @Override
    public Track smell() {
        Track minTrack = super.smell();
        if (minTrack == null)
            return null;
        if(minTrack.getType() != this.antHeap.getIdentity())
            return null;
        /*switch (this.target){
            case 1 ->{//looking for shortest way to home
                if(this.lost || minTrack.getRange() < this.rangeToHome) {
                    this.freeWayCount = 1;
                    this.lost = false;
                    this.dir = new Vector2(minTrack.getPosition().getX(), minTrack.getPosition().getY()).sub(new Vector2(this.getPosition().getX(), this.getPosition().getY())).nor();
                    this.rotate();
                    this.rangeToHome = minTrack.getRange();
                }
            }
            case 0 ->{//looking for shortest way food
                if(this.lost || minTrack.getRange() < this.rangeToFood) {
                    this.freeWayCount = 1;
                    this.lost = false;
                    this.dir = new Vector2(minTrack.getPosition().getX(), minTrack.getPosition().getY()).sub(new Vector2(this.getPosition().getX(), this.getPosition().getY())).nor();
                    this.rotate();
                    this.rangeToFood = minTrack.getRange();
                }
            }
        }*/
        minTrack.increaseTimeLine();
        return minTrack;
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

    }

    @Override
    public void friendBud() {

    }

    @Override
    public void alienHome() {
        this.lost = false;

    }

    @Override
    public void alienBug() {

    }

    @Override
    public void food() {

    }

    private void damageAlienHeap(AntHeap heap){
        heap.stilledFood(10);
    }
}
