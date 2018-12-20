package aoc2018;

import aoc.Day;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a full iterative approach
 * Processing this code takes about 4 seconds. We cut of the first 400 unused columns, otherwise it would take around 12 seconds
 *
 * For future reference I include a link to a full recursive solution that I would like to maybe tackle when I try this with another language
 * https://github.com/akaritakai/AdventOfCode2018/blob/master/src/main/java/net/akaritakai/aoc2018/Problem17.java
 */
public class Day17 extends Day {

    private Set<Point> clay;
    private char[][] cave;

    public Day17(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        processInput(inputs);
        cave = buildCave();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            //printCave();
            if (processFlow()) {
                break;
            }
        }

        printCave();

        int minY = clay.stream().mapToInt(p -> p.y).min().orElseThrow();
        int count = countFlowAndSettle(minY);

        printSolution(1, count);
    }

    @Override
    protected void part2(List<String> inputs) {
        int minY = clay.stream().mapToInt(p -> p.y).min().orElseThrow();
        int count = countSettle(minY);

        printSolution(2, count);
    }

    private int countSettle(int minY) {
        int count = 0;

        for (int i = minY; i < cave.length; i++) {
            for (int j = 0; j < cave[i].length; j++) {
                if (cave[i][j] == '~') {
                    count++;
                }
            }
        }

        return count;
    }

    private int countFlowAndSettle(int minY) {
        int count = 0;

        for (int i = minY; i < cave.length; i++) {
            for (int j = 0; j < cave[i].length; j++) {
                if (cave[i][j] == '|' || cave[i][j] == '~') {
                    count++;
                }
            }
        }

        return count;
    }

    private boolean processFlow() {
        boolean noneNew = true;

        for (int i = 0; i < cave.length; i++) {
            for (int j = 0; j < cave[i].length; j++) {
                if (pourDown(i, j)) {
                    cave[i + 1][j] = '|';
                    noneNew = false;
                }
                if (settle(i, j)) {
                    cave[i][j] = '~';
                    noneNew = false;
                }
                if (settleFlow(i, j)) {
                    cave[i][j] = '~';
                    noneNew = false;
                }
                if (overflow(i, j)) {
                    cave[i][j] = '|';
                    noneNew = false;
                }
            }
        }

        return noneNew;
    }

    /**
     * Check if the current sand tile becomes an overflow tile
     */
    private boolean overflow(int i, int j) {
        if (cave[i][j] != '.') {
            return false;
        }

        if (j + 1 >= cave[0].length || j - 1 < 0 || i + 1 == cave.length) {
            return false;
        }

        // since this happens after the settle check we don't have to check for a single boundary.
        // instead we just check if we are over clay or water and if the tile directly to the left or right is a flow tile
        if ((cave[i + 1][j] == '~' || cave[i + 1][j] == '#') && (cave[i][j - 1] == '|' || cave[i][j + 1] == '|' )) {
            return true;
        }

        // to actually get over the right edge of a pocket we check the tile to the left and the tile below that
        if (cave[i][j - 1] == '|' && cave[i + 1][j - 1] == '#') {
            return true;
        }

        // to actually get over the left edge of a pocket we check the tile to the right and the tile below that
        if (cave[i][j + 1] == '|' && cave[i + 1][j + 1] == '#') {
            return true;
        }

        return false;
    }

    /**
     * Check if the current flow tile will transform into settled water.
     *
     * Exit early if we break the cave boundaries
     */
    private boolean settleFlow(int i, int j) {
        if (cave[i][j] != '|') {
            return false;
        }

        if (j + 1 >= cave[0].length || j - 1 < 0 || i + 1 == cave.length) {
            return false;
        }

        // This check is for those 1 width pockets which ruined my first try on the part 2 solution
        if (cave[i][j - 1] == '#' && cave[i][j + 1] == '#' && (cave[i + 1][j] == '#' || cave[i + 1][j] == '~')) {
            return true;
        }

        // Only if all tiles in this line (besides the flow tile) are already water tiles, it is allowed to transform the flow tile
        // otherwise we would leave on side of the flow tile forever as sand
        return (cave[i][j - 1] == '~' && cave[i][j + 1] != '.') || (cave[i][j + 1] == '~' && cave[i][j - 1] != '.');
    }

    /**
     * Check if a the given tile will become settled water by testing if we are between boundaries and
     * a flow tile is actually within the boundaries
     */
    private boolean settle(int i, int j) {
        if (cave[i][j] != '.') {
            return false;
        }

        int leftBoundary = leftBoundaryExists(i, j);
        int rightBoundary = rightBoundaryExists(i, j);
        if (leftBoundary > 0 && rightBoundary > 0) {
            return downPourExists(leftBoundary, rightBoundary, i);
        }

        return false;
    }

    /**
     * Only that pockets that are reached by a flow tile can produce settled water
     */
    private boolean downPourExists(int leftBoundary, int rightBoundary, int i) {
        for (int j = leftBoundary; j < rightBoundary ; j++) {
            if (cave[i][j] == '|') {
                return true;
            }
        }

        return false;
    }

    /**
     * As long as we are not at the bottom of the cave check if the current tile has a right clay boundary
     *
     * We check for every tile right of the given tile if it is over water or clay until we hit clay on the right.
     * Because of process order it could also be possible to have flow tile below temporarily
     * The index for the clay boundary is then returned.
     */
    private int rightBoundaryExists(int i, int j) {
        int k = j;

        if (i + 1 == cave.length) {
            return -1;
        }

        while (k < cave[i].length && cave[i][k] != '#') {
            if (cave[i + 1][k] != '~' && cave[i + 1][k] != '#' && cave[i + 1][k] != '|') {
                return -1;
            }
            k++;
        }

        return k;
    }

    /**
     * As long as we are not at the bottom of the cave check if the current tile has a left clay boundary
     *
     * We check for every tile left of the given tile if it is over water or clay until we hit clay on the left.
     * Because of process order it could also be possible to have flow tile below temporarily
     * The index for the clay boundary is then returned.
     */
    private int leftBoundaryExists(int i, int j) {
        int k = j;

        if (i + 1 == cave.length) {
            return -1;
        }

        while (k >= 0 && cave[i][k] != '#') {
            if (cave[i + 1][k] != '~' && cave[i + 1][k] != '#' && cave[i + 1][k] != '|') {
                return -1;
            }
            k--;
        }

        return k;
    }

    /**
     * As long as we don't reach the end check for the tile below
     *
     * If the current tile is the source or a flow tile
     * and the one below is sand we return true
     */
    private boolean pourDown(int i, int j) {
        if (i + 1 == cave.length) {
            return false;
        }

        return (cave[i][j] == '+' && cave[i + 1][j] == '.') ||
                (cave[i][j] == '|' && cave[i + 1][j] == '.');
    }

    private void processInput(List<String> inputs) {
        final var regex = "^[xy]=(\\d+), [xy]=(\\d+)..(\\d+)$";

        clay = new HashSet<>();
        for (String line : inputs) {
            if (line.startsWith("x")) {
                final var x = Integer.parseInt(line.replaceAll(regex, "$1")) - 400;
                final var yMin = Integer.parseInt(line.replaceAll(regex, "$2"));
                final var yMax = Integer.parseInt(line.replaceAll(regex, "$3"));
                for (var y = yMin; y <= yMax; y++) {
                    clay.add(new Point(x, y));
                }
            }
            if (line.startsWith("y")) {
                final var xMin = Integer.parseInt(line.replaceAll(regex, "$2")) - 400;
                final var xMax = Integer.parseInt(line.replaceAll(regex, "$3")) - 400;
                final var y = Integer.parseInt(line.replaceAll(regex, "$1"));
                for (var x = xMin; x <= xMax; x++) {
                    clay.add(new Point(x, y));
                }
            }
        }
    }

    private char[][] buildCave() {
        int maxY = clay.stream().mapToInt(p -> p.y).max().orElseThrow();
        int maxX = clay.stream().mapToInt(p -> p.x).max().orElseThrow();

        char[][] cave = new char[maxY + 1][maxX + 1];

        for (int i = 0; i < cave.length; i++) {
            for (int j = 0; j < cave[i].length; j++) {
                if (clay.contains(new Point(j, i))) {
                    cave[i][j] = '#';
                } else {
                    if (i == 0 && j == 100) {
                        cave[i][j] = '+';
                    } else {
                        cave[i][j] = '.';
                    }
                }
            }
        }

        return cave;
    }

    private void printCave() {
        for (int i = 0; i < cave.length; i++) {
            var row = "";
            for (int j = 0; j < cave[i].length; j++) {
                row += cave[i][j];
            }
            System.out.println(row);
        }
        System.out.println();
    }
}
