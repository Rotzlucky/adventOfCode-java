package aoc2018;

import aoc.Day;
import aoc2018.helper.FactionGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day24 extends Day {

    private List<FactionGroup> infections = new ArrayList<>();
    private List<FactionGroup> immuneSystems = new ArrayList<>();
    private boolean debug = false;


    public Day24(String year, String day) {
        super(year, day);
    }

    @Override
    protected void part1(List<String> inputs) {
        prepareInput(inputs);

        while (!infections.isEmpty() && !immuneSystems.isEmpty()) {
            printStatus();
            targetPhase();
            attackPhase();
            resetDefendingState();
        }

        int solution;
        if (!immuneSystems.isEmpty()) {
            solution = immuneSystems.stream().mapToInt(FactionGroup::getUnitCount).sum();
        } else {
            solution = infections.stream().mapToInt(FactionGroup::getUnitCount).sum();
        }

        printStatus();
        printSolution(1, solution);
    }

    @Override
    protected void part2(List<String> inputs) {

    }

    private void resetDefendingState() {
        infections.forEach(FactionGroup::setAvailable);
        infections.forEach(f -> f.setDefender(null));
        immuneSystems.forEach(FactionGroup::setAvailable);
        immuneSystems.forEach(f -> f.setDefender(null));

        infections = infections.stream().filter(factionGroup -> factionGroup.getUnitCount() > 0).collect(Collectors.toList());
        immuneSystems = immuneSystems.stream().filter(factionGroup -> factionGroup.getUnitCount() > 0).collect(Collectors.toList());
    }

    private void targetPhase() {
        Comparator<FactionGroup> targetComparator = Comparator
                .comparingInt(FactionGroup::getEffectivePower)
                .thenComparingInt(FactionGroup::getInitiative).reversed();

        infections = infections.stream()
                .sorted(targetComparator)
                .collect(Collectors.toList());

        runTargetPhaseForFaction(infections);

        immuneSystems = immuneSystems.stream()
                .sorted(targetComparator)
                .collect(Collectors.toList());

        runTargetPhaseForFaction(immuneSystems);

        printEmptyLine();
    }

    private void attackPhase() {
        Comparator<FactionGroup> initiativeComparator = Comparator.comparingInt(FactionGroup::getInitiative).reversed();

        List<FactionGroup> factionGroups = Stream.of(infections, immuneSystems)
                .flatMap(Collection::stream)
                .sorted(initiativeComparator)
                .collect(Collectors.toList());

        for (FactionGroup factionGroup : factionGroups) {
            if (factionGroup.getUnitCount() > 0 && factionGroup.getDefender() != null) {
                FactionGroup defender = factionGroup.getDefender();
                int killCount = factionGroup.attack(defender);

                printAttack(factionGroup, defender, killCount);
            }
        }
        printEmptyLine();
    }

    private void runTargetPhaseForFaction(List<FactionGroup> factionGroups) {
        printTargetOrder(factionGroups);

        for (FactionGroup factionGroup : factionGroups) {
            Optional<FactionGroup> defender = Optional.empty();
            if (factionGroup.getFaction().equals(FactionGroup.IMMUNE_SYSTEM)) {
                defender = getDefender(infections, factionGroup);
            } else if (factionGroup.getFaction().equals(FactionGroup.INFECTION)) {
                defender = getDefender(immuneSystems, factionGroup);
            }

            defender.ifPresent(f -> {
                f.setDefending();
                factionGroup.setDefender(f);
            });
        }
    }

    private Optional<FactionGroup> getDefender(List<FactionGroup> enemyGroups, FactionGroup attacker) {
        Comparator<FactionGroup> targetComparator = Comparator
                .comparingInt((FactionGroup f) -> f.getPossibleDamage(attacker.getEffectivePower(), attacker.getAttackType()))
                .thenComparingInt(FactionGroup::getEffectivePower)
                .thenComparingInt(FactionGroup::getInitiative)
                .reversed();

        enemyGroups.stream().filter(f -> !f.isDefending()).forEach(defender -> printTarget(defender, attacker));

        Optional<FactionGroup> possibleDefender = enemyGroups.stream().filter(f -> !f.isDefending()).min(targetComparator);

        if (possibleDefender.isPresent()) {
            if (possibleDefender.get().getPossibleDamage(attacker.getEffectivePower(), attacker.getAttackType()) == 0) {
                printNoTarget(attacker);
                return Optional.empty();
            } else {
                printSelectedTarget(attacker, possibleDefender);
                return possibleDefender;
            }
        }

        return possibleDefender;
    }

    private void printEmptyLine() {
        if (!debug) {
            return;
        }

        System.out.println();
    }

    private void printAttack(FactionGroup factionGroup, FactionGroup defender, int killCount) {
        if (!debug) {
            return;
        }

        System.out.println(factionGroup.getFaction() + " group " + factionGroup.getNumber() + " attacks defending group " +
                defender.getNumber() + ", killing " + killCount + " units " + "(initiative: " + factionGroup.getInitiative() + ")");
    }

    private void printNoTarget(FactionGroup attacker) {
        if (!debug) {
            return;
        }

        printEmptyLine();
        System.out.println(attacker.getFaction() + " group " + attacker.getNumber() + " selected no group, because preferred group" +
                " would receive 0 damage");
    }

    private void printSelectedTarget(FactionGroup attacker, Optional<FactionGroup> possibleDefender) {
        if (!debug) {
            return;
        }

        printEmptyLine();
        System.out.println(attacker.getFaction() + " group " + attacker.getNumber() + " selected defending group " +
                possibleDefender.get().getNumber() + " " + possibleDefender.get().getPossibleDamage(attacker.getEffectivePower(), attacker.getAttackType()) + " damage " +
                " (effectivePower: " + possibleDefender.get().getEffectivePower() + ", initiative: " + possibleDefender.get().getInitiative() + ")");
    }

    private void printStatus() {
        if (!debug) {
            return;
        }

        if (immuneSystems.isEmpty()) {
            System.out.println("Immune System:");
            System.out.println("No groups remain.");
        } else {
            System.out.println("Immune System:");
            immuneSystems.stream()
                    .sorted(Comparator.comparingInt(FactionGroup::getNumber))
                    .forEach(f -> System.out.println("Group " + f.getNumber() + " contains " + f.getUnitCount() + " units"));
        }

        if (infections.isEmpty()) {
            System.out.println("Infection:");
            System.out.println("No groups remain.");
        } else {
            System.out.println("Infection:");
            infections.stream()
                    .sorted(Comparator.comparingInt(FactionGroup::getNumber))
                    .forEach(f -> System.out.println("Group " + f.getNumber() + " contains " + f.getUnitCount() + " units"));
        }
        printEmptyLine();
    }

    private void printTargetOrder(List<FactionGroup> factionGroups) {
        if (!debug) {
            return;
        }

        System.out.println("Target order:");
        factionGroups.forEach(factionGroup -> System.out.println(factionGroup.getFaction() + " group " + factionGroup.getNumber() +
                " with effectivePower: " + factionGroup.getEffectivePower() + " and initiative: " + factionGroup.getInitiative()));
        printEmptyLine();
    }

    private void printTarget(FactionGroup defender, FactionGroup attacker) {
        if (!debug) {
            return;
        }

        System.out.println(attacker.getFaction() + " group " + attacker.getNumber() + " would deal defending group " +
                defender.getNumber() + " " + defender.getPossibleDamage(attacker.getEffectivePower(), attacker.getAttackType()) + " damage " +
                " (effectivePower: " + defender.getEffectivePower() + ", initiative: " + defender.getInitiative() + ")");
    }

    private void prepareInput(List<String> inputs) {
        Pattern pattern = Pattern.compile("^(?<unitCount>\\d*) units each with (?<hitPoints>\\d*) hit points " +
                "(?<modifiers>\\(.*\\) )?with an attack that does (?<attackPower>\\d*) (?<attackType>[a-z]*) " +
                "damage at initiative (?<initiative>\\d*)$");

        String faction = "";
        int count = 0;
        for (String input : inputs) {
            if (input.startsWith(FactionGroup.IMMUNE_SYSTEM)) {
                faction = FactionGroup.IMMUNE_SYSTEM;
                count = 1;
                continue;
            } else if (input.startsWith(FactionGroup.INFECTION)) {
                faction = FactionGroup.INFECTION;
                count = 1;
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
            group.setNumber(count);

            if ("Infection".equals(faction)) {
                infections.add(group);
            } else if ("Immune System".equals(faction)) {
                immuneSystems.add(group);
            }

            count++;
        }
    }
}
