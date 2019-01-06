package aoc2018.helper;

import java.util.List;

public class FactionGroup {

    public static final String IMMUNE_SYSTEM = "Immune System";
    public static final String INFECTION = "Infection";

    private int number;
    private final String faction;
    private final int hitPoints;
    private final int attackPower;
    private final int initiative;
    private final String attackType;
    private final List<String> immunities;
    private final List<String> weakness;

    private int unitCount;
    private boolean defending;
    private FactionGroup defender;

    public FactionGroup(String faction,
                        int unitCount,
                        int hitPoints,
                        int attackPower,
                        int initiative,
                        String attackType,
                        List<String> immunities,
                        List<String> weakness) {

        this.faction = faction;
        this.unitCount = unitCount;
        this.hitPoints = hitPoints;
        this.attackPower = attackPower;
        this.initiative = initiative;
        this.attackType = attackType;
        this.immunities = immunities;
        this.weakness = weakness;
    }

    public int attack(FactionGroup defender) {
        int possibleDamage = defender.getPossibleDamage(this.getEffectivePower(), this.getAttackType());
        int defenderHitPoints = defender.getHitPoints();

        int possibleKillCount = possibleDamage / defenderHitPoints;

        int killCount = defender.killUnits(possibleKillCount);

        return killCount;
    }

    private int killUnits(int possibleKillCount) {
        int killCount;
        if (getUnitCount() >= possibleKillCount) {
            killCount = possibleKillCount;
        } else {
            killCount = getUnitCount();
        }

        this.unitCount = this.getUnitCount() - possibleKillCount;

        return killCount;
    }

    public String getFaction() {
        return faction;
    }

    public int getUnitCount() {
        return unitCount;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getInitiative() {
        return initiative;
    }

    public String getAttackType() {
        return attackType;
    }

    public List<String> getImmunities() {
        return immunities;
    }

    public List<String> getWeakness() {
        return weakness;
    }

    public int getEffectivePower() {
        return getAttackPower() * getUnitCount();
    }

    public int getPossibleDamage(int effectivePower, String attackType) {
        if (immunities.contains(attackType)) {
            return 0;
        }

        if (weakness.contains(attackType)) {
            return effectivePower * 2;
        }

        return effectivePower;
    }

    public boolean isDefending() {
        return defending;
    }

    public void setDefending() {
        this.defending = true;
    }

    public void setAvailable() {
        this.defending = false;
    }

    public void setDefender(FactionGroup defender) {
        this.defender = defender;
    }

    public FactionGroup getDefender() {
        return defender;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
