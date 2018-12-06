package aoc2018;

import aoc.Day;

import java.util.ArrayList;
import java.util.List;

public class Day5 extends Day {
    public Day5(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        char[] finalPolymer = new char[0];
        for (String input : inputs) {
            char[] chars = input.toCharArray();

            finalPolymer = getFinalPolymer(chars);

        }
        printSolution(1, finalPolymer.length);
    }

    @Override
    protected void part2(List<String> inputs) {
        int shortestPolymer = -1;
        for (String input : inputs) {

            char[] alphabet = getAlphabet();

            for (int i = 0; i < alphabet.length; i++) {
                char c = alphabet[i];
                String replace = input.replaceAll("(?i)" + c, "");

                char[] chars = replace.toCharArray();

                char[] finalPolymer = getFinalPolymer(chars);

                if (shortestPolymer == -1 || shortestPolymer > finalPolymer.length) {
                    shortestPolymer = finalPolymer.length;
                }
            }
        }
        printSolution(2, shortestPolymer);
    }

    private char[] getFinalPolymer(char[] chars) {
        boolean unstable = true;

        while (unstable) {
            char[] mutation = getNextMutation(chars);
            if (mutation.length == chars.length) {
                unstable = false;
            }
            chars = mutation;
        }
        return chars;
    }

    private char[] getAlphabet() {
        char[] alphabet = new char[26];
        for (int i = 0; i < 26; i++) {
            alphabet[i] = (char) (97 + i);
        }
        return alphabet;
    }

    private char[] getNextMutation(char[] chars) {
        List<Character> characters = new ArrayList<>();

        for (int i = 0; i < chars.length; i++) {
            char currentChar = chars[i];
            String currentString = String.valueOf(currentChar);

            if (i + 1 == chars.length) {
                characters.add(currentChar);
                break;
            }

            char nextChar = chars[i + 1];
            String nextString = String.valueOf(nextChar);

            if (!currentString.equals(nextString) && currentString.equalsIgnoreCase(nextString)) {
                i++;
            } else {
                characters.add(currentChar);
            }
        }

        char[] mutation = new char[characters.size()];
        int i = 0;
        for (Character character : characters) {
            mutation[i] = character;
            i++;
        }

        return mutation;
    }
}
