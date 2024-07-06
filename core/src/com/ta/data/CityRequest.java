package com.ta.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CityRequest {
    private int xCoord;
    private int yCoord;
    private String terrainType;
    private Map<String,String> enemies;
}
