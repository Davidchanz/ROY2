package com.example.roy2.roy;

import org.engine.maths.Vector3f;
import org.engine.utils.Color;

public class HomeTrack extends Track{
    public HomeTrack(int type, int range, Vector3f pos){
        super(2, range, pos);
        this.type = type;
        this.shape.setColor(Color.RED);
    }
}
