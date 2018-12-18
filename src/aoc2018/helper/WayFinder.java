package aoc2018.helper;

import com.sun.istack.internal.NotNull;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.function.ToIntFunction;

public class WayFinder {

    private final char[][] map;
    private final Map<Point, Character> blockedCoordinates;
    private final Map<Point, List<Point>> cachedNeighbours;

    private final Map<Point, Integer> distanceMap = new HashMap<>();
    private final Map<Point, Point> shortestEdge = new HashMap<>();

    public static final Comparator<Point> POINT_READING_ORDER = Comparator
                .comparingInt((Point point) -> point.y)
                .thenComparingInt(point -> point.x);

    public WayFinder(char[][] map, Map<Point, Character> blockedCoordinates, Point start) {
        this.map = map;
        this.blockedCoordinates = blockedCoordinates;
        this.cachedNeighbours = new HashMap<>();

        processQueue(getQueue(start));
    }

    private TreeSet<Point> getQueue(@NotNull Point startingPoint) {
        final Set<Point> visited = new HashSet<>();
        final Stack<Point> stack = new Stack<>();

        // Initialize all nodes with an infinite distance, except starting node
        stack.push(startingPoint);
        while (!stack.isEmpty()) {
            final Point p = stack.pop();
            if (visited.add(p)) {
                distanceMap.put(p, Integer.MAX_VALUE);
                for (final Point adjacent : getNonBlockedNeighbours(p)) {
                    stack.push(adjacent);
                }
            }
        }
        distanceMap.put(startingPoint, 0);

        // Define the processing order for points in the queue
        final Comparator comparator = Comparator
                // Points are compared by distance from our starting point
                .comparingInt((ToIntFunction<Point>) distanceMap::get)
                // Then compared by their reading order
                .thenComparing(POINT_READING_ORDER);

        final TreeSet<Point> queue = new TreeSet<>(comparator);
        queue.addAll(visited);
        return queue;
    }

    private void processQueue(TreeSet<Point> queue) {
        while (!queue.isEmpty()) {
            final Point point = queue.pollFirst();
            assert point != null;
            for (final Point adjacent : getNonBlockedNeighbours(point)) {
                if (distanceMap.get(point) == Integer.MAX_VALUE) {
                    break;
                }
                final int currentDistance = distanceMap.get(adjacent);
                final int proposedDistance = distanceMap.get(point) + 1;
                if (proposedDistance < currentDistance) {
                    distanceMap.put(adjacent, proposedDistance);
                    shortestEdge.put(adjacent, point);
                    queue.remove(adjacent);
                    queue.add(adjacent);
                }
                // Tie-break equidistant points by their reading order
                if (proposedDistance == currentDistance) {
                    if (POINT_READING_ORDER.compare(getFirstPoint(point), getFirstPoint(shortestEdge.get(adjacent))) < 0) {
                        shortestEdge.put(adjacent, point);
                    }
                }
            }
        }
    }

    public List<Point> getNonBlockedNeighbours(@NotNull final Point point) {
        if (!cachedNeighbours.containsKey(point)) {
            final List<Point> neighbours = new ArrayList<>(4);

            // Add left neighbour
            Point p = new Point(point.x - 1, point.y);
            if (isValid(p)) {
                neighbours.add(p);
            }

            // Add right neighbour
            p = new Point(point.x + 1, point.y);
            if (isValid(p)) {
                neighbours.add(p);
            }

            // Add top neighbour
            p = new Point(point.x, point.y - 1);
            if (isValid(p)) {
                neighbours.add(p);
            }

            // Add bottom neighbour
            p = new Point(point.x, point.y + 1);
            if (isValid(p)) {
                neighbours.add(p);
            }

            cachedNeighbours.put(point, neighbours);
        }

        return cachedNeighbours.get(point);
    }

    private Point getFirstPoint(Point point) {
        return getShortestPath(point).getFirst();
    }

    public LinkedList<Point> getShortestPath(Point point) {
        final LinkedList<Point> path = new LinkedList<>();

        Point p = point;

        while (p != null) {
            path.addFirst(p);
            p = shortestEdge.get(p);
        }

        path.removeFirst();

        return path;
    }

    private boolean isValid(Point point) {
        return isInVerticalBoundaries(point) && isInHorizontalBoundaries(point) && !blockedCoordinates.containsKey(point);
    }

    private boolean isInHorizontalBoundaries(Point point) {
        return point.y >= 0 && point.y < map.length;
    }

    private boolean isInVerticalBoundaries(Point point) {
        return point.x >= 0 && point.x < map[0].length;
    }

    public int getDistance(Point point) {
        return distanceMap.getOrDefault(point, Integer.MAX_VALUE);
    }

    public boolean unreachable(@NotNull final Point point) {
        return !distanceMap.containsKey(point);
    }
}
