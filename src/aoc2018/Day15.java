package aoc2018;

import aoc.Day;
import aoc2018.helper.Combatant;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Inspired by https://www.reddit.com/r/adventofcode/comments/a6chwa/2018_day_15_solutions/
 *
 * especially https://github.com/akaritakai/AdventOfCode2018/blob/master/src/main/java/net/akaritakai/aoc2018/Problem15.java
 *
 * Part 2 not optimized. Takes a few seconds
 */
public class Day15 extends Day {

    private static final int ROUND = 1;
    private static final int COMBATANT = 2;

    public Day15(String year, String day) {
        super(year, day);
    }

    private Map<Point, Character> blockedCoordinates = new HashMap<>();
    private char[][] map;
    private List<Combatant> elves = new ArrayList<>();
    private List<Combatant> goblins = new ArrayList<>();

    @Override
    protected void part1(List<String> inputs) {
        prepare(inputs, 3);

        printSolution(1, runSimulation(0));
    }

    @Override
    protected void part2(List<String> inputs) {

        for (int i = 3; i < Integer.MAX_VALUE; i++) {
            prepare(inputs, i);

            int initialElvesCount = elves.size();

            int solution = runSimulation(0);

            if (elves.size() == initialElvesCount) {
                int sum = Stream.of(elves, goblins)
                        .flatMap(Collection::stream)
                        .filter(c -> c.getHitPoints() > 0)
                        .mapToInt(c -> c.getHitPoints())
                        .sum();
                System.out.println("Elves win with attackPower: " + i + " in Round " + solution / sum + " . " +
                        "Elves alive: " + elves.size() + " hitpoints: " + sum);
                printSolution(2, solution);
                break;
            }
        }
    }

    private int runSimulation(int printMode) {
        int rounds = 0;

        simulation:
        while (true) {
            List<Combatant> combatants = Stream.of(elves, goblins)
                    .flatMap(Collection::stream)
                    .sorted(Combatant.READING_ORDER_COMPARATOR)
                    .collect(Collectors.toList());

            List<Combatant> enemies;

            if (printMode >= ROUND) {
                printMap(rounds);
            }

            for (Combatant combatant : combatants) {

                if (printMode == COMBATANT) {
                    printMap(rounds);
                }

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

        if (printMode >= ROUND) {
            printMap(-1);
        }

        cleanUpDead();

        int sum = Stream.of(elves, goblins)
                .flatMap(Collection::stream)
                .filter(c -> c.getHitPoints() > 0)
                .mapToInt(c -> c.getHitPoints())
                .sum();

        return rounds * sum;
    }

    private void printMap(int rounds) {

        if (rounds == 0) {
            System.out.println("Initially:");
        } else if (rounds == -1) {
            System.out.println("Incomplete last round:");
        } else {
            System.out.println("After " + rounds + " round:");
        }

        for (int i = 0; i < map.length; i++) {
            String row = "";
            List<Combatant> combatants = new ArrayList<>();
            for (int j = 0; j < map[0].length; j++) {
                if (blockedCoordinates.containsKey(new Point(j, i))) {
                    row += blockedCoordinates.get(new Point(j, i));
                    combatants.add(getCombatant(new Point(j, i)));
                } else {
                    row += map[i][j];
                }
            }

            row += "   ";
            StringJoiner joiner = new StringJoiner(", ");
            for (Combatant combatant : combatants) {
                if (combatant != null) {
                    joiner.add(combatant.getIdentity() + "(" + combatant.getHitPoints() + ")");
                }
            }
            System.out.println(row + joiner.toString());
        }

        System.out.println();
    }

    private Combatant getCombatant(Point point) {
        return Stream.of(elves, goblins)
                .flatMap(Collection::stream)
                .filter(c -> c.getX() == point.x && c.getY() == point.y)
                .findFirst()
                .orElse(null);
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

    private void prepare(List<String> inputs, int elvenAttackPower) {
        reset();

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
                        elves.add(new Combatant(point, currentChar, elvenAttackPower));
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

    private void reset() {
        goblins = new ArrayList<>();
        elves = new ArrayList<>();
        blockedCoordinates = new HashMap<>();
    }
}
