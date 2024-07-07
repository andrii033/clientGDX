package com.ta.data;

import java.util.HashMap;

public class CityRequest {
    private String terrainType;
    private HashMap<String, String> enemies;
    private int xcoord;
    private int ycoord;

    // No-argument constructor
    public CityRequest() {
        enemies = new HashMap<>();
    }

    // Getters and setters
    public String getTerrainType() {
        return terrainType;
    }

    public void setTerrainType(String terrainType) {
        this.terrainType = terrainType;
    }

    public HashMap<String, String> getEnemies() {
        return enemies;
    }

    public void setEnemies(HashMap<String, String> enemies) {
        this.enemies = enemies;
    }

    public int getXcoord() {
        return xcoord;
    }

    public void setXcoord(int xcoord) {
        this.xcoord = xcoord;
    }

    public int getYcoord() {
        return ycoord;
    }

    public void setYcoord(int ycoord) {
        this.ycoord = ycoord;
    }
}
