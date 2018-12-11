package aoc2018;

import aoc.Day;
import aoc2018.helper.PowerBlock;

import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day11 extends Day {

    public Day11(String year, String day) {
        super(year, day);
    }

    private Map<PowerBlock, Integer> cellPowerLevels;

    @Override
    protected void part1(List<String> inputs) {
        Integer gridSerialNumber = Integer.valueOf(inputs.get(0));

        cellPowerLevels = calculateCellPowerLevels(gridSerialNumber);

        for (int i = 2; i <= 3; i++) {
            calculateBlockPowerLevels(i);
            System.out.println(i + " - " + new Date(System.currentTimeMillis()));
        }

        PowerBlock maxPowerLevelKey = Collections.max(cellPowerLevels.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

        printSolution(1, maxPowerLevelKey.getX() + "," + maxPowerLevelKey.getY());
    }

    @Override
    protected void part2(List<String> inputs) {
        for (int i = 4; i <= 300; i++) {
            calculateBlockPowerLevels(i);
            System.out.println(i + " - " + new Date(System.currentTimeMillis()));
        }

        PowerBlock maxPowerLevelKey = Collections.max(cellPowerLevels.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

        printSolution(1, maxPowerLevelKey.getX() + "," + maxPowerLevelKey.getY() + "," + maxPowerLevelKey.getSize());

    }

    private Integer calculate(PowerBlock powerCell, int size) {
        Integer blockPowerLevel = 0;

        for (int j = powerCell.getY(); j < powerCell.getY() + size; j++) {
            PowerBlock cell = new PowerBlock(powerCell.getX() + size - 1, j, 1);
            blockPowerLevel += cellPowerLevels.get(cell);
        }
        for (int j = powerCell.getX(); j < powerCell.getX() + size - 1; j++) {
            PowerBlock cell = new PowerBlock(j, powerCell.getY() + size - 1, 1 );
            blockPowerLevel += cellPowerLevels.get(cell);
        }

        return blockPowerLevel;
    }


    private void calculateBlockPowerLevels(int size) {
        int limit = 301 - size;

        for (int i = 1; i <= limit; i++) {
            for (int j = 1; j <= limit; j++) {
                Integer blockValue = 0;
                PowerBlock powerBlock = new PowerBlock(i, j, size);
                blockValue += cellPowerLevels.get(new PowerBlock(i, j, size-1));
                blockValue += calculate(powerBlock, size);
                cellPowerLevels.put(powerBlock, blockValue);
            }
        }
    }

    private Map<PowerBlock, Integer> calculateCellPowerLevels(Integer gridSerialNumber) {
        Map<PowerBlock, Integer> cellPowerLevel = new HashMap<>();
        for (int i = 1; i <= 300; i++) {
            for (int j = 1; j <= 300; j++) {
                PowerBlock cell = new PowerBlock(i, j, 1);
                cellPowerLevel.put(cell, calculateCellPowerLevel(cell, gridSerialNumber));
            }
        }

        return cellPowerLevel;
    }

    private Integer calculateCellPowerLevel(PowerBlock powerCell, Integer gridSerialNumber) {
        double rackId = powerCell.getX() + 10;
        double powerLevel = (((rackId * powerCell.getY()) + gridSerialNumber) * rackId) / 1000;
        powerLevel = powerLevel - Math.floor(powerLevel);
        return (int) (powerLevel * 10) - 5;
    }
}
