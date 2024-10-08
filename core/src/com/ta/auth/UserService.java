package com.ta.auth;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.*;
import com.ta.ClientGDX;
import com.ta.data.*;
import com.ta.screens.*;
import org.json.JSONException;
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

    public void getCharacterInfo() {
        HttpRequest httpRequest = new HttpRequest(HttpMethods.GET);
        httpRequest.setUrl("http://localhost:8080/character/getCharacter");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setHeader("Authorization", "Bearer " + token);

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
                            game.setScreen(new BattleCityScreen(game, character, enemies,token));
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



//    public void party() {
//        String userJson = "id"; //исправить потом
//
//        System.out.println("party");
//
//        // Create the HTTP request
//        HttpRequest httpRequest = new HttpRequest(HttpMethods.POST);
//        httpRequest.setUrl("http://localhost:8080/party/create");
//        httpRequest.setHeader("Content-Type", "application/json");
//        httpRequest.setHeader("Authorization", "Bearer " + token);
//        httpRequest.setContent(userJson);
//
//        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
//            @Override
//            public void handleHttpResponse(HttpResponse httpResponse) {
//                int statusCode = httpResponse.getStatus().getStatusCode();
//                String responseString = httpResponse.getResultAsString();
//
//                if (statusCode == 200) {
//                    try {
//                        Gdx.app.log("party ", responseString);
//                        Gdx.app.postRunnable(() -> {
//                            //game.setScreen(new BattleCityScreen(game, enemies, character));
//                        });
//                    } catch (Exception e) {
//                        Gdx.app.log("party", "Failed to parse response JSON", e);
//                    }
//                } else {
//                    Gdx.app.log("party", "Failed: " + statusCode);
//                }
//            }
//
//            @Override
//            public void failed(Throwable t) {
//
//            }
//
//            @Override
//            public void cancelled() {
//
//            }
//        });
//    }


    public void getLvlUp(){
        HttpRequest httpRequest = new HttpRequest(HttpMethods.GET);
        httpRequest.setUrl("http://localhost:8080/character/lvlup");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setHeader("Authorization", "Bearer " + token);

        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                String responseString = httpResponse.getResultAsString();
                if (statusCode == 200) {
                    try{
                        Json json = new Json();
                        JsonReader jsonReader = new JsonReader();
                        JsonValue jsonValue = jsonReader.parse(responseString);
                        LvlUpRequest lvlUpRequest = json.readValue(LvlUpRequest.class, jsonValue);
                        Gdx.app.postRunnable(()->
                                game.setScreen(new LvlUpScreen(game,lvlUpRequest)));
                    }catch (Exception e){
                        Gdx.app.log("getLvlUp", "Failed to parse response JSON", e);
                    }
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
    public void postLvlUp(LvlUpRequest lvlUpRequest){
        HttpRequest httpRequest = new HttpRequest(HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/character/lvlup");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setHeader("Authorization", "Bearer " + token);

        JSONObject json = new JSONObject();
        try {
            json.put("unallocatedMainPoints", lvlUpRequest.getUnallocatedMainPoints());
            json.put("unallocatedStrPoints", lvlUpRequest.getUnallocatedStrPoints());
            json.put("unallocatedAgiPoints", lvlUpRequest.getUnallocatedAgiPoints());
            json.put("unallocatedIntePoints", lvlUpRequest.getUnallocatedIntePoints());

            json.put("str", lvlUpRequest.getStr());
            json.put("agi", lvlUpRequest.getAgi());
            json.put("inte", lvlUpRequest.getInte());

            json.put("physicalHarm", lvlUpRequest.getPhysicalHarm());
            json.put("armorPiercing", lvlUpRequest.getArmorPiercing());
            json.put("reduceBlockDam", lvlUpRequest.getReduceBlockDam());
            json.put("maxHealth", lvlUpRequest.getMaxHealth());

            json.put("critChance", lvlUpRequest.getCritChance());
            json.put("attackSpeed", lvlUpRequest.getAttackSpeed());
            json.put("avoidance", lvlUpRequest.getAvoidance());
            json.put("blockChance", lvlUpRequest.getBlockChance());

            json.put("magicDam", lvlUpRequest.getMagicDam());
            json.put("magicCritChance", lvlUpRequest.getMagicCritChance());
            json.put("manaRegen", lvlUpRequest.getManaRegen());
            json.put("maxMana", lvlUpRequest.getMaxMana());
        } catch (JSONException e) {
            Gdx.app.log("postLvlUp", "Failed to create JSON payload", e);
            return;
        }
        String jsonString = json.toString();
        httpRequest.setContent(jsonString);

        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                String responseString = httpResponse.getResultAsString();
                if (statusCode == 200) {
                    try {
                        getCharacterInfo();
                    } catch (Exception e) {
                        Gdx.app.log("postLvlUp", "Failed to parse response JSON", e);
                    }
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("postLvlUp", "Request failed", t);
            }

            @Override
            public void cancelled() {
                Gdx.app.log("postLvlUp", "Request cancelled");
            }
        });
    }

}
