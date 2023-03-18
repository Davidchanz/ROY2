package com.roy;

import com.map.borders.Border;
import com.map.borders.Stone;
import com.roy.bugs.AntWorker;
import com.roy.bugs.Bug;
import com.roy.buildings.heap.AntHeap;
import com.roy.utils.Constants;
import org.UnityMath.Vector2;
import org.engine.maths.Vector3f;
import org.engine.utils.Color;

public class Test {
    public static void main(String[] args) {
        /*Border border = new Stone(new Vector3f(0, 100, 0));
        AntHeap heap = new AntHeap(0, 30, new Vector3f(0, 90, 0), Color.RED, null, null, null, null, null);
        Bug bug = new Bug(1, heap, 5, "") {
            @Override
            public void mark() {

            }

            @Override
            public void friendHome() {

            }

            @Override
            public void friendBud() {

            }

            @Override
            public void alienHome() {

            }

            @Override
            public void alienBug() {

            }

            @Override
            public void food() {

            }
        };

        bug.setPosition(new Vector3f(84, 0, 0));
        border.body.get(0).setPosition(new Vector3f(100, 0, 0));
        System.out.println("Right " + (Constants.BORDER_RIGHT == Roy.getBorderDir(bug, border.body.get(0))));

        bug.setPosition(new Vector3f(84, 10, 0));
        border.body.get(0).setPosition(new Vector3f(100, 0, 0));
        System.out.println("Right " + (Constants.BORDER_RIGHT == Roy.getBorderDir(bug, border.body.get(0))));

        bug.setPosition(new Vector3f(84, -10, 0));
        border.body.get(0).setPosition(new Vector3f(100, 0, 0));
        System.out.println("Right " + (Constants.BORDER_RIGHT == Roy.getBorderDir(bug, border.body.get(0))));

        bug.setPosition(new Vector3f(115, 0, 0));
        border.body.get(0).setPosition(new Vector3f(100, 0, 0));
        System.out.println("Left " + (Constants.BORDER_LEFT == Roy.getBorderDir(bug, border.body.get(0))));

        bug.setPosition(new Vector3f(115, 10, 0));
        border.body.get(0).setPosition(new Vector3f(100, 0, 0));
        System.out.println("Left " + (Constants.BORDER_LEFT == Roy.getBorderDir(bug, border.body.get(0))));

        bug.setPosition(new Vector3f(115, -10, 0));
        border.body.get(0).setPosition(new Vector3f(100, 0, 0));
        System.out.println("Left " + (Constants.BORDER_LEFT == Roy.getBorderDir(bug, border.body.get(0))));



        bug.setPosition(new Vector3f(0, 84, 0));
        border.body.get(0).setPosition(new Vector3f(0, 100, 0));
        System.out.println("Up " + (Constants.BORDER_UP == Roy.getBorderDir(bug, border.body.get(0))));

        bug.setPosition(new Vector3f(10, 84, 0));
        border.body.get(0).setPosition(new Vector3f(0, 100, 0));
        System.out.println("Up " + (Constants.BORDER_UP == Roy.getBorderDir(bug, border.body.get(0))));

        bug.setPosition(new Vector3f(-10, 84, 0));
        border.body.get(0).setPosition(new Vector3f(0, 100, 0));
        System.out.println("Up " + (Constants.BORDER_UP == Roy.getBorderDir(bug, border.body.get(0))));

        bug.setPosition(new Vector3f(0, 115, 0));
        border.body.get(0).setPosition(new Vector3f(0, 100, 0));
        System.out.println("Down " + (Constants.BORDER_DOWN == Roy.getBorderDir(bug, border.body.get(0))));

        bug.setPosition(new Vector3f(10, 115, 0));
        border.body.get(0).setPosition(new Vector3f(0, 100, 0));
        System.out.println("Down " + (Constants.BORDER_DOWN == Roy.getBorderDir(bug, border.body.get(0))));

        bug.setPosition(new Vector3f(-10, 115, 0));
        border.body.get(0).setPosition(new Vector3f(0, 100, 0));
        System.out.println("Down " + (Constants.BORDER_DOWN == Roy.getBorderDir(bug, border.body.get(0))));*/

        Vector2 dir = new Vector2(1f,0f);
        var angle = dir.angleDeg();
        System.out.println(dir.angleDeg());
        if(angle <= 90f)
            dir.setAngleDeg(180 - angle);
        else
            dir.setAngleDeg(180 - angle + 360);
        System.out.println(dir.angleDeg());



        /*
        *   BORDER_UP = 5;
            BORDER_DOWN = 6;
            BORDER_RIGHT = 7;
            BORDER_LEFT = 8;
        * */
    }
}
