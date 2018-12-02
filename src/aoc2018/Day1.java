package aoc2018;

import aoc.Day;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day1 extends Day {

    public Day1(String year, String day) {
        super(year, day);
    }

    @Override
    public void solve() {

        List<String> inputs = getLineInput(getFileName());

        solveParts(inputs);
    }

    protected void part1(List<String> inputs) {
        List<Integer> integerList = getInputsAsInteger(inputs);

        int sum = 0;

        for (Integer integer : integerList) {
            sum += integer;
        }

        printSolution(1, sum);
    }

    protected void part2(List<String> inputs) {
        List<Integer> integerList = getInputsAsInteger(inputs);

        int sum = 0;

        Set<Integer> bla = new HashSet<>();
        bla.add(0);

        outerLoop:
        for (int j : integerList) {
            for (int i : integerList) {
                sum += i;

                if (bla.contains(sum)) {
                    break outerLoop;
                }

                bla.add(sum);
            }
        }

        printSolution(2, sum);
    }
}
