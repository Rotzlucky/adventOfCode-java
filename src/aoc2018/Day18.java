package aoc2018;

import aoc.Day;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Day18 extends Day {

    public Day18(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        char[][] map = processInput(inputs);

        for (int i = 0; i < 10; i++) {
            //printMap(map);
            map = transformMap(map);
        }

        //printMap(map);
        printSolution(1, getResources(map));

    }

    @Override
    protected void part2(List<String> inputs) {
        char[][] map = processInput(inputs);

        Map<Integer, Integer> resourceMap = new HashMap<>();

        Map.Entry<Integer, Integer> lastDoubleEntry = null;
        for (int i = 1; i < 1000; i++) {
            map = transformMap(map);

            int resources = getResources(map);
            if (resourceMap.containsValue(resources)) {
                // this is an assumption that if we see two keys in a row that we have already seen,
                // that we found the cycle
                if (lastDoubleEntry != null && lastDoubleEntry.getKey() + 1 == i) {
                    break;
                }
                lastDoubleEntry = new AbstractMap.SimpleEntry<>(i, resources);
            }
            resourceMap.put(i, resources);
        }

        Integer startOfSecondCycle = lastDoubleEntry.getValue();
        int cycleStart = resourceMap.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(startOfSecondCycle))
                .map(e -> e.getKey())
                .findFirst()
                .orElse(0);

        int cycleLength = lastDoubleEntry.getKey() - cycleStart;
        int remainingMinutes = 1000000000 - cycleStart;
        int remainder = remainingMinutes % cycleLength;

        Integer resources = resourceMap.get(cycleStart + remainder);

        printSolution(2, resources);
    }

    private int getResources(char[][] map) {
        int trees = 0;
        int lumberyards = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == '|') {
                    trees++;
                } else if (map[i][j] == '#') {
                    lumberyards++;
                }
            }
        }

        return trees * lumberyards;
    }

    private char[][] transformMap(char[][] map) {
        char[][] transformedMap = new char[map.length][map[0].length];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                List<Character> surroundingTiles = getSurroundingTiles(map, i, j);

                char currentTile = map[i][j];

                transformedMap[i][j] = getNextTile(currentTile, surroundingTiles);
            }
        }

        return transformedMap;
    }

    private char getNextTile(char currentTile, List<Character> surroundingTiles) {
        if (currentTile == '.') {
            return surroundingTiles.stream().filter(c -> c == '|').count() >= 3 ? '|' : '.';
        } else if (currentTile == '|') {
            return surroundingTiles.stream().filter(c -> c == '#').count() >= 3 ? '#' : '|';
        } else {
            if (surroundingTiles.stream().filter(c -> c == '#').count() >= 1 &&
                    surroundingTiles.stream().filter(c -> c == '|').count() >= 1) {
                return '#';
            } else {
                return '.';
            }
        }
    }

    private List<Character> getSurroundingTiles(char[][] map, int i, int j) {
        List<Character> surroundingTiles = new ArrayList<>();

        getTile(map, i - 1, j - 1).ifPresent(surroundingTiles::add);
        getTile(map, i - 1, j).ifPresent(surroundingTiles::add);
        getTile(map, i - 1, j + 1).ifPresent(surroundingTiles::add);

        getTile(map, i, j - 1).ifPresent(surroundingTiles::add);
        getTile(map, i, j + 1).ifPresent(surroundingTiles::add);

        getTile(map, i + 1, j - 1).ifPresent(surroundingTiles::add);
        getTile(map, i + 1, j).ifPresent(surroundingTiles::add);
        getTile(map, i + 1, j + 1).ifPresent(surroundingTiles::add);

        return surroundingTiles;
    }

    private Optional<Character> getTile(char[][] map, int i, int j) {
        try {
            char c = map[i][j];
            return Optional.of(c);
        } catch (ArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    private void printMap(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            String row = "";
            for (int j = 0; j < map[0].length; j++) {
                row += map[i][j];
            }

            System.out.println(row);
        }

        System.out.println();
    }

    private char[][] processInput(List<String> inputs) {
        char[][] map = new char[inputs.size()][inputs.get(0).length()];

        for (int i = 0; i < inputs.size(); i++) {
            char[] row = inputs.get(i).toCharArray();
            for (int j = 0; j < row.length; j++) {
                map[i][j] = row[j];
            }
        }

        return map;
    }
}
