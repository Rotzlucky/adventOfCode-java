package aoc2018;

import aoc.Day;
import aoc2018.helper.Star;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10 extends Day {
    public Day10(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        List<Star> stars = getStars(inputs);

        int i = 0;
        Map<Integer, Integer> deltas = calculateDeltas(stars, i);

        int minDelta = Collections.min(deltas.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

        i = 0;
        stars = getStars(inputs);

        printSolution(1, "");

        while (true) {
            updateStars(stars);

            if (i == minDelta) {
                printStars(stars);
                return;
            }

            i++;
        }
    }

    @Override
    protected void part2(List<String> inputs) {
        List<Star> stars = getStars(inputs);

        int i = 0;
        Map<Integer, Integer> deltas = calculateDeltas(stars, i);

        int minDeltaKey = Collections.min(deltas.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

        printSolution(2, minDeltaKey + 1);
    }

    private Map<Integer, Integer> calculateDeltas(List<Star> stars, int i) {
        Map<Integer, Integer> deltas = new HashMap<>();
        while (i < 150000) {
            updateStars(stars);

            deltas.put(i, getMinDeltas(stars));

            i++;
        }
        return deltas;
    }

    private int getMinDeltas(List<Star> stars) {
        int minX = stars.stream().mapToInt(Star::getX).min().orElse(0);
        int maxX = stars.stream().mapToInt(Star::getX).max().orElse(0);
        int minY = stars.stream().mapToInt(Star::getY).min().orElse(0);
        int maxY = stars.stream().mapToInt(Star::getY).max().orElse(0);

        return (maxX - minX) + (maxY - minY);
    }

    private void printStars(List<Star> stars) {
        int minX = stars.stream().mapToInt(Star::getX).min().orElse(0);
        int maxX = stars.stream().mapToInt(Star::getX).max().orElse(0);
        int minY = stars.stream().mapToInt(Star::getY).min().orElse(0);
        int maxY = stars.stream().mapToInt(Star::getY).max().orElse(0);

        //System.out.println((maxX - minX) + "/" + (maxY - minY));

        for (int y = minY; y < maxY + 1; y++) {
            String row = "";
            for (int x = minX; x < maxX + 1; x++) {
                if (starIsInCoordinate(stars, x, y)) {
                    row += "#";
                } else {
                    row += ".";
                }
            }
            System.out.println(row);
        }
    }

    private boolean starIsInCoordinate(List<Star> stars, int x, int y) {
        for (Star star : stars) {
            if (star.getX() == x && star.getY() == y) {
                return true;
            }
        }

        return false;
    }

    private void updateStars(List<Star> stars) {
        for (Star star : stars) {
            star.move();
        }
    }

    private List<Star> getStars(List<String> inputs) {
        List<Star> stars = new ArrayList<>();

        for (String input : inputs) {
            stars.add(getStar(input));
        }

        return stars;
    }

    private Star getStar(String input) {
        Pattern pattern = Pattern.compile("-?\\d{1,5}");
        Matcher matcher = pattern.matcher(input);

        matcher.find();
        Integer x = Integer.valueOf(matcher.group(0));
        matcher.find();
        Integer y = Integer.valueOf(matcher.group(0));
        matcher.find();
        Integer dx = Integer.valueOf(matcher.group(0));
        matcher.find();
        Integer dy = Integer.valueOf(matcher.group(0));
        return new Star(x, y, dx, dy);
    }
}
