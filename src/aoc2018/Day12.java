package aoc2018;

import aoc.Day;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12 extends Day {

    public Day12(String year, String day) {
        super(year, day);
    }

    private String initialState;
    private String state;
    private int negativePots;
    private Map<String, String> mutations;

    @Override
    protected void part1(List<String> inputs) {

        Pattern initialPattern = Pattern.compile("initial state: (.*)");
        Matcher matcher = initialPattern.matcher(inputs.get(0));

        matcher.find();
        state = matcher.group(1);

        Pattern mutationPattern = Pattern.compile("(.....) => (.)");
        Matcher mutationMatcher;

        mutations = new HashMap<>();

        for (String input : inputs.subList(2, inputs.size())) {
            mutationMatcher = mutationPattern.matcher(input);
            mutationMatcher.find();

            mutations.put(mutationMatcher.group(1), mutationMatcher.group(2));
        }

        initialState = state;

        printSolution(1, String.valueOf(runGenerations(20)));

    }

    @Override
    protected void part2(List<String> inputs) {
        // at sometime the generations mutate on a stable pattern. For me this was at 112
        double stable = runGenerations(112);
        // so we only shift to the right an the increase each generation is just as high as the plant count
        double solution = stable + ((50000000000d - 112) * plantCount);

        printSolution(2, String.valueOf(solution));
    }

    private double runGenerations(double amount) {
        state = "...." + initialState + "....";
        negativePots = 4;
        for (double i = 0; i < amount; i++) {

            String copy = state.replaceAll("#", ".");

            for (Map.Entry<String, String> entry : mutations.entrySet()) {
                if (entry.getValue().equals(".")) {
                    continue;
                }
                int index = state.indexOf(entry.getKey());
                while (index != -1) {
                    copy = copy.substring(0, index + 2) + entry.getValue() + copy.substring(index + 3);
                    index = state.indexOf(entry.getKey(), index + 1);
                }
            }

            state = copy;
            extendPotRange();
        }

        return sum();
    }

    int plantCount;

    private double sum() {
        plantCount = 0;
        double sum = 0;
        char[] chars = state.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (aChar == '#') {
                plantCount++;
                sum = sum + i - negativePots;
            }
        }

        return sum;
    }

    private void extendPotRange() {
        if (state.substring(0, 1).equals("#")) {
            state = "...." + state;
            negativePots += 4;
        } else if (state.substring(1, 2).equals("#")) {
            state = "..." + state;
            negativePots += 3;
        } else if (state.substring(2, 3).equals("#")) {
            state = ".." + state;
            negativePots += 2;
        } else if (state.substring(3, 4).equals("#")) {
            state = "." + state;
            negativePots += 1;
        }

        if (state.substring(state.length() - 1).equals("#")) {
            state = state + "...";
        } else if (state.substring(state.length() - 2, state.length() - 1).equals("#")) {
            state = state + "..";
        } else if (state.substring(state.length() - 3, state.length() - 2).equals("#")) {
            state = state + ".";
        }
    }
}
