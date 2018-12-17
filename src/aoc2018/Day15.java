package aoc2018;

import aoc.Day;
import aoc2018.helper.Combatant;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Inspired by https://www.reddit.com/r/adventofcode/comments/a6chwa/2018_day_15_solutions/
 *
 * especially https://github.com/akaritakai/AdventOfCode2018/blob/master/src/main/java/net/akaritakai/aoc2018/Problem15.java
 */
public class Day15 extends Day {

    public Day15(String year, String day) {
        super(year, day);
    }

    private Map<Point, Character> blockedCoordinates = new HashMap<>();
    private char[][] map;
    private List<Combatant> elves = new ArrayList<>();
    private List<Combatant> goblins = new ArrayList<>();

    @Override
    protected void part1(List<String> inputs) {
        prepare(inputs);

        runSimulation();
    }

    private void runSimulation() {
        int rounds = 0;

        printMap();

        simulation:
        while (true) {
            List<Combatant> combatants = Stream.of(elves, goblins)
                    .flatMap(Collection::stream)
                    .sorted(Combatant.READING_ORDER_COMPARATOR)
                    .collect(Collectors.toList());

            List<Combatant> enemies;

            for (Combatant combatant : combatants) {
                if (battleIsOver()) {
                    break simulation;
                }

                if (combatant.getHitPoints() <= 0) {
                    continue;
                }

                enemies = combatant.getIdentity() == Combatant.ELF ? goblins : elves;

                List<Combatant> enemiesInFightingRange = combatant.getEnemiesInFightingRange(enemies);

                if (!enemiesInFightingRange.isEmpty()) {
                    Combatant enemy = enemiesInFightingRange.get(0);
                    if (combatant.attack(enemy)) {
                        blockedCoordinates.remove(enemy.getPosition());
                    }
                    continue;
                }

                if (!combatant.move(enemies, map, blockedCoordinates)) {
                    continue;
                }

                enemiesInFightingRange = combatant.getEnemiesInFightingRange(enemies);
                if (!enemiesInFightingRange.isEmpty()) {
                    Combatant enemy = enemiesInFightingRange.get(0);
                    if (combatant.attack(enemy)) {
                        blockedCoordinates.remove(enemy.getPosition());
                    }
                }
            }

            rounds++;
            cleanUpDead();

        }

        printMap();

        int sum = Stream.of(elves, goblins)
                .flatMap(Collection::stream)
                .filter(c -> c.getHitPoints() > 0)
                .mapToInt(c -> c.getHitPoints())
                .sum();
        printSolution(1, rounds * sum);
    }

    private void printMap() {

        System.out.println("-------------------------");

        for (int i = 0; i < map.length; i++) {
            String row = "";
            for (int j = 0; j < map[0].length; j++) {
                if (blockedCoordinates.containsKey(new Point(j, i))) {
                    row += blockedCoordinates.get(new Point(j, i));
                } else {
                    row += map[i][j];
                }
            }
            System.out.println(row);
        }

        System.out.println("-------------------------");
    }

    private void cleanUpDead() {
        elves = elves.stream().filter(e -> e.getHitPoints() > 0).collect(Collectors.toList());
        goblins = goblins.stream().filter(e -> e.getHitPoints() > 0).collect(Collectors.toList());
    }

    private boolean battleIsOver() {
        List<Combatant> livingElves = elves.stream().filter(e -> e.getHitPoints() > 0).collect(Collectors.toList());
        List<Combatant> livingGoblins = goblins.stream().filter(e -> e.getHitPoints() > 0).collect(Collectors.toList());
        return livingElves.size() == 0 || livingGoblins.size() == 0;
    }

    private void prepare(List<String> inputs) {
        int height = inputs.size();
        int width = inputs.stream().mapToInt(s -> s.length()).max().orElse(0);

        map = new char[height][width];

        int y = 0;
        for (String input : inputs) {
            char[] chars = input.toCharArray();
            for (int x = 0; x < chars.length; x++) {
                char currentChar = chars[x];

                Point point = new Point(x, y);
                switch (currentChar) {
                    case '#':
                        blockedCoordinates.put(point, currentChar);
                        map[y][x] = currentChar;
                        break;
                    case 'E':
                        blockedCoordinates.put(point, currentChar);
                        elves.add(new Combatant(point, currentChar));
                        map[y][x] = '.';
                        break;
                    case 'G':
                        blockedCoordinates.put(point, currentChar);
                        goblins.add(new Combatant(point, currentChar));
                        map[y][x] = '.';
                        break;
                    default:
                       map[y][x] = '.';
                }
            }
            y++;
        }
    }

    @Override
    protected void part2(List<String> inputs) {

    }
}
