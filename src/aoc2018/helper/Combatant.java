package aoc2018.helper;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Combatant {

    public static final Comparator<Combatant> READING_ORDER_COMPARATOR = Comparator.comparingInt(Combatant::getY).thenComparingInt(Combatant::getX);
    public static final char ELF = 'E';
    public static final char GOBLIN = 'G';

    protected int x;
    protected int y;
    private int hitPoints;
    private char enemy;
    private char identity;
    private int attack = 3;

    public Combatant(Point startPosition, char identity) {
        this.x = startPosition.x;
        this.y = startPosition.y;
        this.hitPoints = 200;
        this.identity = identity;

        if (identity == ELF) {
            enemy = GOBLIN;
        } else {
            enemy = ELF;
        }
    }

    public Combatant(Point point, char currentChar, int attackPower) {
        this(point, currentChar);
        this.attack = attackPower;
    }

    public List<Combatant> getEnemiesInFightingRange(List<Combatant> enemies) {
        return enemies
                .stream()
                .filter(c -> c.getHitPoints() > 0 && Math.abs(getX() - c.getX()) + Math.abs(getY() - c.getY()) == 1)
                .sorted(Comparator.comparingInt(Combatant::getHitPoints).thenComparing(READING_ORDER_COMPARATOR))
                .collect(Collectors.toList());
    }

    public boolean attack(Combatant enemy) {
        enemy.setHitPoints(enemy.getHitPoints() - getAttack());
        return enemy.getHitPoints() <= 0;
    }

    public boolean move(List<Combatant> enemies, char[][] map, Map<Point, Character> blockedCoordinates) {
        WayFinder wayFinder = new WayFinder(map, blockedCoordinates, getPosition());

        LinkedList<Point> shortestPath = getPathToDestination(enemies, wayFinder);

        if (!shortestPath.isEmpty()) {
            Point point = shortestPath.removeFirst();

            blockedCoordinates.remove(getPosition());
            setX(point.x);
            setY(point.y);
            blockedCoordinates.put(getPosition(), identity);

            return true;
        }

        return false;
    }

    private LinkedList<Point> getPathToDestination(List<Combatant> enemies, WayFinder wayFinder) {
        List<Point> destinations = new ArrayList<>();
        enemies.stream()
                .filter(c -> c.getHitPoints() > 0)
                .forEach(c -> destinations.addAll(wayFinder.getNonBlockedNeighbours(c.getPosition())));

        if (destinations.isEmpty()) {
            return new LinkedList<>();
        }

        Optional<Point> selectedDestination = destinations.stream()
                .collect(Collectors.groupingBy(wayFinder::getDistance, Collectors.toList()))
                .entrySet().stream()
                .min(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .orElse(Collections.emptyList()).stream()
                .min(WayFinder.POINT_READING_ORDER);

        if (selectedDestination.isPresent() && !wayFinder.unreachable(selectedDestination.get())) {
            return wayFinder.getShortestPath(selectedDestination.get());
        }

        return new LinkedList<>();
    }

    public Point getPosition() {
        return new Point(getX(), getY());
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

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public char getEnemy() {
        return enemy;
    }

    public void setEnemy(char enemy) {
        this.enemy = enemy;
    }

    public char getIdentity() {
        return identity;
    }

    public void setIdentity(char identity) {
        this.identity = identity;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }
}
