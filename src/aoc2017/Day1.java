package aoc2017;

import aoc.Day;

import java.util.List;

public class Day1 extends Day {

    public Day1(String year, String day) {
        super(year, day);
    }

    @Override
    public void solve() {
        List<String> inputs = getIntegerInputFromDigits(getFileName());

        solveParts(inputs);
    }

    protected void part1(List<String> inputs) {
        List<Integer> integerList = getInputsAsInteger(inputs);

        int first = 0;
        Integer last = null;
        int sum = 0;
        for (Integer input : integerList) {
            if (last == null) {
                last = input;
                first = input;
                continue;
            }

            if (input == last) {
                sum += input;
            }

            last = input;
        }

        if (last == first) {
            sum += first;
        }

        printSolution(1, sum);
    }

    protected void part2(List<String> inputs) {
        List<Integer> integerList = getInputsAsInteger(inputs);

        int sum = 0;
        for (int i = 0; i < integerList.size(); i++) {
            Integer current = integerList.get(i);
            int halfSize = inputs.size() / 2;
            Integer compareIndex = i + halfSize >= integerList.size() ? i + halfSize - integerList.size() : i + halfSize;

            if (current == integerList.get(compareIndex)) {
                sum += current;
            }
        }

        printSolution(2, sum);
    }
}
