package com.krld.pathfinding.ants.model;

/**
 * Created by Andrey on 5/8/2014.
 */
public class Ant {
    private MoveBehaviour moveBehaviour;
    private final MyGame context;
    private final Nest nest;
    private Directions direction;
    private Point position;
    private float rotation;
    private Action action;
    private AntDestination distination;
    private WayPoint pickedWayPoint;

    public Ant(int x, int y, MyGame context, Nest nest) {
        position = new Point(x, y);
        this.context = context;
        this.nest = nest;
        setAntMagicMoveBehaviour();
        setDestination(AntDestination.FROM_NEST);
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

    public Directions getDirection() {
        return direction;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    public MoveBehaviour getMoveBehaviour() {
        return moveBehaviour;
    }

    public AntDestination getDestination() {
        return distination;
    }

    public void setDestination(AntDestination destination) {
        this.distination = destination;
    }

    public Nest getNest() {
        return nest;
    }

    public WayPoint getPickedWayPoint() {
        return pickedWayPoint;
    }

    public void setPickedWayPoint(WayPoint pickedWayPoint) {
        this.pickedWayPoint = pickedWayPoint;
    }

    public void setMoveBehaviour(AntMagicMoveBehaviour moveBehaviour) {
        this.moveBehaviour = moveBehaviour;
    }

    public MyGame getContext() {
        return context;
    }

    public void setAStarMoveBehaviour() {
        moveBehaviour = new AntAStarMoveBehaviour();
        moveBehaviour.setAnt(this);
        moveBehaviour.setContext(context);
    }

    public void setAntMagicMoveBehaviour() {
        moveBehaviour = new AntMagicMoveBehaviour();
        moveBehaviour.setAnt(this);
        moveBehaviour.setContext(context);
    }
}
