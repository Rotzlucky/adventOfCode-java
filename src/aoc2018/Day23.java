package aoc2018;

import aoc.Day;
import aoc2018.helper.Cube;
import aoc2018.helper.NanoBot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day23 extends Day {

    public Day23(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        List<NanoBot> nanoBots = getNanoBots(inputs);

        Optional<NanoBot> first = nanoBots.stream().max(Comparator.comparingInt(NanoBot::getRadius));

        List<NanoBot> botsInRange = new ArrayList<>();
        if (first.isPresent()) {
            NanoBot nanoBot = first.get();
            botsInRange = findBotsInRange(nanoBots, nanoBot);
        }

        printSolution(1, botsInRange.size());
    }


    @Override
    protected void part2(List<String> inputs) {
        List<NanoBot> nanoBots = getNanoBots(inputs);

        Comparator comparator = Comparator
                .comparingLong(Cube::getNumBots).reversed()
                .thenComparingLong(Cube::getEdgeLength)
                .thenComparingLong(Cube::getDistance)
                .thenComparingLong(Cube::getX)
                .thenComparingLong(Cube::getY)
                .thenComparingLong(Cube::getZ);

        TreeSet<Cube> cubeQueue = new TreeSet(comparator);

        Cube startCube = getStartCube();
        startCube.setNumBots(nanoBots.size());

        cubeQueue.add(startCube);

        long distance = 0;
        while (!cubeQueue.isEmpty()) {
            Cube cube = cubeQueue.pollFirst();

            assert cube != null;

            if (cube.getEdgeLength() == 1) {
                distance = cube.getDistance();
                break;
            }

            for (int i = 0; i < 8; i++) {
                Cube child = new Cube();
                long delta = cube.getEdgeLength() / 2;
                child.setEdgeLength(delta);
                child.setX(cube.getX() + (((i & 1) == 0) ? delta : 0 ));
                child.setY(cube.getY() + (((i & 2) == 0) ? delta : 0 ));
                child.setZ(cube.getZ() + (((i & 4) == 0) ? delta : 0 ));
                child.setDistance(distance(child));
                child.setNumBots(findBotsInRange(nanoBots, child).size());
                cubeQueue.add(child);
            }
        }

        printSolution(2, distance);
    }

    private List<NanoBot> findBotsInRange(List<NanoBot> nanoBots, Cube cube) {
        return nanoBots.stream().filter(n -> cubeDistance(cube, n.getX(), n.getY(), n.getZ()) <= n.getRadius()).collect(Collectors.toList());
    }

    private List<NanoBot> findBotsInRange(List<NanoBot> nanoBots, NanoBot nanoBot) {
        return nanoBots.stream().filter(n -> distance(n, nanoBot) <= nanoBot.getRadius()).collect(Collectors.toList());
    }

    private long distance(Cube cube) {
        return Math.abs(cube.getX()) +
                Math.abs(cube.getY()) +
                Math.abs(cube.getZ());
    }

    private int distance(NanoBot otherBot, NanoBot nanoBot) {
        return Math.abs(otherBot.getX() - nanoBot.getX()) +
                Math.abs(otherBot.getY() - nanoBot.getY()) +
                Math.abs(otherBot.getZ() - nanoBot.getZ());
    }

    private long cubeDistance(Cube cube, int x, int y, int z) {
        long edgeLength = cube.getEdgeLength() - 1;

        return getDistanceOfBoundary(x, cube.getX(), cube.getX() + edgeLength) +
                getDistanceOfBoundary(y, cube.getY(), cube.getY() + edgeLength) +
                getDistanceOfBoundary(z, cube.getZ(), cube.getZ() + edgeLength);
    }

    private long getDistanceOfBoundary(int value, long minBoundary, long maxBoundary) {
        if (value < minBoundary) {
            return minBoundary - value;
        } else if (value > maxBoundary) {
            return value - maxBoundary;
        }

        return 0;
    }

    private Cube getStartCube() {
        Cube cube = new Cube();
        cube.setX(-1073741824L);
        cube.setY(-1073741824L);
        cube.setZ(-1073741824L);
        cube.setEdgeLength(2147483648L);
        cube.setDistance(distance(cube));

        return cube;
    }

    private List<NanoBot> getNanoBots(List<String> inputs) {
        List<NanoBot> nanoBots = new ArrayList<>();
        for (String input : inputs) {
            nanoBots.add(getNanoBot(input));
        }
        return nanoBots;
    }

    private NanoBot getNanoBot(String input) {
        int x = Integer.parseInt(input.replaceAll("pos=<(-?\\d*),(-?\\d*),(-?\\d*)>, r=(\\d*)", "$1"));
        int y = Integer.parseInt(input.replaceAll("pos=<(-?\\d*),(-?\\d*),(-?\\d*)>, r=(\\d*)", "$2"));
        int z = Integer.parseInt(input.replaceAll("pos=<(-?\\d*),(-?\\d*),(-?\\d*)>, r=(\\d*)", "$3"));
        int radius = Integer.parseInt(input.replaceAll("pos=<(-?\\d*),(-?\\d*),(-?\\d*)>, r=(\\d*)", "$4"));

        return new NanoBot(x, y, z, radius);
    }
}
