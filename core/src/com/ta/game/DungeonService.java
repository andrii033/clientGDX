package com.ta.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.ta.data.CharacterRequest;
import com.ta.data.EnemyRequest;
import com.ta.data.FightRequest;
import com.ta.screens.BattleCityScreen;

import java.util.List;

public class DungeonService {

    private final BattleCityScreen battleCityScreen;

    public DungeonService(BattleCityScreen battleCityScreen) {
        this.battleCityScreen = battleCityScreen;
    }

    public void fight(String id, String token) {
        String userJson = id;

        System.out.println("id " + id);

        // Create the HTTP request
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/character/fight");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setHeader("Authorization", "Bearer " + token);  // Ensure `token` is defined in your class or passed appropriately
        httpRequest.setContent(userJson);

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                String responseString = httpResponse.getResultAsString();

                if (statusCode == 200) {
                    try {
                        Json json = new Json();
                        JsonReader jsonReader = new JsonReader();
                        JsonValue jsonValue = jsonReader.parse(responseString);

                        if (jsonValue == null) {
                            Gdx.app.log("fight", "Failed to parse JSON response.");
                            return;
                        }

                        FightRequest fightRequest = json.readValue(FightRequest.class, jsonValue);
                        CharacterRequest characterRequest = fightRequest.getCharacterRequest();
                        List<EnemyRequest> enemyRequests = fightRequest.getEnemyRequest();

                        Gdx.app.log("fight", "Response: " + responseString);

                        // Update the existing screen's data
                        Gdx.app.postRunnable(() -> {
                            battleCityScreen.updateCharacter(characterRequest);
                            battleCityScreen.updateEnemies(enemyRequests);
                        });
                    } catch (Exception e) {
                        Gdx.app.log("fight", "Failed to parse response JSON", e);
                    }
                } else {
                    Gdx.app.log("fight", "Failed: " + statusCode);
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("fight", "Request failed", t);
            }

            @Override
            public void cancelled() {
                Gdx.app.log("fight", "Request cancelled");
            }
        });
    }
}
