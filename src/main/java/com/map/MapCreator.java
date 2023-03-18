package com.map;

import com.map.borders.Border;
import com.map.borders.Stone;
import com.roy.buildings.food.Food;
import com.roy.buildings.heap.AntHeap;
import com.roy.utils.Constants;
import org.engine.Scene;
import org.engine.cameras.Camera;
import org.engine.cameras.PerspectiveCamera;
import org.engine.events.GLKeyEvent;
import org.engine.events.GLMouseEvent;
import org.engine.maths.Matrix4f;
import org.engine.maths.Vector3f;
import org.engine.objects.ShapeObject;
import org.engine.utils.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;

public class MapCreator {
    private Scene scene;
    private ArrayList<ShapeObject> objects;
    double lastX, lastY;
    int map[][];

    public MapCreator(){
        this.objects = new ArrayList<>();

        this.scene = new Scene(800, 800, 800, 800, Color.CRIMSON);
        this.map = new int[800][800];
        for(var i: this.map)
            Arrays.fill(i, 0);
        //Camera camera = new PerspectiveCamera();
        //this.scene.setCamera(camera);
        //this.scene.addMouseButtonPressedListener(this::onCanvasPressed);
        this.scene.addMouseDraggedListener(this::onCanvasDragged);
        this.scene.addKeyPressedListener(glKeyEvent -> {
            if(glKeyEvent.getKey() == GLKeyEvent.S)
                saveMap();
        });
        this.scene.start();
    }

    private void onCanvasDragged(GLMouseEvent glMouseEvent) {
        float mouseX = (float) glMouseEvent.getX();
        float mouseY = (float) glMouseEvent.getY();
        if(Math.abs(lastX) - Math.abs(mouseX) >= 10 ||
                Math.abs(lastY) - Math.abs(mouseY) >= 10 ||
                Math.abs(lastX) - Math.abs(mouseX) <= -10 ||
                Math.abs(lastY) - Math.abs(mouseY) <= -10){
            onCanvasPressed(glMouseEvent);
        }
    }

    public void onCanvasPressed(GLMouseEvent glMouseEvent) {
        if(glMouseEvent.isLeftButtonDown()) {
            float mouseX = (float) glMouseEvent.getX();
            float mouseY = (float) glMouseEvent.getY();
            var screenPos = new Vector3f(mouseX, mouseY, 0);
            var mapPos = Scene.toScreenDimension(screenPos);

            int i = (int)mapPos.getX() / Constants.STONE_SIZE;
            int j = (int)mapPos.getY() / Constants.STONE_SIZE;
            System.out.println(this.map[i][j]);
            if(this.map[i][j] == 0){
                this.map[i][j] = Constants.BORDER_STONE_ID;
                Vector3f scenePos = new Vector3f(i*Constants.STONE_SIZE, j*Constants.STONE_SIZE, 0);
                scenePos = Scene.toSceneDimension(scenePos);
                Stone stone = new Stone(scenePos);
                this.objects.add(stone);
                this.scene.add(stone);
            }
            lastX = mouseX;
            lastY = mouseY;
        }
    }

    public void saveMap(){
        try (FileWriter fw = new FileWriter("save.map")){
            this.objects.forEach(object -> {
                var pos = Scene.toGLDimension(object.getPosition());
                var id = object.getId();
                try {
                    fw.write(id + "," + String.valueOf(pos.getX()) + "," + String.valueOf(pos.getY()) + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<ShapeObject> loadMap(){
        ArrayList<ShapeObject> foundedObjects = new ArrayList<>();
        try (FileReader fr = new FileReader("save.map")){
            Scanner scLine = new Scanner(fr);
            scLine.useDelimiter("\n");
            while (scLine.hasNext()){
                Scanner scPos = new Scanner(scLine.next());
                scPos.useDelimiter(",");
                while (scPos.hasNext()){
                    int id = Integer.parseInt(scPos.next());
                    float posX = Float.parseFloat(scPos.next());
                    float posY = Float.parseFloat(scPos.next());
                    switch (id){
                        case Constants.ANT_HEAP_ID -> {
                            /*AntHeap stone = new AntHeap(new Vector3f(posX, posY, 0));//TODO
                            foundedObjects.add(stone);*/
                        }
                        case Constants.FOOD_ID -> {
                            /*Stone stone = new Stone(new Vector3f(posX, posY, 0));//TODO
                            foundedObjects.add(stone);*/
                        }
                        case Constants.BORDER_STONE_ID -> {
                            Stone stone = new Stone(new Vector3f(posX, posY, 0));
                            foundedObjects.add(stone);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return foundedObjects;
    }

    public static void main(String[] args) {
        System.out.println("Map Creator!");
        new MapCreator();
    }
}
