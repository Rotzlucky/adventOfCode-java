package aoc2018;

import aoc.Day;

import java.util.ArrayList;
import java.util.List;

public class Day14 extends Day {

    public Day14(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        Integer numberOfRecipes = Integer.valueOf(inputs.get(0));

        List<Integer> scoreBoard = new ArrayList<>();
        scoreBoard.add(3);
        scoreBoard.add(7);

        int scoreEntry1 = 0;
        int scoreEntry2 = 1;

        while (scoreBoard.size() < numberOfRecipes + 15) {
            int combinedScore = scoreBoard.get(scoreEntry1) + scoreBoard.get(scoreEntry2);
            for (char digit : String.valueOf(combinedScore).toCharArray()) {
                scoreBoard.add(Integer.parseInt(String.valueOf(digit)));
            }

            scoreEntry1 = (scoreEntry1 + scoreBoard.get(scoreEntry1) + 1) % scoreBoard.size();
            scoreEntry2 = (scoreEntry2 + scoreBoard.get(scoreEntry2) + 1) % scoreBoard.size();
        }

        String solution = "";
        for (Integer integer : scoreBoard.subList(numberOfRecipes, numberOfRecipes + 10)) {
            solution += integer;
        }
        printSolution(1, solution);
    }


    @Override
    protected void part2(List<String> inputs) {
        String breakCondition = inputs.get(0);

        List<Integer> scoreBoard = new ArrayList<>();
        scoreBoard.add(3);
        scoreBoard.add(7);

        int scoreEntry1 = 0;
        int scoreEntry2 = 1;

        String scoreBoardString = "37";
        loop:
        while (true) {
            int combinedScore = scoreBoard.get(scoreEntry1) + scoreBoard.get(scoreEntry2);
            for (char digit : String.valueOf(combinedScore).toCharArray()) {
                scoreBoard.add(Integer.parseInt(String.valueOf(digit)));
                if (scoreBoardString.length() >= breakCondition.length()) {
                    scoreBoardString = scoreBoardString.substring(1);
                }
                scoreBoardString += digit;

                if (scoreBoardString.equals(breakCondition)) {
                    break loop;
                }
            }

            scoreEntry1 = (scoreEntry1 + scoreBoard.get(scoreEntry1) + 1) % scoreBoard.size();
            scoreEntry2 = (scoreEntry2 + scoreBoard.get(scoreEntry2) + 1) % scoreBoard.size();

        }

        printSolution(2, scoreBoard.size() - 6);
    }
}
