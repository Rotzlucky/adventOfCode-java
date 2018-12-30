package aoc2018;

import aoc.Day;
import aoc2018.helper.AStarGridFinder;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day22 extends Day {

    private int depth;
    private Point target;

    Map<Point, Double> erosionLevels = new HashMap<>();

    public Day22(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        prepareInput(inputs);

        int[][] cave = new int[target.y + 1][target.x + 1];

        int riskLevel = 0;

        for (int y = 0; y < cave.length; y++) {
            String row = "";
            for (int x = 0; x < cave[0].length; x++) {
                int type = getType(x, y);

                if (type == 0) {
                    row += ".";
                } else if (type == 1) {
                    row += "=";
                } else if (type == 2) {
                    row += "|";
                }

                riskLevel += type;
            }
            //System.out.println(row);
        }

        printSolution(1, riskLevel);
    }

    @Override
    protected void part2(List<String> inputs) {
        prepareInput(inputs);

        int[][] cave = new int[target.y * 2][target.x * 6];

        for (int y = 0; y < cave.length; y++) {
            for (int x = 0; x < cave[0].length; x++) {
                cave[y][x] = getType(x, y);
            }
        }

        AStarGridFinder aStarGridFinder = new AStarGridFinder(cave, new Point(0, 0), target);

        printSolution(2, aStarGridFinder.getTotalTime());
    }

    private int getType(int x, int y) {
        Point key = new Point(x, y);
        Double value;
        if ((x == 0 && y == 0) || (y == target.y && x == target.x)) {
            value = 0D;
        } else if (y == 0) {
            value = x * 16807D;
        } else if (x == 0) {
            value = y * 48271D;
        } else {
            Double left = erosionLevels.get(new Point(x-1, y));
            Double top = erosionLevels.get(new Point(x, y-1));
            value = left * top;
        }
        double erosionLevel = (value + depth) % 20183;
        erosionLevels.put(key, erosionLevel);

        int type = ((int) erosionLevel) % 3;
        return type;
    }

    private void prepareInput(List<String> inputs) {
        depth = Integer.parseInt(inputs.get(0).replaceAll("depth: (\\d*)", "$1"));
        int x = Integer.parseInt(inputs.get(1).replaceAll("target: (\\d*),(\\d*)", "$1"));
        int y = Integer.parseInt(inputs.get(1).replaceAll("target: (\\d*),(\\d*)", "$2"));
        target = new Point(x, y);
    }
}
