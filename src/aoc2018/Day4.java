package aoc2018;

import aoc.Day;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 extends Day {
    public Day4(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {

        Map<LocalDateTime, String> sortedEntries = getSortedEntries(inputs);
        Map<Integer, int[]> matrix = getMatrix(sortedEntries);

        int mostSleepingId = getMostSleeping(matrix);
        int mostSleepingMinute = getMostSleepingMinute(matrix.get(mostSleepingId));
        int solution = mostSleepingId * mostSleepingMinute;

        printSolution(1, solution);
    }

    @Override
    protected void part2(List<String> inputs) {
        Map<LocalDateTime, String> sortedEntries = getSortedEntries(inputs);
        Map<Integer, int[]> matrix = getMatrix(sortedEntries);

        int highestId = getHighestSleepingCorrelationId(matrix);
        int amount = getMostSleepingMinute(matrix.get(highestId));
        int solution = highestId * amount;

        printSolution(2, solution);
    }

    private int getHighestSleepingCorrelationId(Map<Integer, int[]> matrix) {
        int highestId = 0;
        int highestAmount = 0;
        for (Map.Entry<Integer, int[]> entry : matrix.entrySet()) {
            int amount = Arrays.stream(entry.getValue()).max().orElse(0);
            if (amount > highestAmount) {
                highestAmount = amount;
                highestId = entry.getKey();
            }
        }

        return highestId;
    }

    private Map<Integer, int[]> getMatrix(Map<LocalDateTime, String> sortedEntries) {
        Pattern idPattern = Pattern.compile("(?<=#)\\d*");
        Pattern sleepPattern = Pattern.compile("falls asleep");
        Pattern wakePattern = Pattern.compile("wakes up");
        Matcher matcher;

        Map<Integer, int[]> matrix = new HashMap<>();
        int lastId = 0;
        int lastSleepMinute = 0;
        for (Map.Entry<LocalDateTime, String> entry : sortedEntries.entrySet()) {
            int minute = entry.getKey().getMinute();
            String value = entry.getValue();

            matcher = idPattern.matcher(value);
            if (matcher.find()) {
                lastId = Integer.valueOf(matcher.group(0));
                continue;
            }

            matcher = sleepPattern.matcher(value);
            if (matcher.find()) {
                lastSleepMinute = minute;
                continue;
            }

            matcher = wakePattern.matcher(value);
            if (matcher.find()) {
                if (!matrix.containsKey(lastId)) {
                    matrix.put(lastId, new int[60]);
                }
                int[] minutes = matrix.get(lastId);
                for (int i = lastSleepMinute; i < minute; i++) {
                    minutes[i] += 1;
                }
                matrix.put(lastId, minutes);
            }
        }

        return matrix;
    }

    private Map<LocalDateTime, String> getSortedEntries(List<String> inputs) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Map<LocalDateTime, String> entries = new TreeMap<>();
        Pattern datePattern = Pattern.compile("(?<=\\[).*(?=\\])");
        Pattern contentPattern = Pattern.compile("(?<=] ).*$");
        Matcher matcher;
        for (String input : inputs) {
            String dateString = "";
            String content = "";
            matcher = datePattern.matcher(input);
            if (matcher.find()) {
                dateString = matcher.group(0);
            }
            matcher = contentPattern.matcher(input);
            if (matcher.find()) {
                content = matcher.group(0);
            }

            entries.put(LocalDateTime.parse(dateString, formatter), content);
        }

        return entries;
    }

    private int getMostSleepingMinute(int[] minutes) {
        int mostSleepingMinuteIndex = 0;

        for (int i = 1; i < minutes.length; i++) {
            if (minutes[i] > minutes[mostSleepingMinuteIndex]) {
                mostSleepingMinuteIndex = i;
            }
        }

        return mostSleepingMinuteIndex;
    }

    private int getMostSleeping(Map<Integer, int[]> matrix) {
        int mostSleeping = 0;
        int amountSleeping = 0;
        for (Map.Entry<Integer, int[]> entry : matrix.entrySet()) {
            int sum = Arrays.stream(entry.getValue()).sum();
            if (sum > amountSleeping) {
                mostSleeping = entry.getKey();
                amountSleeping = sum;
            }
        }

        return mostSleeping;
    }
}
