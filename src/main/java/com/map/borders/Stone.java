package com.map.borders;

import com.roy.utils.Constants;
import org.engine.maths.Vector3f;
import org.engine.shapes.Rectangle;
import org.engine.utils.Color;

public class Stone extends Border{
    public Stone(Vector3f position) {
        super("Stone", Constants.BORDER_STONE_ID);
        this.shape = new Rectangle(Constants.STONE_SIZE, position, Color.WHITE, "src/main/resources/com/roy/stone.png");
        this.add(this.shape);
    }

}
