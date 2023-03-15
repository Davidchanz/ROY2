package com.roy;

import com.map.MapCreator;
import com.map.borders.Border;
import com.map.borders.Stone;
import com.roy.bugs.Bug;
import com.roy.buildings.Building;
import com.roy.buildings.food.Food;
import com.roy.buildings.heap.AntHeap;
import com.roy.tracks.FoodTrack;
import com.roy.tracks.HomeTrack;
import com.roy.tracks.Track;
import com.roy.utils.Constants;
import org.UnityMath.Vector2;
import org.UnityMath.Vector3;
import org.engine.Scene;
import org.engine.cameras.Camera;
import org.engine.cameras.PerspectiveCamera;
import org.engine.cameras.ThirdPerson3DCamera;
import org.engine.events.GLKeyEvent;
import org.engine.events.GLMouseEvent;
import org.engine.maths.Vector3f;
import org.engine.objects.GameObject;
import org.engine.objects.ShapeObject;
import org.engine.utils.BorderType;
import org.engine.utils.Color;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Roy {
    private Scene scene;
    private ArrayList<Bug> bugs;
    private ArrayList<Bug> newBugs;
    private ShapeObject homeTrack;
    private ShapeObject foodTrack;
    public Roy(){
        javax.swing.Timer timer = new Timer(Constants.BUG_SPEED, this::game);

        int w = Constants.WIDTH;
        int h = Constants.HEIGHT;

        this.scene = new Scene(w, h, 800, 800, Color.TRANSPARENT);
        //this.scene.enableDevelopMode();
        //this.scene.setBorderVisible(true);
        this.newBugs = new ArrayList<>();

        this.bugs = new ArrayList<>();

        this.createAnteHeap(2, Constants.FOOD_VALUE/4, Color.RED, new Vector3f(-300, -300, 0));
        //this.createAnteHeap(3, Constants.FOOD_VALUE/2, Color.GREEN, new Vector3f(300, -300, 0));
        //this.createAnteHeap(4, Constants.FOOD_VALUE/2, Color.CYAN, new Vector3f(-300, 320, 0));
        //this.createAnteHeap(5, Constants.FOOD_VALUE/2, Color.YELLOW, new Vector3f(300, 300, 0));
        //this.createAnteHeap(6, Constants.FOOD_VALUE/2, Color.WHITE, new Vector3f(0, 0, 0));

        this.scene.addMouseButtonPressedListener(this::onCanvasPressed);
        this.scene.addKeyPressedListener(glKeyEvent -> {
            if(glKeyEvent.getKey() == GLKeyEvent.H)
                this.homeTrack.setVisible(!this.homeTrack.isVisible());
            else if(glKeyEvent.getKey() == GLKeyEvent.F)
                this.foodTrack.setVisible(!this.foodTrack.isVisible());
        });
        this.scene.addCloseListener(this::close);
        this.loadMap();

        this.homeTrack = new ShapeObject("HomeTrack", Constants.HOME_TRACK_ID);
        this.foodTrack = new ShapeObject("FoodTrack", Constants.FOOD_TRACK_ID);

        this.homeTrack.setSpriteSize(new Vector2(1,1));
        this.foodTrack.setSpriteSize(new Vector2(1,1));

        this.homeTrack.setVisible(false);
        this.foodTrack.setVisible(false);

        this.scene.add(this.homeTrack);
        this.scene.add(this.foodTrack);

        this.scene.start();

        while (true){
            if(this.scene.isReady()) {
                break;
            }
        }
        //Camera camera = new PerspectiveCamera();
        //camera.setTargetObject(this.newBugs.get(0));
        //this.scene.setCamera(camera);

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

        this.scene.add(antHeap);

        while (antHeap.getBugCount() < Constants.BUG_COUNT) {
            antHeap.addWorker();
        }
    }

    private void close(){
        System.exit(0);
    }

    public void createBug(Bug bug){
        this.scene.add(bug);
        this.newBugs.add(bug);
    }

    public void markAction(Track track){
        if(track instanceof HomeTrack)
            this.homeTrack.add(track);
        if(track instanceof FoodTrack)
            this.foodTrack.add(track);
        //this.scene.add(track);
    }

    public void removeFromScene(ShapeObject object){
        this.scene.remove(object);
    }

    public Integer moveAction(Bug bug){
        var borderType = this.scene.isOutSceneBorder(bug.getPosition());
        if(borderType != BorderType.NO_BORDER){
            return this.getBorderDir(borderType);
        }else {
            var border = this.scene.getObject(Constants.BORDER_STONE_ID, bug.getPosition());
            if(border != null)
                return this.getBorderDir(bug, border);
            else {
                var foundedObjects = this.findInSmellRadius(bug, (pos) -> {
                    GameObject object = this.scene.getObject(bug.getTarget(), pos);
                    if(object != null) {
                        var build = object.getParent();
                        if (build instanceof Building) {
                            return (Building) build;
                        }
                    }
                    return null;
                });

                if(foundedObjects == null)
                    return Constants.FREEWAY;
                Building shortestObject = null;
                for(var object: foundedObjects){
                    if (shortestObject == null)
                        shortestObject = object;
                    else if (this.range(shortestObject.getPosition(), bug.getPosition()) > this.range(object.getPosition(), bug.getPosition()))
                        shortestObject = object;
                }
                if (shortestObject instanceof AntHeap) {
                    if (bug.getBugHome() != (AntHeap) shortestObject) {
                        bug.setMemory(shortestObject);
                        return Constants.ALIEN_HOME;
                    } else {
                        return Constants.ANT_HEAP_ID;
                    }
                } else if (shortestObject instanceof Food) {
                    if (!((Food) shortestObject).isAlive())
                        this.removeFromScene(shortestObject);
                    else {
                        bug.setMemory(shortestObject);
                        return Constants.FOOD_ID;
                    }
                }
                return Constants.FREEWAY;
            }
        }
    }

    private Integer getBorderDir(Bug bug, GameObject border) {
        var borderPos = border.getPosition();
        var bugPosition = bug.getPosition();
        if(bugPosition.getX() == borderPos.getX()) {
            if(bugPosition.getY() < borderPos.getY())
                return Constants.BORDER_UP;
            else
                return Constants.BORDER_DOWN;
        }else if(bugPosition.getX() < borderPos.getX()){
            if(bugPosition.getY() == borderPos.getY())
                return Constants.BORDER_RIGHT;
            else if(bugPosition.getY() < borderPos.getY()) {
                if (borderPos.getY() - bugPosition.getY() > border.getParent().getSpriteSize().y)
                    return Constants.BORDER_RIGHT;
                else
                    return Constants.BORDER_UP;
            }else {
                if (bugPosition.getY() - borderPos.getY() > border.getParent().getSpriteSize().y)
                    return Constants.BORDER_RIGHT;
                else
                    return Constants.BORDER_DOWN;
            }
        }else /*if(bugPosition.getX() > borderPos.getX())*/{
            if(bugPosition.getY() == borderPos.getY())
                return Constants.BORDER_LEFT;
            else if(bugPosition.getY() < borderPos.getY()) {
                if (borderPos.getY() - bugPosition.getY() > border.getParent().getSpriteSize().y)
                    return Constants.BORDER_LEFT;
                else
                    return Constants.BORDER_UP;
            }else {
                if (bugPosition.getY() - borderPos.getY() > border.getParent().getSpriteSize().y)
                    return Constants.BORDER_RIGHT;
                else
                    return Constants.BORDER_DOWN;
            }
        }
    }

    private Integer getBorderDir(BorderType borderType) {
        switch (borderType){
            case BORDER_RIGHT -> {
                return Constants.BORDER_RIGHT;
            }
            case BORDER_LEFT -> {
                return Constants.BORDER_LEFT;
            }
            case BORDER_DOWN -> {
                return Constants.BORDER_DOWN;
            }
            case BORDER_UP -> {
                return Constants.BORDER_UP;
            }
            default -> {
                return 0;//TODO
            }
        }
    }

    public double range(Vector3f pos1, Vector3f pos2){
        var a = Math.abs(pos1.getX() - pos2.getX());
        var b = Math.abs(pos1.getY() - pos2.getY());
        return Math.sqrt(a*a + b*b);
    }

    public ArrayList<Track> smellAction(Bug bug){
        if(this.scene.isOutSceneBorder(bug.getPosition()) != BorderType.NO_BORDER){
            return null;
        }else {
            var foundedTracks = this.findInSmellRadius(bug, (pos) -> {
                GameObject object = this.scene.getObject(this.getBugTargetId(bug), pos);
                if (object instanceof Track)
                    if (((Track) object).isActive())
                        return (Track) object;
                return null;
            });
            if(!foundedTracks.isEmpty())
                return foundedTracks;
            return null;
        }
    }

    public <T> ArrayList<T> findInSmellRadius(Bug bug, Function<Vector3f, T> function){
        ArrayList<T> foundedObjects = new ArrayList<>();
        Vector3f scenePos = new Vector3f(bug.getPosition());
        scenePos = Scene.toGLDimension(scenePos);
        for(float i = scenePos.getX() - bug.getSmellRadius(); i <= scenePos.getX() + bug.getSmellRadius(); i++) {
            for (float j = scenePos.getY() - bug.getSmellRadius(); j <= scenePos.getY() + bug.getSmellRadius(); j++) {
                Vector3f tmp = new Vector3f(i / Scene.WIDTH, j / Scene.HEIGHT, 0f);
                if (this.scene.isOutSceneBorder(tmp) == BorderType.NO_BORDER) {
                    var foundedObject = function.apply(tmp);
                    if(foundedObject != null)
                        foundedObjects.add(foundedObject);
                }
            }
        }
        return foundedObjects;
    }

    public int getBugTargetId(Bug bug){
        int id = -1;
        if(bug.getTarget() == Constants.FOOD_ID){
            id = Constants.FOOD_TRACK_ID;
        }else if(bug.getTarget() == Constants.ANT_HEAP_ID)
            id = Constants.HOME_TRACK_ID;
        return id;
    }

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
    }

    public void onCanvasPressed(GLMouseEvent event) {
        if(event.isLeftButtonDown()) {
            float mouseX = (float) event.getX();
            float mouseY = (float) event.getY();
            var screenPos = new Vector3f(mouseX, mouseY, 0);

            System.out.println(screenPos);
            Food food = new Food(Constants.FOOD_VALUE / 4, screenPos, Color.GREEN);
            this.scene.add(food);
        }
    }

    public static void main(String[] args) {
        new Roy();
    }
}
