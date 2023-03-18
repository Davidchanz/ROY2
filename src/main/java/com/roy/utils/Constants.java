package com.roy.utils;

public class Constants {
    public static int WIDTH;
    public static int HEIGHT;
    public static int BUG_SPEED = 5;
    public static int BUG_COUNT = 1000;
    public static int FOOD_VALUE;
    public static float ANT_WORKER_SIZE = 10f;
    public static final int BUG_ID = 4;
    public static int BUG_SMELL_RADIUS = 12;
    public static int ANT_WORKER_FOOD_CAPACITY = 1;
    public static int BUG_MARK_FREQUENCY = 10;
    public static int BUG_TURN_DEGREE = 120;
    public static int BUG_LOST_STEPS = 50;
    public static int BUG_TURN_STEPS = 50;
    public static final int FOOD_ID = 1;
    public static final int ANT_HEAP_ID = 0;
    public static final int HOME_TRACK_ID = 2;
    public static final int FOOD_TRACK_ID = 3;
    public static final int ALIEN_HOME = 4;
    public static final int BORDER_UP = 5;
    public static final int BORDER_DOWN = 6;
    public static final int BORDER_RIGHT = 7;
    public static final int BORDER_LEFT = 8;
    public static final int BORDER_CORNER = 9;
    public static final int FREEWAY = -1;
    public static int TRACK_LIFE_CAPACITY = 1000;
    public static final String ANT_WORKER = "src/main/resources/com/roy/white_ant.png";
    public static final int BORDER_STONE_ID = 5;
    public static final int STONE_SIZE = 15;
    private Constants(){}

    public static void ini(int w, int h, int foodValue, int speed, int bugsCount){
        WIDTH = w;
        HEIGHT = h;
        FOOD_VALUE = foodValue;
        BUG_COUNT = bugsCount;
        BUG_SPEED = speed;
    }
}
