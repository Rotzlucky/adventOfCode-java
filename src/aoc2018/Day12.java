package aoc2018;

import aoc.Day;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Optimized version using https://en.wikipedia.org/wiki/Summed-area_table
 *
 * Inspired by https://www.reddit.com/r/adventofcode/comments/a53r6i/2018_day_11_solutions/
 */
public class Day12 extends Day {

    public Day12(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {

        Pattern initialPattern = Pattern.compile("initial state: (.*)");
        Matcher matcher = initialPattern.matcher(inputs.get(0));

        matcher.find();
        String initialState = matcher.group(1);
        initialState = "...." + initialState + "....";
        int negativPots = 4;

        Pattern mutationPattern = Pattern.compile("(.....) => (.)");
        Matcher mutationMatcher;

        Map<String, String> mutations = new HashMap<>();

        for (String input : inputs.subList(2, inputs.size())) {
            mutationMatcher = mutationPattern.matcher(input);
            mutationMatcher.find();

            mutations.put(mutationMatcher.group(1), mutationMatcher.group(2));
        }

        System.out.println(initialState);
        for (int i = 0; i < 20; i++) {
            String copy = initialState.replaceAll("#", ".");

            for (Map.Entry<String, String> entry : mutations.entrySet()) {
                if (entry.getValue().equals(".")) {
                    continue;
                }
                int index = initialState.indexOf(entry.getKey());
                while (index != -1 ) {
                    copy = copy.substring(0, index + 2) + entry.getValue() + copy.substring(index + 3);
                    index = initialState.indexOf(entry.getKey(), index + 1);
                }
            }

            initialState = copy;

            if (initialState.substring(0, 1).equals("#")) {
                initialState = "...." + initialState;
                negativPots += 4;
            } else if (initialState.substring(1, 2).equals("#")){
                initialState = "..." + initialState;
                negativPots += 3;
            } else if (initialState.substring(2, 3).equals("#")){
                initialState = ".." + initialState;
                negativPots += 2;
            } else if (initialState.substring(3, 4).equals("#")){
                initialState = "." + initialState;
                negativPots += 1;
            }

            if (initialState.substring(initialState.length() - 1).equals("#")) {
                initialState = initialState + "...";
            } else if (initialState.substring(initialState.length() - 2, initialState.length() - 1).equals("#")) {
                initialState = initialState + "..";
            } else if (initialState.substring(initialState.length() - 3, initialState.length() - 2).equals("#")) {
                initialState = initialState + ".";
            }

            System.out.println(initialState);
        }

        int sum = 0;
        char[] chars = initialState.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (aChar == '#') {
                sum = sum + i - negativPots;
            }
        }

        printSolution(1, sum);
    }

    @Override
    protected void part2(List<String> inputs) {
    }

}
