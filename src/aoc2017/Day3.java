package aoc2017;

import aoc.Day;

import java.awt.*;
import java.util.List;

public class Day3 extends Day {

    public Day3(String year, String day) {
        super(year, day);
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
        int position = (int) diff % (rows - 1);
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
        int right = 1;
        int up = 2;
        int left = 3;
        int down = 4;

        Integer goal = Integer.valueOf(inputs.get(0));

        int x = 500;
        int y = 500;
        int[][] matrix = new int[999][999];

        matrix[x][y] = 1;
        int current = 1;
        int steps = 0;
        int direction = 0;

        boolean run = true;

        while (run) {
            if (direction % 2 == 0) {
                steps++;
            }
            direction++;

            whileLoop:
            for (int i = 1; i <= steps; i++) {
                if (direction == right) {
                    x++;
                } else if (direction == up) {
                    y--;
                } else if (direction == left) {
                    x--;
                } else if (direction == down) {
                    y++;
                }
                current = calculate(matrix, x, y);
                matrix[x][y] = current;
                if (current > goal) {
                    run = false;
                    break;
                }
            }

            if (direction == 4) {
                direction = 0;
            }

        }

        printSolution(1, current);
    }

    private int calculate(int[][] matrix, int x, int y) {
        int current = 0;

        current += matrix[x + 1][y];
        current += matrix[x + 1][y + 1];
        current += matrix[x + 1][y - 1];
        current += matrix[x - 1][y];
        current += matrix[x - 1][y + 1];
        current += matrix[x - 1][y - 1];
        current += matrix[x][y + 1];
        current += matrix[x][y - 1];

        return current;
    }
}
