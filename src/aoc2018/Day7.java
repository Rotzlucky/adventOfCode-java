package aoc2018;

import aoc.Day;
import aoc2018.helper.Worker;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7 extends Day {

    public Day7(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        TreeSet<String> allSteps = getAllSteps(inputs);
        Map<String, List<String>> dependencies = getDependencies(inputs);

        String solution = "";

        while (true) {
            Optional<String> stringOptional = getNextStep(dependencies, allSteps);

            if (!stringOptional.isPresent()) {
                break;
            }

            String nextStep = stringOptional.get();
            solution += nextStep;

            for (List<String> value : dependencies.values()) {
                value.remove(nextStep);
            }
        }

        printSolution(1, solution);
    }

    @Override
    protected void part2(List<String> inputs) {
        TreeSet<String> allSteps = getAllSteps(inputs);
        Map<String, List<String>> dependencies = getDependencies(inputs);

        List<Worker> workers = Worker.getWorkers(5, 60);

        int completionTime = 0;
        boolean oneIsWorking;

        for (int i = 0; i < 1000; i++) {
            oneIsWorking = false;
            for (Worker worker : workers) {
                int work = worker.work();
                if (work == 0) {
                    String assignedStep = worker.getAssignedStep();
                    for (List<String> value : dependencies.values()) {
                        value.remove(assignedStep);
                    }
                }

                if (!worker.isWorking()) {
                    Optional<String> nextStep = getNextStep(dependencies, allSteps);
                    nextStep.ifPresent(worker::setAssignedStep);
                }

                if (worker.isWorking()) {
                    oneIsWorking = true;
                }
            }

            if (!oneIsWorking) {
                completionTime = i;
                break;
            }
        }
        printSolution(2, completionTime);
    }

    private Map<String, List<String>> getDependencies(List<String> inputs) {
        Map<String, List<String>> dependencies = new HashMap<>();
        for (String input : inputs) {
            Pair<String, String> directive = getDirective(input);

            List<String> arrayList = dependencies.getOrDefault(directive.getValue(), new ArrayList<>());
            arrayList.add(directive.getKey());
            dependencies.put(directive.getValue(), arrayList);
        }
        return dependencies;
    }

    private TreeSet<String> getAllSteps(List<String> inputs) {
        TreeSet<String> allSteps = new TreeSet<>();

        for (String input : inputs) {
            Pair<String, String> directive = getDirective(input);
            allSteps.add(directive.getValue());
            allSteps.add(directive.getKey());
        }
        return allSteps;
    }

    private Optional<String> getNextStep(Map<String, List<String>> dependencies, TreeSet<String> nextSteps) {
        if (nextSteps.isEmpty()) {
            return Optional.empty();
        }

        String nextStep = null;
        for (String possibleNextStep : nextSteps) {
            if (!dependencies.keySet().contains(possibleNextStep) || dependencies.get(possibleNextStep).isEmpty()) {
                nextStep = possibleNextStep;
                break;
            }
        }

        if (nextStep != null) {
            nextSteps.remove(nextStep);
        }
        return Optional.ofNullable(nextStep);
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
}
