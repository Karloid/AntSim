package com.krld.ant.model;

/**
 * Created by Andrey on 5/8/2014.
 */
public class Ant {
    private final MoveBehaviour moveBehaviour;
    private final MyGame context;
    private final Nest nest;
    private Direction direction;
    private Point position;
    private float rotation;
    private Action action;

    public Ant(int x, int y, MyGame context, Nest nest) {
        position = new Point(x, y);
        this.context = context;
        this.nest = nest;
        moveBehaviour = new AntMoveBehaviour();
        moveBehaviour.setAnt(this);
        moveBehaviour.setContext(context);

    }


    public void update() {
        moveBehaviour.update();

    }



    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public float getRotation() {
        return rotation;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public MoveBehaviour getMoveBehaviour() {
        return moveBehaviour;
    }
}
