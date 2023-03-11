package com.example.roy2;

import com.example.roy2.roy.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.UnityMath.Vector2;
import org.engine.Scene;
import org.engine.events.GLMouseEvent;
import org.engine.listeners.SceneMouseButtonPressedListener;
import org.engine.maths.Vector3f;
import org.engine.objects.ShapeObject;
import org.engine.utils.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class Roy {
    private Scene scene;
    private AntHeap antHeapRed;
    private AntHeap antHeapBlue;
    private ArrayList<Bug> bugs;
    private TreeMap<Integer, Track[][]> trackFields;
    private TreeMap<Integer, Building[][]> buildingFields;
    private ArrayList<Building> buildings;
    private HelloController controller;
    private ArrayList<Bug> newBugs;
    public Roy(HelloController controller){
        this.controller = controller;
        /*Timeline timeline = new Timeline(new KeyFrame(Duration.millis(30), this::render));
        timeline.setCycleCount(Timeline.INDEFINITE);*/
        Timeline gameline = new Timeline(new KeyFrame(Duration.millis(5), this::game));
        gameline.setCycleCount(Timeline.INDEFINITE);

        int w = 1000;
        int h = 1000;

        this.scene = new Scene(w, h, Color.BLACK);
        //this.scene.enableDevelopMode();
        //this.scene.setBorderVisible(true);
        //this.scene.repaint();
        //this.canvas.setFitHeight(h);
        //this.canvas.setFitWidth(w);
        this.trackFields = new TreeMap<>();
        this.buildingFields = new TreeMap<>();

        this.buildings = new ArrayList<>();
        this.newBugs = new ArrayList<>();

        this.bugs = new ArrayList<>();

        this.antHeapRed = new AntHeap(2, 30, new Vector3f(-300, 0, 0), Color.RED,this::createBug, this::markAction, this::smellAction, this::moveAction, this::removeFromScene);
        this.antHeapBlue = new AntHeap(3, 30, new Vector3f(300, 0, 1), Color.CYAN,this::createBug, this::markAction, this::smellAction, this::moveAction, this::removeFromScene);

        Food food = new Food(50, new Vector3f(0,0,0), Color.GREEN);
        this.scene.add(food);
        this.buildings.add(food);

        this.buildings.add(this.antHeapRed);
        this.buildings.add(this.antHeapBlue);

        this.scene.add(this.antHeapRed);
        this.scene.add(this.antHeapBlue);

        while (this.newBugs.size() < 1000) {
            this.antHeapRed.addBug();
            this.antHeapBlue.addBug();
        }

        this.scene.addMouseEventListener(this::onCanvasPressed);

        //timeline.play();
        gameline.play();
    }

    private void addBuilding(Building building){
        var sceneCoord = new Vector3f(building.getPosition());
        sceneCoord = Scene.toGLDimension(sceneCoord);
        sceneCoord = Scene.toScreenDimension(sceneCoord);
        int size = building.getFoodCount()/50;
        //System.out.println(size);
        for(int x = (int)sceneCoord.getX()-size; x <= (int)sceneCoord.getX()+size; x++){
            for(int y = (int)sceneCoord.getY()-size; y <= (int)sceneCoord.getY()+size; y++){
                var buffer = this.buildingFields.get(building.id);
                if(buffer != null){
                    buffer[x][y] = building;
                }else {
                    this.buildingFields.put(building.id, new Building[Scene.WIDTH+1][Scene.HEIGHT+1]);
                    buffer = this.buildingFields.get(building.id);
                    for (var i : buffer) {
                        Arrays.fill(i, null); //TODO
                    }
                    buffer[x][y] = building;
                }
            }
        }
        /*
        * var buffer = this.buildingFields.get(building.id);
        if(buffer != null){
            buffer[(int)sceneCoord.getX()][(int)sceneCoord.getY()] = building;
        }else {
            this.buildingFields.put(building.id, new Building[Scene.WIDTH+1][Scene.HEIGHT+1]);
            buffer = this.buildingFields.get(building.id);
            for (var i : buffer) {
                Arrays.fill(i, null); //TODO
            }
            buffer[(int)sceneCoord.getX()][(int)sceneCoord.getY()] = building;
        }
        * */
    }

    public void createBug(Bug bug){
        this.scene.add(bug);
        this.newBugs.add(bug);
    }

    private void addTrack(Track track){
        var sceneCoord = new Vector3f(track.getPosition());
        if(this.scene.isOutSceneBorder(sceneCoord))
            return;
        sceneCoord = Scene.toGLDimension(sceneCoord);
        sceneCoord = Scene.toScreenDimension(sceneCoord);
        var buffer = this.trackFields.get(track.id);
        if (buffer != null) {
            buffer[(int) sceneCoord.getX()][(int) sceneCoord.getY()] = track;
        } else {
            this.trackFields.put(track.id, new Track[Scene.WIDTH + 1][Scene.HEIGHT + 1]);
            buffer = this.trackFields.get(track.id);
            for (var i : buffer) {
                Arrays.fill(i, null); //TODO
            }
            buffer[(int) sceneCoord.getX()][(int) sceneCoord.getY()] = track;
        }
    }

    public void markAction(Track track){
        this.addTrack(track);
        //this.tracks.add(track);
        //this.scene.add(track);
    }

    public void removeFromScene(ShapeObject object){
        //if(object instanceof Bug)
        //synchronized (bugs) {
        //this.bugs.remove(object);//TODO
        //}
        if(object instanceof Building)
            this.buildings.remove(object);
        this.scene.remove(object);
    }

    public Integer moveAction(Bug bug){
        if(this.scene.isOutSceneBorder(bug.getPosition())){
            return -2;
        }else {
            int id = 0;
            if(bug.getType() == 1){
                id = 0;
            }else if(bug.getType() == 0)
                id = 1;
            //for(float i = bug.getPosition().getX()-bug.getSmellRadius()/(float)Scene.WIDTH; i < bug.getPosition().getX()+bug.getSmellRadius()/(float)Scene.WIDTH; i+= 0.01)
                //for(float j = (int)bug.getPosition().getY()-bug.getSmellRadius()/(float)Scene.HEIGHT; j < bug.getPosition().getY()+bug.getSmellRadius()/(float)Scene.HEIGHT; j+=0.01){
                   // if(!this.scene.isOutSceneBorder(new Vector3f(i, j, 1))) {
                     //   System.out.println(new Vector3f(i, j, 1));
                        Building object = this.getBuilding(id, bug.getPosition());
                        if (object != null) {
                            if(object instanceof AntHeap) {
                                if(bug.getAntHeap() != (AntHeap) object && !bug.isFoundFood()){
                                    bug.still((AntHeap) object);
                                    //bug.setAntHeap((AntHeap) object);
                                    return 2;
                                }else
                                    return 0;
                            }
                            else if(object instanceof Food) {
                                if(!((Food)object).isAlive())
                                    this.removeFromScene(object);
                                else {
                                    if(!bug.isFoundFood())
                                        bug.eatFood((Food)object);
                                    return 1;
                                }
                            }
                        }
                  //  }
               // }
            //Building object = this.getBuilding(1, new Vector2(bug.getPosition().getX(), bug.getPosition().getY()));
            /*if (object != null) {
                if(object instanceof AntHeap) {
                    if(bug.getAntHeap() != (AntHeap) object && !bug.isFoundFood()){
                        bug.still((AntHeap) object);
                        //bug.setAntHeap((AntHeap) object);
                        return 2;
                    }else
                        return 0;
                }
                else if(object instanceof Food) {
                    if(!((Food)object).isAlive())
                        this.removeFromScene(object);
                    else {
                        if(!bug.isFoundFood())
                            bug.eatFood((Food)object);
                        return 1;
                    }
                }
            }*/
            return -1;
        }
    }

    public ArrayList<Track> smellAction(Bug bug){
        if(this.scene.isOutSceneBorder(bug.getPosition())){
            return null;
        }else {
            ArrayList<Track> foundedTracks = new ArrayList<>();
            int id = 0;
            if(bug.getType() == 1){
                id = 2;
            }else if(bug.getType() == 0)
                id = 3;
            Vector3f scenePos = new Vector3f(bug.getPosition());
            scenePos = Scene.toGLDimension(scenePos);
            //scenePos = Scene.toScreenDimension(scenePos);

            for(float i = scenePos.getX()-bug.getSmellRadius(); i < scenePos.getX()+bug.getSmellRadius(); i++)
                for(float j = scenePos.getY()-bug.getSmellRadius(); j < scenePos.getY()+bug.getSmellRadius(); j++){
                    Vector3f tmp = new Vector3f(i, j, 0f);
                    //System.out.println(tmp);
                    if(!this.scene.isOutSceneBorder(new Vector3f(tmp.getX()/Scene.WIDTH, tmp.getY()/Scene.HEIGHT, tmp.getZ()/Scene.WIDTH))) {
                        //System.out.println(tmp);
                        //this.scene.add(new HomeTrack(1, 1, tmp));
                        /*tmp = Scene.toGLDimension(tmp);
                        tmp = Scene.toScreenDimension(tmp);
                        System.out.println(tmp);*/
                        Track object = this.getTrack(id, tmp);
                        if (object != null) {
                            //System.out.println("TRACK!");
                            foundedTracks.add(object);
                        }
                    }
                }
            if(!foundedTracks.isEmpty())
                return foundedTracks;
            return null;
        }
    }

    public Building getBuilding(int id, Vector3f pos){
        var buffer = this.buildingFields.get(id);
        if(buffer != null){
            Vector3f scenePos = new Vector3f(pos);
            scenePos = Scene.toGLDimension(scenePos);
            scenePos = Scene.toScreenDimension(scenePos);
            Vector2 tmp = new Vector2(scenePos.getX(), scenePos.getY());
            /*if(tmp.epsilonEquals(new Vector2(0,0), 2))
                System.out.println("QQQQQQQQQQQ");*/

            //Scene.toSceneDimension(scenePos);
            if(buffer[(int)scenePos.getX()][(int)scenePos.getY()] != null)
                return buffer[(int)scenePos.getX()][(int)scenePos.getY()];
            return null;
        }else
            return null;
    }

    public Track getTrack(int id, Vector3f pos){
        var buffer = this.trackFields.get(id);
        if(buffer != null){
            Vector3f scenePos = new Vector3f(pos);
            //scenePos = Scene.toGLDimension(scenePos);
            scenePos = Scene.toScreenDimension(scenePos);
            //Scene.toSceneDimension(scenePos);
            if(buffer[(int)scenePos.getX()][(int)scenePos.getY()] != null)
                if(this.isTrackAlive(buffer[(int)scenePos.getX()][(int)scenePos.getY()]))
                    return buffer[(int)scenePos.getX()][(int)scenePos.getY()];
                else
                    return null;
            return null;
        }else
            return null;
    }

    public boolean isTrackAlive(Track track){
        if(!track.isActive()) {
            Vector3f scenePos = new Vector3f(track.getPosition());
            //Scene.toSceneDimension(scenePos);
            this.trackFields.get(track.id)[(int)scenePos.getX()][(int)scenePos.getY()] = null;
            return false;
        }
        else {
            track.decreaseTimeLine();
            return true;
        }
    }

    private void game(ActionEvent event){
        //synchronized (bugs) {
        while (!this.newBugs.isEmpty()){
            this.bugs.add(newBugs.get(0));
            this.newBugs.remove(0);
        }
        this.bugs.parallelStream().forEach(Bug::move);
        this.buildingFields.clear();
        Arrays.stream(this.buildings.toArray(new Building[0])).toList().forEach(this::addBuilding);

       // controller.PopulationBLUE.setText("Population BLUE: "+this.bugs.size());
       /* controller.PopulationRED.setText("Population RED: "+this.antHeapRed.getBugCount());
        controller.PopulationBLUE.setText("Population BLUE: "+this.antHeapBlue.getBugCount());

*/
        /*int th = Arrays.stream(this.trackFields.get(2)).toList().forEach(tracks -> Arrays.stream(tracks).toList().forEach());
        controller.TracksHOME.setText("Tracks HOME: "+this.antHeapRed.getFoodCount());
        controller.TRACKFOOD.setText("Tracks FOOD: "+this.antHeapBlue.getFoodCount());*/
        //}
    }

    /*private void render(ActionEvent event) {
        this.redBugs.setText("RED bugs: "+this.antHeapRed.getBugCount());
        this.blueBugs.setText("BLUE bugs: "+this.antHeapBlue.getBugCount());

        this.redFood.setText("RED food: "+this.antHeapRed.getFoodCount());
        this.blueFood.setText("BLUE food: "+this.antHeapBlue.getFoodCount());

        this.scene.repaint();
        this.canvas.setImage(this.scene.getImage());
    }*/

    public void onCanvasPressed(GLMouseEvent event) {
        float mouseX = (float) event.getX();
        float mouseY = (float) event.getY();
        //System.out.println(event.getX());
        var screenPos = new Vector3f(mouseX, mouseY, 0);
        //Scene.toScreenDimension(screenPos);

        Food food = new Food(80, screenPos, Color.GREEN);
        this.buildings.add(food);
        this.scene.add(food);
    }

    public static void main(String[] args) {
        new Roy(null);
    }
}
