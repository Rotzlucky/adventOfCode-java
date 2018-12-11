package aoc2018;

import aoc.Day;

import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 extends Day {

    public Day11(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        Integer gridSerialNumber = Integer.valueOf(inputs.get(0));

        Map<Point, Integer> blockPowerLevels = calculateBlockPowerLevels(gridSerialNumber, 2);

        Point maxPowerLevelKey = Collections.max(blockPowerLevels.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

        printSolution(1, maxPowerLevelKey.x + "," + maxPowerLevelKey.y);
    }

    private Map<Point, Integer> calculateBlockPowerLevels(Integer gridSerialNumber, int size) {
        Map<Point, Integer> cellPowerLevel = new HashMap<>();
        Map<Point, Integer> blockPowerLevel = new HashMap<>();

        int limit = 301 - size;
        for (int x = 1; x <= limit; x++) {
            for (int y = 1; y <= limit; y++) {
                Point powerCell = new Point(x, y);
                blockPowerLevel.put(powerCell, calculateBlockPowerLevel(powerCell, gridSerialNumber, cellPowerLevel, size));
            }
        }

        return blockPowerLevel;
    }

    @Override
    protected void part2(List<String> inputs) {
        Integer gridSerialNumber = Integer.valueOf(inputs.get(0));

        Map<Integer, Map<Point, Integer>> blockPowerLevels = new HashMap<>();

        for (int i = 1; i < 300; i++) {
            blockPowerLevels.put(i, calculateBlockPowerLevels(gridSerialNumber, i));
        }

    }

    private Integer calculateBlockPowerLevel(Point powerCell, Integer gridSerialNumber, Map<Point, Integer> cellPowerLevel, int size) {
        Integer blockPowerLevel = 0;

        for (int i = powerCell.x; i < powerCell.x + size; i++) {
            for (int j = powerCell.y; j < powerCell.y + size; j++) {
                Point cell = new Point(i, j);
                blockPowerLevel += cellPowerLevel.getOrDefault(cellPowerLevel.get(cell), calculateCellPowerLevel(cell, gridSerialNumber));
            }
        }

        return blockPowerLevel;
    }

    private Integer calculateCellPowerLevel(Point powerCell, Integer gridSerialNumber) {
        double rackId = powerCell.getX() + 10;
        double powerLevel = (((rackId * powerCell.getY()) + gridSerialNumber) * rackId) / 1000;
        powerLevel = powerLevel - Math.floor(powerLevel);
        return (int) (powerLevel * 10) - 5;
    }
}
