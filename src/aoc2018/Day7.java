package aoc2018;

import aoc.Day;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7 extends Day {

    public Day7(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        Map<String, List<String>> dependencies = new HashMap<>();
        TreeSet<String> nextSteps = new TreeSet<>();

        for (String input : inputs) {
            Pair<String, String> directive = getDirective(input);

            nextSteps.add(directive.getKey());
            nextSteps.add(directive.getValue());

            List<String> arrayList = dependencies.getOrDefault(directive.getValue(), new ArrayList<>());
            arrayList.add(directive.getKey());
            dependencies.put(directive.getValue(), arrayList);
        }

        String solution = "";

        while (true) {
            String nextStep = getNextStep(dependencies, nextSteps);

            if (nextStep == null) {
                break;
            }

            solution += nextStep;

            for (List<String> value : dependencies.values()) {
                value.remove(nextStep);
            }
        }

        printSolution(1, solution);
    }

    private String getNextStep(Map<String, List<String>> dependencies, TreeSet<String> nextSteps) {
        if (nextSteps.isEmpty()) {
            return null;
        }

        String nextStep = null;
        for (String possibleNextStep : nextSteps) {
            if (!dependencies.keySet().contains(possibleNextStep) || dependencies.get(possibleNextStep).isEmpty()) {
                nextStep = possibleNextStep;
                break;
            }
        }

        nextSteps.remove(nextStep);
        return nextStep;
    }

    private Pair<String, String> getDirective(String input) {
        Pattern prePattern = Pattern.compile("(?<=Step ).*(?= must)");
        Pattern postPattern = Pattern.compile("(?<=step ).*(?= can begin.)");

        String pre = null;
        Matcher preMatcher = prePattern.matcher(input);
        if (preMatcher.find()) {
            pre = preMatcher.group(0);
        }
        String post = null;
        Matcher postMatcher = postPattern.matcher(input);
        if (postMatcher.find()) {
            post = postMatcher.group(0);
        }

        if (pre == null || post == null) {
            throw new IllegalArgumentException("Null found in Input!");
        }

        return new Pair<>(pre, post);
    }

    @Override
    protected void part2(List<String> inputs) {

    }
}
