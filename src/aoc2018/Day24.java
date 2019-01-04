package aoc2018;

import aoc.Day;
import aoc2018.helper.FactionGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day24 extends Day {

    public Day24(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {

        prepareInput(inputs);

    }

    private void prepareInput(List<String> inputs) {
        Pattern pattern = Pattern.compile("^(?<unitCount>\\d*) units each with (?<hitPoints>\\d*) hit points " +
                "(?<modifiers>\\(.*\\) )?with an attack that does (?<attackPower>\\d*) (?<attackType>[a-z]*) " +
                "damage at initiative (?<initiative>\\d*)$");

        String faction = "";
        for (String input : inputs) {
            if (input.startsWith("Immune System:")) {
                faction = "Immune System";
                continue;
            } else if (input.startsWith("Infection:")) {
                faction = "Infection";
                continue;
            } else if (input.isEmpty()) {
                continue;
            }

            Matcher matcher = pattern.matcher(input);
            matcher.find();
            int unitCount = Integer.parseInt(matcher.group("unitCount"));
            int hitPoints = Integer.parseInt(matcher.group("hitPoints"));
            int attackPower = Integer.parseInt(matcher.group("attackPower"));
            int initiative = Integer.parseInt(matcher.group("initiative"));
            String attackType = matcher.group("attackType");
            String modifiers = matcher.group("modifiers");
            List<String> immunities = new ArrayList<>();
            List<String> weakness = new ArrayList<>();

            if (modifiers != null) {
                Pattern immunitiesPattern = Pattern.compile("(?<=immune to )(?<immunities>[a-z, ]*)(?=[;\\)])");
                Pattern weaknessPattern = Pattern.compile("(?<=weak to )(?<weakness>[a-z, ]*)(?=[;\\)])");

                matcher = immunitiesPattern.matcher(modifiers);
                if (matcher.find()) {
                    immunities = Arrays.asList(matcher.group("immunities").split(", "));
                    immunities.removeAll(Collections.singleton(null));
                }
                matcher = weaknessPattern.matcher(modifiers);
                if (matcher.find()) {
                    weakness = Arrays.asList(matcher.group("weakness").split(", "));
                    weakness.removeAll(Collections.singleton(null));
                }
            }

            FactionGroup group = new FactionGroup(faction, unitCount, hitPoints, attackPower, initiative, attackType, immunities, weakness);

            if ("Infection".equals(faction)) {
                infections.add(group);
            } else if ("Immune System".equals(faction)) {
                immuneSystems.add(group);
            }
        }
    }

    List<FactionGroup> infections = new ArrayList<>();
    List<FactionGroup> immuneSystems = new ArrayList<>();

    @Override
    protected void part2(List<String> inputs) {

    }
}
