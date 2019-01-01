package aoc2018.helper;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;

public class AStarGridFinder {

    private static final int NEITHER = 0;
    private static final int TORCH = 1;
    private static final int CLIMBING_GEAR = 2;

    private final int[][] grid;
    private final Point start;
    private final Point target;

    private Map<String, QueueNode> visited = new HashMap<>();

    public AStarGridFinder(int[][] grid, Point start, Point target) {
        this.grid = grid;
        this.start = start;
        this.target = target;

        processQueue(getQueue());
    }

    public int getTotalTime() {
        List<QueueNode> track = new ArrayList<>();
        QueueNode queueNode = visited.get(target.x + "|" + target.y + "|" + TORCH);
        while (queueNode.getPredecessor() != null) {
            track.add(0, queueNode);
            queueNode = queueNode.getPredecessor();
        }
        track.add(0, getTargetNode());

        track.add(0, visited.get(start.x + "|" + start.y + "|" + TORCH));

//        for (QueueNode node : track) {
//            System.out.println("Current square: " + node.getSelf().x + "/" + node.getSelf().y + " with toolType: " + node.getTool() + "." +
//                    " Terrain is " + grid[node.getSelf().y][node.getSelf().x] + "." +
//                    " Reached in " + node.getPassedTime() + " seconds");
//        }

        return track.get(track.size() - 1).getPassedTime();
    }

    private QueueNode getTargetNode() {
        QueueNode torchNode = getNode(target.x, target.y, TORCH);
        QueueNode neitherNode = getNode(target.x, target.y, NEITHER);
        QueueNode gearNode = getNode(target.x, target.y, CLIMBING_GEAR);

        Optional<QueueNode> concurringTarget = getConcurringTarget(neitherNode, gearNode);

        if (concurringTarget.isPresent()) {
            QueueNode concurringNode = concurringTarget.get();
            if (concurringNode.getPassedTime() <= torchNode.getPassedTime() - 7) {
                return concurringNode;
            } else {
                return torchNode;
            }
        } else {
            return torchNode;
        }
    }

    private Optional<QueueNode> getConcurringTarget(QueueNode neitherNode, QueueNode gearNode) {
        if (neitherNode == null && gearNode == null) {
            return Optional.empty();
        } else if (neitherNode == null) {
            return Optional.of(gearNode);
        } else if (gearNode == null) {
            return Optional.of(neitherNode);
        } else if (neitherNode.getPassedTime() < gearNode.getPassedTime()) {
            return Optional.of(neitherNode);
        } else if (gearNode.getPassedTime() < neitherNode.getPassedTime()) {
            return Optional.of(gearNode);
        }

        return Optional.empty();
    }

    private QueueNode getNode(int x, int y, int tool) {
        return visited.get(x + "|" + y + "|" + tool);
    }

    private void processQueue(TreeSet<QueueNode> queue) {
        while (!queue.isEmpty()) {

            //printQueue(queue);
            QueueNode queueNode = queue.pollFirst();
            assert queueNode != null;

            int type = grid[queueNode.getSelf().y][queueNode.getSelf().x];

            for (Point connectedPoint : getConnectedNodes(queueNode)) {
                int proposedDistance;
                int time;
                int tool;
                int connectedType = grid[connectedPoint.y][connectedPoint.x];

                if (connectedType != queueNode.getTool()) {
                    proposedDistance = queueNode.getPassedTime() + 1 + getManhattanDistance(connectedPoint, target);
                    tool = queueNode.getTool();
                    time = queueNode.getPassedTime() + 1;
                } else {
                    proposedDistance = queueNode.getPassedTime() + 8 + getManhattanDistance(connectedPoint, target);
                    tool = getSharedTool(type, connectedType);
                    time = queueNode.getPassedTime() + 8;
                }

                QueueNode connectedNode = getQueueNode(connectedPoint, tool);
                Integer currentDistance = connectedNode.getDistance();
                if (proposedDistance <= currentDistance) {
                    connectedNode.setPredecessor(queueNode);
                    connectedNode.setDistance(proposedDistance);
                    connectedNode.setTool(tool);
                    connectedNode.setPassedTime(time);
                    queue.remove(connectedNode);
                    queue.add(connectedNode);
                }
                visited.put(connectedNode.getKey(), connectedNode);
            }

            if (queueNode.getSelf().equals(target)) {
                if (queueNode.getTool() == TORCH || !targetReachedWithTorchInNext7Steps(queue, queueNode)) {
                    break;
                }
            }
        }
    }

    private boolean targetReachedWithTorchInNext7Steps(TreeSet<QueueNode> queue, QueueNode queueNode) {
        return queue.stream()
                .filter(n -> n.getDistance() <= queueNode.getDistance() + 6)
                .filter(n -> n.getSelf().x == target.x && n.getSelf().y == target.y)
                .anyMatch(n -> n.getTool() == TORCH);
    }

    private void printQueue(TreeSet<QueueNode> queue) {
        System.out.println("------------------");
        Iterator<QueueNode> iterator = queue.iterator();

        while (iterator.hasNext()) {
            QueueNode next = iterator.next();
            System.out.println(next.getSelf().x + "/" + next.getSelf().y + " " + next.getDistance() + " " + next.getTool());
        }
    }

    private QueueNode getQueueNode(Point connectedPoint, int tool) {
        QueueNode queueNode;
        String hash = connectedPoint.x + "|" + connectedPoint.y + "|" + tool;
        if (visited.containsKey(hash)) {
            queueNode = visited.get(hash);
        } else {
            queueNode = new QueueNode(connectedPoint);
            queueNode.setTool(tool);
            queueNode.setDistance(Integer.MAX_VALUE);
            queueNode.setPassedTime(0);
        }

        return queueNode;
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

    private TreeSet<QueueNode> getQueue() {
        final Comparator comparator = Comparator.comparingInt(QueueNode::getDistance)
                .thenComparingInt(QueueNode::getY)
                .thenComparingInt(QueueNode::getX)
                .thenComparingInt(QueueNode::getTool);

        final TreeSet<QueueNode> queue = new TreeSet<>(comparator);

        QueueNode queueNode = new QueueNode(start);
        queueNode.setTool(TORCH);
        queueNode.setDistance(getManhattanDistance(start, target));
        queueNode.setPassedTime(0);
        queue.add(queueNode);

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
