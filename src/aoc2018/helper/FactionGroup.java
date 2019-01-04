package aoc2018.helper;

import java.util.List;

public class FactionGroup {
    private final String faction;
    private final int unitCount;
    private final int hitPoints;
    private final int attackPower;
    private final int initiative;
    private final String attackType;
    private final List<String> immunities;
    private final List<String> weakness;

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
}
