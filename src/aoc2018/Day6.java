package aoc2018;

import aoc.Day;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Day6 extends Day {
    public Day6(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        List<Point> coordinates = new ArrayList<>();
        for (String input : inputs) {
            Point coordinate = getPoint(input);
            coordinates.add(coordinate);
        }

        List<Point> infinites = getInfinites(coordinates);
        Map<Point, Integer> finite = new HashMap<>();

        for (int i = 1; i < 999; i++) {
            for (int j = 1; j < 999; j++) {
                getNearestCoordinate(new Point(i, j), coordinates).ifPresent(point -> {
                    if (!infinites.contains(point)) {
                        finite.put(point, finite.getOrDefault(point, 0) + 1 );
                    }
                });
            }
        }

        int max = finite.values().stream().mapToInt(Integer::intValue).max().orElse(0);

        printSolution(1, max);
    }

    @Override
    protected void part2(List<String> inputs) {
        List<Point> coordinates = new ArrayList<>();
        for (String input : inputs) {
            Point coordinate = getPoint(input);
            coordinates.add(coordinate);
        }

        int count = 0;
        for (int i = 1; i < 999; i++) {
            for (int j = 1; j < 999; j++) {
                int sum = getDistanceSum(new Point(i, j), coordinates);

                if (sum < 10000) {
                    count++;
                }
            }
        }

        printSolution(2, count);
    }

    private int getDistanceSum(Point point, List<Point> coordinates) {
        int sum = 0;

        for (Point coordinate : coordinates) {
            sum += Math.abs(coordinate.getX() - point.getX()) + Math.abs(coordinate.getY() - point.getY());
        }

        return sum;
    }

    private List<Point> getInfinites(List<Point> coordinates) {
        List<Point> infinites = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            getNearestCoordinate(new Point(i, 0), coordinates).ifPresent(point -> {
                if (!infinites.contains(point)) {
                    infinites.add(point);
                }
            });
            getNearestCoordinate(new Point(i, 999), coordinates).ifPresent(point -> {
                if (!infinites.contains(point)) {
                    infinites.add(point);
                }
            });
            getNearestCoordinate(new Point(0, i), coordinates).ifPresent(point -> {
                if (!infinites.contains(point)) {
                    infinites.add(point);
                }
            });
            getNearestCoordinate(new Point(999, i), coordinates).ifPresent(point -> {
                if (!infinites.contains(point)) {
                    infinites.add(point);
                }
            });
        }

        return infinites;
    }

    private Optional<Point> getNearestCoordinate(Point point, List<Point> coordinates) {
        Point nearest = null;

        double shortestDistance = -1.0;
        boolean multipleShortestDistance = false;

        for (Point coordinate : coordinates) {
            double distance = Math.abs(point.getX() - coordinate.getX()) + Math.abs(point.getY() - coordinate.getY());

            if (shortestDistance == distance) {
                multipleShortestDistance = true;
            } else if (shortestDistance == -1.0 || shortestDistance > distance) {
                shortestDistance = distance;
                nearest = coordinate;
                multipleShortestDistance = false;
            }
        }

        if (multipleShortestDistance) {
            return Optional.empty();
        }
        return Optional.of(nearest);
    }

    private Point getPoint(String input) {
        String[] split = input.split(",");

        return new Point(Integer.parseInt(split[0].trim()), Integer.parseInt(split[1].trim()));
    }
}
