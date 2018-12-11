package aoc2018;

import aoc.Day;

import java.util.List;

/**
 * Optimized version using https://en.wikipedia.org/wiki/Summed-area_table
 *
 * Inspired by https://www.reddit.com/r/adventofcode/comments/a53r6i/2018_day_11_solutions/
 */
public class Day11 extends Day {

    public Day11(String year, String day) {
        super(year, day);
    }

    private int best = Integer.MIN_VALUE;
    private int bestX = Integer.MIN_VALUE;
    private int bestY = Integer.MIN_VALUE;
    private int bestSize = Integer.MIN_VALUE;

    int[][] sum = new int[301][301];

    @Override
    protected void part1(List<String> inputs) {
        Integer gridSerialNumber = Integer.valueOf(inputs.get(0));

        calculatePowerCellLevels(gridSerialNumber);
        calculatePowerBlockValues(3);

        printSolution(1, bestX + "," + bestY);
    }

    @Override
    protected void part2(List<String> inputs) {
        calculatePowerBlockValues(300);

        printSolution(2, bestX + "," + bestY  + "," + bestSize);
    }

    private void calculatePowerBlockValues(int size) {
        for (int s = 1; s <= size; s++) {
            for (int x = s; x <= 300; x++) {
                for (int y = s; y <= 300; y++) {
                    int total = sum[x][y] - sum[x - s][y] - sum[x][y - s] + sum[x - s][y - s];

                    if (total > best) {
                        best = total;
                        bestX = x - s + 1;
                        bestY = y - s + 1;
                        bestSize = s;
                    }
                }
            }
        }
    }

    private void calculatePowerCellLevels(Integer gridSerialNumber) {
        for (int x = 1; x <= 300; ++x) {
            for (int y = 1; y <= 300; y++) {
                double rackId = x + 10;
                double powerLevel = (((rackId * y) + gridSerialNumber) * rackId) / 1000;
                powerLevel = powerLevel - Math.floor(powerLevel);
                int value = (int) (powerLevel * 10) - 5;

                sum[x][y] = value + sum[x - 1][y] + sum[x][y - 1] - sum[x - 1][y - 1];
            }
        }
    }
}
