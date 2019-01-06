package aoc2018;

import aoc.Day;
import aoc2018.helper.SpaceTimeCoordinate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Day25 extends Day {

    public Day25(String year, String day) {
        super(year, day);
    }

    ArrayList<ArrayList<SpaceTimeCoordinate>> constellations = new ArrayList<>();

    @Override
    protected void part1(List<String> inputs) {
        LinkedList<SpaceTimeCoordinate> coordinates = getSpaceTimeCoordinates(inputs);

        while (!coordinates.isEmpty()) {
            ArrayList<SpaceTimeCoordinate> newConstellation = new ArrayList<>();
            newConstellation.add(coordinates.removeFirst());

            int size = 0;
            // as long as we add something new to the constellation recheck all remaining coordinates again
            while (size != coordinates.size()) {
                size = coordinates.size();
                Iterator<SpaceTimeCoordinate> iter = coordinates.iterator();
                while (iter.hasNext()) {
                    SpaceTimeCoordinate coordinate = iter.next();
                    for (SpaceTimeCoordinate spaceTimeCoordinate : newConstellation) {
                        if (distance(coordinate, spaceTimeCoordinate) <= 3) {
                            newConstellation.add(coordinate);
                            iter.remove();
                            break;
                        }
                    }
                }
            }

            constellations.add(newConstellation);
        }

        printSolution(1, constellations.size());
    }

    private int distance(SpaceTimeCoordinate first, SpaceTimeCoordinate second) {
        return Math.abs(first.getA() - second.getA()) +
                Math.abs(first.getB() - second.getB()) +
                Math.abs(first.getC() - second.getC()) +
                Math.abs(first.getD() - second.getD());
    }

    private LinkedList<SpaceTimeCoordinate> getSpaceTimeCoordinates(List<String> inputs) {
        LinkedList<SpaceTimeCoordinate> coordinates = new LinkedList<>();

        for (String input : inputs) {
            String[] split = input.trim().split(",");
            SpaceTimeCoordinate coordinate = new SpaceTimeCoordinate(
                    Integer.parseInt(split[0]),
                    Integer.parseInt(split[1]),
                    Integer.parseInt(split[2]),
                    Integer.parseInt(split[3]));

            coordinates.add(coordinate);
        }
        return coordinates;
    }

    @Override
    protected void part2(List<String> inputs) {
        printSolution(2, "No solution necessary!");
    }
}
