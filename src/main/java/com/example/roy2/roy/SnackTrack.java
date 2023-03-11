package com.example.roy2.roy;

import org.engine.maths.Vector3f;
import org.engine.utils.Color;

public class SnackTrack extends Track{
    public SnackTrack(int type, int range, Vector3f pos){
        super(3, range, pos);
        this.type = type;
        this.shape.setColor(Color.GREEN);
    }
}
