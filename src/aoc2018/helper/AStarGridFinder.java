package aoc2018.helper;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.ToIntFunction;

public class AStarGridFinder {

    private static final int NEITHER = 0;
    private static final int TORCH = 1;
    private static final int CLIMBING_GEAR = 2;

    private final int[][] grid;
    private final Point start;
    private final Point target;

    private Map<Point, Integer> distances = new HashMap<>();
    private Map<Point, QueueNode> visited = new HashMap<>();

    public AStarGridFinder(int[][] grid, Point start, Point target) {
        this.grid = grid;
        this.start = start;
        this.target = target;

        processQueue(getQueue());
    }

    public int getTotalTime() {
        List<QueueNode> track = new ArrayList<>();
        QueueNode queueNode = visited.get(target);
        while (queueNode.getPredecessor() != null) {
            track.add(0, queueNode);
            queueNode = visited.get(queueNode.getPredecessor());
        }
        track.add(0, visited.get(start));

//        for (QueueNode node : track) {
//            System.out.println("Current square: " + node.getSelf().x + "/" + node.getSelf().y + " with toolType: " + node.getTool() + "." +
//                    " Terrain is " + grid[node.getSelf().y][node.getSelf().x] + "." +
//                    " Reached in " + node.getPassedTime() + " seconds");
//        }

        int maxX = track.stream().mapToInt(node -> node.getSelf().x).max().orElse(0);
        System.out.println("Max X is " + maxX + " at gridWidth: " + grid[0].length);

        return track.get(track.size() - 1).getPassedTime();
    }

    private void processQueue(PriorityQueue<Point> queue) {
        while (!queue.isEmpty()) {
            Point point = queue.poll();
            assert point != null;

            QueueNode queueNode = visited.get(point);
            // on firstNode initialize
            if (queueNode.getTool() == -1) {
                queueNode.setTool(TORCH); // set default tool as torch
                queueNode.setPassedTime(0);
            }

            int type = grid[point.y][point.x];

            for (Point connectedPoint : getConnectedNodes(queueNode)) {
                QueueNode connectedNode = visited.get(connectedPoint);
                Integer currentDistance = distances.get(connectedPoint);
                int connectedType = grid[connectedPoint.y][connectedPoint.x];

                int proposedDistance;
                int time;
                int tool;
                if (connectedType != queueNode.getTool()) {
                    proposedDistance = queueNode.getPassedTime() + 1 + getManhattanDistance(connectedPoint, target);
                    tool = queueNode.getTool();
                    time = queueNode.getPassedTime() + 1;
                } else {
                    proposedDistance = queueNode.getPassedTime() + 8 + getManhattanDistance(connectedPoint, target);
                    tool = getSharedTool(type, connectedType);
                    time = queueNode.getPassedTime() + 8;
                }

                if (proposedDistance < currentDistance) {
                    connectedNode.setPredecessor(queueNode.getSelf());
                    distances.put(connectedPoint, proposedDistance);
                    connectedNode.setTool(tool);
                    connectedNode.setPassedTime(time);
                    queue.remove(connectedPoint);
                    queue.add(connectedPoint);
                }
            }

            if (queueNode.getSelf().equals(target)) {
                if (queueNode.getTool() != TORCH) {
                    queueNode.setTool(TORCH);
                    queueNode.setPassedTime(queueNode.getPassedTime() + 7);
                }
                break;
            }
        }
    }

    private int getSharedTool(int type, int connectedType) {
        if ((type == NEITHER && connectedType == TORCH)|| (type == TORCH && connectedType == NEITHER) ) {
            return CLIMBING_GEAR;
        } else if ((type == TORCH && connectedType == CLIMBING_GEAR)|| (type == CLIMBING_GEAR && connectedType == TORCH) ) {
            return NEITHER;
        } else if ((type == NEITHER && connectedType == CLIMBING_GEAR)|| (type == CLIMBING_GEAR && connectedType == NEITHER) ) {
            return TORCH;
        }

        return -1;
    }

    private PriorityQueue<Point> getQueue() {

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                QueueNode queueNode = new QueueNode(new Point(j, i));
                distances.put(queueNode.getSelf(), Integer.MAX_VALUE);
                visited.put(queueNode.getSelf(), queueNode);
            }
        }

        distances.put(start, getManhattanDistance(start, target));

        final Comparator comparator = Comparator.comparingInt((ToIntFunction<Point>) distances::get);

        final PriorityQueue<Point> queue = new PriorityQueue<>(comparator);
        queue.addAll(visited.keySet());
        return queue;
    }

    private Integer getManhattanDistance(Point point1, Point point2) {
        return Math.abs(point1.x - point2.x) + Math.abs(point1.y - point2.y);
    }

    private List<Point> getConnectedNodes(QueueNode queueNode) {
        final List<Point> neighbours = new ArrayList<>(4);

        Point point = queueNode.getSelf();

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

        if (queueNode.getPredecessor() != null) {
            neighbours.remove(queueNode.getPredecessor());
        }

        return neighbours;
    }

    private boolean isValid(Point point) {
        return isInVerticalBoundaries(point) && isInHorizontalBoundaries(point);
    }

    private boolean isInHorizontalBoundaries(Point point) {
        return point.y >= 0 && point.y < grid.length;
    }

    private boolean isInVerticalBoundaries(Point point) {
        return point.x >= 0 && point.x < grid[0].length;
    }
}
