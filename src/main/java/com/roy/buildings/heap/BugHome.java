package com.roy.buildings.heap;

import com.roy.bugs.Bug;
import com.roy.tracks.Track;
import org.engine.maths.Vector3f;
import org.engine.objects.ShapeObject;
import org.engine.utils.Color;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public interface BugHome {
    Vector3f getPosition();
    Color getColor();
    void hireBug(Bug bug);
    public Consumer<Track> getMarkAction();
    public Function<Bug, ArrayList<Track>> getSmellAction();
    public Function<Bug, Integer> getMoveAction();
    public Consumer<ShapeObject> getDeathAction();
}
