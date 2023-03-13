package com.roy.tracks;

import com.roy.tracks.Track;
import com.roy.utils.Constants;
import org.engine.maths.Vector3f;
import org.engine.utils.Color;

public class HomeTrack extends Track {
    public HomeTrack(int type, int range, Vector3f pos){
        super(Constants.HOME_TRACK_ID, range, pos);
        this.type = type;
        this.shape.setColor(Color.RED);
    }
}
