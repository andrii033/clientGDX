package com.ta.data;

public class TurnOrderEntry {
    private int initiative;
    private String name;

    public TurnOrderEntry(int initiative, String name) {
        this.initiative = initiative;
        this.name = name;
    }

    public int getInitiative() {
        return initiative;
    }

    public String getName() {
        return name;
    }
}
