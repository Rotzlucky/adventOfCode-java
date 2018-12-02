package aoc2018;

import aoc.Day;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day2 extends Day {
    public Day2(String year, String day) {
        super(year, day);
    }

    @Override
    public void solve() {
        List<String> inputs = getLineInput(getFileName());

        solveParts(inputs);
    }

    protected void part1(List<String> lineInput) {
        Integer countOfTwos = 0;
        Integer countOfThrees = 0;

        for (String line : lineInput) {


            Map<Character, Integer> counts = new HashMap<>();

            for (char c : line.toCharArray()) {
                counts.put(c, counts.getOrDefault(c, 0) + 1);
            }

            boolean incrementTwos = false;
            boolean incrementThrees = false;

            for (Map.Entry<Character, Integer> characterIntegerEntry : counts.entrySet()) {
                if (characterIntegerEntry.getValue() == 2) {
                    incrementTwos = true;
                } else if (characterIntegerEntry.getValue() == 3) {
                    incrementThrees = true;
                }
            }

            if (incrementTwos) {
                countOfTwos++;
            }
            if (incrementThrees) {
                countOfThrees++;
            }
        }

        printSolution(1, countOfTwos * countOfThrees);
    }

    protected void part2(List<String> lineInput) {
        String match = null;
        for (String line : lineInput) {
            match = processLine(line, lineInput);

            if (match != null) {
                break;
            }
        }

        printSolution(2, match);
    }

    private static String processLine(String line, List<String> lines) {
        for (String compareLine : lines) {
            int offBy = 0;
            List<Character> commonChars = new ArrayList<>();

            if (compareLine.equals(line)) {
                continue;
            }

            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) != compareLine.charAt(i)) {
                    offBy++;
                } else {
                    commonChars.add(line.charAt(i));
                }
            }

            if (offBy == 1) {
                String commonCharsString = "";
                for (Character commonChar : commonChars) {
                    commonCharsString += commonChar;
                }

                return commonCharsString;
            }
        }

        return null;
    }

}

