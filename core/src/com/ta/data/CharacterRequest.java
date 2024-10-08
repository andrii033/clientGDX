package com.ta.data;

import lombok.Data;

@Data
public class CharacterRequest {

    private String characterName;
    private Long id;

    private int str;
    private int agi;
    private int inte;

    private int def;
    private int hp;
    private int mana;

    private int physicalHarm;
    private int armorPiercing;
    private int reduceBlockDam;
    private int maxHealth;

    private int critChance;
    private int attackSpeed;
    private int avoidance;
    private int blockChance;

    private int magicDam;
    private int magicCritChance;
    private int manaRegen;
    private int maxMana;

    private int gold;
    private int res;

    private int exp;
    private int lvl;

    private int unallocatedMainPoints;
    private int unallocatedStrPoints;
    private int unallocatedAgiPoints;
    private int unallocatedIntePoints;

    private int latestDamage;
}