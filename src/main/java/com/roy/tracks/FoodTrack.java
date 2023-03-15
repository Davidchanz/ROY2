package com.roy.tracks;

import com.roy.utils.Constants;
import org.engine.maths.Vector3f;
import org.engine.utils.Color;

public class FoodTrack extends Track {
    public FoodTrack(int type, int range, Vector3f pos){
        super(Constants.FOOD_TRACK_ID, range, pos, Color.GREEN);
        this.type = type;
    }
}
