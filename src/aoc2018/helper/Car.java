package aoc2018.helper;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Car {
    public static final List<Character> CAR_SYMBOLS = Arrays.asList('>', '<', 'v', '^');
    private int index;
    private int x;
    private int y;
    private char direction;
    private LinkedList<Character> directions = new LinkedList<>();
    private LinkedList<Character> turns = new LinkedList<>();
    private boolean broken = false;

    public Car(int index, int x, int y, char direction) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.direction = direction;
        directions.add('l');
        directions.add('s');
        directions.add('r');
        turns.add('^');
        turns.add('>');
        turns.add('v');
        turns.add('<');

        while (direction != turns.getFirst()) {
            turns.addLast(turns.removeFirst());
        }
    }

    public static boolean isCar(char element) {
        return CAR_SYMBOLS.contains(element);
    }

    public Point move() {
        if (direction == '^') {
            y--;
        } else if (direction == '>') {
            x++;
        } else if (direction == 'v') {
            y++;
        } else if (direction == '<') {
            x--;
        }

        return new Point(x, y);
    }

    public char getInitialTrackTile() {
        if (direction == '>' || direction == '<') {
            return '-';
        } else {
            return '|';
        }
    }

    public void checkTurn(char trackTile) {
        if (trackTile == '/' || trackTile == '\\') {
            turnOnCurve(trackTile);
        } else if (trackTile == '+') {
            turnOnCrossing();
        }
    }

    private void turnOnCurve(char trackTile) {
        if (trackTile == '/') {
            if (direction == '>' || direction == '<') {
                turnLeft();
            } else if (direction == '^' || direction == 'v') {
                turnRight();
            }
        } else if (trackTile == '\\') {
            if (direction == '>' || direction == '<') {
                turnRight();
            } else if (direction == '^' || direction == 'v') {
                turnLeft();
            }
        }
    }

    private void turnOnCrossing() {
        Character character = directions.removeFirst();

        if (character == 'l') {
            turnLeft();
        } else if (character == 'r') {
            turnRight();
        }

        directions.addLast(character);
    }

    private void turnLeft() {
        turns.addFirst(turns.removeLast());

        direction = turns.getFirst();
    }

    private void turnRight() {
        turns.addLast(turns.removeFirst());

        direction = turns.getFirst();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getDirection() {
        return direction;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public static Comparator<Car> getPositionComparator() {
        return Comparator.comparingInt(Car::getY)
                .thenComparing((Car::getX));
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }
}
