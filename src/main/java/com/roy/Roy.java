package com.roy;

import com.map.MapCreator;
import com.map.borders.Border;
import com.map.borders.Stone;
import com.roy.bugs.Bug;
import com.roy.buildings.Building;
import com.roy.buildings.food.Food;
import com.roy.buildings.heap.AntHeap;
import com.roy.tracks.Track;
import com.roy.utils.Constants;
import org.UnityMath.Vector2;
import org.engine.Scene;
import org.engine.cameras.Camera;
import org.engine.cameras.PerspectiveCamera;
import org.engine.cameras.ThirdPerson3DCamera;
import org.engine.events.GLMouseEvent;
import org.engine.maths.Vector3f;
import org.engine.objects.ShapeObject;
import org.engine.utils.Color;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeMap;
import java.util.List;

public class Roy {
    private Scene scene;
    private ArrayList<Bug> bugs;
   // private TreeMap<Integer, Track[][]> trackFields;
   // private TreeMap<Integer, Building[][]> buildingFields;
    //private ArrayList<Building> buildings;
    private ArrayList<Bug> newBugs;
    public Roy(){
        javax.swing.Timer timer = new Timer(Constants.BUG_SPEED, this::game);

        int w = Constants.WIDTH;
        int h = Constants.HEIGHT;

        this.scene = new Scene(w, h, 800, 800, Color.TRANSPARENT);
        //this.scene.enableDevelopMode();
        //this.scene.setBorderVisible(true);
        //this.scene.repaint();
        //this.canvas.setFitHeight(h);
        //this.canvas.setFitWidth(w);
        //this.trackFields = new TreeMap<>();
        //this.buildingFields = new TreeMap<>();

        //this.buildings = new ArrayList<>();
        this.newBugs = new ArrayList<>();

        this.bugs = new ArrayList<>();

        this.createAnteHeap(2, Constants.FOOD_VALUE/2, Color.RED, new Vector3f(-300, -300, 0));
        //this.createAnteHeap(3, Constants.FOOD_VALUE/2, Color.GREEN, new Vector3f(300, -300, 0));
        this.createAnteHeap(4, Constants.FOOD_VALUE/2, Color.CYAN, new Vector3f(-300, 320, 0));
        this.createAnteHeap(5, Constants.FOOD_VALUE/2, Color.YELLOW, new Vector3f(300, 300, 0));

        this.scene.addMouseButtonPressedListener(this::onCanvasPressed);
        this.scene.addCloseListener(this::close);
        this.loadMap();

        this.scene.start();

        while (true){
            if(this.scene.isReady()) {
                break;
            }
        }

        Camera camera = new PerspectiveCamera();
        //camera.setTargetObject(this.newBugs.get(0));
        this.scene.setCamera(camera);

        timer.start();
    }

    private void loadMap() {
        var objects = MapCreator.loadMap();
        objects.forEach(object -> {
            if(object instanceof Border) {//TODO
                this.addBorder((Border)object);
            }
        });
    }

    private void addBorder(Border object) {
        this.scene.add(object);
    }

    private void createAnteHeap(int id, int foodValue, Color color, Vector3f position){
        AntHeap antHeap = new AntHeap(id, foodValue, position, color, this::createBug, this::markAction, this::smellAction, this::moveAction, this::removeFromScene);

        //this.buildings.add(antHeap);

        this.scene.add(antHeap);

        while (antHeap.getBugCount() < Constants.BUG_COUNT) {
            antHeap.addWorker();
        }
    }

    private void close(){
        System.exit(0);
    }

   /* private void addBuilding(Building building){
        var sceneCoord = new Vector3f(building.getPosition());
        sceneCoord = Scene.toGLDimension(sceneCoord);
        sceneCoord = Scene.toScreenDimension(sceneCoord);
        int size = building.getFoodCount()/Constants.FOOD_VALUE/4;//TODO
        for(int x = (int)sceneCoord.getX()-size; x <= (int)sceneCoord.getX()+size; x++){
            for(int y = (int)sceneCoord.getY()-size; y <= (int)sceneCoord.getY()+size; y++){
                if(x < 0 || x > Scene.WIDTH || y < 0 || y > Scene.HEIGHT)
                    continue;
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
    }*/

    public void createBug(Bug bug){
        this.scene.add(bug);
        this.newBugs.add(bug);
    }

    /*private void addTrack(Track track){
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
    }*/

    public void markAction(Track track){
        //this.tracks.add(track);//TODO simple dot shape in Engine
       // this.scene.addObjectInBuffer(track);
        this.scene.add(track);
    }

    public void removeFromScene(ShapeObject object){
        /*if(object instanceof Building)
            this.buildings.remove(object);*/
        this.scene.remove(object);
    }

