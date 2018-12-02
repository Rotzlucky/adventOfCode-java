package aoc2017;

import aoc.Day;

import java.util.Arrays;
import java.util.List;

public class Day2 extends Day {

    public Day2(String year, String day) {
        super(year, day);
    }

    @Override
    public void solve() {
        List<String> inputs = getLineInput(getFileName());

        solveParts(inputs);
    }

    protected void part1(List<String> inputs) {
        int sum = 0;

        for (String input : inputs) {
            int[] numbers = Arrays.stream(input.split(" |\t")).mapToInt(Integer::parseInt).toArray();

            Arrays.sort(numbers);

            sum += Integer.valueOf(numbers[numbers.length - 1]) - Integer.valueOf(numbers[0]);
        }

        printSolution(1, sum);
    }

    protected void part2(List<String> inputs) {
        int sum = 0;

        for (String input : inputs) {
            int[] numbers = Arrays.stream(input.split(" |\t")).mapToInt(Integer::parseInt).toArray();

            outerloop:
            for (int i = 0; i < numbers.length; i++) {
                int firstNumber = numbers[i];
                for (int j = numbers.length -1; j >= 0; j--) {
                    int secondNumber = numbers[j];
                    if (i == j) {
                        continue;
                    }
                    if (firstNumber % secondNumber == 0 ) {
                        sum += firstNumber / secondNumber;
                        break outerloop;
                    } else if (secondNumber % firstNumber == 0) {
                        sum += secondNumber / firstNumber;
                        break outerloop;
                    }
                }
            }
        }

        printSolution(2, sum);
    }
}
