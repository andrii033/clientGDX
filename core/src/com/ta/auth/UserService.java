package com.ta.auth;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.*;
import com.ta.ClientGDX;
import com.ta.data.*;
import com.ta.screens.BattleCityScreen;
import com.ta.screens.ChooseCharacterScreen;
import com.ta.screens.LoginScreen;
import com.ta.screens.MainCityScreen;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UserService {
    private static String token;
    private final ClientGDX game;

    public UserService(ClientGDX game) {
        this.game = game;
    }

    public void createUser(User user) {
        Gdx.app.log("UserService", "Creating user");

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json); // Ensure JSON format with double quotes
        String userJson = json.toJson(user);

        // Log the JSON payload
        Gdx.app.log("UserService", "JSON Payload: " + userJson);

        HttpRequest httpRequest = new HttpRequest(HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/auth/sign-up");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setContent(userJson);

        // Send the HTTP request asynchronously
        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                String responseString = httpResponse.getResultAsString();
                Json json = new Json();
                JwtAuthenticationResponse serverResponse = null;

                try {
                    serverResponse = json.fromJson(JwtAuthenticationResponse.class, responseString);
                } catch (Exception e) {
                    Gdx.app.log("UserService", "Failed to parse response JSON", e);
                }

                if (statusCode == 200 && serverResponse != null) {
                    Gdx.app.log("UserService", "User created successfully: " + serverResponse.getToken());
                    Gdx.app.postRunnable(() -> game.setScreen(new LoginScreen(game)));
                } else {
                    Gdx.app.log("UserService", "Failed to create user: " + statusCode);
                    Gdx.app.log("UserService", "Response: " + responseString);
                    // Handle failure, e.g., show error message to user
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("UserService", "User creation failed", t);
            }

            @Override
            public void cancelled() {
                Gdx.app.log("UserService", "User creation cancelled");
            }
        });
        // The program continues executing immediately after sending the request
    }

    public void signIn(User user) {
        Gdx.app.log("UserService", "Signing in");

        // Create JSON payload
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", user.getUsername());
            requestBody.put("password", user.getPassword());
        } catch (Exception e) {
            Gdx.app.log("UserService", "Failed to create JSON payload", e);
            return;
        }

        String userJson = requestBody.toString();

        // Log the JSON payload
        Gdx.app.log("UserService", "JSON Payload: " + userJson);

        // Create HttpRequest
        HttpRequest httpRequest = new HttpRequest(HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/auth/sign-in");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setContent(userJson);

        // Send the HTTP request asynchronously
        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                String responseString = httpResponse.getResultAsString();
                if (statusCode == 200) {
                    try {
                        // Assuming the response is a JSON object with a "token" field
                        JSONObject responseBody = new JSONObject(responseString);
                        token = responseBody.getString("token");
                        Gdx.app.log("UserService", "Signed in successfully, token: " + token);
                        // Gdx.app.postRunnable(() -> game.setScreen(new ChooseCharacterScreen(game)));
                        getCharacters(); // get list and open ChooseCharacterScreen
                    } catch (Exception e) {
                        Gdx.app.log("UserService", "Failed to parse response JSON", e);
                    }
                } else {
                    Gdx.app.log("UserService", "Failed to sign in: " + statusCode);
                    Gdx.app.log("UserService", "Response: " + responseString);
                    // Update the message label or handle the error
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("UserService", "Sign in failed", t);
                // Update the message label or handle the error
            }

            @Override
            public void cancelled() {
                Gdx.app.log("UserService", "Sign in cancelled");
                // Update the message label or handle the error
            }
        });
    }


    // In UserService.java

    public void getCharacters() {
        HttpRequest httpRequest = new HttpRequest(HttpMethods.GET);
        httpRequest.setUrl("http://localhost:8080/character/choose");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setHeader("Authorization", "Bearer " + token);

        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                String responseString = httpResponse.getResultAsString();
                Gdx.app.log("getCharacters", "Response Status: " + statusCode);
                Gdx.app.log("getCharacters", "Response: " + responseString);

                try {
                    if (statusCode == 200 && responseString != null && !responseString.trim().isEmpty()) {
                        Json json = new Json();
                        Array<CreateCharacterRequest> characters = json.fromJson(Array.class, CreateCharacterRequest.class, responseString);
                        Gdx.app.postRunnable(() -> {
                            game.setScreen(new ChooseCharacterScreen(game, characters));
                        });
                    } else {
                        Gdx.app.log("getCharacters", "Empty or invalid response received");
                    }
                } catch (Exception e) {
                    Gdx.app.log("getCharacters", "Failed to parse response JSON: " + e.getMessage(), e);
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("getCharacters", "Failed to get characters", t);
            }

            @Override
            public void cancelled() {
                Gdx.app.log("getCharacters", "Request cancelled");
            }
        });
    }

    public void createCharacter(CreateCharacterRequest request) {
        Gdx.app.log("createCharacter", "Creating character");

        // Create the JSON payload
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", request.getName());
            requestBody.put("str", request.getStr());
            requestBody.put("agi", request.getAgi());
            requestBody.put("inte", request.getInte());
        } catch (Exception e) {
            Gdx.app.log("createCharacter", "Failed to create JSON payload", e);
            return;
        }

        String userJson = requestBody.toString();
        Gdx.app.log("createCharacter", "JSON Payload: " + userJson);

        // Create the HTTP request
        HttpRequest httpRequest = new HttpRequest(HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/character/create");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setHeader("Authorization", "Bearer " + token);
        httpRequest.setContent(userJson);

        // Send the HTTP request
        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                String responseString = httpResponse.getResultAsString();

                if (statusCode == 200) {
                    // Handle success response
                    if (responseString != null && !responseString.trim().isEmpty()) {
                        Gdx.app.log("createCharacter", "Character created successfully: " + responseString); // Log the success message
                        Gdx.app.postRunnable(() -> {
                            //game.setScreen(new ChooseCharacterScreen(game));
                            getCharacters();
                        });
                    } else {
                        Gdx.app.log("createCharacter", "Empty or null response received.");
                    }
                } else {
                    Gdx.app.log("createCharacter", "Failed to create character, status code: " + statusCode);
                    Gdx.app.log("createCharacter", "Response: " + responseString);
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("createCharacter", "Failed to create character", t);
            }

            @Override
            public void cancelled() {
                Gdx.app.log("createCharacter", "Create character request cancelled");
            }
        });
    }


    public void chooseCharacter(String id) {
        HttpRequest httpRequest = new HttpRequest(HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/character/choose");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setHeader("Authorization", "Bearer " + token);
        httpRequest.setContent(id);

        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                String responseString = httpResponse.getResultAsString();
                Gdx.app.log("chooseCharacter", "Character chosen successfully " + responseString);

                if (responseString == null || responseString.isEmpty()) {
                    Gdx.app.log("UserService", "Empty or null response received.");
                    return;
                }

                try {
                    Json json = new Json();
                    CharacterRequest characterRequest = json.fromJson(CharacterRequest.class, responseString);

                    if (characterRequest == null) {
                        Gdx.app.log("chooseCharacter", "Failed to parse JSON response.");
                        return;
                    }

                    Gdx.app.log("chooseCharacter", "Character: " + characterRequest.getCharacterName());

                    Gdx.app.postRunnable(() -> game.setScreen(new MainCityScreen(game, characterRequest)));

                } catch (Exception e) {
                    Gdx.app.log("chooseCharacter", "Error parsing JSON response", e);
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("chooseCharacter", "Failed to choose character", t);
            }

            @Override
            public void cancelled() {
                Gdx.app.log("chooseCharacter", "Cancelled choose character");
            }
        });
    }


    public void moveBattleCity(CharacterRequest character) {
        String userJson = "2";

        // Create the HTTP request
        HttpRequest httpRequest = new HttpRequest(HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/character/move");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setHeader("Authorization", "Bearer " + token);
        httpRequest.setContent(userJson);

        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                String responseString = httpResponse.getResultAsString();

                if (statusCode == 200) {
                    try {
                        Json json = new Json();
                        JsonReader jsonReader = new JsonReader();
                        JsonValue jsonValue = jsonReader.parse(responseString);

                        if (jsonValue == null) {
                            Gdx.app.log("moveBattleCity", "Failed to parse JSON response.");
                            return;
                        }

                        List<EnemyRequest> enemies = new ArrayList<>();
                        for (JsonValue value : jsonValue) {
                            EnemyRequest enemy = json.readValue(EnemyRequest.class, value);
                            enemies.add(enemy);
                        }

                        Gdx.app.log("moveBattleCity enemies ", enemies.toString());

                        Gdx.app.postRunnable(() -> {
                            game.setScreen(new BattleCityScreen(game, character, enemies));
                        });
                    } catch (Exception e) {
                        Gdx.app.log("moveBattleCity", "Failed to parse response JSON", e);
                    }
                } else {
                    Gdx.app.log("moveBattleCity", "Failed to move: " + statusCode);
                }

            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });
    }

    public void fight(String id, List<EnemyRequest> enemies) {
        String userJson = id;

        System.out.println("id " + id);

        // Create the HTTP request
        HttpRequest httpRequest = new HttpRequest(HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/character/fight");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setHeader("Authorization", "Bearer " + token);
        httpRequest.setContent(userJson);

        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
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
                        CharacterRequest character = mapCharacterRequestToCharacter(fightRequest.getCharacterRequest());

                        Gdx.app.log("fight", "Response: " + responseString);
                        Gdx.app.postRunnable(() -> {
                            game.setScreen(new BattleCityScreen(game, character, enemies));
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

    public CharacterRequest mapCharacterRequestToCharacter(CharacterRequest characterRequest) {
        CharacterRequest character = new CharacterRequest();

        character.setCharacterName(characterRequest.getCharacterName());
        character.setId(characterRequest.getId());

        character.setStr(characterRequest.getStr());
        character.setAgi(characterRequest.getAgi());
        character.setInte(characterRequest.getInte());

        character.setDef(characterRequest.getDef());
        character.setHp(characterRequest.getHp());
        character.setMana(characterRequest.getMana());

        character.setPhysicalHarm(characterRequest.getPhysicalHarm());
        character.setArmorPiercing(characterRequest.getArmorPiercing());
        character.setReduceBlockDam(characterRequest.getReduceBlockDam());
        character.setMaxHealth(characterRequest.getMaxHealth());

        character.setCritChance(characterRequest.getCritChance());
        character.setAttackSpeed(characterRequest.getAttackSpeed());
        character.setAvoidance(characterRequest.getAvoidance());
        character.setBlockChance(characterRequest.getBlockChance());

        character.setMagicDam(characterRequest.getMagicDam());
        character.setMagicCritChance(characterRequest.getMagicCritChance());
        character.setManaRegen(characterRequest.getManaRegen());
        character.setMaxMana(characterRequest.getMaxMana());

        character.setGold(characterRequest.getGold());
        character.setRes(characterRequest.getRes());

        character.setExp(characterRequest.getExp());
        character.setLvl(characterRequest.getLvl());

        character.setUnallocatedMainPoints(characterRequest.getUnallocatedMainPoints());
        character.setUnallocatedStrPoints(characterRequest.getUnallocatedStrPoints());
        character.setUnallocatedAgiPoints(characterRequest.getUnallocatedAgiPoints());
        character.setUnallocatedIntePoints(characterRequest.getUnallocatedIntePoints());

        character.setLatestDamage(characterRequest.getLatestDamage());

        return character;
    }



    public void party() {
        String userJson = "id"; //исправить потом

        System.out.println("party");

        // Create the HTTP request
        HttpRequest httpRequest = new HttpRequest(HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/party/create");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setHeader("Authorization", "Bearer " + token);
        httpRequest.setContent(userJson);

        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                String responseString = httpResponse.getResultAsString();

                if (statusCode == 200) {
                    try {
                        Gdx.app.log("party ", responseString);
                        Gdx.app.postRunnable(() -> {
                            //game.setScreen(new BattleCityScreen(game, enemies, character));
                        });
                    } catch (Exception e) {
                        Gdx.app.log("party", "Failed to parse response JSON", e);
                    }
                } else {
                    Gdx.app.log("party", "Failed: " + statusCode);
                }
            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });
    }
}
