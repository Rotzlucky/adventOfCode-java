package aoc;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Day {
    private final String year;
    private final String day;

    public Day(String year, String day) {
        this.year = year;
        this.day = day;
    }

    protected String getFileName() {
        return getYear() + "_" + getDay() + ".txt";
    }

    protected void printSolution(int part, int solution) {
        printSolution(part, String.valueOf(solution));
    }

    public void solve() {
        solveParts(getLineInput(getFileName()));
    }

    protected void solveParts(List<String> inputs) {
        part1(inputs);
        part2(inputs);
    }

    protected abstract void part1(List<String> inputs);

    protected abstract void part2(List<String> inputs);

    protected void printSolution(int part, String solution) {
        System.out.println("Solution for Year: " + getYear() +
                " day: " + getDay() +
                " part " + part +
                " is: " + solution);
    }

    protected List<String> getLineInput(String name) {
        URL resource = Day.class.getResource(name);

        try (Stream<String> stream = Files.lines(Paths.get(resource.getPath()))) {
            return stream.collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<Integer> getInputsAsInteger(List<String> inputs) {
        return inputs.stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    protected List<String> getIntegerInputFromDigits(String name) {
        URL resource = Day.class.getResource(name);

        List<String> strings = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(resource.getPath()))) {
            stream.forEach(line -> {
                char[] chars = line.toCharArray();
                for (char aChar : chars) {
                    strings.add(String.valueOf(aChar));
                }
            });

            return strings;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getYear() {
        return year;
    }

    public String getDay() {
        return day;
    }
}
