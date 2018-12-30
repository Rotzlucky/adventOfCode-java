package aoc2018.helper;

import java.awt.Point;
import java.util.Objects;

public class QueueNode {
    private Point self;
    private QueueNode predecessor;
    private int tool = -1;
    private int passedTime;
    private int distance;

    public QueueNode(Point self) {
        this.self = self;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof QueueNode)) {
            return false;
        }

        QueueNode queueNode = (QueueNode) o;
        return tool == queueNode.tool &&
                Objects.equals(self, queueNode.self);
    }

    @Override
    public int hashCode() {
        return Objects.hash(self, tool);
    }

    public Point getSelf() {
        return self;
    }

    public void setSelf(Point self) {
        this.self = self;
    }

    public QueueNode getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(QueueNode predecessor) {
        this.predecessor = predecessor;
    }

    public int getTool() {
        return tool;
    }

    public void setTool(int tool) {
        this.tool = tool;
    }

    public int getPassedTime() {
        return passedTime;
    }

    public void setPassedTime(int passedTime) {
        this.passedTime = passedTime;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getY() {
        return getSelf().y;
    }

    public int getX() {
        return getSelf().x;
    }

    public String getKey() {
        return getSelf().x + "|" + getSelf().y + "|" + getTool();
    }
}
