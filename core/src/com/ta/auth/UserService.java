package com.ta.auth;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.ta.ClientGDX;
import com.ta.data.CharacterRequest;
import com.ta.data.JwtAuthenticationResponse;
import com.ta.data.User;

import com.ta.screens.ChooseCharacterScreen;
import com.ta.screens.LoginScreen;
import org.json.JSONObject;


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
                        //createCharacter();
                        // Navigate to main screen
                        Gdx.app.postRunnable(() -> game.setScreen(new ChooseCharacterScreen(game)));
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

    public void getCharacters(final ChooseCharacterScreen screen) {
        HttpRequest httpRequest = new HttpRequest(HttpMethods.GET);
        httpRequest.setUrl("http://localhost:8080/character/choose");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setHeader("Authorization", "Bearer " + token);
        Gdx.app.log("UserService getChar","token "+token);

        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                String responseString = httpResponse.getResultAsString();
                Json json = new Json();
                Array<CharacterRequest> characters = json.fromJson(Array.class, CharacterRequest.class, responseString);
                screen.setCharacters(characters);
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("UserService", "Failed to get characters", t);
            }

            @Override
            public void cancelled() {
                Gdx.app.log("UserService", "Cancelled get characters");
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
                Gdx.app.log("UserService", "Character chosen successfully");
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("UserService", "Failed to choose character", t);
            }

            @Override
            public void cancelled() {
                Gdx.app.log("UserService", "Cancelled choose character");
            }
        });
    }


    public void createCharacter() {
        Gdx.app.log("UserService", "Creating character");

        // Create the JSON payload
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("characterName", "New Character");
        } catch (Exception e) {
            Gdx.app.log("UserService", "Failed to create JSON payload", e);
            return;
        }

        String userJson = requestBody.toString();
        Gdx.app.log("UserService", "JSON Payload: " + userJson);

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
                Gdx.app.log("UserService", "Response Status: " + statusCode);

                if (statusCode == 200) {
                    try {
                        // Assuming the response is a JSON object with a "name" field
                        JSONObject responseBody = new JSONObject(responseString);
                        String characterName = responseBody.getString("name");
                        Gdx.app.log("UserService", "Created character successfully: " + characterName);

                        // Navigate to another screen or handle the response as needed
                        Gdx.app.postRunnable(() -> {
                            // Replace CharScreen with your desired screen
                            //game.setScreen(new ChooseCharacterScreen(game,"new name"));
                        });
                    } catch (Exception e) {
                        Gdx.app.log("UserService", "Failed to parse response JSON", e);
                    }
                } else {
                    Gdx.app.log("UserService", "Failed to create character, status code: " + statusCode);
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("UserService", "Failed to create character", t);
            }

            @Override
            public void cancelled() {
                Gdx.app.log("UserService", "Create character request cancelled");
            }
        });
    }

}
