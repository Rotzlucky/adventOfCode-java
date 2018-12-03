package aoc2017;

import aoc.Day;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class Day3 extends Day {

    public Day3(String year, String day) {
        super(year, day);
    }

    @Override
    public void solve() {
        solveParts(Collections.singletonList("361527"));
    }

    @Override
    protected void part1(List<String> inputs) {
        String value = inputs.get(0);
        Integer integer = Integer.valueOf(value);

        int previousSquare = 1;
        int square = 1;
        int rows = 1;
        while (square < integer) {
            rows += 2;
            previousSquare = square;
            square = rows * rows;
        }

        double diff = integer - previousSquare;

        Integer edge = (int) Math.ceil(diff / (rows - 1));
        int position = (int )diff % (rows - 1);
        if (position == 0) {
            position = rows - 1;
        }

        int x = 1;
        int y = 1;
        if (edge == 1) {
            x = rows;
            y = rows - position;
        } else if (edge == 2) {
            x = rows - position;
        } else if (edge == 3) {
            y = y + position;
        } else if (edge == 4) {
            x = x + position;
            y = rows;
        }

        int middle = (rows + 1) / 2;

        Point middlePoint = new Point(middle, middle);
        Point valuePoint = new Point(x, y);

        int distance = Math.abs(middlePoint.x - valuePoint.x) + Math.abs(middlePoint.y - valuePoint.y);

        printSolution(1, distance);
    }

    @Override
    protected void part2(List<String> inputs) {

    }
}
