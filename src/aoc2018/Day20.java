package aoc2018;

import aoc.Day;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Day20 extends Day {

    public Day20(String year, String day) {
        super(year, day);
    }

    private Map<Point, Integer> distances;

    @Override
    protected void part1(List<String> inputs) {
        distances = calculateDistances(inputs);

        Integer solution = distances.values().stream().max(Integer::compare).orElse(0);
        printSolution(1, solution);
    }

    @Override
    protected void part2(List<String> inputs) {
        long count = distances.values().stream().filter(v -> v >= 1000).count();
        printSolution(2, count);
    }

    private Map<Point, Integer> calculateDistances(List<String> inputs) {
        Stack<Point> junction = new Stack();

        Point current = new Point(0, 0);
        Point previous = new Point(0, 0);
        Map<Point, Integer> distances = new HashMap<>();
        distances.put(current, 0);
        for (char character : inputs.get(0).toCharArray()) {
            switch (character) {
                case '(':
                    junction.push(new Point(current.x, current.y));
                    break;
                case ')':
                    current = junction.pop();
                    break;
                case '|':
                    current = junction.peek();
                    break;
                default: {
                    switch (character) {
                        case 'N':
                            current = new Point(current.x, current.y - 1);
                            break;
                        case 'S':
                            current = new Point(current.x, current.y + 1);
                            break;
                        case 'E':
                            current = new Point(current.x + 1, current.y);
                            break;
                        case 'W':
                            current = new Point(current.x - 1, current.y);
                            break;
                    }
                    final var proposed = distances.get(previous) + 1;
                    distances.compute(current, (k, v) -> (v == null) ? proposed : Math.min(v, proposed));
                    break;
                }
            }

            previous = new Point(current.x, current.y);
        }
        return distances;
    }
}
