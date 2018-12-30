package aoc2018.helper;

import java.awt.Point;
import java.util.Objects;

public class QueueNode {
    private Point self;
    private Point predecessor;
    private int tool = -1;
    private int passedTime;

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
        return Objects.equals(self, queueNode.self);
    }

    @Override
    public int hashCode() {
        return Objects.hash(self);
    }

    public Point getSelf() {
        return self;
    }

    public void setSelf(Point self) {
        this.self = self;
    }

    public Point getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Point predecessor) {
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
}
