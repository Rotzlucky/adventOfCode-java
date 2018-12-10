package aoc2018;

import aoc.Day;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day9 extends Day {

    public Day9(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        String input = inputs.get(0);

        Pattern pattern = Pattern.compile("(\\d{1,3}) players; last marble is worth (\\d{2,5})");
        Matcher matcher = pattern.matcher(input);

        matcher.find();
        Integer playerCount = Integer.valueOf(matcher.group(1));
        Integer lastMarbleValue = Integer.valueOf(matcher.group(2));

        Long max = playGame(playerCount, lastMarbleValue);

        printSolution(1, max);
    }

    @Override
    protected void part2(List<String> inputs) {
        String input = inputs.get(0);

        Pattern pattern = Pattern.compile("(\\d{1,3}) players; last marble is worth (\\d{2,5})");
        Matcher matcher = pattern.matcher(input);

        matcher.find();
        Integer playerCount = Integer.valueOf(matcher.group(1));
        Integer lastMarbleValue = Integer.valueOf(matcher.group(2));

        Long max = playGame(playerCount, lastMarbleValue * 100);

        printSolution(2, max);
    }

    private Long playGame(Integer playerCount, Integer lastMarbleValue) {
        int marbleValue = 0;
        LinkedList<Integer> marbles = new LinkedList<>();
        marbles.addFirst(marbleValue);
        Map<Integer, Long> scores = new HashMap<>();

        while (marbleValue < lastMarbleValue) {
            marbleValue++;

            if (marbleValue % 23 == 0) {
                int player = marbleValue % playerCount;
                rotate(marbles, -7);
                scores.put(player, scores.getOrDefault(player, 0L) + marbleValue + marbles.removeLast());
                rotate(marbles, 1);
            } else {
                rotate(marbles, 1);
                marbles.addLast(marbleValue);
            }
        }

        return Collections.max(scores.values());
    }

    private void rotate(LinkedList<Integer> marbles, int rotation) {
        if (rotation >= 0) {
            for (int i = 0; i < rotation; i++) {
                marbles.addLast(marbles.removeFirst());
            }
        } else {
            for (int i = 0; i < 0 - rotation; i++) {
                marbles.addFirst(marbles.removeLast());
            }
        }

    }
}