    public Integer moveAction(Bug bug){
        if(this.scene.isOutSceneBorder(bug.getPosition())){
            return Constants.BORDER;
        }else {
            ShapeObject object = this.scene.getObject(bug.getTarget(), bug.getPosition());
            if(object instanceof Building) {
                if(object instanceof AntHeap) {
                    if(bug.getBugHome() != (AntHeap) object){
                        bug.setMemory(object);
                        return Constants.ALIEN_HOME;
                    }else {
                        return Constants.ANT_HEAP_ID;
                    }
                }
                else if(object instanceof Food) {
                    if(!((Food)object).isAlive())
                        this.removeFromScene(object);
                    else {
                        bug.setMemory(object);
                        return Constants.FOOD_ID;
                    }
                }
            }else if(this.scene.getObject(Constants.BORDER_STONE_ID, bug.getPosition()) != null){
                return Constants.BORDER;
            }
            return Constants.FREEWAY;
        }
    }

    public ArrayList<Track> smellAction(Bug bug){
        if(this.scene.isOutSceneBorder(bug.getPosition())){
            return null;
        }else {
            ArrayList<Track> foundedTracks = new ArrayList<>();
            Vector3f scenePos = new Vector3f(bug.getPosition());
            scenePos = Scene.toGLDimension(scenePos);
            for(float i = scenePos.getX()-bug.getSmellRadius(); i < scenePos.getX()+bug.getSmellRadius(); i++)
                for(float j = scenePos.getY()-bug.getSmellRadius(); j < scenePos.getY()+bug.getSmellRadius(); j++){
                    Vector3f tmp = new Vector3f(i, j, 0f);
                    if(!this.scene.isOutSceneBorder(new Vector3f(tmp.getX()/Scene.WIDTH, tmp.getY()/Scene.HEIGHT, tmp.getZ()/Scene.WIDTH))) {
                        ShapeObject object = this.scene.getObject(this.getBugTargetId(bug), new Vector3f(tmp.getX()/Scene.WIDTH, tmp.getY()/Scene.HEIGHT, tmp.getZ()/Scene.WIDTH));
                        if(object instanceof Track)
                            foundedTracks.add((Track) object);

                    }
                }
            if(!foundedTracks.isEmpty())
                return foundedTracks;
            return null;
        }
    }

    public int getBugTargetId(Bug bug){
        int id = -1;
        if(bug.getTarget() == Constants.FOOD_ID){
            id = Constants.FOOD_TRACK_ID;
        }else if(bug.getTarget() == Constants.ANT_HEAP_ID)
            id = Constants.HOME_TRACK_ID;
        return id;
    }

    /*public Building getBuilding(int id, Vector3f pos){
        var buffer = this.buildingFields.get(id);
        if(buffer != null){
            Vector3f scenePos = new Vector3f(pos);
            scenePos = Scene.toGLDimension(scenePos);
            scenePos = Scene.toScreenDimension(scenePos);
            if(buffer[(int)scenePos.getX()][(int)scenePos.getY()] != null)
                return buffer[(int)scenePos.getX()][(int)scenePos.getY()];
            return null;
        }else
            return null;
    }*/

    /*public Track getTrack(int id, Vector3f pos){
        var buffer = this.trackFields.get(id);
        if(buffer != null){
            Vector3f scenePos = new Vector3f(pos);
            scenePos = Scene.toScreenDimension(scenePos);
            if(buffer[(int)scenePos.getX()][(int)scenePos.getY()] != null)
                if(this.isTrackAlive(buffer[(int)scenePos.getX()][(int)scenePos.getY()]))
                    return buffer[(int)scenePos.getX()][(int)scenePos.getY()];
                else
                    return null;
            return null;
        }else
            return null;
    }*/

   /* public boolean isTrackAlive(Track track){//TODO
        if(!track.isActive()) {
            this.scene.remove(track);
            return false;
        }
        else {
            track.decreaseTimeLine();
            return true;
        }
    }*/

    private void game(ActionEvent event){
        this.bugs.addAll(this.newBugs);
        this.newBugs.clear();
        List<Bug> oldBugs = Collections.synchronizedList(new ArrayList<>());
        this.bugs.parallelStream().forEach(bug -> {
            if(!bug.isAlive()) {
                oldBugs.add(bug);
            }
            else
                bug.move();
        });
        this.bugs.removeAll(oldBugs);

        this.scene.updateObjectBuffer(Constants.FOOD_ID, Constants.ANT_HEAP_ID);
        //this.buildingFields.clear();
        //Arrays.stream(this.buildings.toArray(new Building[0])).toList().forEach(this::addBuilding);
    }

    public void onCanvasPressed(GLMouseEvent event) {
        if(event.isLeftButtonDown()) {
            float mouseX = (float) event.getX();
            float mouseY = (float) event.getY();
            var screenPos = new Vector3f(mouseX, mouseY, 0);

            System.out.println(screenPos);
            Food food = new Food(Constants.FOOD_VALUE / 4, screenPos, Color.GREEN);
            //this.buildings.add(food);
            this.scene.add(food);
        }
    }

    public static void main(String[] args) {
        new Roy();
    }
}
