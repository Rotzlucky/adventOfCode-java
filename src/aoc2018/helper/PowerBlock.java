package aoc2018.helper;

import java.util.Objects;

public class PowerBlock {
    private int x;
    private int y;
    private int size;

    public PowerBlock(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PowerBlock that = (PowerBlock) o;
        return x == that.x &&
                y == that.y &&
                size == that.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, size);
    }
}
