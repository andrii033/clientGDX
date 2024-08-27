package com.ta.data;

import lombok.Data;
import java.util.List;

@Data
public class FightRequest {
    private CharacterRequest characterRequest;
    private List<EnemyRequest> enemyRequest;
}
